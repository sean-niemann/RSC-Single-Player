package org.nemotech.rsc.plugins.listeners.action;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;

public interface UnwieldListener {

    public void onUnwield(Player player, InvItem item);

}