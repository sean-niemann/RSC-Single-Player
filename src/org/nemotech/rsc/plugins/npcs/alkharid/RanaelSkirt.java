package org.nemotech.rsc.plugins.npcs.alkharid;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class RanaelSkirt implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    public static final int npcid = 103;

    private final Shop shop = new Shop("Ranael's Plateskirt Shop", false, 25000, 100, 65, 1, new InvItem(214,
            5), new InvItem(215, 3), new InvItem(225, 2), new InvItem(434, 1),
            new InvItem(226, 1), new InvItem(227, 1));

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
        npcTalk(p, n, "Do you want to buy any armoured skirts?",
                "Designed especially for ladies who like to fight");

        int option = showMenu(p, n, "Yes please", "No thank you that's not my scene");
        if (option == 0) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }

}