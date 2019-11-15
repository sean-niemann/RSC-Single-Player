package org.nemotech.rsc.plugins.npcs.varrock;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class HorvikTheArmourer  implements
        ShopInterface, TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Horvik's Armour Shop", false, 30000, 100, 60, 2, new InvItem(113,
            5), new InvItem(7, 3), new InvItem(114, 3), new InvItem(115, 1),
            new InvItem(117, 3), new InvItem(8, 1), new InvItem(118, 1),
            new InvItem(196, 1), new InvItem(119, 1), new InvItem(9, 1));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == 48;
    }

    @Override
    public Shop[] getShops() {
        return new Shop[] { shop };
    }

    @Override
    public boolean isMembers() {
        return false;
    }

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        npcTalk(p, n, "Hello, do you need any help?");
        int option = showMenu(p,n,
                "No thanks. I'm just looking around",
                "Do you want to trade?");
        
        if (option == 1) {
            npcTalk(p, n, "Yes, I have a fine selection of armour");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }

}
