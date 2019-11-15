package org.nemotech.rsc.plugins.misc;

import static org.nemotech.rsc.plugins.Plugin.*;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.DropListener;
import org.nemotech.rsc.plugins.listeners.action.InvActionListener;
import org.nemotech.rsc.plugins.listeners.executive.DropExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.InvActionExecutiveListener;

public class KittenToCat implements DropListener, DropExecutiveListener, InvActionListener, InvActionExecutiveListener {
    
    private static final int KITTEN = 1096;

    @Override
    public boolean blockDrop(Player p, InvItem i) {
        if(i.getID() == KITTEN) {
            return true;
        }
        return false;
    }

    @Override
    public void onDrop(Player p, InvItem i) {
        if(i.getID() == KITTEN) {
            removeItem(p, KITTEN, 1);
            message(p, 1200, "you drop the kitten");
            message(p, 0, "it's upset and runs away");
        }
    }

    @Override
    public boolean blockInvAction(InvItem item, Player p) {
        if(item.getID() == KITTEN) {
            return true;
        }
        return false;
    }

    @Override
    public void onInvAction(InvItem item, Player p) {
        if(item.getID() == KITTEN) {
            message(p, "you softly stroke the kitten",
                    "@yel@kitten:..purr..purr..");
            message(p, 600, "the kitten appreciates the attention");    
        }
    }
}
