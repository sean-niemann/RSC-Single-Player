package org.nemotech.rsc.plugins.listeners.action;

import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.player.Player;

public interface PickupListener {
    
    public void onPickup(Player p, Item i);
    
}