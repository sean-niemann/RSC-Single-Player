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

public final class AuburysRunes implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Aubury's Rune Shop", false, 3000, 100, 70, 2, new InvItem(31,
            50), new InvItem(32, 50), new InvItem(33, 50), new InvItem(34,
            50), new InvItem(35, 50), new InvItem(36, 50));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == 54;
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
            npcTalk(p, n, "Do you want to buy some runes?");
            int opt = showMenu(p, n, "Yes please",
                    "Oh it's a rune shop. No thank you, then");
            if (opt == 0) {
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            }
            if (opt == 1) {
                npcTalk(p, n,
                        "Well if you do find someone who does want runes,",
                        "send them my way");
            }
    }

}
