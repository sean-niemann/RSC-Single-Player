package org.nemotech.rsc.plugins.npcs.catherby;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class HarrysFishingShack  implements ShopInterface,
        TalkToNpcListener, TalkToNpcExecutiveListener {

    private static final int HARRY = 250;
    
    private final Shop shop = new Shop("Harry's Fishing Shack", false, 3000, 100, 70,2, new InvItem(376, 5),
            new InvItem(377, 3), new InvItem(379, 2), new InvItem(375, 2),
            new InvItem(380, 200), new InvItem(548, 5), new InvItem(349, 0),
            new InvItem(354, 0), new InvItem(361, 0), new InvItem(552, 0),
            new InvItem(550, 0), new InvItem(351, 0), new InvItem(366, 0),
            new InvItem(372, 0), new InvItem(554, 0), new InvItem(369, 0),
            new InvItem(545, 0));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == HARRY;
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
        npcTalk(p, n, "Welcome you can buy fishing equipment at my store",
                "We'll also buy fish that you catch off you");
        final int option = showMenu(p, n, new String[] {
                "Let's see what you've got then", "Sorry, I'm not interested" });
        if (option == 0) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }

}
