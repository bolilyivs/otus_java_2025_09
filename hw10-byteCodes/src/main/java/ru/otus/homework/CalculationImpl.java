package ru.otus.homework;

import ru.otus.homework.aop.Log;

public class CalculationImpl implements Calculation {

    @Override
    public double calculate(int param1) {
        return param1;
    }

    @Log
    @Override
    public double calculate(int param1, int param2) {
        return (double) param1 + param2;
    }

    @Override
    public double calculate(double param1, double param2) {
        return param1 + param2;
    }
}
