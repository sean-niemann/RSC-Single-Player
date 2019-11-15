package org.nemotech.rsc.plugins.quests.members.legendsquest.npcs.shop;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class SiegfriedErkel implements ShopInterface, TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Legends' Guild Shop", false, 60000, 150, 50, 2,
            new InvItem(796, 6), new InvItem(596, 5), new InvItem(52, 4),
            new InvItem(421, 3), new InvItem(1276, 1), new InvItem(1288, 3));

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        if(p.getQuestStage(Plugin.LEGENDS_QUEST) != -1) {
            npcTalk(p, n, "I'm sorry but the services of this shop are only for ",
                    "the pleasure of those who are rightfull members of the ",
                    "Legends Guild. I would get into serious trouble if I sold ",
                    "a non-member an item from this store.");
        } else {
            npcTalk(p, n, "Hello there and welcome to the shop of useful items.",
                    "Can I help you at all?");
            int option = showMenu(p, n, "Yes please. What are you selling?",
                    "No thanks");
            switch (option) {
            case 0:
                npcTalk(p, n, "Take a look");
                p.setAccessingShop(shop);
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
                break;
            case 1:
                npcTalk(p, n, "Ok, well, if you change your mind, do pop back.");
                break;
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 779;
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
