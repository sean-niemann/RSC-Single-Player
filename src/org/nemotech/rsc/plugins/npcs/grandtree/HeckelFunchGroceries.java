package org.nemotech.rsc.plugins.npcs.grandtree;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class HeckelFunchGroceries implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {
    
    public static int HECKEL_FUNCH = 535;

    private final Shop shop = new Shop("Funch's Fine Groceries", false, 30000, 100, 55,1,
            new InvItem(876, 5), new InvItem(870, 5), new InvItem(869, 5),
            new InvItem(868, 5), new InvItem(861, 5), new InvItem(873, 3),
            new InvItem(857, 5), new InvItem(855, 5), new InvItem(863, 5),
            new InvItem(765, 3), new InvItem(834, 5), new InvItem(337, 5),
            new InvItem(772, 5), new InvItem(871, 5), new InvItem(22, 5),
            new InvItem(13, 5), new InvItem(851, 5));

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        playerTalk(p, n, "hello there");
        npcTalk(p, n, "good day to you my friend ..and a beautiful one at that",
                "would you like some groceries? i have all sorts",
                "alcohol also, if your partial to a drink");

        int option = showMenu(p, n, "no thank you", "i'll have a look");
        switch (option) {
        case 0:
            npcTalk(p, n, "ahh well, all the best to you");
            break;

        case 1:
            npcTalk(p, n, "there's a good human");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            break;
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == HECKEL_FUNCH;
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
