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

        private final TestLoggingInterface myClass;
        private final Set<String> loggingMethods = Collections.synchronizedSet(new HashSet<>());

        public LoggingInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
            prepareLoggingMethods(myClass);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methodContainsLogAnnotation(method)) {
                log.info("executed method: {}, params: {}", method.getName(), args);
            }

            return method.invoke(myClass, args);
        }

        private void prepareLoggingMethods(TestLoggingInterface myClass) {
            for (Method method : myClass.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Log.class)) {
                    loggingMethods.add(method.getName() + Arrays.toString(method.getParameterTypes()));
                }
            }
        }

        private boolean methodContainsLogAnnotation(Method method) {
            String name = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();

            return loggingMethods.contains(name + Arrays.toString(parameterTypes));
        }
    }
}
