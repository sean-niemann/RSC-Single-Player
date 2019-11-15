package org.nemotech.rsc.plugins.npcs.brimhaven;

import static org.nemotech.rsc.plugins.Plugin.message;
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

public class AlfonseTheWaiter implements ShopInterface, TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("The Shrimp and Parrot", false, 10000, 110, 75, 2,
            new InvItem(362, 5), new InvItem(551, 5), new InvItem(367, 5), new InvItem(373, 3), new InvItem(370, 2));

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == 260) {
            npcTalk(p,n, "Welcome to the shrimp and parrot",
                    "Would you like to order sir?");
            int menu;
            if(p.getQuestStage(Plugin.HEROS_QUEST) != 1 && p.getQuestStage(Plugin.HEROS_QUEST) != 2 && !p.getCache().hasKey("pheonix_mission") && !p.getCache().hasKey("pheonix_alf")) {
                menu = showMenu(p,n,
                        "Yes please",
                        "No thankyou");
            } else {
                menu = showMenu(p,n,
                        "Yes please",
                        "No thankyou",
                        "Do you sell Gherkins?");
            }
            if(menu == 0) {
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            } else if(menu == 2) {
                npcTalk(p,n, "Hmm ask Charlie the cook round the back",
                        "He may have some Gherkins for you");
                message(p, "Alfonse winks");
                p.getCache().store("talked_alf", true);
                p.getCache().remove("pheonix_alf");
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if(n.getID() == 260) {
            return true;
        }
        return false;
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
