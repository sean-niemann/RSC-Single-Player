package org.nemotech.rsc.plugins.items;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.listeners.action.InvActionListener;
import org.nemotech.rsc.plugins.listeners.executive.InvActionExecutiveListener;

public class SleepingBag extends Plugin implements InvActionListener, InvActionExecutiveListener {
    
    @Override
    public boolean blockInvAction(InvItem item, Player player) {
        return item.getID() == 1263;
    }

    @Override
    public void onInvAction(InvItem item, Player player) {
        player.getSender().sendMessage("You rest in the sleeping bag");
        player.getSender().sendEnterSleep();
        player.startSleepEvent(false);
    }
    
}