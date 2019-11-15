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

public class JatixHerblawShop  implements ShopInterface,
        TalkToNpcListener, TalkToNpcExecutiveListener {

    private final int JATIX = 230;
    private final Shop shop = new Shop("Jatix's Herblaw Shop", false, 10000, 100, 70, 2,
            new InvItem(465, 50), new InvItem(468, 3), new InvItem(270, 50));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == JATIX;
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
        npcTalk(p, n, "Hello how can I help you?");
        final int option = showMenu(p, n, new String[] {
                "What are you selling?", "You can't, I'm beyond help",
                "I'm okay, thankyou" });

        if (option == 0) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }

    }

}
