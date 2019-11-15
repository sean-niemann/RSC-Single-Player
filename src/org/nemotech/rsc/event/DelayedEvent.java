package org.nemotech.rsc.event;

import org.nemotech.rsc.core.DelayedEventHandler;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.player.Player;

public abstract class DelayedEvent {
    
    public static final World world = World.getWorld();
    protected boolean running = true;
    protected int delay = 500;
    protected Player owner;
    private long lastRun = System.currentTimeMillis();
    protected final DelayedEventHandler handler = World.getWorld().getDelayedEventHandler();
    
    public DelayedEvent(Player owner, int delay) {
        this.owner = owner;
        this.delay = delay;
    }

    public boolean is(DelayedEvent e) {
        return (e.getIdentifier() != null && e.getIdentifier().equals(getIdentifier()));
    }

    public Object getIdentifier() {
        return null;
    }
    
    public void setDelay(int delay) {
        this.delay = delay;
    }
    
    public void setLastRun(long time) {
        lastRun = time;
    }

    public final boolean shouldRun() {
        return running && System.currentTimeMillis() - lastRun >= delay;
    }
    
    public int timeTillNextRun() {
        int time = (int)(delay - (System.currentTimeMillis() - lastRun));
        return time < 0 ? 0 : time;
    }
    
    public abstract void run();
    
    public final void updateLastRun() {
        lastRun = System.currentTimeMillis();
    }
    
    public void interrupt() {
        if(owner != null) {
            owner.setBusyTimer(0);
        }
        running = false;
    }
    
    public final boolean shouldRemove() {
        return !running;
    }
    
    public boolean belongsTo(Player player) {
        return this.owner != null && this.owner.equals(player);
    }
    
    public boolean hasOwner() {
        return owner != null;
    }
    
    public Player getOwner() {
        return owner;
    }

}