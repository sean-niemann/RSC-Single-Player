package org.nemotech.rsc.plugins.npcs.ardougne.east;

import static org.nemotech.rsc.plugins.Plugin.*;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class BartenderFlyingHorseInn implements TalkToNpcListener, TalkToNpcExecutiveListener {

    public final int BARTENDER = 340;

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if(n.getID() == BARTENDER) {
            return true;
        }
        return false;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == BARTENDER) {
            npcTalk(p, n, "Would you like to buy a drink?");
            playerTalk(p, n, "What do you serve?");
            npcTalk(p, n, "Beer");
            int menu = showMenu(p, n,
                    "I'll have a beer then",
                    "I'll not have anything then");
            if(menu == 0) {
                npcTalk(p, n, "Ok, that'll be two coins");
                if(hasItem(p, 10, 2)) {
                    removeItem(p, 10, 2);
                    addItem(p, 193, 1);
                    p.message("You buy a pint of beer");
                } else {
                    playerTalk(p, n, "Oh dear. I don't seem to have enough money");
                }
            }
        }
    }
}
