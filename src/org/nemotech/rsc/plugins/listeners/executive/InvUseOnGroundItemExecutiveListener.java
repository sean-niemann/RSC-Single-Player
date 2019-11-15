package org.nemotech.rsc.plugins.listeners.executive;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.player.Player;

public interface InvUseOnGroundItemExecutiveListener {
    
    public boolean blockInvUseOnGroundItem(InvItem item, Item groundItem, Player player);
    
}