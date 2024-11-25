package ru.otus.domain;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestContext {

    private Method beforeMethod;
    private Method afterMethod;
    private List<Method> testMethods = new ArrayList<>();

    public Method getBeforeMethod() {
        return beforeMethod;
    }

    public void setBeforeMethod(Method beforeMethod) {
        this.beforeMethod = beforeMethod;
    }

    public Method getAfterMethod() {
        return afterMethod;
    }

    public void setAfterMethod(Method afterMethod) {
        this.afterMethod = afterMethod;
    }

    public List<Method> getTestMethods() {
        return testMethods;
    }
}
