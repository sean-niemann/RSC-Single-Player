package org.nemotech.rsc.plugins.listeners.action;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.player.Player;

public interface InvUseOnGroundItemListener {
    
    public void onInvUseOnGroundItem(InvItem item, Item groundItem, Player player);
    
}