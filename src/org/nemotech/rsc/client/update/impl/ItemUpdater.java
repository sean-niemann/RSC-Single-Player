package org.nemotech.rsc.client.update.impl;

import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.landscape.ActiveTile;
import org.nemotech.rsc.client.update.Updater;
import org.nemotech.rsc.util.StatefulEntityCollection;

import java.util.Collection;

public class ItemUpdater extends Updater {
    
    @Override
    public void handlePositionUpdate(Player player) {
        StatefulEntityCollection<Item> watchedItems = player.getWatchedItems();
        if(watchedItems.changed()) {
            Collection<Item> newItems = watchedItems.getNewEntities();
            Collection<Item> knownItems = watchedItems.getKnownEntities();
            for(Item item : knownItems) {
                if(watchedItems.isRemoving(item)) {
                    int id = item.getID();
                    int x = item.getX() - mc.regionX;
                    int y = item.getY() - mc.regionY;
                    int count = 0;
                    for(int i = 0; i < mc.groundItemCount; i++) {
                        if(mc.groundItemX[i] != x || mc.groundItemY[i] != y || mc.groundItemID[i] != id) {
                            if(i != count) {
                                mc.groundItemX[count] = mc.groundItemX[i];
                                mc.groundItemY[count] = mc.groundItemY[i];
                                mc.groundItemID[count] = mc.groundItemID[i];
                                mc.groundItemZ[count] = mc.groundItemZ[i];
                            }
                            count++;
                        }
                    }
                    mc.groundItemCount = count;
                }
            }
            for(Item item : newItems) {
                ActiveTile tile = World.getWorld().getTile(item.getLocation());
                int id = item.getID();
                int x = item.getX() - mc.regionX;
                int y = item.getY() - mc.regionY;
                int z = 0;
                if(x > 0 && y > 0) {
                    mc.groundItemX[mc.groundItemCount] = x;
                    mc.groundItemY[mc.groundItemCount] = y;
                    mc.groundItemID[mc.groundItemCount] = id;
                    if(tile.hasGameObject()) {
                        z = tile.getGameObject().getGroundItemVar();
                    }
                    mc.groundItemZ[mc.groundItemCount] = z;
                    mc.groundItemCount++;
                }
            }
        }
    }
    
}