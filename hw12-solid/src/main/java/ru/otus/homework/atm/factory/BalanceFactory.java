package ru.otus.homework.atm.factory;

import java.util.HashMap;
import ru.otus.homework.atm.store.Balance;
import ru.otus.homework.atm.store.BalanceImpl;

public class BalanceFactory {
    private BalanceFactory() {}

    public static Balance createBalance() {
        return new BalanceImpl(new HashMap<>());
    }
}
