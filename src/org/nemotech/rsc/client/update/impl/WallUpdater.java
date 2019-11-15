package org.nemotech.rsc.client.update.impl;

import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.client.Model;
import org.nemotech.rsc.client.update.Updater;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.util.StatefulEntityCollection;

import java.util.Collection;

public class WallUpdater extends Updater {
    
    @Override
    public void handlePositionUpdate(Player player) {
        StatefulEntityCollection<GameObject> watchedObjects = player.getWatchedObjects();
        if(watchedObjects.changed()) {
            Collection<GameObject> newObjects = watchedObjects.getNewEntities();
            Collection<GameObject> knownObjets = watchedObjects.getKnownEntities();
            for(GameObject wall : knownObjets) {
                if(wall.getType() != 1) {
                    continue;
                }
                // we should remove ones miles away differently I think
                if(watchedObjects.isRemoving(wall)) {
                    int id = wall.getID();
                    int x = wall.getX() - mc.regionX;
                    int y = wall.getY() - mc.regionY;
                    int direction = wall.getDirection();
                    
                    int count = 0;
                    for (int i = 0; i < mc.wallObjectCount; i++) {
                        if (mc.wallObjectX[i] != x || mc.wallObjectY[i] != y || mc.wallObjectDirection[i] != direction) {
                            if (i != count) {
                                mc.wallObjectModel[count] = mc.wallObjectModel[i];
                                mc.wallObjectModel[count].key = count + 10000;
                                mc.wallObjectX[count] = mc.wallObjectX[i];
                                mc.wallObjectY[count] = mc.wallObjectY[i];
                                mc.wallObjectDirection[count] = mc.wallObjectDirection[i];
                                mc.wallObjectId[count] = mc.wallObjectId[i];
                            }
                            count++;
                        } else {
                            mc.scene.removeModel(mc.wallObjectModel[i]);
                            mc.world.updateDoor(mc.wallObjectX[i], mc.wallObjectY[i], mc.wallObjectDirection[i], mc.wallObjectId[i]);
                        }
                    }
                    mc.wallObjectCount = count;
                }
            }
            for(GameObject wall : newObjects) {
                if(wall.getType() != 1) {
                    continue;
                }
                
                int id = wall.getID();
                int x = wall.getX() - mc.regionX;
                int y = wall.getY() - mc.regionY;
                int direction = wall.getDirection();
                
                mc.world.method408(x, y, direction, id);
                Model model = mc.createWallModel(x, y, direction, id, mc.wallObjectCount);
                mc.wallObjectModel[mc.wallObjectCount] = model;
                mc.wallObjectX[mc.wallObjectCount] = x;
                mc.wallObjectY[mc.wallObjectCount] = y;
                mc.wallObjectId[mc.wallObjectCount] = id;
                mc.wallObjectDirection[mc.wallObjectCount++] = direction;
            }
        }
    }
    
}