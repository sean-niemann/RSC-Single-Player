package org.nemotech.rsc.plugins.minigames.gnomerestaurant;

import static org.nemotech.rsc.plugins.Plugin.*;

import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.InvActionListener;
import org.nemotech.rsc.plugins.listeners.action.PickupListener;
import org.nemotech.rsc.plugins.listeners.executive.InvActionExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.PickupExecutiveListener;

public class SwampToads implements PickupListener, PickupExecutiveListener, InvActionListener, InvActionExecutiveListener {

    public static final int SWAMP_TOAD = 895;
    public static final int TOAD_LEGS = 896;
    
    @Override
    public boolean blockInvAction(InvItem item, Player p) {
        if(item.getID() == SWAMP_TOAD) {
            return true;
        }
        return false;
    }

    @Override
    public void onInvAction(InvItem item, Player p) {
        if(item.getID() == SWAMP_TOAD) {
            message(p, 1900, "you pull the legs off the toad");
            p.message("poor toad..at least they'll grow back");
            p.getInventory().replace(item.getID(), TOAD_LEGS);
        }
    }

    @Override
    public boolean blockPickup(Player p, Item i) {
        if(i.getID() == SWAMP_TOAD) {
            return true;
        }
        return false;
    }

    @Override
    public void onPickup(Player p, Item i) {
        if(i.getID() == SWAMP_TOAD) {
            if(Util.random(0, 10) >= 3) {
                p.message("you pick up the swamp toad");
                message(p, 1900, "but it jumps out of your hands..");
                p.message("..slippery little blighters");
            } else {
                message(p, 1900, "you pick up the swamp toad");
                i.remove();
                addItem(p, 895, 1);
                p.message("you just manage to hold onto it");
            }
        }
    }
}
