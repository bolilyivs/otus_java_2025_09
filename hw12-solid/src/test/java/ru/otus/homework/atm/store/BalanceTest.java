package ru.otus.homework.atm.store;

import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.homework.atm.store.balance.BalanceImpl;
import ru.otus.homework.atm.store.balance.ControlledBalance;

class BalanceTest {

    @Test
    void addCash() {
        ControlledBalance balance = new BalanceImpl(new HashMap<>());
        balance.addCash(Cash.of(1000, 10));
        balance.addCash(Cash.of(100, 5));

        Assertions.assertEquals(balance.getCashFromBanknote(Banknote.of(1000)), Cash.of(1000, 10));
        Assertions.assertEquals(balance.getCashFromBanknote(Banknote.of(100)), Cash.of(100, 5));
    }

    @Test
    void subCash() {
        ControlledBalance balance = new BalanceImpl(new HashMap<>());
        balance.addCash(Cash.of(1000, 10));
        balance.subCash(Cash.of(1000, 3));
        balance.subCash(Cash.of(1000, 2));

        Assertions.assertEquals(balance.getCashFromBanknote(Banknote.of(1000)), Cash.of(1000, 5));
    }

    @Test
    void getSum() {
        ControlledBalance balance = new BalanceImpl(new HashMap<>());
        balance.addCash(Cash.of(1_000, 10));
        balance.addCash(Cash.of(100, 3));

        Assertions.assertEquals(1_000 * 10 + 100 * 3, balance.getSum());
    }

    @Test
    void getMaxBanknoteCashUpTo() {
        ControlledBalance balance = new BalanceImpl(new HashMap<>());
        balance.addCash(Cash.of(5_000, 5));
        balance.addCash(Cash.of(1_000, 10));
        balance.addCash(Cash.of(100, 3));

        Assertions.assertEquals(Cash.of(5_000, 5), balance.getMaxBanknoteCashUpTo(10_000));
        Assertions.assertEquals(Cash.of(5_000, 5), balance.getMaxBanknoteCashUpTo(5_000));
        Assertions.assertEquals(Cash.of(1_000, 10), balance.getMaxBanknoteCashUpTo(4_999));
    }

    @Test
    void copy() {
        ControlledBalance balance = new BalanceImpl(new HashMap<>());
        balance.addCash(Cash.of(5_000, 5));

        ControlledBalance balance2 = (ControlledBalance) balance.copy();
        balance2.addCash(Cash.of(1_000, 4));

        Assertions.assertEquals(5_000 * 5, balance.getSum());
        Assertions.assertEquals((5_000 * 5) + (1_000 * 4), balance2.getSum());
    }
}
