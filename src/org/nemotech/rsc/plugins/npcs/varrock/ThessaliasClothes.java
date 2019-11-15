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

public final class ThessaliasClothes implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Thessalia's Fine Clothes", false, 30000, 100, 55, 3, new InvItem(182,
            3), new InvItem(15, 12), new InvItem(16, 10), new InvItem(17, 10),
            new InvItem(191, 1), new InvItem(194, 5), new InvItem(195, 3),
            new InvItem(187, 2), new InvItem(183, 4), new InvItem(200, 5),
            new InvItem(807, 3), new InvItem(808, 3));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == 59;
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
        npcTalk(p, n, "Hello", "Do you want to buy any fine clothes?");

        /**
         * I have lost my scythe can I get another please? Ohh you poor dear, I
         * have another here 'Thessalia gives you a new scythe' I have lost my
         * bunny ears can I get some more please? Ohh you poor dear, I have
         * another here 'Thessalia gives you some new bunny ears'
         * 
         */
        final String[] options = new String[] { "What have you got?",
                "No, thank you" };
        int option = showMenu(p,n, options);
        switch (option) {
        case 0:
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            break;
        }

    }

}
