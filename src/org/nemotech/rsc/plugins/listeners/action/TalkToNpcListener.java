package org.nemotech.rsc.plugins.listeners.action;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

public interface TalkToNpcListener {
    
    public void onTalkToNpc(Player player, NPC npc);
    
}