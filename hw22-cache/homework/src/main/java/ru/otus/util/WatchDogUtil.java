package ru.otus.util;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class WatchDogUtil {

    private static final Logger log = LoggerFactory.getLogger(WatchDogUtil.class);

    public static long run(Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        return System.currentTimeMillis() - start;
    }

    public static void runWithLog(String jobName, Runnable runnable) {
        log.info("[{}] Time: {}", jobName, run(runnable));
    }
}
