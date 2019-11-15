package org.nemotech.rsc.client.action.impl;

import java.util.ArrayList;
import java.util.Map;

import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.util.Formulae;
import org.nemotech.rsc.client.sound.SoundEffect;
import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.client.action.ActionHandler;

public class WieldHandler implements ActionHandler {
    
    public void handleWield(int index) {
        Player player = World.getWorld().getPlayer();
        if (player.isBusy() && !player.inCombat()) {
            return;
        }
        
        player.reset();
        
        InvItem item = player.getInventory().get(index);
        int id = item.getID();
        
        String requirements = "";
        
        for (Map.Entry<Integer, Integer> e : EntityManager.getItemWieldableDef(id).getStatsRequired()) {
            if (player.getMaxStat(e.getKey()) < e.getValue()) {
                requirements += e.getValue() + " " + Formulae.STAT_NAMES[e.getKey()] + ", ";
            }
        }
        if (!requirements.equals("")) {
            player.getSender().sendMessage("You must have at least " + requirements.substring(0, requirements.length() - 2) + " to use this item");
            return;
        }
        if (EntityManager.getItemWieldableDef(id).femaleOnly() && player.isMale()) {
            player.getSender().sendMessage("This piece of armor is for a female only");
            return;
        }
        ArrayList<InvItem> items = player.getInventory().getItems();
        for (InvItem i : items) {
            if (item.wieldingAffectsItem(i) && i.isWielded()) {
                handleUnwield(i, false);
            }
        }
        if(PluginManager.getInstance().blockDefaultAction("Wield", new Object[] { player, item })) {
            return;
        }
        item.setWield(true);
        player.getSender().sendSound(SoundEffect.CLICK);
        int pos = EntityManager.getItemWieldableDef(id).getWieldPos();
        player.updateWornItems(pos, EntityManager.getItemWieldableDef(id).getSprite());
        player.getSender().sendInventory();
        player.getSender().sendEquipmentStats();
    }
    
    public void handleUnwield(int index) {
        Player player = World.getWorld().getPlayer();
        handleUnwield(player.getInventory().get(index), true);
    }
    
    public void handleUnwield(InvItem item, boolean sound) {
        Player player = World.getWorld().getPlayer();
        if (player.isBusy() && !player.inCombat()) {
            return;
        }
        
        player.reset();
        int id = item.getID();
        if(PluginManager.getInstance().blockDefaultAction("Unwield", new Object[] { player, item })) {
            return;
        }
        item.setWield(false);
        if (sound) {
            player.getSender().sendSound(SoundEffect.CLICK);
        }
        int pos = EntityManager.getItemWieldableDef(id).getWieldPos();
        player.updateWornItems(pos, player.getAppearance().getSprite(pos));
        player.getSender().sendInventory();
        player.getSender().sendEquipmentStats();
    }
    
}