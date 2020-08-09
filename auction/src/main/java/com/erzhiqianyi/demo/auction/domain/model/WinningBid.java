package com.erzhiqianyi.demo.auction.domain.model;

import lombok.Getter;

import java.time.Instant;

@Getter
public class WinningBid {
    private Guid auctionId;
    private Guid bidderId;
    private Money maximumBid;
    private Instant timeOfBid;
    private Price currentAuctionPrice;

    public WinningBid(Guid bidderId, Money maximumBid, Money bid, Instant timeOfBid) {
        if (null == bidderId) {
            throw new IllegalArgumentException("Bidder cannot be null.");
        }

        if (null == maximumBid) {
            throw new IllegalArgumentException("MaximumBid  cannot be null.");
        }

        if (null == timeOfBid) {
            throw new IllegalArgumentException("TimeOfBid must have a value.");
        }
        this.bidderId = bidderId;
        this.maximumBid = maximumBid;
        this.timeOfBid = timeOfBid;
        this.currentAuctionPrice = new Price(bid);
    }

    public WinningBid raiseMaximumBidTo(Money newAmount) {
        if (newAmount.greaterThan(maximumBid)) {
            return new WinningBid(bidderId, newAmount, currentAuctionPrice.getAmount(), Instant.now());
        } else {
            throw new IllegalArgumentException("Maximum bid increase must be larger than the current maximum bid. ");
        }
    }

    public boolean wasMadeBy(Guid bidder) {
        return bidderId.equals(bidder);
    }

    public WinningBid determineWinningBidIncrement(Bid newBid) {

        boolean fromProxyBidding = canMeetOrExceedBidIncrement(maximumBid)
                && canMeetOrExceedBidIncrement(newBid.getMaximumBid());
        if (fromProxyBidding) {
            return determineWinnerFromProxyBidding(this, newBid);
        } else {
            if (canMeetOrExceedBidIncrement(newBid.getMaximumBid())) {
                return createNewBid(newBid.getId(), currentAuctionPrice.bidIncrement(),
                        newBid.getMaximumBid(), newBid.getTimeOfOffer());
            } else {
                return this;
            }
        }
    }

    private WinningBid determineWinnerFromProxyBidding(WinningBid winningBid, Bid newBid) {
        WinningBid nextIncrement;
        if (winningBid.maxBidCanBeExceededBy(newBid.getMaximumBid())) {
            nextIncrement = createNewBid(bidderId, maximumBid, maximumBid, timeOfBid);
            if (nextIncrement.canMeetOrExceedBidIncrement(newBid.getMaximumBid())) {
                return createNewBid(newBid.getId(), nextIncrement.getCurrentAuctionPrice().bidIncrement(),
                        newBid.getMaximumBid(), newBid.getTimeOfOffer());
            } else {
                return createNewBid(newBid.getId(), newBid.getMaximumBid(), newBid.getMaximumBid(),
                        newBid.getTimeOfOffer());
            }
        } else {
            nextIncrement = createNewBid(newBid.getId(), newBid.getMaximumBid(), newBid.getMaximumBid(), newBid.getTimeOfOffer());
            if (nextIncrement.canMeetOrExceedBidIncrement(winningBid.getMaximumBid())) {
                return createNewBid(winningBid.getBidderId(), nextIncrement.getCurrentAuctionPrice().bidIncrement(),
                        winningBid.getMaximumBid(), winningBid.getTimeOfBid());
            } else {
                return createNewBid(winningBid.getBidderId(), winningBid.getMaximumBid(),
                        winningBid.getMaximumBid(), winningBid.getTimeOfBid());
            }
        }
    }

    private WinningBid createNewBid(Guid bidder, Money bid, Money maxBid, Instant timeOfBid) {
        return new WinningBid(bidder, bid, maxBid, timeOfBid);
    }

    private boolean maxBidCanBeExceededBy(Money bid) {
        return !this.maximumBid.greaterThanOrEqualTo(bid);
    }

    public boolean canMeetOrExceedBidIncrement(Money offer) {
        return currentAuctionPrice.canBeExceededBy(offer);
    }

    public boolean hasNotReachedMaximumBid() {
        return maximumBid.greaterThan(currentAuctionPrice.getAmount());
    }
}
