package org.nemotech.rsc.plugins.listeners.action;

import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;

public interface InvUseOnWallObjectListener {
    
    public void onInvUseOnWallObject(GameObject door, InvItem item, Player player);
    
}