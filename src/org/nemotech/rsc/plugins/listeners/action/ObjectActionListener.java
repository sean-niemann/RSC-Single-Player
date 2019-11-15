package org.nemotech.rsc.plugins.listeners.action;

import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;

public interface ObjectActionListener {

    public void onObjectAction(GameObject object, String command, Player player);

}