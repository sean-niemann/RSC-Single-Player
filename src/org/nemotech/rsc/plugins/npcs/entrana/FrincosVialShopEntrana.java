package org.nemotech.rsc.plugins.npcs.entrana;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class FrincosVialShopEntrana implements ShopInterface,
TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Entrana Herblaw Shop", false, 3000, 100, 70,2,
            new InvItem(465, 50), new InvItem(468, 3), new InvItem(270, 50));

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p,n, "Hello how can I help you?");
        int menu = showMenu(p,n,
                "What are you selling?",
                "You can't, I'm beyond help",
                "I'm okay, thankyou");
        if(menu == 0) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 297;
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
