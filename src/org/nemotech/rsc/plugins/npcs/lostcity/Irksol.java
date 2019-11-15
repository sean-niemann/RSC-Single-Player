package org.nemotech.rsc.plugins.npcs.lostcity;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class Irksol implements ShopInterface, TalkToNpcExecutiveListener,
        TalkToNpcListener {

    private final Shop shop = new Shop("Ruby Ring Shop", false, 3000, 50, 30,2,
            new InvItem(286, 5));

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        if (n.getID() == 218) {
            npcTalk(p, n, "selling ruby rings",
                    "The best deals in all the planes of existance");
            int option = showMenu(p, n, "I'm interested in these deals",
                    "No thank you");
            switch (option) {

            case 0:
                npcTalk(p, n, "Take a look at these beauties");
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
                break;
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 218;
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
