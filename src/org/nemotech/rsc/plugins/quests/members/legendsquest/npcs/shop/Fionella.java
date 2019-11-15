package org.nemotech.rsc.plugins.quests.members.legendsquest.npcs.shop;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Fionella implements ShopInterface, TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Legends' Guild General Store", true, 20000, 155, 55, 13,
            new InvItem(370, 2), new InvItem(257, 5), new InvItem(1263, 1),
            new InvItem(474, 3), new InvItem(640, 50));

    public static final int FIONELLA = 788;

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == FIONELLA) {
            npcTalk(p, n, "Can I help you at all?");
            int menu = showMenu(p, n,
                    "Yes please. What are you selling?",
                    "No thanks");
            if(menu == 0) {
                npcTalk(p, n, "Take a look");
                p.setAccessingShop(shop);
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if(n.getID() == FIONELLA) {
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