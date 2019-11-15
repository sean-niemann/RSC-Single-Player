package org.nemotech.rsc.plugins.listeners.action;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;

public interface InvUseOnItemListener {

    public void onInvUseOnItem(Player player, InvItem item1, InvItem item2);
    
}