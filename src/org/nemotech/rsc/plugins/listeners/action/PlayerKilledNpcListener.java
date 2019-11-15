package org.nemotech.rsc.plugins.listeners.action;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

public interface PlayerKilledNpcListener {

    public void onPlayerKilledNpc(Player player, NPC npc);
    
}