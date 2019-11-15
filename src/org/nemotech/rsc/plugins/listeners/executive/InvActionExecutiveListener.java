package org.nemotech.rsc.plugins.listeners.executive;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;

public interface InvActionExecutiveListener {

    public boolean blockInvAction(InvItem item, Player player);
    
}