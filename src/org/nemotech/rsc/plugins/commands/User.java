package org.nemotech.rsc.plugins.commands;

import org.nemotech.rsc.client.mudclient;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.listeners.action.CommandListener;

public class User extends Plugin implements CommandListener {

    @Override
    public void onCommand(String command, String[] args, Player player) {
        if(command.equals("help")) {
            mudclient.getInstance().showAlert("@yel@Single Player RSC Help % %" + "@whi@Type ::stuck if your character gets stuck.", false);
            return;
        }
        if(command.equals("stuck")) {
            player.teleport(122, 647, true);
            return;
        }
    }

}