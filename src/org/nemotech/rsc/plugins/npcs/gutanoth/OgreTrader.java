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

public class OgreTrader implements ShopInterface, TalkToNpcListener,
TalkToNpcExecutiveListener {

    private final Shop shop = new Shop("Gu'tanoth General Store", false, 15000, 130, 40, 3,
            new InvItem(135, 3), 
            new InvItem(140, 2), 
            new InvItem(144, 2), 
            new InvItem(21, 2), 
            new InvItem(166, 2), 
            new InvItem(167, 2), 
            new InvItem(168, 5),
            new InvItem(1263, 10));

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 687;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p,n, "What the human be wantin'");
        int menu = showMenu(p,n,
                "Can I see what you are selling ?",
                "I don't need anything");
        if(menu == 0) {
            npcTalk(p,n, "I suppose so...");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        } else if(menu == 1) {
            npcTalk(p,n, "As you wish");
        }
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
