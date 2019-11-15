package org.nemotech.rsc.plugins.npcs.rimmington;


import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class MasterCrafter implements TalkToNpcExecutiveListener, TalkToNpcListener {

    private final int MASTER_CRAFTER = 231;

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == MASTER_CRAFTER) {
            npcTalk(p, n, "Hello welcome to the Crafter's guild",
                    "Accomplished crafters from all over the land come here",
                    "All to use our top notch workshops");
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if(n.getID() == MASTER_CRAFTER) {
            return true;
        }
        return false;
    }
}
