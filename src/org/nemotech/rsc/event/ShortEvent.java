package org.nemotech.rsc.event;

import org.nemotech.rsc.model.player.Player;

public abstract class ShortEvent extends SingleEvent {
    
    public ShortEvent(Player owner) {
        super(owner, 1500);
    }
    
    @Override
    public abstract void action();

}