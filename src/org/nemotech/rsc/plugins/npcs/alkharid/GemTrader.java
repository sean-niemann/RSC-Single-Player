package org.nemotech.rsc.plugins.npcs.alkharid;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;

import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import static org.nemotech.rsc.plugins.Plugin.FAMILY_CREST;
import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class GemTrader implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener {

    public static final int npcid = 308;

    private final Shop shop = new Shop("Gem Trader", false, 60000 * 10, 100, 70, 3, new InvItem(160,
            1), new InvItem(159, 1), new InvItem(158, 0), new InvItem(157, 0),
            new InvItem(164, 1), new InvItem(163, 1), new InvItem(162, 0),
            new InvItem(161, 0));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == npcid;
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
        if (n.getID() == npcid) {
            npcTalk(p, n, "good day to you " + ((p.isMale()) ? "sir"
                    : "madam"), "Would you be interested in buying some gems?");

            final String[] options;
            if (p.getQuestStage(FAMILY_CREST) <= 2
                    || p.getQuestStage(FAMILY_CREST) >= 5) {
                options = new String[] { "Yes please", "No thanks" };
            } else {
                options = new String[] { "Yes please", "No thanks",
                        "I'm in search of a man named adam fitzharmon" };
            }
            int option = showMenu(p, n, options);

            if (option == 0) {
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            } else if (option == 2) {
                npcTalk(p,
                        n,
                        "Fitzharmon eh?",
                        "Thats the name of a Varrocian noble family if I'm not mistaken",
                        "I have seen a man of that persuasion about the place as of late",
                        "Wearing a poncey yellow cape",
                        "Came to my store, said he was after jewelry made from the perfect gold",
                        "Whatever that means",
                        "He's round about the desert still, looking for the perfect gold",
                        "He'll be somewhere where he might get some gold I'd wager",
                        "He might even be desperate enough to brave the scorpions");
                p.updateQuestStage(FAMILY_CREST, 4);
            }
        }
    }

}