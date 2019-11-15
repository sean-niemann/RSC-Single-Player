package org.nemotech.rsc.plugins.npcs.dwarvenmine;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Drogo  implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Drogo's Mining Emporium", false, 30000, 100, 70, 2, new InvItem(168,
            4), new InvItem(156, 4), new InvItem(150, 0), new InvItem(202, 0),
            new InvItem(151, 0), new InvItem(155, 0), new InvItem(169, 0),
            new InvItem(170, 0), new InvItem(172, 0));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == 113;
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
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p, n, "Ello");
        int m = showMenu(p, n, "Do you want to trade?", "Hello shorty",
                "Why don't you ever restock ores and bars?");
        if (m == 0) {
            npcTalk(p, n, "Yeah sure, I run a mining store.");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        } else if (m == 1) {
            npcTalk(p, n, "I may be short, but at least I've got manners");
        } else if (m == 2) {
            npcTalk(p, n, "The only ores and bars I sell are those sold to me");
        }
    }
}
