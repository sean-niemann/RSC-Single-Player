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

public final class ZekeScimitars implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    public static final int npcid = 84;

    private final Shop shop = new Shop("Zeke's Superior Scimitars", false, 25000, 100, 55, 2,
            new InvItem(82, 5), new InvItem(83, 3), new InvItem(84, 2),
            new InvItem(85, 1));

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
        npcTalk(p, n, "A thousand greetings " + ((p.isMale()) ? "sir"
                : "madam"));

        final String[] options = new String[] { "Do you want to trade?", "Nice cloak" };
        int option = showMenu(p, n, options);
        if (option == 0) {
            npcTalk(p, n, "Yes, certainly","I deal in scimitars");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        } else if(option == 1) {
            npcTalk(p, n, "Thank you");
        }
    }

}