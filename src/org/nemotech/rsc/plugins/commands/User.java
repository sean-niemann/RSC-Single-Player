package org.nemotech.rsc.plugins.commands;

import org.nemotech.rsc.client.mudclient;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.listeners.action.CommandListener;

public class User extends Plugin implements CommandListener {

    @Override
    public void onCommand(String command, String[] args, Player player) {
        if(command.equals("help")) {
            mudclient.getInstance().showAlert("@yel@Single Player RSC Help % %" + "@whi@Type ::stuck if your character gets stuck. % "
                + "Type ::pos to list your current location in the world.", false);
            return;
        }
        if(command.equals("stuck")) {
            player.teleport(122, 647, true);
            return;
        }
        if(command.equals("pos") || command.equals("coords") || command.equals("sector")) {
            int x = player.getX();
            int y = player.getY();
            int sectorH = 0;
            int sectorX = 0;
            int sectorY = 0;
            if (x != -1 && y != -1) {
                if (y >= 0 && y <= 1007)
                    sectorH = 0;
                else if (y >= 1007 && y <= 1007 + 943) {
                    sectorH = 1;
                    y -= 943;
                } else if (y >= 1008 + 943 && y <= 1007 + (943 * 2)) {
                    sectorH = 2;
                    y -= 943 * 2;
                } else {
                    sectorH = 3;
                    y -= 943 * 3;
                }
                sectorX = (x / 48) + 48;
                sectorY = (y / 48) + 37;
            }
            player.getSender().sendMessage(String.format("@whi@X:%d Y:%d (Sector h%dx%dy%d)@que@", player.getX(), player.getY(), sectorH, sectorX, sectorY));
            return;
        }
    }

}