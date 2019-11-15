package org.nemotech.rsc.event.impl;

import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.model.player.Player;

public abstract class BatchEvent extends DelayedEvent {

    private int repeatFor;
    private int repeated;
    
    public BatchEvent(Player owner, int delay, int repeatFor) {
        super(owner, delay);
        this.repeatFor = repeatFor;
        owner.setBusyTimer(delay + 200);
    }

    @Override
    public void run() {
        if (repeated < getRepeatFor()) {
            owner.setBusyTimer(delay + 200);
            action();
            repeated++;
            if(repeated >= getRepeatFor()) {
                interrupt();
            }
        }
    }

    public abstract void action();
    
    public boolean isCompleted() {
        return (repeated + 1) >= getRepeatFor() || !running;
    }
    
    @Override
    public void interrupt() {
        owner.setBusyTimer(0);
        owner.setBatchEvent(null);
        running = false;
    }

    public int getRepeatFor() {
        return repeatFor;
    }
    
    public void setRepeatFor(int i) {
        repeatFor = i;
    }
    
}