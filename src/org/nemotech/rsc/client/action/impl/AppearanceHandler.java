package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Inventory;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.player.Appearance;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.client.action.ActionHandler;
import org.nemotech.rsc.client.mudclient;

public class AppearanceHandler implements ActionHandler {

    public void handleAppearanceChange(int headGender, int headType, int bodyGender, int unknown, int hairColor, int topColor, int bottomColor, int skinColor) {
        Player player = World.getWorld().getPlayer();

        int headSprite = headType + 1;
        int bodySprite = bodyGender + 1;

        Appearance appearance = new Appearance(hairColor, topColor, bottomColor, skinColor, headSprite, bodySprite);
        if (!appearance.isValid()) {
            System.err.println("INVALID APPEARANCE!");
            return;
        }

        player.setMale(headGender == 1);

        if (player.isMale()) {
            Inventory inv = player.getInventory();
            for (int slot = 0; slot < inv.size(); slot++) {
                InvItem i = inv.get(slot);
                if (i.getDef().isWieldable() && i.getWieldableDef().getWieldPos() == 1 && i.isWielded() && i.getWieldableDef().femaleOnly()) {
                    i.setWield(false);
                    player.updateWornItems(i.getWieldableDef().getWieldPos(), player.getAppearance().getSprite(i.getWieldableDef().getWieldPos()));
                    player.getSender().sendUpdateItem(slot);
                    break;
                }
            }
        }
        int[] oldWorn = player.getWornItems();
        int[] oldAppearance = player.getAppearance().getSprites();
        player.setAppearance(appearance);
        int[] newAppearance = player.getAppearance().getSprites();
        for (int i = 0; i < 12; i++) {
            if (oldWorn[i] == oldAppearance[i]) {
                player.updateWornItems(i, newAppearance[i]);
            }
        }
        // added
        mudclient.getInstance().localPlayer.colourHair = appearance.getHairColor();
        mudclient.getInstance().localPlayer.colourTop = appearance.getShirtColor();
        mudclient.getInstance().localPlayer.colourBottom = appearance.getPantsColor();
        mudclient.getInstance().localPlayer.colourSkin = appearance.getSkinColor();
    }
    
}