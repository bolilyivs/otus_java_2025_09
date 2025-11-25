package ru.otus.processor.provider;

import java.time.LocalDateTime;

@FunctionalInterface
public interface DateTimeProvider {
    LocalDateTime getDate();
}
