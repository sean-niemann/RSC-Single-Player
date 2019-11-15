package org.nemotech.rsc.plugins.skills;

import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.listeners.action.InvActionListener;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.client.sound.SoundEffect;
import org.nemotech.rsc.plugins.listeners.executive.InvActionExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;

/**
 * Handles the prayer skill
 * @author Sean Niemann
 */
public class Prayer extends Plugin implements ObjectActionListener, InvActionListener,
    ObjectActionExecutiveListener, InvActionExecutiveListener {
    
    @Override
    public boolean blockObjectAction(GameObject object, String command, Player player) {
        if (command.equals("recharge at")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean blockInvAction(InvItem item, Player player) {
        if (item.getID() == 20 || item.getID() == 604 || item.getID() == 413 || item.getID() == 814) {
            return true;
        }
        return false;
    }
    
    @Override
    public void onObjectAction(GameObject object, String command, Player player) {
        int maxPray = object.getID() == 200 ? player.getMaxStat(PRAYER) + 2 : player.getMaxStat(PRAYER);
        if ((player.getCurStat(PRAYER) >= maxPray) && object.getID() != 200) {
            player.getSender().sendMessage("You already have full prayer points");
        } else {
            player.setCurStat(PRAYER, maxPray);
            player.getSender().sendSound(SoundEffect.RECHARGE);
            player.getSender().sendMessage("You recharge at the altar");
            player.getSender().sendStat(PRAYER);
        }
        if(object.getID() == 625 && object.getY() == 3573) {
            sleep(650);
            message(player, "Suddenly a trapdoor opens beneath you");
            player.teleport(608, 3525);
        }
    }
    
    @Override
    public void onInvAction(InvItem item, Player player) {
        player.setBusy(true);
        player.getSender().sendMessage("You dig a hole in the ground");
        sleep(500);
        player.getSender().sendMessage("You bury the " + item.getDef().getName());
        player.getInventory().remove(item);
        switch (item.getID()) {
            case 20: // Bones
                player.incExp(PRAYER, 8, true);
                break;
            case 604: // Bat bones
                player.incExp(PRAYER, 8, true);
                break;
            case 413: // Big bones
                player.incExp(PRAYER, 24, true);
                break;
            case 814: // Dragon bones
                player.incExp(PRAYER, 90, true);
                break;
        }
        player.getSender().sendInventory();
        player.setBusy(false);
    }

}