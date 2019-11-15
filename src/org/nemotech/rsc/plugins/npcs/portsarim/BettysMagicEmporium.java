package org.nemotech.rsc.plugins.npcs.portsarim;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class BettysMagicEmporium  implements
        ShopInterface, TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Betty's Magic Emporium", false, 6000, 100, 75, 2, new InvItem(31,
            30), new InvItem(32, 30), new InvItem(33, 30), new InvItem(34,
            30), new InvItem(35, 30), new InvItem(36, 30), new InvItem(270,
            30), new InvItem(185, 1), new InvItem(199, 1));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == 149;
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
        if (n.getID() == 149) {
            npcTalk(p, n, "Welcome to the magic emporium");
            int opt = showMenu(p, n, "Can I see your wares?",
                    "Sorry I'm not into magic");
            if (opt == 0) {
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            }
            if (opt == 1) {
                npcTalk(p, n, "Send anyone my way who is");
            }
        }
    }

}
