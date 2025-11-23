package ru.otus.homework.atm;

import ru.otus.homework.atm.store.Balance;
import ru.otus.homework.atm.store.Cash;

public interface ATM {
    void addMoney(Cash cash);

    Balance takeMoney(long sum);

    Balance getBalance();
}
