package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.event.SingleEvent;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.player.states.Action;
import org.nemotech.rsc.client.sound.SoundEffect;
import org.nemotech.rsc.client.action.ActionHandler;

public class DropHandler implements ActionHandler {
    
    public void handleDrop(int index) {
        Player player = World.getWorld().getPlayer();
        
        if (player.isBusy()) {
            player.resetPath();
            return;
        }
        player.reset();
        
        final InvItem item = player.getInventory().get(index);
        
        if (PluginManager.getInstance().blockDefaultAction("Drop", new Object[]{player, item})) {
            return;
        }

        // drop item after a path has finished
        if(player.getPathHandler() != null && !player.getPathHandler().finishedPath()) {
            waitAndDrop(player, item);
        } else {
            drop(player, item);
        }
    }
    
    public void drop(Player player, final InvItem item) {
        player.setStatus(Action.DROPPING_GITEM);
        World.getWorld().getDelayedEventHandler().add(new DelayedEvent(player, 500) {
            public void run() {
                if (owner.isBusy() || !owner.getInventory().contains(item) || owner.getStatus() != Action.DROPPING_GITEM) {
                    running = false;
                    return;
                }
                if (owner.hasMoved()) {
                    this.interrupt();
                    return;
                }
                owner.getSender().sendSound(SoundEffect.DROP_OBJECT);
                owner.getInventory().remove(item);
                owner.getSender().sendInventory();
                world.registerItem(new Item(item.getID(), owner.getX(), owner.getY(), item.getAmount(), owner));
                running = false;
            }
        });
    }
    
    public void waitAndDrop(final Player player,final InvItem item) {
        World.getWorld().getDelayedEventHandler().add(new SingleEvent(player, 500) {
            public void action() {
                if(owner.dropTickCount > 100) { // stop after 100 ticks, something went wrong
                    owner.dropTickCount = 0;
                    interrupt();
                } else {
                    owner.dropTickCount++;
                    if(owner.getPathHandler() != null && !owner.getPathHandler().finishedPath()) {
                        waitAndDrop(owner, item);
                    } else {
                        drop(owner, item);
                        owner.dropTickCount = 0;
                    }
                }
            }
        });
    }
    
}