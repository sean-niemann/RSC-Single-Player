package org.nemotech.rsc.plugins.misc;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.showBubble;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnNpcExecutiveListener;

public class Cow implements InvUseOnNpcListener, InvUseOnNpcExecutiveListener{

    @Override
    public boolean blockInvUseOnNpc(Player player, NPC npc, InvItem item) {
        return npc.getID() == 217 && item.getID() == 21 || npc.getID() == 6 && item.getID() == 21;
    }

    @Override
    public void onInvUseOnNpc(Player player, NPC npc, InvItem item) {
        npc.resetPath();
        npc.face(player);
        npc.setBusy(true);
        showBubble(player, item);
        if (removeItem(player, item.getID(), 1)) {
            addItem(player, 22, 1);
        }
        message(player, 3500, "You milk the cow");
        npc.setBusy(false);
    }
    
}