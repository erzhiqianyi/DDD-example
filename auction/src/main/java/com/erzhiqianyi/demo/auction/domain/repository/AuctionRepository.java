package com.erzhiqianyi.demo.auction.domain.repository;

import com.erzhiqianyi.demo.auction.domain.model.Auction;
import com.erzhiqianyi.demo.auction.domain.model.Guid;

public interface AuctionRepository {
    void  add(Auction item);
    Auction findBy(Guid id);
}
