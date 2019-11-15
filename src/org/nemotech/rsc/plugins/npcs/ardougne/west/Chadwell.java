package org.nemotech.rsc.plugins.npcs.ardougne.west;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class Chadwell  implements ShopInterface, TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("West Ardougne General Store", false, 3000, 130, 40,3,  new InvItem(237, 7), new InvItem(156, 10), new InvItem(357, 2), new InvItem(50, 2), new InvItem(166, 10), new InvItem(259, 2), new InvItem(168, 5), new InvItem(138, 10), new InvItem(17, 10), new InvItem(135, 3), new InvItem(132, 2), new InvItem(188, 2), new InvItem(11, 200), new InvItem(1263, 10));
    
    @Override
    public void onTalkToNpc(Player p, final NPC n) {
            npcTalk(p,n, "hello there", "good day, what can i get you?");
            int options = showMenu(p,n, "nothing thanks, just browsing", "lets see what you've got");
            if(options == 0) {
                npcTalk(p, n, "ok then");
            }
            if(options == 1) {
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            }

    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 661;
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
