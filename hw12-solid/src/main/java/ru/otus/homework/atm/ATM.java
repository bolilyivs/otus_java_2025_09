package ru.otus.homework.atm;

import ru.otus.homework.atm.store.Cash;
import ru.otus.homework.atm.store.balance.Balance;

public interface ATM {
    void addMoney(Cash cash);

    Balance takeMoney(long sum);

    Balance getBalance();
}
