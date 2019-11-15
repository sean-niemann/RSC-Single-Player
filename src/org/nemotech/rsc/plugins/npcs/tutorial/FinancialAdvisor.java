package org.nemotech.rsc.plugins.npcs.tutorial;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class FinancialAdvisor implements TalkToNpcExecutiveListener, TalkToNpcListener {
    /**
     * @author Davve
     * Tutorial island financial advisor
     */

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(p.getCache().hasKey("tutorial") && p.getCache().getInt("tutorial") == 40) {
            npcTalk(p, n, "Please proceed through the next door");
            return;
        }
        npcTalk(p, n, "Hello there",
                "I'm your designated financial advisor");
        playerTalk(p, n, "That's good because I don't have any money at the moment",
                "How do I get rich?");
        npcTalk(p, n, "There are many different ways to make money in runescape",
                "for example certain monsters will drop a bit of loot",
                "To start with killing men and goblins might be a good idea",
                "Some higher level monsters will drop quite a lot of treasure",
                "several of runescapes skills are good money making skills",
                "two of these skills are mining and fishing",
                "there are instructors on the island who will help you with this",
                "using skills and combat to make money is a good plan",
                "because using a skill also slowly increases your level in that skill",
                "A high level in a skill opens up many more oppurtunites",
                "Some other ways of making money include taking quests and tasks",
                "You can find these by talking to certain game controlled characters",
                "Our quest advisors will tell you about this",
                "Sometimes you will find items lying around",
                "Selling these to the shops makes some money too",
                "Now continue through the next door");
        p.getCache().set("tutorial", 40);
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 480;
    }

}
