package ru.otus;

import static ru.otus.AppConstants.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParallelSequence {
    private static final Logger logger = LoggerFactory.getLogger(ParallelSequence.class);
    private static final int[] DATA = new int[] {
        0, // THREAD_1_INDEX
        0 // THREAD_2_INDEX
    };
    private static int step = INC;
    private static int lastThread = THREAD_2_INDEX;

    public static void main(final String[] args) {
        ParallelSequence parallelSequence = new ParallelSequence();
        new Thread(() -> parallelSequence.action(THREAD_1_INDEX), THREAD_1_NAME).start();
        new Thread(() -> parallelSequence.action(THREAD_2_INDEX), THREAD_2_NAME).start();
    }

    private synchronized void action(int threadIndex) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (lastThread == threadIndex) {
                    this.wait();
                }
                if (DATA[threadIndex] >= MAX_VALUE) {
                    step = DEC;
                }
                if (DATA[threadIndex] <= MIN_VALUE) {
                    step = INC;
                }
                DATA[threadIndex] += step;
                lastThread = threadIndex;

                logger.info("{}: {}", Thread.currentThread().getName(), DATA[threadIndex]);
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(SLEEP_MS);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
