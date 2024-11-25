package ru.otus.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.exception.InvalidOperationException;

public class TestCases {

    private static final Logger logger = LoggerFactory.getLogger(TestCases.class);

    @Before
    public void before() {
        logger.info("инициализация перед тестом прошла успешно");
    }

    @Test(priority = 7)
    public void testFirst() {
        logger.info("первый тест выполнен");
    }

    @Test
    public void testSecond() {
        logger.info("второй тест выполнен");
    }

    @Test(priority = 7)
    public void testThird() {
        logger.info("третий тест выполнен");
    }

    @Test
    public void testFourth() {
        logger.info("четвертый тест выполнен");
    }

    @Test(priority = 10)
    public void testFifth() {
        throw new InvalidOperationException("ошибка при выполнении пятого теста");
    }

    @After
    public void after() {
        logger.info("закрытие ресурсов после теста прошло успешно");
    }
}
