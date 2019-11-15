package org.nemotech.rsc.plugins.listeners.action;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;

public interface DropListener {
    
    public void onDrop(Player p, InvItem i);
    
}
