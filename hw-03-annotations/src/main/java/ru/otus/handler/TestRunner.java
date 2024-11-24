package ru.otus.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.domain.TestContext;
import ru.otus.domain.TestResult;
import ru.otus.domain.TestStatistic;
import ru.otus.exception.InvalidOperationException;
import ru.otus.exception.InvalidTestConfigurationException;

public class TestRunner {

    private static final Logger log = LoggerFactory.getLogger(TestRunner.class);

    private TestRunner() {}

    public static void run(Class<?> testClass) {
        TestContext context = prepareTestMethods(testClass);
        List<TestStatistic> statistics = executeTests(testClass, context);
        TestStatisticHelper.process(statistics, testClass);
    }

    private static TestContext prepareTestMethods(Class<?> testClass) {
        TestContext context = new TestContext();

        for (Method method : testClass.getDeclaredMethods()) {
            processMethodAnnotation(method, context);
        }

        return context;
    }

    private static void processMethodAnnotation(Method method, TestContext context) {
        Method beforeMethod = determineMethod(method, Before.class, context.getBeforeMethod());
        Method afterMethod = determineMethod(method, After.class, context.getAfterMethod());

        if (beforeMethod != null) {
            context.setBeforeMethod(beforeMethod);
            return;
        }

        if (afterMethod != null) {
            context.setAfterMethod(afterMethod);
            return;
        }

        validateAndPrepareTestMethod(method, context);
    }

    private static Method determineMethod(
            Method currentMethod, Class<? extends Annotation> annotation, Method alreadyExistsMethod) {
        if (!currentMethod.isAnnotationPresent(annotation)) {
            return null;
        }

        if (alreadyExistsMethod != null) {
            throw new InvalidTestConfigurationException("Class: %s. Аннотация @%s встречается более 1 раза"
                    .formatted(currentMethod.getDeclaringClass().getName(), annotation.getSimpleName()));
        }

        validateConcurrentAnnotations(currentMethod);

        return currentMethod;
    }

    private static void validateConcurrentAnnotations(Method method) {
        if (method.isAnnotationPresent(Test.class)
                && (method.isAnnotationPresent(Before.class) || method.isAnnotationPresent(After.class))) {
            throw new InvalidTestConfigurationException(
                    "Class: %s. Аннотация @Test не может применяться вместе с аннотациями @Before или @After"
                            .formatted(method.getDeclaringClass().getName()));
        }

        if (method.isAnnotationPresent(Before.class) && method.isAnnotationPresent(After.class)) {
            throw new InvalidTestConfigurationException(
                    "Class: %s. Аннотации @Before и @After не могут применяться вместе"
                            .formatted(method.getDeclaringClass().getName()));
        }
    }

    private static void validateAndPrepareTestMethod(Method method, TestContext context) {
        if (method.isAnnotationPresent(Test.class)) {
            validateTestPriorityAttribute(method);
            validateConcurrentAnnotations(method);

            context.getTestMethods().add(method);
        }
    }

    private static void validateTestPriorityAttribute(Method method) {
        Test testAnnotation = method.getAnnotation(Test.class);

        if (testAnnotation.priority() < 1 || testAnnotation.priority() > 10) {
            throw new InvalidTestConfigurationException("Некорректно задан priority аннотации @Test в классе %s"
                    .formatted(method.getDeclaringClass().getSimpleName()));
        }
    }

    private static List<TestStatistic> executeTests(Class<?> testClass, TestContext context) {
        List<TestStatistic> testStatistic = new ArrayList<>();
        context.getTestMethods()
                .sort(Comparator.comparingInt(
                                (Method m) -> m.getAnnotation(Test.class).priority())
                        .reversed());

        for (Method testMethod : context.getTestMethods()) {
            executeTestMethod(testClass, context, testMethod, testStatistic);
        }

        return testStatistic;
    }

    private static void executeTestMethod(
            Class<?> testClass, TestContext context, Method testMethod, List<TestStatistic> testStatistic) {
        Object testInstance = createInstance(testClass);

        try {
            invokeMethod(testInstance, context.getBeforeMethod());

            testMethod.invoke(testInstance);
            testStatistic.add(new TestStatistic(testMethod, TestResult.SUCCESS));
        } catch (Exception e) {
            log.warn(e.getCause().getMessage());
            testStatistic.add(new TestStatistic(testMethod, TestResult.ERROR));
        } finally {
            invokeMethod(testInstance, context.getAfterMethod());
        }
    }

    private static Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new InvalidOperationException("Ошибка создания экземпляра класса теста", e);
        }
    }

    private static void invokeMethod(Object testInstance, Method method) {
        if (method == null) {
            return;
        }

        try {
            method.invoke(testInstance);
        } catch (Exception e) {
            throw new InvalidOperationException("Class: %s. Ошибка выполнения метода %s: %s"
                    .formatted(method.getDeclaringClass().getName(), method.getName(), e.getLocalizedMessage()));
        }
    }
}
