package org.nemotech.rsc.plugins.npcs.dwarvenmine;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class NurmofPickaxe implements ShopInterface,
TalkToNpcExecutiveListener, TalkToNpcListener {

    public static final int npcid = 773;

    private final Shop shop = new Shop("Nurmof's Pickaxe Shop", false, 25000, 100, 60, 2, new InvItem(156,
            6), new InvItem(1258, 5), new InvItem(1259, 4),
            new InvItem(1260, 3), new InvItem(1261, 2), new InvItem(1262, 1));

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
        npcTalk(p, n, "greetings welcome to my pickaxe shop",
                "Do you want to buy my premium quality pickaxes");

        int option = showMenu(p, n, "Yes please", "No thankyou", "Are your pickaxes better than other pickaxes then?");
        if (option == 0) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        } else if(option == 2) {
            npcTalk(p, n, "Of course they are",
                    "My pickaxes are made of higher grade metal than your ordinary bronze pickaxes",
                    "Allowing you to have multiple swings at a rock until you get the ore from it");
        }
    }

}