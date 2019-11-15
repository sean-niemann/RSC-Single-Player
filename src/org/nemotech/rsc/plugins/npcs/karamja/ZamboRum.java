package org.nemotech.rsc.plugins.npcs.karamja;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class ZamboRum implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    public static final int npcid = 165;

    private final Shop shop = new Shop("Karamja Wines, Spirits, and Beers", false, 25000, 100, 70, 2, new InvItem(193,
            3), new InvItem(318, 3), new InvItem(142, 1));

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
        npcTalk(p,
                n,
                "Hey are you wanting to try some of my fine wines and spirits?",
                "All brewed locally on Karamja island");

        final String[] options = new String[] { "Yes please", "No thankyou" };
        int option = showMenu(p, n, options);
        if (option == 0) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }

}