package org.nemotech.rsc.plugins.misc;

import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;

public class Bed implements ObjectActionExecutiveListener, ObjectActionListener {

    @Override
    public void onObjectAction(final GameObject object, String command, Player owner) { 
        if((command.equalsIgnoreCase("rest") || command.equalsIgnoreCase("sleep")) && !owner.isSleeping()) {
            owner.getSender().sendMessage("You rest in the bed");
            owner.getSender().sendEnterSleep();
            owner.startSleepEvent(true);
        } 
    }

    @Override
    public boolean blockObjectAction(GameObject obj, String command, Player player) { // FIX
        if (command.equals("rest") || command.equals("sleep")) {
            return true;
        }
        return false;
    }
}
