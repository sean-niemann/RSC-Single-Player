package org.nemotech.rsc.client.update.impl;

import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.client.Model;
import org.nemotech.rsc.client.update.Updater;
import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.util.StatefulEntityCollection;

import java.util.Collection;

public class ObjectUpdater extends Updater {
    
    @Override
    public void handlePositionUpdate(Player player) {
        StatefulEntityCollection<GameObject> watchedObjects = player.getWatchedObjects();
        if(watchedObjects.changed()) {
            Collection<GameObject> newObjects = watchedObjects.getNewEntities();
            Collection<GameObject> knownObjets = watchedObjects.getKnownEntities();
            for(GameObject object : knownObjets) {
                if(object.getType() != 0) {
                    continue;
                }
                // We should remove ones miles away differently I think
                if(watchedObjects.isRemoving(object)) {
                    int id = object.getID();
                    int x = object.getX() - mc.regionX;
                    int y = object.getY() - mc.regionY;
                    int direction = object.getDirection();
                    int count = 0;
                    for(int i = 0; i < mc.objectCount; i++) {
                        if(mc.objectX[i] != x || mc.objectY[i] != y) {
                            if(i != count) {
                                mc.objectModel[count] = mc.objectModel[i];
                                mc.objectModel[count].key = count;
                                mc.objectX[count] = mc.objectX[i];
                                mc.objectY[count] = mc.objectY[i];
                                mc.objectID[count] = mc.objectID[i];
                                mc.objectDirection[count] = mc.objectDirection[i];
                            }
                            count++;
                        } else {
                            mc.scene.removeModel(mc.objectModel[i]);
                            mc.world.updateObject(mc.objectX[i], mc.objectY[i], mc.objectID[i], mc.objectDirection[i]);
                        }
                    }
                    mc.objectCount = count;
                }
            }
            for(GameObject object : newObjects) {
                if(object.getType() != 0) {
                    continue;
                }
                int id = object.getID();
                int x = object.getX() - mc.regionX;
                int y = object.getY() - mc.regionY;
                int direction = object.getDirection();
                //mc.world.registerObjectDir(x, y, direction);
                int width;
                int height;
                if (direction == 0 || direction == 4) {
                    width = EntityManager.getGameObjectDef(id).getWidth();
                    height = EntityManager.getGameObjectDef(id).getHeight();
                } else {
                    height = EntityManager.getGameObjectDef(id).getWidth();
                    width = EntityManager.getGameObjectDef(id).getHeight();
                }
                int mX = ((x + x + width) * mc.magicLoc) / 2;
                int mY = ((y + y + height) * mc.magicLoc) / 2;
                int modelIdx = EntityManager.getGameObjectDef(id).modelID;
                Model model = mc.gameModels[modelIdx].copy();
                mc.scene.addModel(model);
                model.key = mc.objectCount;
                model.rotate(0, direction * 32, 0);
                model.translate(mX, -mc.world.getAveragedElevation(mX, mY), mY);
                model.setLight(true, 48, 48, -50, -10, -50);
                mc.world.method412(x, y, id, direction);
                if (id == 74) {
                    model.translate(0, -480, 0);
                }
                mc.objectX[mc.objectCount] = x;
                mc.objectY[mc.objectCount] = y;
                mc.objectID[mc.objectCount] = id;
                mc.objectDirection[mc.objectCount] = direction;
                mc.objectModel[mc.objectCount++] = model;
            }
        }
    }
    
}