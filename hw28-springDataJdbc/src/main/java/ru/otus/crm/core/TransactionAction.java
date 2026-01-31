package ru.otus.crm.core;

import java.util.function.Supplier;

public interface TransactionAction<T> extends Supplier<T> {}
