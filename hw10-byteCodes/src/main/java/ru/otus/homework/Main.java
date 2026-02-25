package ru.otus.homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.aop.ProxyCreator;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private Main() {}

    public static void main(String[] args) {
        Calculation calculation = ProxyCreator.createProxy(Calculation.class, CalculationImpl::new);
        log.info("{}", calculation.calculate(1));
        log.info("{}", calculation.calculate(1, 2));
        log.info("{}", calculation.calculate(3.0, 4.0));
    }
}
