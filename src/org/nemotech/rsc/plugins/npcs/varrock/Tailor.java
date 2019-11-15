package org.nemotech.rsc.plugins.npcs.varrock;

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

public final class Tailor  implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Fancy Clothes Store", false, 30000, 130, 40,2, new InvItem(192,
            0), new InvItem(185, 3), new InvItem(512, 1), new InvItem(541, 3),
            new InvItem(146, 3), new InvItem(39, 3), new InvItem(43, 100),
            new InvItem(16, 10), new InvItem(17, 10), new InvItem(807, 3),
            new InvItem(808, 3), new InvItem(191, 1), new InvItem(194, 5),
            new InvItem(195, 3), new InvItem(187, 2), new InvItem(183, 4),
            new InvItem(609, 3));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == 501;
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
        npcTalk(p, n, "Now you look like someone who goes to a lot of fancy dress parties");
        playerTalk(p, n,"Errr... what are you saying exactly?");
        npcTalk(p, n, "I'm just saying that perhaps you would like to peruse my selection of garments");
        int opt = showMenu(p,n, "I think I might leave the perusing for now thanks",
                "OK,lets see what you've got then" );
        if(opt == 1) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }

}
