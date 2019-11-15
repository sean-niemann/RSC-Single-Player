package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.client.action.ActionHandler;

public class InventoryUseOnItemHandler implements ActionHandler {

    public void handleInventoryUseOnItem(int firstItem, int secondItem) {
        Player player = World.getWorld().getPlayer();

        if (player.isBusy()) {
            player.resetPath();
            return;
        }
        player.reset();
        
        InvItem item1 = player.getInventory().get(firstItem);
        InvItem item2 = player.getInventory().get(secondItem);
        if (item1 == null || item2 == null) {
            return;
        }

        if (item1.getDef().isMembers() || item2.getDef().isMembers()) {
            if (!Constants.MEMBER_WORLD) {
                player.getSender().sendMessage(Constants.MEMBERS_ONLY_MESSAGE);
                return;
            }
        }

        if (PluginManager.getInstance().blockDefaultAction("InvUseOnItem", new Object[]{ player, item1, item2 })) {
            return;
        }

        //player.getActionSender().sendMessage("Nothing interesting happens");
    }
    
}