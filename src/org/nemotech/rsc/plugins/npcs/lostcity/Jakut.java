package org.nemotech.rsc.plugins.npcs.lostcity;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class Jakut implements ShopInterface, TalkToNpcExecutiveListener,
        TalkToNpcListener {

    private final Shop shop = new Shop("Dragon Sword Shop", false, 3000, 100, 60,2,
            new InvItem(593, 2));

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        npcTalk(p, n, "Dragon swords, get your Dragon swords",
                "Straight from the plane of frenaskrae");

        int option = showMenu(p, n, "Yes please",
                "No thank you, I'm just browsing the marketplace");
        switch (option) {

        case 0:
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            break;
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 220;
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
