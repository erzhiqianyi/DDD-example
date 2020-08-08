package com.erzhiqianyi.demo.auction.domain.model;

import java.util.UUID;

public class Guid {
    private final String code;
    private final String id;

    public Guid(String code) {
        this.code = code;
        this.id = UUID.randomUUID().toString();
    }


}
