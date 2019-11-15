package org.nemotech.rsc.plugins.listeners.executive;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;

public interface InvUseOnItemExecutiveListener {

    public boolean blockInvUseOnItem(Player player, InvItem item1, InvItem item2);

}