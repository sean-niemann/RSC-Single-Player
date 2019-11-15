package org.nemotech.rsc.plugins.npcs.hemenster;

import static org.nemotech.rsc.plugins.Plugin.*;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class MasterFisher implements TalkToNpcListener, TalkToNpcExecutiveListener {

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 368;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p, n, "Hello, welcome to the fishing guild.",
                    "Please feel free to make use of any of our facilities");
    }
}
