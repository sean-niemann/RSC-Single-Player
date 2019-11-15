package org.nemotech.rsc.plugins.minigames.fishingtrawler;

import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.plugins.listeners.action.InvActionListener;
import org.nemotech.rsc.plugins.listeners.executive.InvActionExecutiveListener;

public class BailingBucket implements InvActionExecutiveListener, InvActionListener {

    @Override
    public void onInvAction(InvItem item, Player player) {
        // TODO
        /*if (player.isBusy())
            return;
        if (World.getWorld().getFishingTrawler().getShipAreaWater().inBounds(player.getLocation())
                || World.getWorld().getFishingTrawler().getShipArea().inBounds(player.getLocation())) {
            player.setBusyTimer(650);
            player.message("you bail a little water...");
            sleep(650);
            World.getWorld().getFishingTrawler().bailWater();
        } else {
            // player.message("");
        }*/
    }

    @Override
    public boolean blockInvAction(InvItem item, Player player) {
        return item.getID() == 1282;
    }

}
