package org.nemotech.rsc.plugins.listeners.executive;

import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;

public interface InvUseOnObjectExecutiveListener {

    public boolean blockInvUseOnObject(GameObject obj, InvItem item, Player player);
    
}