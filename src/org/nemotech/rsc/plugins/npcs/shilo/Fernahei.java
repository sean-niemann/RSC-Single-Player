package org.nemotech.rsc.plugins.npcs.shilo;

import org.nemotech.rsc.client.action.ActionManager;
import org.nemotech.rsc.client.action.impl.ShopHandler;
import static org.nemotech.rsc.plugins.Plugin.*;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Fernahei implements ShopInterface,
TalkToNpcExecutiveListener, TalkToNpcListener {

    public static final int FERNAHEI = 616;

    private final Shop shop = new Shop("Fernahei's Fishing Shop", true, 15000, 100, 70, 2,
            new InvItem(377, 5), new InvItem(378, 5), new InvItem(380, 200),
            new InvItem(381, 200), new InvItem(358, 0), new InvItem(363, 0),
            new InvItem(356, 0));

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == FERNAHEI) {
            npcTalk(p, n, "Welcome to Fernahei's Fishing Shop Bwana!",
                    "Would you like to see my items?");
            int menu = showMenu(p, n,
                    "Yes please!",
                    "No, but thanks for the offer.");
            if(menu == 0) {
                ActionManager.get(ShopHandler.class).handleShopOpen(shop);
            } else if(menu == 1) {
                npcTalk(p, n, "That's fine and thanks for your interest.");
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == FERNAHEI;
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
