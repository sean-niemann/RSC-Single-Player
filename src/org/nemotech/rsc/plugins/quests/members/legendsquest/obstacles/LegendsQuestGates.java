package org.nemotech.rsc.plugins.quests.members.legendsquest.obstacles;

import static org.nemotech.rsc.plugins.Plugin.*;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;

public class LegendsQuestGates implements ObjectActionListener, ObjectActionExecutiveListener {

    public static final int LEGENDS_HALL_DOOR = 1080;

    @Override
    public boolean blockObjectAction(GameObject obj, String command, Player p) {
        if(obj.getID() == LEGENDS_HALL_DOOR) {
            return true;
        }
        return false;
    }

    @Override
    public void onObjectAction(GameObject obj, String command, Player p) {
        if(obj.getID() == LEGENDS_HALL_DOOR) {
            if(command.equalsIgnoreCase("open")) {
                if(p.getQuestStage(Plugin.LEGENDS_QUEST) >= 11 || p.getQuestStage(Plugin.LEGENDS_QUEST) == -1) {
                    doDoor(obj, p, 497);
                    p.message("You open the impressive wooden doors.");
                    if(p.getY() <= 539) {
                        p.teleport(513, 541);
                    } else {
                        p.teleport(513, 539);
                    }
                } else {
                    message(p, 1300, "You need to complete the Legends Guild Quest");
                    message(p, 1200, "before you can enter the Legends Guild");
                }
            } else if(command.equalsIgnoreCase("search")) {
                p.message("Nothing interesting happens");
            }
        }
    }
}
