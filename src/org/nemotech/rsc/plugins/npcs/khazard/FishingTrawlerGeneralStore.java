package org.nemotech.rsc.plugins.npcs.khazard;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class FishingTrawlerGeneralStore implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Fishing Trawler General Store", false, 3000, 130, 40, 3,
            new InvItem(156, 5), new InvItem(135, 3), new InvItem(140, 2),
            new InvItem(144, 2), new InvItem(50, 2), new InvItem(166, 2),
            new InvItem(167, 2), new InvItem(168, 5), new InvItem(237, 30),
            new InvItem(136, 30), new InvItem(1282, 30), new InvItem(785, 30));

    @Override
    public void onTalkToNpc(Player p, final NPC n) {

        npcTalk(p, n, "Can I help you at all");

        String[] options = new String[] { "Yes please. What are you selling?",
                "No thanks" };
        int option = showMenu(p,n, options);
        switch (option) {
        case 0:
            npcTalk(p, n, "Take a look");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            break;
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 391;
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
