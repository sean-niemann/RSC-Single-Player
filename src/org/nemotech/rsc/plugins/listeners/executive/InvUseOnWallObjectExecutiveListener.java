package org.nemotech.rsc.plugins.listeners.executive;

import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;

public interface InvUseOnWallObjectExecutiveListener {

    public boolean blockInvUseOnWallObject(GameObject door, InvItem item, Player player);
    
}