package org.nemotech.rsc.plugins.misc;

import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.showBubble;

import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnObjectListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnObjectExecutiveListener;

public class CrystalChest implements InvUseOnObjectListener,
        InvUseOnObjectExecutiveListener {

    @Override
    public boolean blockInvUseOnObject(GameObject obj, InvItem item, Player player) {
        // TODO Auto-generated method stub
        return item.getID() == 525 && obj.getID() == 248;
    }

    @Override
    public void onInvUseOnObject(GameObject obj, InvItem item, Player player) {
        showBubble(player, item);
        message(player, "You use the key to unlock the chest");
        if (player.getInventory().remove(item) > -1) {
            player.getInventory().add(new InvItem(542, 1));
            InvItem[] loot = null;
            int percent = Util.random(0, 100);
            if (percent <= 100) {
                loot = new InvItem[] { new InvItem(179, 1), new InvItem(10, 2000) };
            }
            if (percent < 80) {
                loot = new InvItem[] { new InvItem(179, 1), new InvItem(10, 2000) };
            }
            if (percent < 60) {
                loot = new InvItem[] { new InvItem(179, 1), new InvItem(536, 1),
                        new InvItem(10, 1000) };
            }
            if (percent < 40) {
                loot = new InvItem[] { new InvItem(33, 50), new InvItem(32, 50),
                        new InvItem(34, 50), new InvItem(31, 50), new InvItem(35, 50),
                        new InvItem(36, 50), new InvItem(41, 10), new InvItem(46, 10),
                        new InvItem(40, 10), new InvItem(42, 10), new InvItem(38, 10) };
            }
            if (percent < 18) {
                loot = new InvItem[] { new InvItem(518, 20) };
            }
            if (percent < 15) {
                loot = new InvItem[] { new InvItem(162, 2), new InvItem(161, 2) };
            }
            if (percent < 12) {
                loot = new InvItem[] { new InvItem(526, 1), new InvItem(10, 750) };
            }
            if (percent < 10) {
                if (Util.random(0, 1) == 1) {
                    loot = new InvItem[] { new InvItem(526, 1), new InvItem(10, 750) };
                } else
                    loot = new InvItem[] { new InvItem(527, 1), new InvItem(10, 750) };
            }
            if (percent < 5) {
                loot = new InvItem[] { new InvItem(408, 3) };
            }
            if (percent < 5) {
                loot = new InvItem[] { new InvItem(517, 30) };
            }
            if (percent < 2) {
                loot = new InvItem[] { new InvItem(127, 1) };
            }
            if (percent < 1) {
                loot = new InvItem[] { new InvItem(402, 1) };
            }
            for (InvItem i : loot) {
                if (i.getAmount() > 1 && !i.getDef().isStackable()) {
                    for (int x = 0; x < i.getAmount(); x++) {
                        player.getInventory().add(new InvItem(i.getID(), 1));
                    }
                } else {
                    player.getInventory().add(i);
                }
            }
        }
    }

}
