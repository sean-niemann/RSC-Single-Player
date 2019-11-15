package org.nemotech.rsc.plugins.misc;

import static org.nemotech.rsc.plugins.Plugin.message;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;

public class Dummy implements ObjectActionListener, ObjectActionExecutiveListener{

    @Override
    public boolean blockObjectAction(GameObject obj, String command, Player player) {
        return obj.getID() == 49;
    }

    @Override
    public void onObjectAction(GameObject obj, String command, Player player) {
        message(player, 3200, "You swing at the dummy");
        if(player.getMaxStat(0) > 7) {
            player.message("There is only so much you can learn from hitting a dummy");
        } else {
            player.message("You hit the dummy");
            player.incExp(0, 5, true);
        }
    }

}
