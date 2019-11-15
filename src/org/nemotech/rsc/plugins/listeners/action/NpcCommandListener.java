package org.nemotech.rsc.plugins.listeners.action;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

public interface NpcCommandListener {

    public void onNpcCommand(NPC n, String command, Player p);
    
}