package org.nemotech.rsc.plugins.listeners.executive;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

public interface NpcCommandExecutiveListener {
    
    public boolean blockNpcCommand(NPC n, String command, Player p);

}