package ru.otus.homework.atm.store.balance;

import ru.otus.homework.atm.store.Banknote;
import ru.otus.homework.atm.store.Cash;
import ru.otus.homework.atm.store.Copyable;

import java.util.Set;

public interface Balance extends Copyable<Balance> {
    Set<Banknote> getBanknotes();

    long getSum();

    Cash getCashFromBanknote(Banknote banknote);
}
