package org.nemotech.rsc.plugins.listeners.executive;

import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;

public interface WallObjectActionExecutiveListener {

    public boolean blockWallObjectAction(GameObject door, Integer click, Player player);

}
