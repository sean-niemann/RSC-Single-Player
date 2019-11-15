package org.nemotech.rsc.plugins.npcs.yanille;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class Frenita implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Frenita's Cooking Shop", false, 3000, 100, 55, 1,
            new InvItem(251, 5), new InvItem(252, 2), new InvItem(338, 2),
            new InvItem(341, 2), new InvItem(348, 5), new InvItem(166, 4),
            new InvItem(140, 1), new InvItem(135, 8), new InvItem(337, 2),
            new InvItem(136, 8));

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        npcTalk(p, n, "Would you like to buy some cooking equipment");

        int option = showMenu(p, n, "Yes please", "No thank you");
        switch (option) {

        case 0:
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            break;
        }

    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 530;
    }

    @Override
    public Shop[] getShops() {
        return new Shop[] { shop };
    }

    @Override
    public boolean isMembers() {
        return true;
    }

}
