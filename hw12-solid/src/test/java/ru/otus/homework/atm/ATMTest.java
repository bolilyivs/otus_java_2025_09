package ru.otus.homework.atm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.homework.atm.factory.ATMFactory;
import ru.otus.homework.atm.store.Balance;
import ru.otus.homework.atm.store.Banknote;
import ru.otus.homework.atm.store.Cash;

class ATMTest {

    @Test
    void addMoney() {
        ATM atm = ATMFactory.createATM();

        atm.addMoney(Cash.of(5_000, 2));

        Assertions.assertEquals(10_000, atm.getBalance().getSum());
    }

    @Test
    void takeMoney() {
        ATM atm = ATMFactory.createATM();

        atm.addMoney(Cash.of(5_000, 2));
        atm.addMoney(Cash.of(2_000, 5));
        atm.addMoney(Cash.of(1_000, 10));

        Balance userBalance = atm.takeMoney(15_000);

        Assertions.assertEquals(15_000, userBalance.getSum());
        Assertions.assertEquals(
                2, userBalance.getCashFromBanknote(Banknote.of(5_000)).amount());
        Assertions.assertEquals(
                2, userBalance.getCashFromBanknote(Banknote.of(2_000)).amount());
        Assertions.assertEquals(
                1, userBalance.getCashFromBanknote(Banknote.of(1_000)).amount());
    }

    @Test
    void takeMoney2() {
        ATM atm = ATMFactory.createATM();

        atm.addMoney(Cash.of(5_000, 2));
        atm.addMoney(Cash.of(2_000, 5));
        atm.addMoney(Cash.of(1_000, 10));
        atm.addMoney(Cash.of(500, 10));

        Balance userBalance = atm.takeMoney(3_500);
        Assertions.assertEquals(3_500, userBalance.getSum());
        Assertions.assertEquals(
                1, userBalance.getCashFromBanknote(Banknote.of(2_000)).amount());
        Assertions.assertEquals(
                1, userBalance.getCashFromBanknote(Banknote.of(1_000)).amount());
    }

    @Test
    void getBalance() {
        ATM atm = ATMFactory.createATM();

        atm.addMoney(Cash.of(5_000, 2));
        atm.addMoney(Cash.of(2_000, 5));
        atm.addMoney(Cash.of(1_000, 10));

        Assertions.assertEquals(
                2, atm.getBalance().getCashFromBanknote(Banknote.of(5_000)).amount());
        Assertions.assertEquals(
                5, atm.getBalance().getCashFromBanknote(Banknote.of(2_000)).amount());
        Assertions.assertEquals(
                10, atm.getBalance().getCashFromBanknote(Banknote.of(1_000)).amount());
    }
}
