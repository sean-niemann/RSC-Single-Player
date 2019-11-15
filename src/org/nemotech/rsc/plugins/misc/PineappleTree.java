package org.nemotech.rsc.plugins.misc;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.replaceObjectDelayed;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;

public class PineappleTree implements ObjectActionExecutiveListener,
ObjectActionListener {

    @Override
    public void onObjectAction(GameObject obj, String command, Player p) {
        if(obj.getID() == 430) {
            p.message("you pick a pineapple");
            addItem(p, 861, 1);
            if(!p.getCache().hasKey("pineapple_pick")) {
                p.getCache().set("pineapple_pick", 1);
            } else {
                int pineappleCount = p.getCache().getInt("pineapple_pick");
                p.getCache().set("pineapple_pick", (pineappleCount + 1));
                if(pineappleCount >= 4) {
                    replaceObjectDelayed(obj, 60000 * 8, 431); // 8 minutes respawn time.
                    p.getCache().remove("pineapple_pick");
                }
            }

        }
        if(obj.getID() == 431) {
            p.message("there are no pineapples left on the tree");
        }
        
    }

    @Override
    public boolean blockObjectAction(GameObject obj, String command, Player player) {
        if(obj.getID() == 431 || obj.getID() == 430) {
            return true;
        }
        return false;
    }

}
