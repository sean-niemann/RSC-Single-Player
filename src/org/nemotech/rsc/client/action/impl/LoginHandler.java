package org.nemotech.rsc.client.action.impl;

import java.io.File;

import org.nemotech.rsc.Constants;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.client.action.ActionHandler;

public class LoginHandler implements ActionHandler {

    public int handleLogin(String username) {
        Player player = World.getWorld().getPlayer();
        byte loginCode = 22;
        try {
            int res = getLogin(username);
            switch (res) {
                case 3:
                    return 1; // account doesn't exist
                default:
                    player.load(username);
                    if(res == 50) {
                        return 50;
                    }
                    return 0;
            }
        } catch(Exception e) {
            e.printStackTrace();
            //loginCode = 7;
        }
        if(loginCode != 22) {
            player.destroy(true);
        }
        return -1;
    }

    public int getLogin(String user) {
        try {
            user = user.replace("_", " ");
            File file = new File(Constants.CACHE_DIRECTORY + "players" + File.separator + user.trim() + "_data.dat");
            if(file.exists()) {
                return 1;
            } else {
                return 3;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}