package org.nemotech.rsc.plugins.npcs.lostcity;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class FairyQueen implements TalkToNpcListener,
TalkToNpcExecutiveListener {

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if (n.getID() == 392) {
            return true;
        }
        return false;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if (n.getID() == 392) {
            int menu = showMenu(p, n, "How do crops and such survive down here?",
                    "What's so good about this place?");
            if(menu == 0) {
                playerTalk(p, n, "Surely they need a bit of sunlight?");
                npcTalk(p, n, "Clearly you come from a plane dependant on sunlight",
                        "Down here the plants grow in the aura of faerie");
            } else if(menu == 1) {
                npcTalk(p, n, "Zanaris is a meeting point of cultures",
                        "those from many worlds converge here to exchange knowledge and goods");
            }
        }
    }
}
