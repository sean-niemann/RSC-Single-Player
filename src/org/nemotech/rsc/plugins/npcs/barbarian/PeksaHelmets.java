package org.nemotech.rsc.plugins.npcs.barbarian;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class PeksaHelmets implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Peksa's Helmet Shop", false, 25000, 100, 60, 1, new InvItem(104,
            5), new InvItem(5, 3), new InvItem(105, 3), new InvItem(106, 1),
            new InvItem(107, 1), new InvItem(108, 4), new InvItem(6, 3),
            new InvItem(109, 2), new InvItem(110, 1), new InvItem(111, 1));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == 75;
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
        npcTalk(p, n, "Are you interested in buying or selling a helmet?");

        int option = showMenu(p, n, "I could be, yes", "No, I'll pass on that");
        if (option == 0) {
            npcTalk(p, n, "Well look at all these great helmets!");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        } else if(option == 1) {
            npcTalk(p, n, "Well come back if you change your mind");
        }
    }

}