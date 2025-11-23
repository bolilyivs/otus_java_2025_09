package ru.otus.homework.atm.store;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CashTest {

    @Test
    void add() {
        Cash cash1 = Cash.of(1000, 10);
        Cash cash2 = Cash.of(1000, 5);
        Cash result = Cash.add(cash1, cash2);
        Assertions.assertEquals(Cash.of(1000, 15), result);
    }

    @Test
    void sub() {
        Cash cash1 = Cash.of(1000, 10);
        Cash cash2 = Cash.of(1000, 5);
        Cash result = Cash.sub(cash1, cash2);
        Assertions.assertEquals(Cash.of(1000, 5), result);
    }

    @Test
    void sum() {
        Cash cash = Cash.of(1000, 10);
        Assertions.assertEquals(10000, cash.getSum());
    }
}
