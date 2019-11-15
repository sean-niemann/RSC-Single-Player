package org.nemotech.rsc.plugins.listeners.executive;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

public interface PlayerAttackNpcExecutiveListener {
    
    public boolean blockPlayerAttackNpc(Player player, NPC npc);

}