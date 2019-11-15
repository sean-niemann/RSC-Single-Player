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

public final class Gulluck implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Gulluck and Sons", false, 3000, 100, 25,1, new InvItem(11,
            200), new InvItem(190, 150), new InvItem(786, 1), new InvItem(189,
            4), new InvItem(188, 2), new InvItem(60, 2), new InvItem(669, 200),
            new InvItem(670, 180), new InvItem(671, 160),
            new InvItem(671, 140), new InvItem(12, 5), new InvItem(88, 3),
            new InvItem(89, 5), new InvItem(90, 2), new InvItem(91, 1),
            new InvItem(76, 4), new InvItem(77, 3), new InvItem(78, 2),
            new InvItem(426, 1), new InvItem(79, 1), new InvItem(80, 1));

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        if (n.getID() == 587) {
            playerTalk(p, n, "hello");
            npcTalk(p, n, "good day brave adventurer",
                    "could i interest you in my fine selection of weapons?");

            int option = showMenu(p, n, "i'll take a look", "No thanks");
            switch (option) {
                case 0:
                    org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
                    break;
                case 1:
                    npcTalk(p, n, "grrr");
                    break;

            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 587;
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
