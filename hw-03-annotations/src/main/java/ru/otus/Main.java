package ru.otus;

import ru.otus.handler.TestRunner;
import ru.otus.test.TestCases;

public class Main {

    public static void main(String[] args) {
        TestRunner.run(TestCases.class);
    }
}
