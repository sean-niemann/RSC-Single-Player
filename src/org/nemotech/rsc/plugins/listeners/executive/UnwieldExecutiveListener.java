package org.nemotech.rsc.plugins.listeners.executive;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;

public interface UnwieldExecutiveListener {
    
    public boolean blockUnwield(Player player, InvItem item);
    
}