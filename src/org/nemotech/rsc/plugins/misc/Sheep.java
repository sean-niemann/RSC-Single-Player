package org.nemotech.rsc.plugins.misc;

import org.nemotech.rsc.event.SingleEvent;
import org.nemotech.rsc.event.impl.BatchEvent;
import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.random;
import static org.nemotech.rsc.plugins.Plugin.showBubble;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnNpcExecutiveListener;
import org.nemotech.rsc.util.Formulae;

public class Sheep extends Plugin implements InvUseOnNpcListener, InvUseOnNpcExecutiveListener {

    @Override
    public boolean blockInvUseOnNpc(Player player, NPC npc, InvItem item) {
        return npc.getID() == 2 && item.getID() == 144;
    }

    @Override
    public void onInvUseOnNpc(final Player player, final NPC npc, InvItem item) {
        npc.resetPath();
        
        npc.face(player);
        player.face(npc);
        showBubble(player, item);
        player.message("You attempt to shear the sheep");
        npc.setBusyTimer(1600);
        player.setBatchEvent(new BatchEvent(player, 1500, Formulae.getRepeatTimes(player, CRAFTING)) {
            @Override
            public void action() {
                npc.setBusyTimer(1600);
                if(random(0, 4) != 0) {
                    owner.message("You get some wool");
                    addItem(owner, 145, 1);
                } else {
                    owner.message("The sheep manages to get away from you!");
                    npc.setBusyTimer(0);
                    interrupt();
                }
            }
        });
    }
}