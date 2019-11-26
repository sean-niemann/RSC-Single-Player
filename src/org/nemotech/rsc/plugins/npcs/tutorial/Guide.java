package org.nemotech.rsc.plugins.npcs.tutorial;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Guide implements TalkToNpcExecutiveListener, TalkToNpcListener {
    /**
     * @author Davve
     * Tutorial island guide first room
     */
    @Override
    public void onTalkToNpc(Player p, NPC n) {
            if(p.getCache().hasKey("tutorial") && p.getCache().getInt("tutorial") == 10) {
                npcTalk(p, n, "Please proceed through the next door");
                return;
            }
            npcTalk(p, n, "Welcome to the world of runescape",
                    "My job is to help newcomers find their feet here");
            playerTalk(p, n, "Ah good, let's get started");
            npcTalk(p, n, "When speaking to characters such as myself",
                    "Sometimes options will appear in the top left corner of the screen",
                    "left click on one of them to continue the conversation");
            int menu = showMenu(p, n, "I would like to skip the tutorial", "So what else can you tell me?", "What other controls do I have?");
            if(menu == 0) {
                npcTalk(p, n, "Okay I shall teleport you to lumbridge");
                p.teleport(122, 647, true);
                p.message("You have skipped the tutorial");
            } else if(menu == 1) {
                npcTalk(p, n, "I suggest you go through the door now",
                        "There are several guides and advisors on the island",
                        "Speak to them",
                        "They will teach you about the various aspects of the game");
                p.getSender().sendAlert("Use the quest history tab at the bottom of the screen to reread things said to you by ingame characters", false);
                p.getCache().set("tutorial", 10);
            } else if(menu == 2) {
                npcTalk(p, n, "I suggest you talk to the controls guide through the door");
            }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 476;
    }

}
