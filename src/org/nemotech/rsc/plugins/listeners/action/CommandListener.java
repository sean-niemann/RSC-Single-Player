package org.nemotech.rsc.plugins.listeners.action;

import org.nemotech.rsc.model.player.Player;

public interface CommandListener {

    public void onCommand(String command, String[] args, Player player);

}