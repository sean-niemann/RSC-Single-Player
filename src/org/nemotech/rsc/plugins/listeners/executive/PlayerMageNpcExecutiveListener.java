package org.nemotech.rsc.plugins.listeners.executive;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

public interface PlayerMageNpcExecutiveListener {

    public boolean blockPlayerMageNpc(Player player, NPC npc, Integer spell);
    
}