package ru.otus.homework.atm.store;

import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BalanceImpl implements Balance {
    private final Map<Banknote, Cash> cashMap;

    @Override
    public Set<Banknote> getBanknotes() {
        return Collections.unmodifiableSet(cashMap.keySet());
    }

    @Override
    public void addCash(Cash cash) {
        Banknote banknote = cash.banknote();
        cashMap.computeIfAbsent(banknote, key -> new Cash(key, 0L));
        Cash sum = Cash.add(cashMap.get(banknote), cash);
        cashMap.put(banknote, sum);
    }

    @Override
    public void subCash(Cash cash) {
        Banknote banknote = cash.banknote();
        cashMap.computeIfAbsent(banknote, key -> new Cash(key, 0L));
        Cash sum = Cash.sub(cashMap.get(banknote), cash);
        cashMap.put(banknote, sum);
    }

    @Override
    public long getSum() {
        return cashMap.values().stream().mapToLong(Cash::getSum).sum();
    }

    @Override
    public Cash getCashFromBanknote(Banknote banknote) {
        return cashMap.getOrDefault(banknote, Cash.ofZeroAmountBanknote(banknote));
    }

    @Override
    public Cash getMaxBanknoteCashUpTo(long sum) {
        return getNotEmptyCashList().stream()
                .filter(cash -> cash.banknote().denomination() <= sum)
                .max(Comparator.comparing(cash -> cash.banknote().denomination()))
                .orElse(null);
    }

    public String toString() {
        String bodyMsg = cashMap.values().stream()
                .sorted(Comparator.comparing(Cash::getDenomination))
                .map(Record::toString)
                .collect(Collectors.joining(";\n"));

        return String.format("Balance: %s %n %s", getSum(), bodyMsg);
    }

    public Balance copy() {
        return new BalanceImpl(new HashMap<>(cashMap));
    }

    private List<Cash> getNotEmptyCashList() {
        return cashMap.values().stream().filter(cash -> cash.amount() > 0).toList();
    }
}
