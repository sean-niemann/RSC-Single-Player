package org.nemotech.rsc.plugins.npcs.varrock;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.PickupListener;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.PickupExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class TeaSeller implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener, PickupExecutiveListener,
        PickupListener {

    private final Shop shop = new Shop("Tea Stall", false, 30000, 100, 60, 2, new InvItem(739,
            20));

    @Override
    public boolean blockPickup(final Player p, final Item i) {
        return i.getID() == 1285;
    }

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == 780;
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
    public void onPickup(final Player p, final Item i) {
        if (i.getID() == 1285) {
            final NPC n = World.getWorld().getNpcById(780);
            if (n == null) {
                return;
            }
            n.face(p);
            npcTalk(p, n, "hey ! get your hands off that tea !",
                    "that's for display purposes only",
                    "Im not running a charity here !");
        }
    }

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        npcTalk(p, n, "Greetings!",
                "Are you in need of refreshment ?");

        final String[] options = new String[] { "Yes please", "No thanks",
                "What are you selling?" };
        int option = showMenu(p,n, options);
        switch (option) {
        case 0:
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            break;
        case 1:
            npcTalk(p, n, "Well if you're sure",
                    "You know where to come if you do !");
            break;
        case 2:
            npcTalk(p, n, "Only the most delicious infusion",
                    "Of the leaves of the tea plant",
                    "Grown in the exotic regions of this world...",
                    "Buy yourself a cup !");
            break;
        }
    }

}
