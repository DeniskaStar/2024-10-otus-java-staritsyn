package ru.otus.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.domain.TestResult;
import ru.otus.domain.TestStatistic;

import java.util.List;

public class TestStatisticHelper {

    private TestStatisticHelper() {
    }

    private static final Logger logger = LoggerFactory.getLogger(TestStatisticHelper.class);

    public static void process(List<TestStatistic> statistics) {
        if (statistics.isEmpty()) {
            logger.error("Не обнаружено методов для подсчета статистики");
            return;
        }

        Class<?> testClass = statistics.getFirst().getTestMethod().getDeclaringClass();

        logger.info("Статистика выполнения тестов класса {}:", testClass.getSimpleName());

        long countSuccessTests = getCountByStatus(statistics, TestResult.SUCCESS);
        long countErrorTests = getCountByStatus(statistics, TestResult.ERROR);

        logger.info("Всего тестов: {}", statistics.size());
        logger.info("Успешно пройденых тестов: {}", countSuccessTests);
        logger.info("Непройденных тестов: {}", countErrorTests);
    }

    private static long getCountByStatus(List<TestStatistic> statistics, TestResult resultStatus) {
        return statistics.stream()
                .filter(it -> it.getTestResult() == resultStatus)
                .count();
    }
}
