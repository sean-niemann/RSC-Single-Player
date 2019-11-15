package org.nemotech.rsc.plugins.npcs.yanille;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class WizardFrumscone implements TalkToNpcListener, TalkToNpcExecutiveListener {
    
    public static int WIZARD_FRUMSCONE = 515;

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == WIZARD_FRUMSCONE;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == WIZARD_FRUMSCONE) {
            npcTalk(p,n, "Do you like my magic zombies",
                    "Feel free to kill them",
                    "Theres plenty more where these came from");
        }
    }
}
