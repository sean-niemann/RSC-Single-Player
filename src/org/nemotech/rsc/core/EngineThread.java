package org.nemotech.rsc.core;

import java.util.ConcurrentModificationException;

import org.nemotech.rsc.Constants;
import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.client.mudclient;
import org.nemotech.rsc.model.Point3D;
import org.nemotech.rsc.model.World;

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

        eventHandler.add(new DelayedEvent(null, 1 * 500) { /* 500 ms */
            @Override
            public void run() {
                if(World.getWorld().getPlayer().isLoggedIn()) {
                    checkMusicChange();
                }
            }
        });
        
        while (running) {
            try { Thread.sleep(50); } catch(InterruptedException ie) {}
            processEvents();
            processClients();
        }
    }
    
    private void checkMusicChange() {
        Player player = World.getWorld().getPlayer();
        int x = player.getX();
        int y = player.getY();
        Point3D sector = getSectorFromCoords(x, y);
        if(!mudclient.getInstance().optionMusicAuto) {
            return;
        }
        if(!mudclient.getInstance().currentSector.equals(sector)) {
            mudclient.getInstance().currentSector = sector;
            String previousSong = mudclient.getInstance().getMusicPlayer().getCurrentSong();
            if(previousSong.equals("scape_original.mid")) {
                mudclient.getInstance().getMusicPlayer().stop();
            }
            String song = getSongBySector(mudclient.getInstance().currentSector);
            if(song != null && !song.endsWith("null.mid")) {
                String currentSong = mudclient.getInstance().getMusicPlayer().getCurrentSong();
                // prevent starting the same song over again (if the player went downstairs and back upstairs, etc)
                if(currentSong == null || !song.equals(currentSong)) {
                    mudclient.getInstance().getMusicPlayer().start(song);
                }
            }
        }
        
    }
    
    private String getSongBySector(Point3D sector) {
        return Constants.MUSIC_REGION_MAP.get(sector) + ".mid";
    }
    
    private Point3D getSectorFromCoords(int x, int y) {
        int sectorH = 0;
        int sectorX = 0;
        int sectorY = 0;
        if (x != -1 && y != -1) {
            if (y >= 0 && y <= 1007)
                sectorH = 0;
            else if (y >= 1007 && y <= 1007 + 943) {
                sectorH = 1;
                y -= 943;
            } else if (y >= 1008 + 943 && y <= 1007 + (943 * 2)) {
                sectorH = 2;
                y -= 943 * 2;
            } else {
                sectorH = 3;
                y -= 943 * 3;
            }
            sectorX = (x / 48) + 48;
            sectorY = (y / 48) + 37;
        }
        return new Point3D(sectorX, sectorY, sectorH);
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
