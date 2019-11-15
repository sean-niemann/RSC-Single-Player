package org.nemotech.rsc.plugins.npcs.falador;

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

public final class CassieShields  implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    public static final int npcid = 101;

    private final Shop shop = new Shop("Cassie's Shields", false, 25000, 100, 60, 2,
            new InvItem(4, 5), new InvItem(124, 3), new InvItem(128, 3),
            new InvItem(3, 2), new InvItem(2, 0), new InvItem(125, 0),
            new InvItem(129, 0), new InvItem(126, 0));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == npcid;
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
        if (n.getID() == npcid) {
            playerTalk(p, n, "What wares are you selling?");
            npcTalk(p,n, "I buy and sell shields", "Do you want to trade?");
            int option = showMenu(p,n, "Yes please", "No thanks");
            if (option == 0) {
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            }
        }
    }

}