package org.nemotech.rsc.core;

import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.model.player.Player;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import org.nemotech.rsc.client.mudclient;
import org.nemotech.rsc.model.Point;
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
    
    private Point3D currentSector = new Point3D(0, 0, 0);
    
    private void checkMusicChange() {
        Player player = World.getWorld().getPlayer();
        int x = player.getX();
        int y = player.getY();
        Point3D sector = getSectorFromCoords(x, y);
        if(!currentSector.equals(sector)) {
            currentSector = sector;
            System.out.println("Sector Changed: " + currentSector.toString() + " Song: " + getSongBySector(currentSector));
            String song = getSongBySector(currentSector);
            if(song != null && !song.equals("null")) {
                mudclient.getInstance().getMusicPlayer().start(getSongBySector(currentSector));
            }
        }
        
    }
    
    private Map<Point3D, String> musicMap = new HashMap<Point3D, String>() {{
        put(new Point3D(49, 49, 0), "arabian_2.midi");
        put(new Point3D(49, 50, 0), "arabian.midi");
        put(new Point3D(49, 51, 0), "al_kharid.midi");
        put(new Point3D(50, 50, 0), "harmony.midi");
        put(new Point3D(50, 49, 0), "autumn_voyage.midi");
        put(new Point3D(50, 51, 0), "yesteryear.midi");
        put(new Point3D(51, 50, 0), "dream.midi");
        put(new Point3D(51, 49, 0), "flute_salad.midi");
        put(new Point3D(51, 51, 0), "book_of_spells.midi");
    }};
    
    private String getSongBySector(Point3D sector) {
        return musicMap.get(sector);
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
