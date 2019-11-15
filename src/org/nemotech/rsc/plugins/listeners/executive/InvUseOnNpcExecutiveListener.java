package org.nemotech.rsc.plugins.listeners.executive;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

public interface InvUseOnNpcExecutiveListener {
    
    public boolean blockInvUseOnNpc(Player player, NPC npc, InvItem item);

}