package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.client.action.ActionHandler;

public class InventoryActionHandler implements ActionHandler {

    public void handleInventoryAction(int idx) {
        Player player = World.getWorld().getPlayer();

        if (player == null || player.getInventory() == null) {
            return;
        }

        if (idx < 0 || idx >= player.getInventory().size()) {
            return;
        }
        final InvItem item = player.getInventory().get(idx);
        if (item == null || item.getDef().getCommand().equals("")) {
            player.setSuspiciousPlayer(true);
            return;
        }

        if (item.getDef().isMembers() && !Constants.MEMBER_WORLD) {
            player.getSender().sendMessage("You need to be a member to use this object");
            return;
        }

        if (player.isBusy()) {
            if (player.inCombat()) {
                player.getSender().sendMessage("You cannot do that whilst fighting!");
            }
            return;
        }

        player.reset();

        if (PluginManager.getInstance().blockDefaultAction("InvAction", new Object[] { item, player })) {
            return;
        }
    }
    
}