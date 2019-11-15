package org.nemotech.rsc.plugins.minigames.fishingtrawler;

import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.removeItem;

import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;

public class InspectNet implements ObjectActionListener, ObjectActionExecutiveListener {

    @Override
    public boolean blockObjectAction(GameObject obj, String command, Player player) {
        return obj.getID() == 1102 || obj.getID() == 1101;
    }

    @Override
    public void onObjectAction(GameObject obj, String command, Player player) {
    
        message(player, 1900, "you inspect the net");
        
        // TODO
        /*if(World.getWorld().getFishingTrawler().isNetBroken()) {
            player.message("it's beginning to rip");
            if(!hasItem(player, 237)) {
                player.message("you'll need some rope to fix it");
                return;
            }
            message(player, 1900, "you attempt to fix it with your rope");
            if(DataConversions.random(0, 1) == 0) {
                player.message("you manage to fix the net");
                removeItem(player, 237, 1);
                World.getWorld().getFishingTrawler().setNetBroken(false);
            } else {
                player.message("but you fail in the harsh conditions");
            }
        } else {
            player.message("it is not damaged");
        }*/
    }

}
