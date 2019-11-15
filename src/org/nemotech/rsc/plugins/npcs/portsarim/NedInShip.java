package org.nemotech.rsc.plugins.npcs.portsarim;

import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.showMenu;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import static org.nemotech.rsc.plugins.Plugin.DRAGON_SLAYER;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class NedInShip implements TalkToNpcExecutiveListener, TalkToNpcListener {

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        npcTalk(p, n, "Hello there lass");
        if (p.getQuestStage(DRAGON_SLAYER) == 3) {
            int menu = showMenu(p, n, "Can you take me back to Crandor again",
                    "How did you get back?");
            if (menu == 0) {
                if (p.getCache().hasKey("ship_fixed")) {
                    message(p, "You feel the ship begin to move",
                        "You are out at sea", "The ship is sailing",
                        "The ship is sailing", "You feel a crunch");
                    p.teleport(281, 3472, false);
                    p.getCache().remove("ship_fixed");
                    npcTalk(p, n, "Aha we've arrived");
                } else {
                    npcTalk(p, n, "Well I would, but the last adventure",
                            "Hasn't left this tub in the best of shapes",
                            "You'll have to fix it again");
                    playerTalk(p, n, "This ship isn't much use with that there");
                }
            } else if (menu == 1) {
                npcTalk(p, n, "I got towed back by a passing friendly whale");
            }
            return;
        }

        int opt = showMenu(p, n,
                "So are you going to take me to Crandor Island now then?",
                "So are you still up to sailing this ship?");
        if (opt == 0) {
            npcTalk(p, n, "Ok show me the map and we'll set sail now");
            if (hasItem(p, 415, 1)) {
                message(p, "You give the map to ned");
                playerTalk(p, n, "Here it is");
                removeItem(p, 415, 1);
                message(p, "You feel the ship begin to move",
                        "You are out at sea", "The ship is sailing",
                        "The ship is sailing", "You feel a crunch");
                p.teleport(281, 3472, false);
                p.getCache().remove("ship_fixed");
                npcTalk(p, n, "Aha we've arrived");
                p.updateQuestStage(DRAGON_SLAYER, 3);

            }
        } else if (opt == 1) {
            npcTalk(p, n, "Well I am a tad rusty",
                    "I'm sure it'll all come back to me, once I get into action",
                    "I hope...");
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 194;
    }
}
