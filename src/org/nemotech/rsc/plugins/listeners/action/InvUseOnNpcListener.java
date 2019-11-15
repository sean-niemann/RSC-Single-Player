package org.nemotech.rsc.plugins.listeners.action;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

public interface InvUseOnNpcListener {
    
    public void onInvUseOnNpc(Player player, NPC npc, InvItem item);

}