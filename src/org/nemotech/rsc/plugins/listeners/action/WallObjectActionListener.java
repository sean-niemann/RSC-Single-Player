package org.nemotech.rsc.plugins.listeners.action;

import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;

public interface WallObjectActionListener {

    public void onWallObjectAction(GameObject door, Integer command, Player player);

}