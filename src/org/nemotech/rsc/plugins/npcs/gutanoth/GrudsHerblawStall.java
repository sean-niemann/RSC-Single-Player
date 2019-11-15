package org.nemotech.rsc.plugins.npcs.gutanoth;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class GrudsHerblawStall implements ShopInterface,
TalkToNpcExecutiveListener, TalkToNpcListener {
    
    private final Shop shop = new Shop("Grud's Herblaw Stall", false, 3000, 100, 70,2,
            new InvItem(465, 50), new InvItem(468, 3), new InvItem(270, 50));

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p,n, "Does The little creature want to buy sumfin'");
        int menu = showMenu(p,n,
        "Yes I do",
        "No I don't");
        if(menu == 0) {
            npcTalk(p,n, "Welcome to Grud's herblaw stall");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        } else if(menu == 1) {
            npcTalk(p,n, "Suit yourself");
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 686;
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
