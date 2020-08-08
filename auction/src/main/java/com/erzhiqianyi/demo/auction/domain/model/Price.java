package com.erzhiqianyi.demo.auction.domain.model;

import java.util.Optional;
import java.util.stream.Stream;

public class Price {
    private final Money amount;

    public Price() {
        this(new Money());
    }

    public Price(Money amount) {
        if (null == amount) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        this.amount = amount;
    }

    public Money bidIncrement() {
        Money incrementStep = IncrementStep.incrementStep(amount);
        return amount.add(incrementStep);
    }

    public boolean canBeExceededBy(Money offer) {
        return offer.greaterThanOrEqualTo(bidIncrement());
    }

    public enum IncrementStep {
        ONE(5, 1, 100),
        TWO(20, 100, 500),
        THREE(50, 500, 1500);

        private Integer incrementStep;
        private Integer min;
        private Integer max;

        IncrementStep(Integer incrementStp, Integer min, Integer max) {
            this.incrementStep = incrementStp;
            this.min = min;
            this.max = max;
        }

        public  static  Money incrementStep(Money amount) {
            Optional<Money> step = Stream.of(values())
                    .filter(item ->
                            amount.greaterThan(new Money(item.min)) &&
                                    amount.lessThanOrEqualTo(new Money(item.max)
                                    ))
                    .map(item -> new Money(item.incrementStep))
                    .findFirst();

            if (step.isPresent()){
                return step.get();
            }else {
                return null;
            }
        }

    }

}
