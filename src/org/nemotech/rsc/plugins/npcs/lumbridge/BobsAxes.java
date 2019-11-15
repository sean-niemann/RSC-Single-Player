package org.nemotech.rsc.plugins.npcs.lumbridge;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class BobsAxes implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Bob's Axes", false, 15000, 100, 60, 2, new InvItem(156,
            5), new InvItem(87, 10), new InvItem(12, 5), new InvItem(88, 3),
            new InvItem(89, 5), new InvItem(90, 2), new InvItem(91, 1));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == 1;
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
        npcTalk(p, n, "Hello. How can I help you?");
        int option = showMenu(p, n, "Give me a quest!",
                "Have you anything to sell?");
        switch (option) {
        case 0:
            npcTalk(p, n, "Get yer own!");
            break;
        case 1:
            npcTalk(p, n, "Yes, I buy and sell axes, take your pick! (or axe)");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            break;
        }
    }
}
