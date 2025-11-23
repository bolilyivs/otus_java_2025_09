package ru.otus.homework;

import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.atm.ATM;
import ru.otus.homework.atm.factory.ATMFactory;
import ru.otus.homework.atm.store.Balance;
import ru.otus.homework.atm.store.Cash;

@Slf4j
public class Main {

    private Main() {}

    public static void main(String[] args) {
        ATM atm = ATMFactory.createATM();
        log.info("ATM balance: {}", atm.getBalance());

        atm.addMoney(Cash.of(5_000, 2));
        atm.addMoney(Cash.of(2_000, 5));
        atm.addMoney(Cash.of(1_000, 10));
        atm.addMoney(Cash.of(500, 20));
        atm.addMoney(Cash.of(100, 100));
        log.info("ATM balance after added money: {}", atm.getBalance());

        Balance userBalance = atm.takeMoney(18_600);
        log.info("UserBalance: {}", userBalance);
        log.info("BankBalance: {}", atm.getBalance());
    }
}
