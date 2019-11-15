package org.nemotech.rsc.plugins.misc;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnObjectListener;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnObjectExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;

public class SinisterChest implements ObjectActionListener, ObjectActionExecutiveListener, InvUseOnObjectListener, InvUseOnObjectExecutiveListener  {

    @Override
    public boolean blockObjectAction(GameObject obj, String command, Player player) {
        if(obj.getID() == 645 && obj.getX() == 617 && obj.getY() == 3567) {
            return true;
        }
        return false;
    }

    @Override
    public void onObjectAction(GameObject obj, String command, Player player) {
        if(obj.getID() == 645 && obj.getX() == 617 && obj.getY() == 3567) {
            player.message("the chest is locked");
        }
    }

    @Override
    public boolean blockInvUseOnObject(GameObject obj, InvItem item, Player player) {
        if(item.getID() == 932 && obj.getID() == 645 && obj.getX() == 617 && obj.getY() == 3567) {
            return true;
        }
        return false;
    }

    @Override
    public void onInvUseOnObject(GameObject obj, InvItem item, Player p) {
        if(item.getID() == 932 && obj.getID() == 645 && obj.getX() == 617 && obj.getY() == 3567) {
            message(p, "you unlock the chest with your key");
            p.message("A foul gas seeps from the chest");
            p.message("You find a lot of herbs in the chest");

            GameObject openedChest = new GameObject(obj.getLocation(), 339, obj.getDirection(), obj.getType());
            GameObject sinisterChest = new GameObject(obj.getLoc());

            World.getWorld().unregisterGameObject(obj);
            World.getWorld().registerGameObject(openedChest);
            sleep(2000);
            World.getWorld().registerGameObject(sinisterChest);

            removeItem(p, 932, 1); // remove the sinister key.
            // ADD 9 HERB ITEMS FROM CHEST.
            int[] RandomHerbs = { 435, 436, 437, 438, 439, 440, 441, 442, 443 };
            for (int i = 0; i < 8; i++) {
                int choosenHerbs = (int) (Math.random() * RandomHerbs.length);
                p.getInventory().add(new InvItem(RandomHerbs[choosenHerbs]));
            }
            // one of the herbs is guaranteed to be a torstol.
            addItem(p, 933, 1);
            // Poison player with damage 6.
            /////p.startPoisonEvent(); TODO, poison not implemented!
            /////PoisonEvent poisonEvent = p.getAttribute("poisonEvent", null);
            /////poisonEvent.setPoisonPower(68);
        }
    }
}
