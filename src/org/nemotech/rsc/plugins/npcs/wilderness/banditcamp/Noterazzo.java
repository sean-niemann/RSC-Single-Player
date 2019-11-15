package org.nemotech.rsc.plugins.npcs.wilderness.banditcamp;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Noterazzo implements ShopInterface, TalkToNpcListener, TalkToNpcExecutiveListener {
    
    private final Shop shop = new Shop("Bandit Camp General Store", true, 12400, 90, 60, 3,
            new InvItem(135, 3), new InvItem(140, 2), new InvItem(166, 2),
            new InvItem(167, 2), new InvItem(168, 5), new InvItem(156, 5),
            new InvItem(87, 10));
    
    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if(n.getID() == 233) {
            return true;
        }
        return false;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == 233) {
        
            npcTalk(p, n, "Hey wanna trade?, I'll give the best deals you can find");
            int menu = showMenu(p, n, "Yes please", "No thankyou", "How can you afford to give such good deals?");
            if(menu == 0) {
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            } else if(menu == 1) {
                //NOTHING
            } else if(menu == 2) {
                npcTalk(p, n, "The general stores in Asgarnia and Misthalin are heavily taxed",
                        "It really makes it hard for them to run an effective buisness",
                        "For some reason taxmen don't visit my store");
                p.message("Noterazzo winks at you");
            }
        }
    }

    @Override
    public Shop[] getShops() {
        return new Shop[] { shop };
    }

    @Override
    public boolean isMembers() {
        return false;
    }
}
