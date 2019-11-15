package org.nemotech.rsc.plugins.npcs.tutorial;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class ControlsGuide implements TalkToNpcExecutiveListener, TalkToNpcListener {
    /**
     * @author Davve
     * Tutorial island second room guide
     */
    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(p.getCache().hasKey("tutorial") && p.getCache().getInt("tutorial") == 15) {
            npcTalk(p, n, "Please proceed through the next door");
            return;
        }
        npcTalk(p, n, "Hello I'm here to tell you more about the game's controls",
                "Most of your options and character information",
                "can be accessed by the menus in the top right corner of the screen",
                "moving your mouse over the map icon",
                "which is the second icon from the right",
                "gives you a view of the area you are in",
                "clicking on this map is an effective way of walking around",
                "though if the route is blocked, for example by a closed door",
                "then your character won't move",
                "Also notice the compass on the map which may be of help to you");
        playerTalk(p, n, "Thank you for your help");
        npcTalk(p, n, "Now carry on to speak to the combat instructor");
        p.getCache().set("tutorial", 15);
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 499;
    }

}
