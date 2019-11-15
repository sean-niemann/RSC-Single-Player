package org.nemotech.rsc.plugins.npcs.taverly;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class GaiusTwoHandlerShop  implements ShopInterface,
        TalkToNpcListener, TalkToNpcExecutiveListener {

    private final int GAIUS = 228;
    private final Shop shop = new Shop("Gaius' Two Handed Shop", false, 30000, 100, 60, 2,
            new InvItem(76, 4), new InvItem(77, 3), new InvItem(78, 2),
            new InvItem(426, 1), new InvItem(79, 1), new InvItem(80, 1));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == GAIUS;
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
        npcTalk(p, n, "Welcome to my two handed sword shop");
        final int option = showMenu(p, n, new String[] { "Let's trade",
                "Thankyou" });
        if (option == 0) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }

}
