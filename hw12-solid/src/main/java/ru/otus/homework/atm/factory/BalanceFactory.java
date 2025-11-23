package ru.otus.homework.atm.factory;

import java.util.HashMap;
import lombok.experimental.UtilityClass;
import ru.otus.homework.atm.store.balance.BalanceImpl;
import ru.otus.homework.atm.store.balance.ControlledBalance;

@UtilityClass
public class BalanceFactory {

    public static ControlledBalance createBalance() {
        return new BalanceImpl(new HashMap<>());
    }
}
