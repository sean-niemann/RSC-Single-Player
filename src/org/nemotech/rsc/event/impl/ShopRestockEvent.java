package org.nemotech.rsc.event.impl;

import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.model.Shop;

public final class ShopRestockEvent extends DelayedEvent {

    private final Shop shop;

    public ShopRestockEvent(Shop shop) {
        super(null, shop.getRespawnRate());
        this.shop = shop;
    }

    @Override
    public void run() {
        shop.restock();
    }

}