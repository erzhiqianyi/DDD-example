package com.erzhiqianyi.demo.auction.domain.model;

import java.time.Instant;
import java.util.List;

public class Auction {

    private Guid id;

    private Instant endsAt;

    private Money startingPrice;

    private Guid listingId;

    private List<HistoricalBid> bids;

    private WinningBid winningBid;

    public Auction(Guid id, Instant endsAt, Money startingPrice, Guid listingId) {
        if (null == id) {
            throw new IllegalArgumentException(" auction id can't be null.");
        }
        if (null == endsAt) {
            throw new IllegalArgumentException("EndsAt must have a value.");
        }
        if (null == startingPrice) {
            throw new IllegalArgumentException("EndsAt must have a value.");
        }

        if (null == listingId) {
            throw new IllegalArgumentException("Listing Id  cannot be null.");
        }

        this.id = id;
        this.endsAt = endsAt;
        this.startingPrice = startingPrice;
        this.listingId = listingId;
    }

    private boolean stillInProgress(Instant currentTime) {
        return endsAt.isAfter(currentTime);
    }


    private void registerFirst(Bid bid) {
        boolean canRegister = isFirstBid() && bid.getMaximumBid().greaterThanOrEqualTo(startingPrice);
        if (canRegister) {
            place(new WinningBid(bid.getId(),bid.getMaximumBid(),startingPrice,bid.getTimeOfOffer()));
        }
    }

    private boolean isFirstBid() {
        return null == winningBid;
    }

    private void place(WinningBid newBid) {
        bids.add(new HistoricalBid(newBid.getBidderId(), newBid.getMaximumBid(), newBid.getTimeOfBid()));
        winningBid = newBid;
    }


    public void placeBidFor(Bid bid,Instant currentTime){
        if (!stillInProgress(currentTime)){
            return;
        }
        if (isFirstBid()){
            registerFirst(bid);
        }else if (bidderIsIncreasingMaximumBid(bid)){
            winningBid = winningBid.raiseMaximumBidTo(bid.getMaximumBid());
        }else if(winningBid.canMeetOrExceedBidIncrement(bid.getMaximumBid())) {
            place(winningBid.determineWinningBidIncrement(bid));
        }

    }

    private boolean bidderIsIncreasingMaximumBid(Bid bid)
    {
        return winningBid.wasMadeBy(bid.getId())
                && bid.getMaximumBid().greaterThan(winningBid.getMaximumBid());
    }

}
