package com.erzhiqianyi.demo.auction.domain.model;

import java.time.Instant;

public class WinningBid {
    private Guid auctionId;
    private Guid bidderId;
    private Money maximumBid;
    private Instant timeOfBid;
    private Price currentAuctionPrice;

}
