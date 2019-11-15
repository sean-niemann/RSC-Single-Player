package org.nemotech.rsc.plugins.npcs.taverly;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class HelemosShop  implements ShopInterface,
TalkToNpcListener, TalkToNpcExecutiveListener {

    private static final int HELEMOS = 269;
    private final Shop shop = new Shop("Dragon Axe Shop", false, 60000, 100, 55,3, new InvItem(594, 1));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == HELEMOS;
    }

    @Override
    public Shop[] getShops() {
        return new Shop[] { shop };
    }

    @Override
    public boolean isMembers() {
        return true;
    }

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        npcTalk(p, n, "Welcome to the hero's guild");
        final int option = showMenu(p, n, new String[] {
                "So do you sell anything here?", "So what can I do here?" });
        if (option == 0) {
            npcTalk(p, n, "Why yes we do run an exclusive shop for our members");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        } else if (option == 1) {
            npcTalk(p, n, "Look around there are all sorts of things to keep our members entertained");
        } 
    }
}
