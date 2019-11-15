package org.nemotech.rsc.plugins.listeners.executive;

import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.player.Player;

public interface PickupExecutiveListener {
    
    public boolean blockPickup(Player p, Item i);
    
}