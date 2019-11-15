package org.nemotech.rsc.plugins.npcs.varrock;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class ChampionsGuild  implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop scavvosShop = new Shop("Scavvo's Rune Store", false, 300000, 100, 60, 2,
            new InvItem(406, 1), new InvItem(402, 1), new InvItem(98, 1),
            new InvItem(400, 1), new InvItem(75, 1), new InvItem(397, 1));
    
    private final Shop valsShop = new Shop("Valaine's Shop of Champions", false, 60000, 130, 40, 3, new InvItem(
            229, 2), new InvItem(230, 1), new InvItem(248, 1), new InvItem(120,
            1));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == 183 || n.getID() == 112;
    }

    @Override
    public Shop[] getShops() {
        return new Shop[] { scavvosShop, valsShop };
    }

    @Override
    public boolean isMembers() {
        return false;
    }

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        switch (n.getID()) {
        case 183:
            npcTalk(p, n, "ello matey", "Want to buy some exciting new toys?");
            int options = showMenu(p,n, "No, toys are for kids", "Lets have a look then", "Ooh goody goody toys");
            if(options == 1 || options == 2) {
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(scavvosShop);
            }
            break;
        case 112: // valaerie
            npcTalk(p, n, "Hello there",
                    "Want to have a look at what we're selling today?");

            int opt = showMenu(p,n, "Yes please", "No thank you");
            if(opt == 0) {
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(valsShop);
            }
            break;
        }
    }

}
