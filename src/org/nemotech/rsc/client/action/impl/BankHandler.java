package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.client.action.ActionHandler;

public class BankHandler implements ActionHandler {
    
    public void handleSwap(int slot1, int item1, int slot2, int item2) {
        Player player = World.getWorld().getPlayer();
        if ((slot1 > -1) && (slot2 > -1) && (item1 > -1) && (item2 > -1) && (slot1 != slot2) && (item1 != item2)) {
            player.getBank().swap(item1, slot1, item2, slot2);
            player.getSender().updateBankItem(slot1, item2, player.getBank().countId(item2));
            player.getSender().updateBankItem(slot2, item1, player.getBank().countId(item1));
        }
    }

    public void handleDeposit(int id, int amount) {
        Player player = World.getWorld().getPlayer();
        InvItem item;
        int slot;
        if (amount < 1 || player.getInventory().countId(id) < amount) {
            return;
        }
        if (PluginManager.getInstance().blockDefaultAction("Deposit", new Object[] { player, id, amount })) {
            return;
        }
        if (EntityManager.getItem(id).isStackable()) {
            item = new InvItem(id, amount);
            if (player.getBank().canHold(item) && player.getInventory().remove(item) > -1) {
                player.getBank().add(item);
            } else {
                player.getSender().sendMessage("You don't have room for that in your bank");
            }
        } else {
            for (int i = 0; i < amount; i++) {
                int idx = player.getInventory().getLastIndexById(id);
                item = player.getInventory().get(idx);
                if (item == null) { // This shouldn't happen
                    break;
                }
                if (player.getBank().canHold(item) && player.getInventory().remove(item) > -1) {
                    player.getBank().add(item);
                } else {
                    player.getSender().sendMessage("You don't have room for that in your bank");
                    break;
                }
            }
        }
        slot = player.getBank().getFirstIndexById(id);
        if (slot > -1) {
            player.getSender().sendInventory();
            player.getSender().updateBankItem(slot, id, player.getBank().countId(id));
        }
    }

    public void handleWithdrawl(int id, int amount) {
        Player player = World.getWorld().getPlayer();
        InvItem item;
        int slot;
        if (amount < 1 || player.getBank().countId(id) < amount) {
            return;
        }

        if (PluginManager.getInstance().blockDefaultAction("Withdraw", new Object[] { player, id, amount })) {
            return;
        }
        
        slot = player.getBank().getFirstIndexById(id);
        if (EntityManager.getItem(id).isStackable()) {
            item = new InvItem(id, amount);
            if (player.getInventory().canHold(item) && player.getBank().remove(item) > -1) {
                player.getInventory().add(item);
            } else {
                player.getSender().sendMessage("You don't have room for that in your inventory");
            }
        } else {
            for (int i = 0; i < amount; i++) {
                if (player.getBank().getFirstIndexById(id) < 0) {
                    break;
                }
                item = new InvItem(id, 1);
                if (player.getInventory().canHold(item) && player.getBank().remove(item) > -1) {
                    player.getInventory().add(item);
                } else {
                    player.getSender().sendMessage("You don't have room for that in your inventory");
                            break;
                }
            }
        }
        if (slot > -1) {
            player.getSender().sendInventory();
            player.getSender().updateBankItem(slot, id, player.getBank().countId(id));
        }
    }
    
}