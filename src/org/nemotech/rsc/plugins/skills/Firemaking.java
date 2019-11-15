package org.nemotech.rsc.plugins.skills;

import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.landscape.ActiveTile;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnGroundItemListener;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnItemListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnGroundItemExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnItemExecutiveListener;

/**
 * Handles the firemaking skill.
 * @author Sean Niemann
 */
public class Firemaking extends Plugin implements InvUseOnGroundItemListener, InvUseOnItemListener,
    InvUseOnGroundItemExecutiveListener, InvUseOnItemExecutiveListener {
    
    @Override
    public boolean blockInvUseOnGroundItem(InvItem item, Item groundItem, Player player) {
        if(item.getID() == TINDERBOX) {
            return true;
        }
        return false;
    }

    @Override
    public boolean blockInvUseOnItem(Player player, InvItem item1, InvItem item2) {
        if((item1.getID() == TINDERBOX && Util.inArray(LOG_ARRAY, item2.getID())) || (item2.getID() == TINDERBOX && Util.inArray(LOG_ARRAY, item1.getID()))) {
            return true;
        }
        return false;
    }
    
    // Inventory item used
    int TINDERBOX = 166;
    
    // Ground items used (level, id, exp, length)
    int[] NORMAL_LOGS = { 1, 14, 25, 90 };
    int[] OAK_LOGS    = { 15, 632, 35, 110 };
    int[] WILLOW_LOGS = { 30, 633, 45, 130 };
    int[] MAPLE_LOGS  = { 45, 634, 55, 150 };
    int[] YEW_LOGS    = { 60, 635, 65, 170 };
    int[] MAGIC_LOGS  = { 75, 636, 75, 190 };
    
    int[] LOG_ARRAY = { 14, 632, 633, 634, 635, 636 };
    
    @Override
    public void onInvUseOnGroundItem(InvItem invItem, Item groundItem, Player player) {
        if(invItem.getID() == TINDERBOX) {
            int level = 1;
            int[] logType = null;
            boolean members = false;
            if(groundItem.getID() == NORMAL_LOGS[level]) {
                logType = NORMAL_LOGS;
            } else if(groundItem.getID() == OAK_LOGS[level]) {
                logType = OAK_LOGS;
                members = true;
            } else if(groundItem.getID() == WILLOW_LOGS[level]) {
                logType = WILLOW_LOGS;
                members = true;
            } else if(groundItem.getID() == MAPLE_LOGS[level]) {
                logType = MAPLE_LOGS;
                members = true;
            } else if(groundItem.getID() == YEW_LOGS[level]) {
                logType = YEW_LOGS;
                members = true;
            } else if(groundItem.getID() == MAGIC_LOGS[level]) {
                logType = MAGIC_LOGS;
                members = true;
            } else {
                player.getSender().sendMessage("You can only light logs with this");
            }
            if(logType != null) {
                if(members && !Constants.MEMBER_WORLD) {
                    player.getSender().sendMessage("You can only light these logs on a members server");
                    return;
                }
                handleFireMaking(player, invItem, groundItem, (int) Math.ceil(player.getMaxStat(FIREMAKING) / 10), logType);
            }
        }
    }
    
    boolean lightLogs(int curLvl, int reqLvl) {
        int difference = curLvl - reqLvl;
        if (difference < 0) {
            return false;
        }
        if (difference >= 20) {
            return true;
        }
        return Util.random(0, difference + 1) != 0;
    }
    
    void handleFireMaking(Player player, InvItem myItem, final Item groundItem, int tries, int[] logType) {
        int retries = --tries;
        int level = 0;
        int exp = 2;
        int length = 3;
        final ActiveTile tile = world.getTile(groundItem.getLocation());
        if(tile.hasGameObject()) {
            player.getSender().sendMessage("You cannot do that here, please move to a new area");
            return;
        }
        if(player.getCurStat(FIREMAKING) < logType[level]) {
            player.getSender().sendMessage("You need at least " + logType[level] + " firemaking to light these logs");
            return;
        }
        player.setBusy(true);
        showBubble(player, TINDERBOX);
        player.getSender().sendMessage("You attempt to light the logs...");
        sleep(1500);
        if(lightLogs(player.getCurStat(FIREMAKING), logType[level])) {
            player.getSender().sendMessage("They catch fire and start to burn");
            world.unregisterItem(groundItem);
            final GameObject fire = new GameObject(groundItem.getLocation(), 97, 0, 0);
            world.registerGameObject(fire);
            world.getDelayedEventHandler().add(new DelayedEvent(null, logType[length] * 1000) {
                @Override
                public void run() {
                    if (tile.hasGameObject() && tile.getGameObject().equals(fire)) {
                        world.unregisterGameObject(fire);
                        world.registerItem(new Item(181, groundItem.getX(), groundItem.getY(), 1, null));
                    }
                }
            });
            player.incExp(FIREMAKING, logType[exp], true);
            player.setBusy(false);
        } else {
            player.getSender().sendMessage("You fail to light them");
            player.setBusy(false);
            if(retries > 0) {
                handleFireMaking(player, myItem, groundItem, retries, logType);
            }
        }
    }

    @Override
    public void onInvUseOnItem(Player player, InvItem item1, InvItem item2) {
        if((item1.getID() == TINDERBOX && Util.inArray(LOG_ARRAY, item2.getID())) || (item2.getID() == TINDERBOX && Util.inArray(LOG_ARRAY, item1.getID()))) {
            player.getSender().sendMessage("Please drop the logs before lighting them");
        }
    }
    
}