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

public class SilverMerchant implements ShopInterface, TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Silver Stall", false, 60000 * 2, 100, 70,2, new InvItem(44,
            2), new InvItem(383, 1), new InvItem(384, 1));

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p, n, "Silver! Silver!", "Best prices for buying and selling in all Kandarin!");
        int menu = showMenu(p, n, "Yes please", "No thankyou");
        if(menu == 0) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        } 
    }
    
    // WHEN STEALING AND CAUGHT BY A MERCHANT ("Hey thats mine");
    // Delay player busy (3000); after stealing and Npc shout out to you.

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 328;
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
