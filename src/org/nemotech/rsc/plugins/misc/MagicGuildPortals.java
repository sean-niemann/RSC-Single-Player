package org.nemotech.rsc.plugins.misc;

import static org.nemotech.rsc.plugins.Plugin.displayTeleportBubble;
import static org.nemotech.rsc.plugins.Plugin.inArray;
import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.WallObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.WallObjectActionExecutiveListener;

public class MagicGuildPortals implements WallObjectActionListener, WallObjectActionExecutiveListener {

    public static int[] MAGIC_PORTALS = { 147, 148, 149 };

    @Override
    public boolean blockWallObjectAction(GameObject obj, Integer click, Player player) {
        return inArray(obj.getID(), MAGIC_PORTALS);
    }

    @Override
    public void onWallObjectAction(GameObject obj, Integer click, Player p) {
        if(inArray(obj.getID(), MAGIC_PORTALS)) {
            p.message("you enter the magic portal");
            if(obj.getID() == MAGIC_PORTALS[0]) {
                p.teleport(212, 695);
            } else if(obj.getID() == MAGIC_PORTALS[1]) {
                p.teleport(511, 1452);
            } else if(obj.getID() == MAGIC_PORTALS[2]) {
                p.teleport(362, 1515);
            }
            sleep(600);
            displayTeleportBubble(p, p.getX(), p.getY(), false);
        }
    }
}
