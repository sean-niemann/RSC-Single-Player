package org.nemotech.rsc.plugins.npcs.lumbridge;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import static org.nemotech.rsc.plugins.Plugin.DRAGON_SLAYER;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;
import org.nemotech.rsc.plugins.menu.Menu;
import org.nemotech.rsc.plugins.menu.Option;

public final class DukeOfLumbridge implements TalkToNpcExecutiveListener,
        TalkToNpcListener {

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        Menu defaultMenu = new Menu();
        npcTalk(p, n, "Greetings welcome to my castle");
        if (p.getQuestStage(DRAGON_SLAYER) >= 2
                || p.getQuestStage(DRAGON_SLAYER) < 0) {
            if (!hasItem(p, 420, 1)) {
                defaultMenu
                        .addOption(new Option(
                                "I seek a shield that will protect me from dragon's breath") {
                            public void action() {
                                npcTalk(p, n,
                                        "A knight going on a dragon quest hmm?");
                                npcTalk(p, n, "A most worthy cause");
                                npcTalk(p, n, "Guard this well my friend");
                                message(p, "The duke hands you a shield");
                                addItem(p, 420, 1);
                            }
                        });
            }
        }
        defaultMenu.addOptions(new Option("Have you any quests for me?") {
            public void action() {
                npcTalk(p, n, "All is well for me");
            }
        }, new Option("Where can I find money?") {
            public void action() {
                npcTalk(p, n,
                        "I've heard the blacksmiths are prosperous amoung the peasantry");
                npcTalk(p, n, "Maybe you could try your hand at that");
            }
        });
        defaultMenu.showMenu(p, n);
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 198;
    }

}
