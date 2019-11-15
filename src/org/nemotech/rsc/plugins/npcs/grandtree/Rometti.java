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

public final class Rometti implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Fine Fashions", false, 3000, 100, 55,1,
            new InvItem(836, 5), new InvItem(837, 5), new InvItem(838, 5),
            new InvItem(839, 5), new InvItem(840, 5), new InvItem(841, 3),
            new InvItem(842, 5), new InvItem(843, 5), new InvItem(844, 5),
            new InvItem(845, 3), new InvItem(846, 5), new InvItem(847, 5),
            new InvItem(848, 5), new InvItem(849, 5), new InvItem(850, 5),
            new InvItem(966, 5), new InvItem(967, 5), new InvItem(968, 5),
            new InvItem(969, 5), new InvItem(970, 5));

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        playerTalk(p, n, "hello");
        npcTalk(p, n, "hello traveller",
                "have a look at my latest range of gnome fashion",
                "rometti is the ultimate label in gnome high society");
        playerTalk(p, n, "really");
        npcTalk(p, n, "pastels are all the rage this season");
        int option = showMenu(p, n, "i've no time for fashion",
                "ok then let's have a look");
        switch (option) {
        case 0:
            npcTalk(p, n, "hmm...i did wonder");
            break;
        case 1:
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            break;
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 532;
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
