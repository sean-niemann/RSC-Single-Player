package org.nemotech.rsc.plugins.npcs.rimmington;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class CraftingEquipmentShops implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    public static final int ROMMIK = 156;
    public static final int DOMMIK = 173;

    private final Shop shop = new Shop("Crafting Store", false, 5000, 100, 65, 2,
            new InvItem(167, 2), new InvItem(293, 4), new InvItem(295, 2),
            new InvItem(294, 2), new InvItem(39, 3), new InvItem(43, 100),
            new InvItem(386, 3));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == ROMMIK || n.getID() == DOMMIK;
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
        npcTalk(p, n, "Would you like to buy some crafting equipment");

        int option = showMenu(p, n, "No I've got all the crafting equipment I need", "Let's see what you've got then");
        if (option == 0) {
            npcTalk(p, n, "Ok fair well on your travels");
        } else if(option == 1) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }

}