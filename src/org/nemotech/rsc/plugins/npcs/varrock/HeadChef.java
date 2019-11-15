package org.nemotech.rsc.plugins.npcs.varrock;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class HeadChef implements TalkToNpcExecutiveListener, TalkToNpcListener {

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p, n, "Hello welcome to the chef's guild",
                "Only accomplished chefs and cooks are allowed in here",
                "Feel free to use any of our facilities");
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 133;
    }

}
