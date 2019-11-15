package org.nemotech.rsc.plugins.npcs.wilderness.banditcamp;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class FatTony  implements ShopInterface,
        TalkToNpcListener, TalkToNpcExecutiveListener {

    private final int FAT_TONY = 235;
    private final Shop shop = new Shop("Pizza Base Shop", false, 5000, 100, 60,2, new InvItem(321, 30));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == FAT_TONY;
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
        npcTalk(p, n, "Go away I'm very busy");
        final int option = showMenu(p, n, new String[] {
                "Sorry to disturb you", "What are you busy doing?",
                "Have you anything to sell?" });
        if (option == 1) {
            npcTalk(p, n, "I'm cooking pizzas for the people in this camp",
                    "Not that these louts appreciate my gourmet cooking");
            final int sub_option = showMenu(p, n, new String[] {
                    "So what is a gourmet chef doing cooking for bandits?",
                    "Can I have some pizza too?", "OK I'll leave you to it" });
            if (sub_option == 0) {
                npcTalk(p,
                        n,
                        "Well I'm an outlaw",
                        "I was accused of giving the king food poisoning",
                        "The thought of it - I think he just drank too much wine that night",
                        "I had to flee the kingdom of Misthalin");
                final int remaining_option = showMenu(p, n,
                        new String[] { "Can I have some pizza too?",
                                "OK I'll leave you to it" });
                if (remaining_option == 0) {
                    wantsPizza(n, p);
                } else if (sub_option == 1) {
                    wantsPizza(n, p);
                }
            } else if (sub_option == 1) {
                wantsPizza(n, p);
            }
        } else if (option == 2) {
            npcTalk(p, n, "Well I guess I can sell you some half made pizzas");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }

    private void wantsPizza(final NPC n, final Player p) {
        npcTalk(p, n, "Well this pizza is really meant for the bandits");
        final int next_option = showMenu(p, n, new String[] { "Yes Okay",
                "Oh if I have to pay I don't want any" });
        if (next_option == 0) {
            npcTalk(p, n, "I guess I could sell you some pizza bases though");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        }
    }
}
