package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.event.WalkToPointEvent;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.Point;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.landscape.ActiveTile;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.player.states.Action;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.client.action.ActionHandler;

public class InventoryUseOnGroundItemHandler implements ActionHandler {
    
    public void handleInventoryUseOnGroundItem(int x, int y, int itemInInventory, int itemOnGround) {
        Player player = World.getWorld().getPlayer();
        
        if (player.isBusy()) {
            player.resetPath();
            return;
        }
        player.reset();
        
        Point location = new Point(x, y);
        
        final ActiveTile tile = World.getWorld().getTile(location);
        if (tile == null) {
            return;
        }
        
        final InvItem invItem = player.getInventory().get(itemInInventory);
        
        if (tile.hasGameObject() && invItem.getID() != 135) {
            player.getSender().sendMessage("You cannot do that here, please move to a new area");
            return;
        }
        
        final Item groundItem = getItem(itemOnGround, player);
        
        if (invItem == null || groundItem == null) {
            player.resetPath();
            return;
        }
        
        player.setStatus(Action.USING_INVITEM_ON_GITEM);
        World.getWorld().getDelayedEventHandler().add(new WalkToPointEvent(player, location, 1, false) {
            public void arrived() {
                if (owner.isBusy() || owner.isRanging() || !tile.hasItem(groundItem) || !owner.withinRange(groundItem, 1) || owner.getStatus() != Action.USING_INVITEM_ON_GITEM) {
                    return;
                }

                if ((invItem.getDef().isMembers() || groundItem.getDef().isMembers()) && !Constants.MEMBER_WORLD) {
                    owner.getSender().sendMessage("Nothing interesting happens");
                    return;
                }

                if (PluginManager.getInstance().blockDefaultAction("InvUseOnGroundItem", new Object[]{ invItem, groundItem, owner })) {
                    return;
                }
            }
        });
    }
    
    private Item getItem(int id, Player player) {
        for (Item i : player.getViewArea().getItemsInView()) {
            if (i.getID() == id && i.visibleTo(player)) {
                return i;
            }
        } 
        return null;
    }
    
}