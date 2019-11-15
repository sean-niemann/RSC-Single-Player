package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.client.action.ActionHandler;

public class LogoutHandler implements ActionHandler {

    public void handleLogout() {
        Player player = World.getWorld().getPlayer();
        if(player != null) {
            if (player.isBusy()) {
                player.getSender().sendCantLogout();
                return;
            }
            if (player.canLogout()) {
                player.destroy(true);
            } else {
                player.destroy(false);
            }
        }
    }

}