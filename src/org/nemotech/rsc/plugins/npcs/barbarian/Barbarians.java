package org.nemotech.rsc.plugins.npcs.barbarian;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;

import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Barbarians implements TalkToNpcListener, TalkToNpcExecutiveListener {

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 76 || n.getID() == 78;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        playerTalk(p, n, "Hello");
        int randomDiag = Util.random(0, 10);
        if(randomDiag == 0) {
            npcTalk(p, n, "Go away",
                    "This is our village");
        } else if(randomDiag == 1) {
            npcTalk(p, n, "Hello");
        } else if(randomDiag == 2) {
            npcTalk(p, n, "Wanna fight?");
            n.startCombat(p);
        } else if(randomDiag == 3) {
            npcTalk(p, n, "Who are you?");
            playerTalk(p, n, "I'm a bold adventurer");
            npcTalk(p, n, "You don't look very strong");
        } else if(randomDiag == 4) {
            p.message("The barbarian grunts");
        } else if(randomDiag == 5) {
            p.message("Ello");
        } else if(randomDiag == 6) {
            npcTalk(p, n, "ug");
        } else if(randomDiag == 7) {
            npcTalk(p, n, "I'm a little busy right now",
                    "We're getting ready for our next barbarian raid");
        } else if(randomDiag == 8) {
            npcTalk(p, n, "Beer?");
        } else if(randomDiag == 9) {
            p.message("The barbarian ignores you");
        } else if(randomDiag == 10) {
            p.message("Grr");
        }
    }
}
