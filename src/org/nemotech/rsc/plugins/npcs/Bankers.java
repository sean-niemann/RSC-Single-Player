package org.nemotech.rsc.plugins.npcs;

import static org.nemotech.rsc.plugins.Plugin.*;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Bankers implements TalkToNpcExecutiveListener, TalkToNpcListener {

    public static int[] BANKERS = { 95, 224, 268, 540, 617 };

    @Override
    public boolean blockTalkToNpc(final Player player, final NPC npc) {
        if(inArray(npc.getID(), BANKERS)) {
            return true;
        }
        return false;
    }

    @Override
    public void onTalkToNpc(Player player, final NPC npc) {
        npcTalk(player, npc, "Good day"+(npc.getID() == 617 ? " Bwana" : "")+", how may I help you?");
        int menu = showMenu(player, npc, 
                "I'd like to access my bank account please", 
                "What is this place?");
        if (menu == 0) {
            npcTalk(player, npc, "Certainly " + (player.isMale() ? "Sir" : "Miss"));
            player.getSender().showBank();
        } else if (menu == 1) {
            npcTalk(player, npc, "This is a branch of the bank of Runescape", "We have branches in many towns");
            int branchMenu = showMenu(player, npc, "And what do you do?",
                    "Didn't you used to be called the bank of Varrock");
            if (branchMenu == 0) {
                npcTalk(player, npc, "We will look after your items and money for you",
                        "So leave your valuables with us if you want to keep them safe");
            } else if (branchMenu == 1) {
                npcTalk(player, npc, "Yes we did, but people kept on coming into our branches outside of varrock",
                        "And telling us our signs were wrong",
                        "As if we didn't know what town we were in or something!");
            }
        }
    }
    
}