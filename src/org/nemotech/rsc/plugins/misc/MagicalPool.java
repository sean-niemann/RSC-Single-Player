package org.nemotech.rsc.plugins.misc;

import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.movePlayer;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;

public class MagicalPool implements ObjectActionListener, ObjectActionExecutiveListener {

    @Override
    public boolean blockObjectAction(GameObject obj, String command, Player player) {
        if(obj.getID() == 1166) { // mage arena gods place pool.
            return true;
        }
        if (obj.getID() == 1155) {
            return true;
        }
        return false;
    }

    @Override
    public void onObjectAction(GameObject obj, String command, Player player) {
        if (obj.getID() == 1155) {
            movePlayer(player, 471, 3385);
            player.message("you are teleported further under ground");
        } else if (obj.getID() == 1166) {
            message(player, 1200, "you step into the sparkling water");
            message(player, 1200, "you feel energy rush through your veins");
            movePlayer(player, 447, 3373);
            player.message("you are teleported to kolodions cave");
        }
    }
}
