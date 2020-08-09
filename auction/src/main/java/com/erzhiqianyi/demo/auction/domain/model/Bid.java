package com.erzhiqianyi.demo.auction.domain.model;

import lombok.Getter;

import java.time.Instant;

@Getter
public class Bid {

    private Guid id;

    private Money maximumBid;

    private Instant timeOfOffer;
}
