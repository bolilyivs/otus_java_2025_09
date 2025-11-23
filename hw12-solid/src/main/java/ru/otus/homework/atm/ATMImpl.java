package ru.otus.homework.atm;

import java.util.HashMap;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import ru.otus.homework.atm.store.Balance;
import ru.otus.homework.atm.store.BalanceImpl;
import ru.otus.homework.atm.store.Cash;

@RequiredArgsConstructor
public class ATMImpl implements ATM {

    private final Balance bankBalance;

    @Override
    public void addMoney(Cash cash) {
        bankBalance.addCash(cash);
    }

    @Override
    public Balance takeMoney(long sum) {
        if (sum > bankBalance.getSum()) {
            throw new IllegalArgumentException("Недостаточно сердств!");
        }

        Balance userBalance = new BalanceImpl(new HashMap<>());

        while (sum > 0) {
            Cash cash = bankBalance.getMaxBanknoteCashUpTo(sum);
            if (Objects.isNull(cash)) {
                throw new IllegalArgumentException("Нет нужной банкноты!");
            }
            Cash subCash = Cash.of(cash.getDenomination(), Math.min(sum / cash.getDenomination(), cash.amount()));

            bankBalance.subCash(subCash);
            userBalance.addCash(subCash);
            sum -= subCash.getSum();
        }

        return userBalance;
    }

    @Override
    public Balance getBalance() {
        return bankBalance.copy();
    }
}
