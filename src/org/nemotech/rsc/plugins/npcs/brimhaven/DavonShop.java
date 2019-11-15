package org.nemotech.rsc.plugins.npcs.brimhaven;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class DavonShop implements ShopInterface, TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Davon's Amulet Store", false, 900000000, 120, 90, 2, new InvItem(44, 0), new InvItem(314, 0), new InvItem(315, 0), new InvItem(316, 0), new InvItem(317, 0));

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p, n, "Pssst come here if you want to do some amulet trading");
        int menu = showMenu(p, n, "What are you selling?", "What do you mean pssst?", "Why don't you ever restock some types of amulets?");
        if(menu == 0) {
            p.message("Davon opens up his jacket to reveal some amulets");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        } else if(menu == 1) {
            npcTalk(p, n, "I was clearing my throat");
        } else if(menu == 2) {
            npcTalk(p, n, "Some of these amulets are very hard to get",
                    "I have to wait until an adventurer supplies me");
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 278;
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
