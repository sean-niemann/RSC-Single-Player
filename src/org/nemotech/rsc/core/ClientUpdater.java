package org.nemotech.rsc.core;

import org.nemotech.rsc.client.update.impl.*;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.util.EntityList;
import org.nemotech.rsc.client.update.UpdateManager;

public class ClientUpdater {
    
    private static World world = World.getWorld();
    
    private EntityList<NPC> npcs = world.getNpcs();

    public void updateClients() {
        Player player = world.getPlayer();
        if(player != null && player.isLoggedIn()) {
            updateNpcPositions();
            updatePlayersPositions();
            updateTimeouts(player);
            
            // entity positions 
            UpdateManager.get(PlayerUpdater.class).handlePositionUpdate(player);
            UpdateManager.get(NPCUpdater.class).handlePositionUpdate(player);
            UpdateManager.get(ObjectUpdater.class).handlePositionUpdate(player);
            UpdateManager.get(WallUpdater.class).handlePositionUpdate(player);
            UpdateManager.get(ItemUpdater.class).handlePositionUpdate(player);
            
            // graphics
            UpdateManager.get(PlayerUpdater.class).handleGraphicsUpdate(player);
            UpdateManager.get(NPCUpdater.class).handleGraphicsUpdate(player);
            
            updateCollections();
        }
    }
    
    /**
     * Update the position of npcs, and check if who (and what) they are aware of needs updated
     */
    private void updateNpcPositions() {
        for(NPC n : npcs) {
            n.resetMoved();
            n.updatePosition();
            n.updateAppearanceID();
        }
    }
    
    /**
     * Update the position of players, and check if who (and what) they are aware of needs updated
     */
    private void updatePlayersPositions() {
        Player p = World.getWorld().getPlayer();
        if(p != null && p.isLoggedIn()) {
            p.resetMoved();
            p.updatePosition();
            p.updateAppearanceID();
            //p.revalidateWatchedPlayers();
            p.revalidateWatchedObjects();
            p.revalidateWatchedItems();
            p.revalidateWatchedNpcs();
            //p.updateViewedPlayers();
            p.updateViewedObjects();
            p.updateViewedItems();
            p.updateViewedNpcs();
        }
    }

    public boolean loggedOut;
    
    /**
     * Sends queued packets to each player
     */
    public void checkIfPlayerIsDestroyed() {
        Player p = World.getWorld().getPlayer();
        if(p.destroyed() && !loggedOut) {
            p.remove();
            loggedOut = true;
        }
    }
    
    /**
     * Checks the player has moved within the last 10 minutes
     */
    private void updateTimeouts(Player p) {
        if(p.destroyed()) {
            return;
        }
        long curTime = System.currentTimeMillis();
        if(p.warnedToMove()) {
            if(curTime - p.getLastMoved() >= 660000 && p.isLoggedIn()) { // 11 min
                p.destroy(false);
            }
        } else if(curTime - p.getLastMoved() >= 600000) { // 10 min
            p.getSender().sendMessage("@cya@You have not moved for 10 mins, please move to a new area to avoid logout");
            p.warnToMove();
        }
    }
    
    /**
     * Updates collections, new becomes known, removing is removed etc.
     */
    private void updateCollections() {
        Player p = World.getWorld().getPlayer();
        
        if(p.isRemoved() && p.initialized()) {
            world.unregisterPlayer(p);
        }
        p.getWatchedPlayers().update();
        p.getWatchedObjects().update();
        p.getWatchedItems().update();
        p.getWatchedNpcs().update();

        p.clearProjectilesNeedingDisplayed();
        p.clearPlayersNeedingHitsUpdate();
        p.clearNpcsNeedingHitsUpdate();
        p.clearChatMessagesNeedingDisplayed();
        p.clearNpcMessagesNeedingDisplayed();

        p.resetSpriteChanged();
        p.setAppearnceChanged(false);
        for(NPC n : npcs) {
            n.resetSpriteChanged();
            n.setAppearnceChanged(false);
        }
    }
    
}