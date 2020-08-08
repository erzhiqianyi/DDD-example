package com.erzhiqianyi.demo.auction.domain.model;

public class Money {

    private final Integer value;

    public Money() {
        this(0);
    }

    public Money(Integer value) {
        if (null == value) {
            throw new IllegalArgumentException("Money cannot be null.");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Money cannot be a negative value.");
        }
        this.value = value;
    }

    public Money add(Money money) {
        throwExceptionIfNull(money);
        return new Money(value + money.value);
    }

    public boolean greaterThan(Money money) {
        throwExceptionIfNull(money);
        return this.value > money.value;
    }

    public boolean greaterThanOrEqualTo(Money money) {
        throwExceptionIfNull(money);
        return this.value >= money.value;
    }

    public boolean lessThanOrEqualTo(Money money) {
        throwExceptionIfNull(money);
        return this.value <= money.value;
    }

    public void throwExceptionIfNull(Money money) {
        if (null == money) {
            throw new IllegalArgumentException("Money cannot be a negative value.");
        }
    }

    @Override
    public String toString() {
        return "Money{" +
                "value=" + value +
                '}';
    }
}
