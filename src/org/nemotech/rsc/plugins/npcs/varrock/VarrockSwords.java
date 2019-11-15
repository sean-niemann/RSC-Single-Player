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

public final class VarrockSwords implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Varrock Swords", false, 30000, 100, 60, 2,
            new InvItem(66, 5), new InvItem(1, 4), new InvItem(67, 4),
            new InvItem(424, 3), new InvItem(68, 3), new InvItem(69, 2),
            new InvItem(70, 4), new InvItem(71, 3), new InvItem(72, 3),
            new InvItem(425, 2), new InvItem(73, 2), new InvItem(74, 1),
            new InvItem(62, 10), new InvItem(28, 6), new InvItem(63, 5),
            new InvItem(423, 4), new InvItem(64, 3), new InvItem(65, 2));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        if (n.getID() == 130 || n.getID() == 56) {
            if (p.getX() >= 133 && p.getX() <= 138 && p.getY() >= 522
                    && p.getY() <= 527) {
                return true;
            }
        }
        return false;
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
        if (n.getID() == 130 || n.getID() == 56
                && p.getLocation().inBounds(133, 522, 138, 527)) {
            npcTalk(p, n, "Hello bold adventurer",
                    "Can I interest you in some swords?");

            final String[] options = new String[] { "Yes please",
                    "No, I'm OK for swords right now" };
            int option = showMenu(p,n, options);
            switch (option) {
            case 0:
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
                break;
            case 1:
                npcTalk(p, n, "Come back if you need any");
                break;
            }
        }
    }

}
