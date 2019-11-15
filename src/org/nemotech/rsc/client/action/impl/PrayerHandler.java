package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.client.action.ActionHandler;
import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.external.definition.PrayerDef;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.player.Player;

public class PrayerHandler implements ActionHandler {
    
    public void handlePrayer(int prayerID, boolean activate) {
        if(prayerID < 0 || prayerID > 14) {
            System.err.println("Prayer ID out of range! [" + prayerID + "]");
            return;
        }
        Player player = World.getWorld().getPlayer();
        PrayerDef prayer = EntityManager.getPrayer(prayerID);
        if(activate) {
            if (player.getMaxStat(5) < prayer.getReqLevel()) {
                player.setSuspiciousPlayer(true);
                player.getSender().sendMessage("Your prayer ability is not high enough to use this prayer");
                return;
            }
            if (player.getCurStat(5) <= 0) {
                player.setPrayer(prayerID, false);
                player.getSender().sendMessage("You have run out of prayer points. Return to a church to recharge");
                return;
            }
            activatePrayer(player, prayerID);
        } else {
            deactivatePrayer(player, prayerID);
        }
        player.getSender().sendPrayers();
    }
    
    private boolean activatePrayer(Player player, int prayerID) {
        if (!player.isPrayerActivated(prayerID)) {
            switch(prayerID) {
                case 11:
                    deactivatePrayer(player, 5);
                    deactivatePrayer(player, 2);
                    break;
                case 5:
                    deactivatePrayer(player, 2);
                    deactivatePrayer(player, 11);
                    break;
                case 2:
                    deactivatePrayer(player, 5);
                    deactivatePrayer(player, 11);
                    break;
                case 10:
                    deactivatePrayer(player, 4);
                    deactivatePrayer(player, 1);
                    break;
                case 4:
                    deactivatePrayer(player, 10);
                    deactivatePrayer(player, 1);
                    break;
                case 1:
                    deactivatePrayer(player, 10);
                    deactivatePrayer(player, 4);
                    break;
                case 9:
                    deactivatePrayer(player, 3);
                    deactivatePrayer(player, 0);
                    break;
                case 3:
                    deactivatePrayer(player, 9);
                    deactivatePrayer(player, 0);
                    break;
                case 0:
                    deactivatePrayer(player, 9);
                    deactivatePrayer(player, 3);
                    break;
                case 6:
                case 7:
                    player.activateQuickHealUpdater(true);
                    break;
                default:
                    break;
            }
            player.setPrayer(prayerID, true);
            player.addPrayerDrain(prayerID);
            return true;
        }
        return false;
    }

    private boolean deactivatePrayer(Player player, int prayerID) {
        if (player.isPrayerActivated(prayerID)) {
            player.setPrayer(prayerID, false);
            player.removePrayerDrain(prayerID);
            if(prayerID == 6 || prayerID == 7) {
                player.activateQuickHealUpdater(false);
            } 
            return true;
        }
        return false;
    }
    
}