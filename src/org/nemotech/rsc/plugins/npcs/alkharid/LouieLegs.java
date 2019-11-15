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

public final class LouieLegs implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    public static final int npcid = 85;

    private final Shop shop = new Shop("Louie's Legs", false, 25000, 100, 65, 1, new InvItem(206,
            5), new InvItem(9, 3), new InvItem(121, 2), new InvItem(248, 1),
            new InvItem(122, 1), new InvItem(123, 1));

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
        npcTalk(p, n, "Hey, wanna buy some armour?");

        int option = showMenu(p, n, "What have you got?", "No, thank you");
        if (option == 0) {
            npcTalk(p, n, "Take a look, see");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }

}