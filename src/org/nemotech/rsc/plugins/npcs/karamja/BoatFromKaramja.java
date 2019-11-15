package org.nemotech.rsc.plugins.npcs.karamja;

import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.movePlayer;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class BoatFromKaramja implements TalkToNpcExecutiveListener,
        TalkToNpcListener {

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        int option = showMenu(p, n, "Can I board this ship?",
                "Does Karamja have any unusal customs then?");
        if (option == 0) {
            npcTalk(p, n, "You need to be searched before you can board");
            int sub_opt = showMenu(p, n, "Why?",
                    "You're not putting your hands on my things!",
                    "Search away I have nothing to hide");
            if (sub_opt == 0) {
                npcTalk(p, n,
                        "Because asgarnia has banned the import of intoxicating spirits");
            } else if (sub_opt == 1) {
                npcTalk(p, n, "You're not getting on this ship then");
            } else if (sub_opt == 2) {
                if (hasItem(p, 318, 1)) {
                    message(p, "The customs officer confiscates your rum");
                    removeItem(p, 318, 1);
                } else {
                    npcTalk(p,
                            n,
                            "Well you've got some odd stuff, but it's all legal",
                            "Now you need to pay a boarding charge of 30 gold");
                    if (showMenu(p, n, "Ok", "Oh, I'll not bother then") == 0) {
                        if (removeItem(p, 10, 30)) {
                            message(p, "You pay 30 gold", "You board the ship");
                            movePlayer(p, 269, 648, true);
                            p.message("You arrive at Port Sarim");
                        } else { // not enough money
                            playerTalk(p, n,
                                    "Oh dear it seems i don't have enough money");
                        }
                    }
                }
            }
        } else if (option == 1) {
            npcTalk(p, n, "I'm not that sort of customs officer");
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 163;
    }
}
