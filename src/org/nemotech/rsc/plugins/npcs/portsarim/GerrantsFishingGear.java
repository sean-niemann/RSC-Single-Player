package org.nemotech.rsc.plugins.npcs.portsarim;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
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

public final class GerrantsFishingGear implements
        ShopInterface, TalkToNpcExecutiveListener, TalkToNpcListener {

    private final Shop shop = new Shop("Gerrant's Fishy Business", false, 12000, 100, 70, 3, new InvItem(376,
            5), new InvItem(377, 5), new InvItem(378, 5), new InvItem(379, 2),
            new InvItem(375, 2), new InvItem(380, 200), new InvItem(381, 200),
            new InvItem(349, 30), new InvItem(354, 0), new InvItem(361, 0),
            new InvItem(351, 0), new InvItem(358, 0), new InvItem(363, 0),
            new InvItem(356, 0), new InvItem(366, 0), new InvItem(372, 0),
            new InvItem(369, 0));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == 167;
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
        npcTalk(p, n, "Welcome you can buy any fishing equipment at my store",
                "We'll also buy anything you catch off you");

        String[] options;
        if (p.getQuestStage(Plugin.HEROS_QUEST) >= 1) {
            options = new String[] { "Let's see what you've got then",
                    "Sorry, I'm not interested",
                    "I want to find out how to catch a lava eel" };
        } else {
            options = new String[] { "Let's see what you've got then",
                    "Sorry, I'm not interested" };
        }
        int option = showMenu(p, n, options);
        if (option == 0) {
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        } else if (option == 2) {
            npcTalk(p,
                    n,
                    "Lava eels eh?",
                    "That's a tricky one that is",
                    "I wouldn't even know where find them myself",
                    "Probably in some lava somewhere",
                    "You'll also need a lava proof fishing line",
                    "The method for this would be take an ordinary fishing rod",
                    "And cover it with fire proof blamish oil");
            if (!hasItem(p, 587)) {
                npcTalk(p, n, "Now I may have a jar of Blamish snail slime",
                        "I wonder where I put it");
                p.message("Gerrant searches about a bit");
                npcTalk(p, n, "Aha here it is");
                p.message("Gerrant passes you a small jar");
                addItem(p, 587, 1);
                npcTalk(p, n,
                        "You'll need to mix this with some of the Harralander herb and water");
            }
        }
    }

}
