package org.nemotech.rsc.plugins.npcs.shilo;

import static org.nemotech.rsc.plugins.Plugin.*;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Jiminua implements ShopInterface,
TalkToNpcExecutiveListener, TalkToNpcListener {

    public static final int JIMINUA = 522;
    
    private final Shop shop = new Shop("Tai Bwo Wannai General Store", true, 15000, 150, 50, 2,
            new InvItem(166, 2), new InvItem(465, 10), new InvItem(468, 3),
            new InvItem(135, 3), new InvItem(87, 3), new InvItem(156, 2),
            new InvItem(12, 5), new InvItem(15, 12), new InvItem(16, 10), 
            new InvItem(17, 10), new InvItem(132, 2), new InvItem(138, 10), 
            new InvItem(169, 10), new InvItem(211, 10), new InvItem(599, 10),
            new InvItem(773, 10), new InvItem(167, 10), new InvItem(168, 10),
            new InvItem(982, 50), new InvItem(983, 50), new InvItem(464, 50),
            new InvItem(1172, 50));

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == JIMINUA) {
            npcTalk(p, n, "Welcome to the Jungle Store, Can I help you at all?");
            int menu = showMenu(p, n,
                    "Yes please. What are you selling?",
                    "No thanks");
            if(menu == 0) {
                npcTalk(p, n, "Take yourself a good look");
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if(n.getID() == JIMINUA) {
            return true;
        }
        return false;
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
