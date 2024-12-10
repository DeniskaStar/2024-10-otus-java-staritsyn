package ru.otus.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.annotation.Log;
import ru.otus.logging.TestLogging;
import ru.otus.logging.TestLoggingInterface;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Ioc {

    public static TestLoggingInterface createTestLogging() {
        InvocationHandler handler = new LoggingInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(
                Ioc.class.getClassLoader(), new Class<?>[] {TestLoggingInterface.class}, handler);
    }

    static class LoggingInvocationHandler implements InvocationHandler {

        private final Object targetObject;
        private final Set<String> methodsWithLogAnnotation;

        public LoggingInvocationHandler(Object targetObject) {
            this.targetObject = targetObject;
            this.methodsWithLogAnnotation = extractAndCollectMethodsWithLogAnnotation(targetObject);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methodContainsLogAnnotation(method)) {
                log.info("executed method: {}, params: {}", method.getName(), args);
            }

            return method.invoke(targetObject, args);
        }

        private Set<String> extractAndCollectMethodsWithLogAnnotation(Object targetObject) {
            Set<String> logMethods = Collections.synchronizedSet(new HashSet<>());

            for (Method method : targetObject.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Log.class)) {
                    logMethods.add(method.getName() + Arrays.toString(method.getParameterTypes()));
                }
            }

            return logMethods;
        }

        private boolean methodContainsLogAnnotation(Method method) {
            String name = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();

            return methodsWithLogAnnotation.contains(name + Arrays.toString(parameterTypes));
        }
    }
}
