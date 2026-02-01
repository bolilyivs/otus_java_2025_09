package ru.otus.crm.core;

public interface TransactionManager {

    <T> T doInTransaction(TransactionAction<T> action);
}
