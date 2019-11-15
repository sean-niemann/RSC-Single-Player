package org.nemotech.rsc.plugins.quests.members.undergroundpass.npcs;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.createGroundItem;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.PlayerKilledNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerKilledNpcExecutiveListener;

public class UndergroundPassIbanDisciple implements PlayerKilledNpcListener, PlayerKilledNpcExecutiveListener {

    @Override
    public boolean blockPlayerKilledNpc(Player p, NPC n) {
        if(n.getID() == 658) {
            return true;
        }
        return false;
    }

    @Override
    public void onPlayerKilledNpc(Player p, NPC n) {
        if(n.getID() == 658) {
            n.killedBy(p);
            if(p.getQuestStage(Plugin.UNDERGROUND_PASS) == 7 || p.getQuestStage(Plugin.UNDERGROUND_PASS) == -1) {
                message(p, "you search the diciples remains");
                if(!hasItem(p, 1031)) {
                    p.message("and find a staff of iban");
                    addItem(p, 1031, 1);
                } else {
                    p.message("but find nothing");
                }
            } else {
                createGroundItem(702, 1, p.getX(), p.getY(), p);
                createGroundItem(703, 1, p.getX(), p.getY(), p);
            }
        }
    }
}
