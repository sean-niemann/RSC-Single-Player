package org.nemotech.rsc.plugins.items;

import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.external.definition.ItemDef;
import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnItemListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnItemExecutiveListener;

public class InvItemPoisoning implements InvUseOnItemListener, InvUseOnItemExecutiveListener {

    @Override
    public boolean blockInvUseOnItem(Player player, InvItem item1, InvItem item2) {
        return item1.getID() == 572 || item2.getID() == 572;
    }

    @Override
    public void onInvUseOnItem(Player player, InvItem item1, InvItem item2) {
        if(item1.getID() == 572) {
            applyPoison(player, item2);
        }
        else if(item2.getID() == 572) {
            applyPoison(player, item1);
        }
    }
    
    
    public void applyPoison(Player player, InvItem item) {
        int makeAmount = 1;
        
        if(item.getDef().isStackable()) {
            makeAmount = hasItem(player, item.getID(), 15) ? 15 : player.getInventory().countId(item.getID());
        }
        InvItem poisonedItem = getPoisonedItem(item.getDef().getName());
        if(poisonedItem != null) {
            if(removeItem(player, 572, 1) && removeItem(player, item.getID(), makeAmount)) {
                player.message("You apply poison to your " + item.getDef().getName());
                addItem(player, poisonedItem.getID(), makeAmount);
            }
        } else {
            player.message("Nothing interesting happens");
        }
    }
    
    private InvItem getPoisonedItem(String name) {
        String poisonedVersion = "Poisoned " + name;
        String poisonedVersion2 = "Poison " + name;
        for(int i = 0; i < EntityManager.getItems().length; i++) {
            ItemDef def = EntityManager.getItem(i);
            if(def.getName().equalsIgnoreCase(poisonedVersion) || def.getName().equalsIgnoreCase(poisonedVersion2)) {
                return new InvItem(i);
            }
        }
        return null;
    }
}
