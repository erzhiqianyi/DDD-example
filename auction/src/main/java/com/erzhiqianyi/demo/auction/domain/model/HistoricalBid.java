package com.erzhiqianyi.demo.auction.domain.model;

import lombok.Getter;

import java.time.Instant;

@Getter
public class HistoricalBid {
    private Guid bidder;
    private Money amount;
    private Instant timeOfBid;

    public HistoricalBid(Guid bidder, Money amount, Instant timeOfBid) {
        this.bidder =  bidder;
        this.amount = amount;
        this.timeOfBid = timeOfBid;
    }
}
