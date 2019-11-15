package org.nemotech.rsc.plugins.npcs.portsarim;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class GrumsGoldShop implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Grum's Gold Exchange", false, 30000, 100, 70,2, new InvItem(283,
            0), new InvItem(284, 0), new InvItem(285, 0), new InvItem(286, 0),
            new InvItem(287, 0), new InvItem(288, 0), new InvItem(289, 0),
            new InvItem(290, 0), new InvItem(291, 0), new InvItem(292, 0),
            new InvItem(301, 0), new InvItem(302, 0));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == 157;
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
        npcTalk(p, n, "Would you like to buy or sell some gold jewellery");
        int option = showMenu(p, n, "Yes please", "No, I'm not that rich");
        switch (option) {
        case 0:
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            break;
        case 1:
            npcTalk(p, n, "Get out then we don't want any riff-raff in here");
            break;
        }

    }

}
