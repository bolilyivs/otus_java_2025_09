package ru.otus.homework.atm.store;

import ru.otus.homework.atm.exception.ATMException;

public record Cash(Banknote banknote, long amount) {

    public long getDenomination() {
        return banknote().denomination();
    }

    public long getSum() {
        return banknote.denomination() * amount();
    }

    @Override
    public String toString() {
        return String.format("Cash: [%s x %s (%s)]", banknote(), amount(), getSum());
    }

    public static Cash of(long denomination, long amount) {
        return new Cash(Banknote.of(denomination), amount);
    }

    public static Cash ofZeroAmountBanknote(Banknote banknote) {
        return new Cash(banknote, 0L);
    }

    public static Cash add(Cash cash1, Cash cash2) {
        if (!cash1.banknote().equals(cash2.banknote())) {
            throw new ATMException("Номиналы банкнот не совпадают!");
        }
        long sum = cash1.amount() + cash2.amount();
        return new Cash(cash1.banknote(), sum);
    }

    public static Cash sub(Cash cash1, Cash cash2) {
        if (!cash1.banknote().equals(cash2.banknote())) {
            throw new ATMException("Номиналы банкнот не совпадают!");
        }
        long sum = cash1.amount() - cash2.amount();
        if (sum < 0) {
            throw new ATMException("Денежных средств не хватает!");
        }
        return new Cash(cash1.banknote(), sum);
    }
}
