package org.nemotech.rsc.plugins.listeners.executive;

import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;

public interface ObjectActionExecutiveListener {

    public boolean blockObjectAction(GameObject object, String command, Player player);
    
}