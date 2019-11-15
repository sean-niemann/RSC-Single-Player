package org.nemotech.rsc.plugins.npcs.ardougne.east;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class ArdougneGeneralShop implements ShopInterface, TalkToNpcListener,
        TalkToNpcExecutiveListener {

    private static final int KORTAN = 337, AEMAD = 336;
    private final Shop shop = new Shop("East Ardougne Adventurers' Store", true, 15000, 130, 40, 3, new InvItem(464, 
            10), new InvItem(156, 2), new InvItem(12, 2), new InvItem(132, 2),
            new InvItem(166, 2), new InvItem(207, 2), new InvItem(11, 30),
            new InvItem(237, 1), new InvItem(982, 50), new InvItem(1263, 10));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == KORTAN || n.getID() == AEMAD;
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
        npcTalk(p, n, "Hello you look like a bold adventurer",
                "You've come to the right place for adventurer's equipment");
        final int option = showMenu(p, n,
                new String[] { "Oh that sounds interesting",
                        "No I've come to the wrong place" });
        if (option == 0) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }

}
