package org.nemotech.rsc.event;

import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.GameObject;

public abstract class WalkToObjectEvent extends DelayedEvent {
    
    protected GameObject obj;
    private boolean stop;
    
    public WalkToObjectEvent(Player owner, GameObject object, boolean stop) {
        super(owner, 500);
        this.obj = object;
        this.stop = stop;
        if(stop && owner.atObject(object)) {
            owner.resetPath();
            arrived();
            super.running = false;
        }
    }
    
    @Override
    public final void run() {
        if(stop && owner.atObject(obj)) {
            owner.resetPath();
            arrived();
        }
        else if(owner.hasMoved()) {
            return; // We're still moving
        }
        else if(owner.atObject(obj)) {
            arrived();
        }
        super.running = false;
    }
    
    public abstract void arrived();

}