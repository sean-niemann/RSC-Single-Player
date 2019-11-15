package org.nemotech.rsc.event;

import org.nemotech.rsc.model.player.Player;

public abstract class MiniEvent extends SingleEvent {
    
    public MiniEvent(Player owner) {
        super(owner, 500);
    }
    
    public MiniEvent(Player owner, int delay) {
        super(owner, delay);
    }
    
    @Override
    public abstract void action();

}