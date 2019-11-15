package org.nemotech.rsc.plugins.npcs.falador;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class FlynnMaces implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    public static final int npcid = 115;

    private final Shop shop = new Shop("Flynn's Mace Shop", false, 25000, 100, 60, 1,
            new InvItem(94, 5), new InvItem(0, 4), new InvItem(95, 4),
            new InvItem(96, 3), new InvItem(97, 2));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == npcid;
    }

    @Override
    public Shop[] getShops() {
        return new Shop[] { shop };
    }

    @Override
    public boolean isMembers() {
        return false;
    }

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        npcTalk(p, n, "Hello do you want to buy or sell any maces?");

        int opt = showMenu(p, n, "No thanks", "Well I'll have a look anyway");
        if (opt == 1) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }

    }

}