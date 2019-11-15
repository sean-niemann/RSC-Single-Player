package org.nemotech.rsc.plugins.listeners.executive;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

public interface PlayerRangeNpcExecutiveListener {
    
    public boolean blockPlayerRangeNpc(Player player, NPC npc);
    
}