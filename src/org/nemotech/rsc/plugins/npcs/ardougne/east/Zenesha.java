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

public class Zenesha implements ShopInterface, TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Zenesha's Plate Mail Shop", false, 30000, 100, 60, 2, new InvItem(308, 3), new InvItem(312, 1), new InvItem(309, 1), new InvItem(313, 1), new InvItem(310, 1));

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        
            npcTalk(p, n, "hello I sell plate mail tops");
            int menu = showMenu(p, n, "I'm not interested", "I may be interested");
            if(menu == 1) {
                npcTalk(p, n, "Look at these fine samples then");
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            } 
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 331;
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
