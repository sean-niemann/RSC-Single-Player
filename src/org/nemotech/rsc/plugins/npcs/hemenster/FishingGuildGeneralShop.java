package org.nemotech.rsc.plugins.npcs.hemenster;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class FishingGuildGeneralShop  implements
        ShopInterface, TalkToNpcListener, TalkToNpcExecutiveListener {

    private static final int SHOPKEEPER = 371;
    private final Shop shop = new Shop("Fishing Guild Shop", true, 15000, 100, 70,2,
            new InvItem(380, 200), new InvItem(381, 200), new InvItem(550, 0),
            new InvItem(552, 0), new InvItem(554, 0), new InvItem(366, 0),
            new InvItem(372, 0), new InvItem(369, 0), new InvItem(551, 0),
            new InvItem(553, 0), new InvItem(555, 0), new InvItem(367, 0),
            new InvItem(373, 0));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == SHOPKEEPER;
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
        npcTalk(p, n, "Would you like to buy some fishing equipment",
                "Or sell some fish");
        final int option = showMenu(p, n, "Yes please",
                "No thankyou"); // "is that your ship"
        if (option == 0) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }

}
