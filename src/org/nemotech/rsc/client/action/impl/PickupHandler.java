package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.Point;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.landscape.ActiveTile;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.event.WalkToPointEvent;
import org.nemotech.rsc.model.player.states.Action;
import org.nemotech.rsc.client.sound.SoundEffect;
import org.nemotech.rsc.client.action.ActionHandler;
import org.nemotech.rsc.model.ChatMessage;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.plugins.Plugin;

public class PickupHandler implements ActionHandler {
    
    public void handlePickup(int x, int y, int id) {
        Player player = World.getWorld().getPlayer();
        
        if (player.isBusy()) {
            player.resetPath();
            return;
        }
        player.reset();
        Point location = new Point(x, y);
        final ActiveTile tile = World.getWorld().getTile(location);
        final Item item = getItem(id, tile, player);
        player.setStatus(Action.TAKING_GITEM);
        World.getWorld().getDelayedEventHandler().add(new WalkToPointEvent(player, location, 1, false) {
            @Override
            public void arrived() {
                if (owner.isBusy() || owner.isRanging() || !tile.hasItem(item) || !owner.withinRange(item, 1) || owner.getStatus() != Action.TAKING_GITEM) {
                    return;
                }
                
                if(item.getDef().isMembers() && !Constants.MEMBER_WORLD) {
                    owner.getSender().sendMessage(Constants.MEMBERS_ONLY_MESSAGE);
                    return;
                }
                
                if(item.getID() == 23) {
                    if(player.getInventory().hasItemId(135)) {
                        player.message("You put the flour in the pot");
                        Plugin.showBubble(owner, item.getID());
                        world.unregisterItem(item);
                        player.getInventory().replace(135, 136);
                    } else {
                        player.message("I can't pick it up!");
                        player.message("I need a pot to hold it in");
                    }
                    return;
                }
                
                owner.reset();
                InvItem invItem = new InvItem(item.getID(), item.getAmount());
                
                if (!owner.getInventory().canHold(invItem)) {
                    owner.getSender().sendMessage("You don't have room for this object!");
                    return;
                }
                
                if (PluginManager.getInstance().blockDefaultAction("Pickup", new Object[]{owner, item})) {
                    return;
                }
                
                if (item.getID() == 59 && item.getX() == 106 && item.getY() == 1476) {
                    NPC n = world.getNpc(37, 103, 107, 1476, 1479);
                    if (n != null && !n.inCombat()) {
                        player.informOfNPCMessage(new ChatMessage(n, "Hey thief!", player));
                        n.setChasing(player);
                    }
                }

                world.unregisterItem(item);
                owner.getSender().sendSound(SoundEffect.TAKE_OBJECT);
                owner.getInventory().add(invItem);
                owner.getSender().sendInventory();
            }
        });
    }
    
    private Item getItem(int id, ActiveTile tile, Player player) {
        if (tile != null)
            for (Item i : tile.getItems()) {
            if (i.getID() == id && i.visibleTo(player)) {
                return i;
            }
        }
        return null;
    }
    
}