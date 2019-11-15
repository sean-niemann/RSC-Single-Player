package org.nemotech.rsc.plugins.misc;

import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;

public class DeadTree implements ObjectActionListener, ObjectActionExecutiveListener {

    @Override
    public boolean blockObjectAction(GameObject obj, String command, Player player) {
        return obj.getID() == 88;
    }

    @Override
    public void onObjectAction(GameObject obj, String command, Player player) {
        player.message("The tree seems to lash out at you!");
        sleep(500);
        player.damage((int) (player.getCurStat(3) * (double) 0.2D));
        player.message("You are badly scratched by the tree");
    }
}