package org.nemotech.rsc.client.action.impl;

import java.util.concurrent.FutureTask;
import org.nemotech.rsc.model.MenuHandler;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.client.action.ActionHandler;

public class OptionHandler implements ActionHandler {

    public void handleOption(final int option) {
        final Player player = World.getWorld().getPlayer();
        final MenuHandler menuHandler = player.getMenuHandler();
        
        FutureTask<Integer> task = new FutureTask<>(() -> {
            if (player.getMenu() != null) {
                player.getMenu().handleReply(player, option);
            } else if (menuHandler != null) {
                final String reply = option == 30 ? "" : menuHandler.getOption(option);
                player.resetMenuHandler();
                if (reply == null) {
                    player.setSuspiciousPlayer(true);
                } else {
                    menuHandler.handleReply(option, reply);
                }
            }
            return 1;
        });
        
        PluginManager.getInstance().getExecutor().execute(task);
    }
    
}