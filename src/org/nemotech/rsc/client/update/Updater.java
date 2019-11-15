package org.nemotech.rsc.client.update;

import org.nemotech.rsc.client.mudclient;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;

public abstract class Updater {
    
    public mudclient mc = mudclient.getInstance();
    
    public Player player = World.getWorld().getPlayer();
    
    public void handlePositionUpdate(Player player) {}
    
    public void handleGraphicsUpdate(Player player) {}
    
}