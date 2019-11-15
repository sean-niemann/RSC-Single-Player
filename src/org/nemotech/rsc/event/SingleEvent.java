package org.nemotech.rsc.event;

import org.nemotech.rsc.model.player.Player;

public abstract class SingleEvent extends DelayedEvent {
    
    public SingleEvent(Player owner, int delay) {
        super(owner, delay);
    }
    
    @Override
    public void run() {
        action();
        super.running = false;
    }
    
    public abstract void action();

}