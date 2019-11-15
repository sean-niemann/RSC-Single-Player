package org.nemotech.rsc.plugins.npcs.grandtree;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class Blurberry  implements ShopInterface, TalkToNpcExecutiveListener, TalkToNpcListener { 

    private final Shop shop = new Shop("Blurberry's Bar", false, 3000, 100, 25,1,  new InvItem(937, 10), new InvItem(938, 10), new InvItem(939, 10), new InvItem(940, 10), new InvItem(941, 10), new InvItem(942, 10), new InvItem(943, 10));
    
    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        npcTalk(p,n, "good day to you", "can i get you drink");
        int opt = showMenu(p,n, "what do you have", "no thanks");
        if (opt == 0) {
            npcTalk(p, n, "take a look");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        } else if(opt == 1) {
            npcTalk(p, n, "ok, take it easy");
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 580;
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
