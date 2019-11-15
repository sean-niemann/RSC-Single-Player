package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.client.action.ActionHandler;

public class CommandHandler implements ActionHandler {

    public void handleCommand(String command) {
        Player player = World.getWorld().getPlayer();
        if(player.isBusy()) {
            player.resetPath();
            return;
        }
        player.reset();
        int firstSpace = command.indexOf(" ");
        String cmd = command;
        String[] arguments = new String[0];
        if(firstSpace != -1) {
            cmd = command.substring(0, firstSpace).trim();
            arguments = command.substring(firstSpace + 1).trim().split(" ");
        }
        PluginManager.getInstance().handleAction("Command", new Object[] { cmd.toLowerCase(), arguments, player });
    }
    
}