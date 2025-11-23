package ru.otus.homework.atm.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record Banknote(long denomination) {

    private static final Map<Long, Banknote> banknoteCache = new ConcurrentHashMap<>();

    public static Banknote of(long denomination) {
        return banknoteCache.computeIfAbsent(denomination, Banknote::new);
    }

    @Override
    public String toString() {
        return String.valueOf(denomination);
    }
}
