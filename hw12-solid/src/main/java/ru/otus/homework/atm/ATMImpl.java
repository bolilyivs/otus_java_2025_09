package ru.otus.homework.atm;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.atm.store.Cash;
import ru.otus.homework.atm.store.balance.Balance;
import ru.otus.homework.atm.store.balance.BalanceImpl;
import ru.otus.homework.atm.store.balance.ControlledBalance;

import java.util.HashMap;
import java.util.Objects;

@RequiredArgsConstructor
public class ATMImpl implements ATM {

    private final ControlledBalance bankBalance;

    @Override
    public void addMoney(Cash cash) {
        bankBalance.addCash(cash);
    }

    @Override
    public Balance takeMoney(long sum) {
        if (sum > bankBalance.getSum()) {
            throw new IllegalArgumentException("Недостаточно сердств!");
        }

        ControlledBalance userBalance = new BalanceImpl(new HashMap<>());
        exchange(bankBalance, userBalance, sum);

        return userBalance;
    }

    private void exchange(ControlledBalance sourceBalance, ControlledBalance targetBalance, long sum) {
        while (sum > 0) {
            Cash cash = sourceBalance.getMaxBanknoteCashUpTo(sum);
            if (Objects.isNull(cash)) {
                throw new IllegalArgumentException(String.format("Нет нужной банкноты для %s!", sum));
            }
            Cash subCash = Cash.of(cash.getDenomination(), Math.min(sum / cash.getDenomination(), cash.amount()));

            sourceBalance.subCash(subCash);
            targetBalance.addCash(subCash);
            sum -= subCash.getSum();
        }
    }

    @Override
    public Balance getBalance() {
        return bankBalance.copy();
    }
}
