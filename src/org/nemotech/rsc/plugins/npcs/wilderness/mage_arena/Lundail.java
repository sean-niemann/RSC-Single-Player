package org.nemotech.rsc.plugins.npcs.wilderness.mage_arena;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class Lundail implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Mage Arena Rune Shop", false, 6000, 190, 60, 10, new InvItem(33,
            1000), new InvItem(31, 1000), new InvItem(32, 1000), new InvItem(34,
            1000), new InvItem(35, 1000), new InvItem(36, 1000));

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        npcTalk(p, n, "well hello sir", "hello brave adventurer",
                "how can i help you?");

        int option = showMenu(p, n, "what are you selling?",
                "what's that big old building behind us?");
        switch (option) {
        case 0:
            npcTalk(p, n, "why, i sell rune stones",
                    "i've got some good stuff, real powerful little rocks",
                    "take a look");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            break;

        case 1:
            npcTalk(p, n, "why that my friend...",
                    "...is the mage battle arena",
                    "top mages come from all over to compete in the arena",
                    "few return back, most get fried...hence the smell");
            npcTalk(p, n, "hmmm.. i did notice");
            break;

        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 793;
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
