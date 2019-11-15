package org.nemotech.rsc.event.impl;

import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.landscape.ActiveTile;

public class RemoveObjectEvent extends DelayedEvent {
    
    private GameObject object;

    public RemoveObjectEvent(GameObject object, int delay) {
        super(null, delay);
        this.object = object;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RemoveObjectEvent) {
            return ((RemoveObjectEvent) o).getObject().equals(getObject());
        }
        return false;
    }

    public GameObject getObject() {
        return object;
    }

    @Override
    public void run() {
        ActiveTile tile = world.getTile(object.getLocation());
        if (!tile.hasGameObject() || !tile.getGameObject().equals(object)) {
            super.running = false;
            return;
        }
        tile.remove(object);
        world.unregisterGameObject(object);
        super.running = false;
    }

}