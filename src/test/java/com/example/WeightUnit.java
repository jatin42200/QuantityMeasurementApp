package com.example;

public enum WeightUnit {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double factor;

    WeightUnit(double factor) {
        this.factor = factor;
    }

    public double toKg(double value) {
        return value * factor;
    }

    public double fromKg(double kgValue) {
        return kgValue / factor;
    }
}
