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

public class ArheinGeneralShop  implements ShopInterface,
        TalkToNpcListener, TalkToNpcExecutiveListener {

    private static final int ARHEIN = 280;
    private final Shop shop = new Shop("Arhein's General Store", true, 15000, 130, 40, 3, new InvItem(21, 10),
            new InvItem(156, 2), new InvItem(341, 2), new InvItem(338, 2),
            new InvItem(166, 2), new InvItem(167, 2), new InvItem(168, 5),
            new InvItem(237, 2), new InvItem(135, 2));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == ARHEIN;
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
        npcTalk(p, n, "Hello would you like to trade");
        final int option = showMenu(p, n, new String[] { "Yes ok",
                "No thankyou" }); // "is that your ship"
        if (option == 0) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }

}
