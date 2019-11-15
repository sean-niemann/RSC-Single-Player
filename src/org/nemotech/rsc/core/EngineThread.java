package org.nemotech.rsc.core;

import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.model.player.Player;

import java.util.ConcurrentModificationException;

/**
 * Core tasks will be run on this thread.
 * 
 */
public class EngineThread extends Thread {
    
    /**
     * Whether the engine's thread is running
     */
    private boolean running = true;
    /**
     * Responsible for updating all connected clients
     */
    private ClientUpdater clientUpdater = new ClientUpdater();
    /**
     * Handles delayed events rather than events to be ran every iteration
     */
    private DelayedEventHandler eventHandler = new DelayedEventHandler();
    /**
     * When the update loop was last ran, required for throttle
     */
    private long lastSentClientUpdate = 0;
        
    /**
     * The thread execution process.
     */
    @Override
    public void run() {
        running = true;
        
        eventHandler.add(new DelayedEvent(null, 20 * 1000) { /* 20 seconds */
            @Override
            public void run() {
                Player player = world.getPlayer();
                if(player != null && player.isLoggedIn()) {
                    player.save();
                    //System.out.println(player.getUsername() + "'s character was automatically saved");
                }
            }
        });
        
        while (running) {
            try { Thread.sleep(50); } catch(InterruptedException ie) {}
            processEvents();
            processClients();
        }
    }
    
    public void kill() {
        System.out.println("terminating engine");
        running = false;
    }
    
    private void processEvents() {
        try {
            eventHandler.doEvents();
        } catch(ConcurrentModificationException cme) {
            // ignore
        }
    }
    
    private void processClients() {
        clientUpdater.checkIfPlayerIsDestroyed();
        
        long now = System.currentTimeMillis();
        if(now - lastSentClientUpdate >= 600) {
            lastSentClientUpdate = now;
            //clientUpdater.updateClients();
        }
    }

}
