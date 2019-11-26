package org.nemotech.rsc.client.update.impl;

import org.nemotech.rsc.client.action.ActionManager;
import org.nemotech.rsc.client.action.impl.SleepHandler;
import org.nemotech.rsc.client.update.Updater;
import org.nemotech.rsc.client.sound.SoundEffect;
import org.nemotech.rsc.plugins.QuestInterface;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Bank;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.util.Formulae;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MiscUpdater extends Updater {
    
    public void sendEnterSleep() {
        player.setSleeping(true);
        BufferedImage word = ActionManager.get(SleepHandler.class).generateCaptcha(player);
        ActionManager.get(SleepHandler.class).handleSleep(word);
    }
    
    public void sendWakeUp(boolean success, boolean silent) {
        if (!silent) {
            if(success) {
                player.handleWakeup();
                player.getSender().sendMessage("You wake up - feeling refreshed");
            } else {
                player.getSender().sendMessage("You are unexpectedly awoken! You still feel tired");
            }
        }
        player.setSleeping(false);
        mc.isSleeping = false;
    }
    
    public void sendAlert(String message, boolean big) {
        mc.showAlert(message, big);
    }
    
    public void sendLogout() {
        mc.logoutTimeout = 1000; // fix?
        World.getWorld().unregisterPlayer(player);
    }
    
    public void sendStat(int stat) {
        mc.playerStatCurrent[stat] = player.getCurStat(stat);
        mc.playerStatBase[stat] = player.getMaxStat(stat);
        mc.playerExperience[stat] = player.getExp(stat);
    }
    
    public void sendSound(SoundEffect sound) {
        mc.playSoundEffect(sound);
    }
    
    public void sendMessage(String message) {
        mc.showMessage(message);
    }
    
    public void sendTempFatigue(int tempFatigue) {
        mc.fatigueSleeping = tempFatigue;
    }
    
    public void sendFatigue(int fatigue) {
        mc.statFatigue = fatigue;
    }
    
    public void sendQuestInfo(int id, int stage) {
        mc.questStage.put(id, stage);
    }
    
    public void sendQuestInfo() {
        for (QuestInterface q : World.getWorld().getQuests()) {
            mc.newQuestNames.put(q.getQuestID(), q.getQuestName());
            mc.questStage.put(q.getQuestID(), player.getQuestStage(q));
        }
    }
    
    public void sendQuestPoints() {
        mc.playerQuestPoints = player.getQuestPoints();
    }
    
    public void sendMenu(String[] options) {
        mc.showOptionMenu = true;
        mc.optionMenuCount = options.length;
        System.arraycopy(options, 0, mc.optionMenuEntry, 0, options.length);
    }
    
    public void hideMenu() {
        mc.showOptionMenu = false;
    }
    
    public void sendPrayers() {
        for(int i = 0; i < 14; i++) {
            boolean on = player.isPrayerActivated(i);
            if(!mc.prayerOn[i] && on) {
                mc.playSoundEffect(SoundEffect.PRAYER_ON);
            }
            if(mc.prayerOn[i] && !on) {
                mc.playSoundEffect(SoundEffect.PRAYER_OFF);
            }
            mc.prayerOn[i] = on;
        }
    }
    
    public void sendWorldInfo() {
        mc.loadingArea = true;
        mc.planeWidth = 2304;
        mc.planeHeight = 1776;
        mc.planeIndex = Formulae.getHeight(player.getLocation());
        mc.planeMultiplier = 944;
        mc.planeHeight -= mc.planeIndex * mc.planeMultiplier;
    }
    
    public void sendInventory() {
        ArrayList<InvItem> items = player.getInventory().getItems();
        mc.inventoryItemsCount = items.size();
        for(int i = 0; i < items.size(); i++) {
            int id = items.get(i).getID() + (items.get(i).isWielded() ? 32768 : 0);
            mc.inventoryItemId[i] = (id & 0x7fff);
            mc.inventoryEquipped[i] = id / 32768;
            if(items.get(i).getDef().isStackable()) {
                mc.inventoryItemStackCount[i] = items.get(i).getAmount();
            } else {
                mc.inventoryItemStackCount[i] = 1;
            }
        }
    }
    
    public void sendEquipmentStats() {
        mc.playerStatEquipment[0] = player.getArmourPoints();
        mc.playerStatEquipment[1] = player.getWeaponAimPoints();
        mc.playerStatEquipment[2] = player.getWeaponPowerPoints();
        mc.playerStatEquipment[3] = player.getMagicPoints();
        mc.playerStatEquipment[4] = player.getPrayerPoints();
    }
    
    public void sendStats() {
        System.arraycopy(player.getCurStats(), 0, mc.playerStatCurrent, 0, player.getCurStats().length);
        System.arraycopy(player.getMaxStats(), 0, mc.playerStatBase, 0, player.getMaxStats().length);
        System.arraycopy(player.getExps(), 0, mc.playerExperience, 0, player.getExps().length);
    }
    
    public void sendGameSettings() {
        mc.optionMouseButtonOne = player.getGameSetting(0);
        mc.optionCameraModeAuto = player.getGameSetting(1);
        mc.optionSoundDisabled = player.getGameSetting(2);
        mc.musicLoop = player.getGameSetting(3);
    }
    
    public void sendCombatStyle() {
        mc.combatStyle = player.getCombatStyle();
    }
    
    public void sendAppearanceScreen() {
        mc.showAppearanceChange = true;
    }
    
    public void sendLoginBox() {
        if (!mc.welcomScreenAlreadyShown) {
            mc.welcomeLastLoggedInDays = player.getDaysSinceLastLogin();
            mc.showDialogWelcome = true;
            mc.welcomScreenAlreadyShown = true;
        }
    }
    
    public void sendDied() {
        mc.deathScreenTimeout = 150;
    }
    
    public void sendItemBubble(int id) {
        mc.localPlayer.bubbleTimeout = 150;
        mc.localPlayer.bubbleItem = id;
    }
    
    public void sendTeleBubble(int x, int y, boolean grab) {
        if (mc.teleportBubbleCount < 50) {
            int type = grab ? 1 : 0;
            int teleX = x - mc.regionX;
            int teleY = y - mc.regionY;
            mc.teleportBubbleType[mc.teleportBubbleCount] = type;
            mc.teleportBubbleTime[mc.teleportBubbleCount] = 0;
            mc.teleportBubbleX[mc.teleportBubbleCount] = teleX;
            mc.teleportBubbleY[mc.teleportBubbleCount] = teleY;
            mc.teleportBubbleCount++;
        }
    }
    
    public void sendScreenshot() {
        
    }
    
    public void sendUpdateItem(int slot) {
        InvItem item = player.getInventory().get(slot);
        int id = item.getID() + (item.isWielded() ? 32768 : 0);
        int amount = 1;
        if(item.getDef().isStackable()) {
            amount = item.getAmount();
        }
        mc.inventoryItemId[slot] = id & 0x7fff;
        mc.inventoryEquipped[slot] = id / 32768;
        mc.inventoryItemStackCount[slot] = amount;
        if (slot >= mc.inventoryItemsCount) {
            mc.inventoryItemsCount = slot + 1;
        }
    }
    
    public void showBank() {
        mc.showDialogBank = true;
        mc.newBankItemCount = player.getBank().size();
        mc.bankItemsMax = Bank.MAX_SIZE;
        for (int i = 0; i < mc.newBankItemCount; i++) {
            mc.newBankItems[i] = player.getBank().getItems().get(i).getID();
            mc.newBankItemsCount[i] = player.getBank().getItems().get(i).getAmount();
        }
        mc.updateBankItems();
    }
    
    public void updateBankItem(int slot, int newID, int amount) {
        if (amount == 0) {
            mc.newBankItemCount--;
            for (int i = slot; i < mc.newBankItemCount; i++) {
                mc.newBankItems[i] = mc.newBankItems[i + 1];
                mc.newBankItemsCount[i] = mc.newBankItemsCount[i + 1];
            }
        } else {
            mc.newBankItems[slot] = newID;
            mc.newBankItemsCount[slot] = amount;
            if (slot >= mc.newBankItemCount) {
                mc.newBankItemCount = slot + 1;
            }
        }
        mc.updateBankItems();
    }
    
    public void sendCantLogout() {
        mc.cantLogout();
    }
    
}