package com.example;

import java.util.Objects;

public class QuantityWeight {

    private static final double EPS = 1e-6;

    private final double value;
    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Invalid value");
        }

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    public QuantityWeight convertTo(WeightUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double kg = unit.toKg(value);
        double converted = targetUnit.fromKg(kg);
        return new QuantityWeight(converted, targetUnit);
    }

    public QuantityWeight add(QuantityWeight other) {
        if (other == null) {
            throw new IllegalArgumentException("Other cannot be null");
        }

        double sumKg = this.unit.toKg(this.value) + other.unit.toKg(other.value);
        return new QuantityWeight(sumKg, WeightUnit.KILOGRAM);
    }

    public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {
        if (other == null) {
            throw new IllegalArgumentException("Other cannot be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double sumKg = this.unit.toKg(this.value) + other.unit.toKg(other.value);
        double converted = targetUnit.fromKg(sumKg);
        return new QuantityWeight(converted, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuantityWeight other)) return false;

        double thisKg = this.unit.toKg(this.value);
        double otherKg = other.unit.toKg(other.value);

        return Math.abs(thisKg - otherKg) < EPS;
    }

    @Override
    public int hashCode() {
        double kg = unit.toKg(value);
        long rounded = Math.round(kg * 1e6); // match EPS precision
        return Objects.hash(rounded);
    }

    @Override
    public String toString(){
        return value + " " + unit;
    }
}