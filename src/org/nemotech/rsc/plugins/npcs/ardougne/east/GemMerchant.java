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

public class GemMerchant implements ShopInterface, TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Gems Stall", false, 60000 * 5, 150, 80, 3, new InvItem(164,
            2), new InvItem(163, 1), new InvItem(162, 1), new InvItem(161,
                    0));

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(p.getCache().hasKey("stolenGem")) {
            npcTalk(p, n, "Do you really think I'm going to buy something",
                    "That you have just stolen from me",
                    "guards guards");
            //Hero = 324, Knight = 322, Guard = 65, Paladin = 323.
            //attacker.setChasing(p);
        } else {
            npcTalk(p, n, "Here, look at my lovely gems");
            int menu = showMenu(p, n, "Ok show them to me", "I'm not interested thankyou");
            if(menu == 0) {
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            } 
        }
    }
    
    // WHEN STEALING AND CAUGHT BY A MERCHANT ("Hey thats mine");
    // Delay player busy (3000); after stealing and Npc shout out to you.

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 330;
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
