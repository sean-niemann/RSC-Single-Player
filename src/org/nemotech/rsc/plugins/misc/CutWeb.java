package org.nemotech.rsc.plugins.misc;

import org.nemotech.rsc.util.Formulae;
import static org.nemotech.rsc.plugins.Plugin.delayedSpawnObject;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.removeObject;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnWallObjectListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnWallObjectExecutiveListener;

public class CutWeb implements InvUseOnWallObjectListener, InvUseOnWallObjectExecutiveListener {

    public static int WEB = 24;

    @Override
    public boolean blockInvUseOnWallObject(GameObject obj, InvItem item, Player p) {
        if(obj.getID() == WEB) {
            return true;
        }
        return false;
    }

    @Override
    public void onInvUseOnWallObject(GameObject obj, InvItem item, Player p) {
        if(obj.getID() == WEB) {
            if(item.getWieldDef().getWieldPos() != 4 && item.getID() != 13) {
                p.message("Nothing interesting happens");
                return;
            }   
            message(p, "You try to destroy the web...");
            if (Formulae.cutWeb()) {
                p.message("You slice through the web");
                removeObject(obj);
                delayedSpawnObject(obj.getLoc(), 30000);
            } else {
                p.message("You fail to cut through it");
            }
        }
    }
}
