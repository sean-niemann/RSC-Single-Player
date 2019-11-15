package org.nemotech.rsc.plugins.npcs.edgeville;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;

import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import static org.nemotech.rsc.plugins.Plugin.DRAGON_SLAYER;
import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class OziachsRunePlateShop  implements ShopInterface,
TalkToNpcListener, TalkToNpcExecutiveListener {

    private final Shop shop = new Shop("Oziach's Body Armour Shop", false, 30000, 100, 60, 2, new InvItem(401, 2));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == 187 && p.getQuestStage(DRAGON_SLAYER) == -1;
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
        playerTalk(p, n, "I have slain the dragon");
        npcTalk(p, n, "Well done");
        final int option = showMenu(p, n, new String[] {
                "Can I buy a rune plate mail body now please?", "Thank you" });
        if (option == 0) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        } 
    }
}
