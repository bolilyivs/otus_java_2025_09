package ru.otus.homework.atm.factory;

import ru.otus.homework.atm.ATM;
import ru.otus.homework.atm.ATMImpl;

public class ATMFactory {
    private ATMFactory() {}

    public static ATM createATM() {
        return new ATMImpl(BalanceFactory.createBalance());
    }
}
