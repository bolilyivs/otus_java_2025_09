package ru.otus.homework.atm.store.balance;

import ru.otus.homework.atm.store.Cash;

public interface ControlledBalance extends Balance {
    void addCash(Cash cash);

    void subCash(Cash cash);

    Cash getMaxBanknoteCashUpTo(long sum);
}
