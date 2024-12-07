package ru.otus.logging;

import lombok.extern.slf4j.Slf4j;
import ru.otus.annotation.Log;

@Slf4j
public class TestLogging implements TestLoggingInterface {

    @Override
    @Log
    public void calculation(int param1) {
        log.info("execute original calculation method with int param: {}", param1);
    }

    @Override
    public void calculation(String param1) {
        log.info("execute original calculation method with String param: {}", param1);
    }

    @Override
    @Log
    public void calculation(int param1, int param2) {
        log.info("execute original calculation method with param: {}, {}", param1, param2);
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {
        log.info("execute original calculation method with param: {}, {}, {}", param1, param2, param3);
    }
}
