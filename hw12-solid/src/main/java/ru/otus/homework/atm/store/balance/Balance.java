package ru.otus.homework.atm.store.balance;

import java.util.Set;
import ru.otus.homework.atm.store.Banknote;
import ru.otus.homework.atm.store.Cash;
import ru.otus.homework.atm.store.Copy;

public interface Balance extends Copy<Balance> {
    Set<Banknote> getBanknotes();

    long getSum();

    Cash getCashFromBanknote(Banknote banknote);
}
