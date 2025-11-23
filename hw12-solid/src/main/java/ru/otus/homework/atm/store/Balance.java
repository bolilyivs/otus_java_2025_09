package ru.otus.homework.atm.store;

import java.util.Set;

public interface Balance extends Copy<Balance> {
    Set<Banknote> getBanknotes();

    void addCash(Cash cash);

    void subCash(Cash cash);

    long getSum();

    Cash getCashFromBanknote(Banknote banknote);

    Cash getMaxBanknoteCashUpTo(long sum);
}
