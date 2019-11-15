package org.nemotech.rsc.plugins.misc;

import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.ChatMessage;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.listeners.action.PlayerMageNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerMageNpcExecutiveListener;

public class SalarinTheTwistedMageAI extends Plugin implements PlayerMageNpcListener, PlayerMageNpcExecutiveListener {
    
    /*
     * Player maging Salarin the twisted AI - Just to degenerate ATTACK AND STRENGTH if over 2 in said skill.
     */

    @Override
    public boolean blockPlayerMageNpc(Player p, NPC n, Integer spell) {
        if(n.getID() == 567 && (p.getCurStat(ATTACK) > 2 || p.getCurStat(STRENGTH) > 2)) {
            return true;
        }
        return false;
    }

    @Override
    public void onPlayerMageNpc(Player p, NPC n, Integer spell) {
        if(n.getID() == 567 && (p.getCurStat(ATTACK) > 2 || p.getCurStat(STRENGTH) > 2)) {
            if(!p.withinRange(n, 5)) 
                return;
            p.informOfNPCMessage(new ChatMessage(n, "Amshalaraz Nithcosh dimarilo", p));
            sleep(600);
            p.message("You suddenly feel much weaker");
            p.setCurStat(ATTACK, 0);
            p.setCurStat(STRENGTH, 0);
        }
    }
}
