package ru.otus.homework.atm.factory;

import lombok.experimental.UtilityClass;
import ru.otus.homework.atm.ATM;
import ru.otus.homework.atm.ATMImpl;

@UtilityClass
public class ATMFactory {

    public static ATM createATM() {
        return new ATMImpl(BalanceFactory.createBalance());
    }
}
