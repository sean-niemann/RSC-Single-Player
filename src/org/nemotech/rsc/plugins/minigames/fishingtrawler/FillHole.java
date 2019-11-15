package org.nemotech.rsc.plugins.minigames.fishingtrawler;

import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.removeObject;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;

public class FillHole implements ObjectActionExecutiveListener, ObjectActionListener{

    @Override
    public void onObjectAction(GameObject obj, String command, Player player) {
        player.setBusyTimer(650);
        if(removeItem(player, 785, 1)) {
            removeObject(obj);
            message(player, 0, "you fill the hole with swamp paste");
        } else {
            message(player, 0, "you'll need some swamp paste to fill that");
        }
    }

    @Override
    public boolean blockObjectAction(GameObject obj, String command, Player player) {
        return obj.getID() == 1077 || obj.getID() == 1071;
    }
}
