package org.nemotech.rsc.plugins.quests.members.undergroundpass.npcs;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.inArray;
import static org.nemotech.rsc.plugins.Plugin.message;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.PlayerKilledNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerKilledNpcExecutiveListener;

public class UndergroundPassDemons implements PlayerKilledNpcListener, PlayerKilledNpcExecutiveListener {

    public static int[] DEMONS = { 645, 646, 647 };
    public static int AMULET_OF_OTHAINIAN = 1009;
    public static int AMULET_OF_DOOMION = 1010;
    public static int AMULET_OF_HOLTHION = 1011;

    @Override
    public boolean blockPlayerKilledNpc(Player p, NPC n) {
        if(inArray(n.getID(), DEMONS)) {    
            return true;
        }
        return false;
    }

    @Override
    public void onPlayerKilledNpc(Player p, NPC n) {
        if(inArray(n.getID(), DEMONS)) {
            n.killedBy(p);
            if(!p.getCache().hasKey("doll_of_iban") && p.getQuestStage(Plugin.UNDERGROUND_PASS) != 6) {
                p.message("the demon slumps to the floor");
                teleportPlayer(p, n);
            } else {
                teleportPlayer(p, n);
                message(p, "the demon slumps to the floor");
                if(!hasItem(p, n.getID() + 364)) {
                    p.message("around it's neck you find a strange looking amulet");
                    addItem(p, n.getID() + 364 , 1); // will give correct ammys for all.
                }
            }
        }
    }
    private void teleportPlayer(Player p, NPC n) {
        if(n.getID() == DEMONS[0]) {
            p.teleport(796, 3541);
        } else if(n.getID() == DEMONS[1]) {
            p.teleport(807, 3541);
        } else if(n.getID() == DEMONS[2]) {
            p.teleport(807, 3528);
        }
    }
}
