package org.nemotech.rsc.plugins.npcs.grandtree;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class GnomeWaiter implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Gnome Restaurant", false, 30000, 100, 25, 1,
            new InvItem(944, 3), new InvItem(945, 3), new InvItem(947, 3),
            new InvItem(948, 3), new InvItem(949, 3), new InvItem(950, 3),
            new InvItem(951, 3), new InvItem(952, 3), new InvItem(953, 3),
            new InvItem(954, 4), new InvItem(955, 4), new InvItem(956, 4),
            new InvItem(957, 4));

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        npcTalk(p, n,  "hello", "good afternoon",
                "can i tempt you with our new menu?");

        int option = showMenu(p, n, "i'll take a look", "not really");
        switch (option) {
        case 0:
            npcTalk(p, n, "i hope you like what you see");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            break;

        case 1:
            npcTalk(p, n, "ok then, enjoy your stay");
            break;
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 581;
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
