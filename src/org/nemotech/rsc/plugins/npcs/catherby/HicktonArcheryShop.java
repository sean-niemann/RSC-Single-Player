package org.nemotech.rsc.plugins.npcs.catherby;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class HicktonArcheryShop  implements ShopInterface,
        TalkToNpcListener, TalkToNpcExecutiveListener {

    private static final int HICKTON = 289;
    private final Shop shop = new Shop("Hickton's Archery Store", false, 1000, 100, 80,1,
            new InvItem(190, 200), new InvItem(11, 200), new InvItem(638, 200),
            new InvItem(640, 0), new InvItem(642, 0), new InvItem(644, 0),
            new InvItem(646, 0), new InvItem(669, 200), new InvItem(670, 180),
            new InvItem(671, 160), new InvItem(672, 140),
            new InvItem(673, 120), new InvItem(674, 100), new InvItem(189, 4),
            new InvItem(188, 2), new InvItem(60, 2), new InvItem(649, 4),
            new InvItem(648, 4));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == HICKTON;
    }

    @Override
    public Shop[] getShops() {
        return new Shop[] { shop };
    }

    @Override
    public boolean isMembers() {
        return true;
    }

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        npcTalk(p, n, "Welcome to Hickton's Archery store",
                "Do you want to see my wares?");
        final int option = showMenu(p, n, new String[] { "Yes please",
                "No I prefer to bash things close up" });
        if (option == 0) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }

}
