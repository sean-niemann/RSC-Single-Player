package org.nemotech.rsc.plugins.listeners.action;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

public interface PlayerMageNpcListener {
    
    public void onPlayerMageNpc(Player player, NPC npc, Integer spell);
    
}