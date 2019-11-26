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
        if(!mudclient.getInstance().musicAuto) {
            return;
        }
        if(!currentSector.equals(sector)) {
            currentSector = sector;
            String song = getSongBySector(currentSector);
            if(song != null && !song.endsWith("null.midi")) {
                // prevent starting the same song over again (if the player went downstairs and back upstairs, etc)
                if(!song.equals(mudclient.getInstance().getMusicPlayer().getCurrentSong())) {
                    mudclient.getInstance().getMusicPlayer().start(song);
                }
            }
        }
        
    }
    
    private Map<Point3D, String> musicMap = new HashMap<Point3D, String>() {{
        put(new Point3D(49, 49, 0), "arabian_2");
        put(new Point3D(49, 50, 0), "arabian");
        put(new Point3D(49, 51, 0), "al_kharid");
        put(new Point3D(50, 50, 0), "harmony");
        put(new Point3D(50, 49, 0), "autumn_voyage");
        put(new Point3D(50, 51, 0), "yesteryear");
        put(new Point3D(51, 50, 0), "dream");
        put(new Point3D(51, 49, 0), "flute_salad");
        put(new Point3D(51, 51, 0), "book_of_spells");
        
        put(new Point3D(49, 52, 0), "egypt");
        put(new Point3D(49, 53, 0), "desert_voyage");
        put(new Point3D(50, 52, 0), "arabian_3");
        put(new Point3D(50, 53, 0), "the_desert");
        put(new Point3D(51, 53, 0), "the_desert");
        
        put(new Point3D(52, 51, 0), "vision");
        put(new Point3D(52, 52, 0), "newbie_melody");
        put(new Point3D(53, 51, 0), "tomorrow");
        put(new Point3D(53, 50, 0), "sea_shanty_2");
        put(new Point3D(54, 50, 0), "long_way_home");
        put(new Point3D(54, 51, 0), "attention");
        put(new Point3D(55, 50, 0), "emperor");
        put(new Point3D(55, 49, 0), "miles_away");
        put(new Point3D(54, 49, 0), "nightfall");
        put(new Point3D(53, 49, 0), "wander");
        put(new Point3D(52, 49, 0), "start");
        
        put(new Point3D(49, 48, 0), "still_night");
        put(new Point3D(48, 48, 0), "venture");
        put(new Point3D(50, 48, 0), "expanse");
        put(new Point3D(51, 48, 0), "greatness");
        put(new Point3D(52, 48, 0), "spooky");
        put(new Point3D(53, 48, 0), "workshop");
        put(new Point3D(54, 48, 0), "fanfare");
        put(new Point3D(55, 48, 0), "arrival");
        
        put(new Point3D(55, 47, 0), "horizon");
        put(new Point3D(54, 47, 0), "scape_soft");
        put(new Point3D(53, 47, 0), "scape_soft");
        put(new Point3D(52, 47, 0), "barbarianism");
        put(new Point3D(51, 47, 0), "spirit");
        put(new Point3D(50, 47, 0), "garden");
        put(new Point3D(49, 47, 0), "medieval");
        put(new Point3D(48, 47, 0), "lullaby");
        
        put(new Point3D(49, 46, 0), "parade");
        put(new Point3D(50, 46, 0), "adventure");
        put(new Point3D(51, 46, 0), "doorways");
        put(new Point3D(52, 46, 0), "forever");
        put(new Point3D(53, 46, 0), "alone");
        put(new Point3D(54, 46, 0), "alone");
        put(new Point3D(55, 46, 0), "splendour");
    }};
    
    private String getSongBySector(Point3D sector) {
        return musicMap.get(sector) + ".midi";
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
