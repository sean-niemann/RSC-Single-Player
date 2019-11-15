package org.nemotech.rsc.plugins.listeners.executive;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

public interface PlayerKilledNpcExecutiveListener {
    
    public boolean blockPlayerKilledNpc(Player player, NPC npc);
    
}