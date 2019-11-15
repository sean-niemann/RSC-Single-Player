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

public final class MagicStoreOwner implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Magic Guild Shop", false, 3000, 100, 75, 2,
            new InvItem(31, 50), new InvItem(32, 50), new InvItem(33, 50),
            new InvItem(34, 50), new InvItem(35, 50), new InvItem(36, 50),
            new InvItem(825, 30), new InvItem(614, 5), new InvItem(101, 2),
            new InvItem(102, 2), new InvItem(103, 2), new InvItem(197, 2));

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        npcTalk(p, n, "Welcome to the magic guild store",
                "would you like to buy some magic supplies");

        int option = showMenu(p, n, "Yes please", "No thank you");
        switch (option) {
        case 0:
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            break;
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 514;
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
