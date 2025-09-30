package ru.otus;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class HelloOtus {

    private static final Logger LOGGER = Logger.getLogger(HelloOtus.class.getName());

    private static final int RANGE_START = 0;
    private static final int RANGE_END = 100;
    private static final int PARTITION_SIZE = 10;

    public static void main(final String[] args) {
        final List<Integer> rangeList = IntStream.range(RANGE_START, RANGE_END).boxed().toList();
        final List<Integer> sumList = Lists.partition(rangeList, PARTITION_SIZE).stream()
                .map(group -> group.stream()
                        .filter(Objects::nonNull)
                        .mapToInt(Integer::intValue)
                        .sum())
                .toList();

        LOGGER.log(Level.INFO, "Sum list: {0}", sumList);
    }
}
