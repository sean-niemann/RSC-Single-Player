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

public final class HerquinGems implements ShopInterface, TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Herquin's Gems", false, 60000 * 10, 100, 70, 3, new InvItem(160,
            1), new InvItem(159, 0), new InvItem(158, 0), new InvItem(157, 0),
            new InvItem(164, 1), new InvItem(163, 0), new InvItem(162, 0),
            new InvItem(161, 0));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == 155;
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
        int option = showMenu(p, n, "Do you wish to trade?", "Sorry i don't want to talk to you actually");
        if (option == 0) {
            npcTalk(p, n, "Why yes this a jewel shop after all");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }

}