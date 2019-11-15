package org.nemotech.rsc.plugins.quests.members.undergroundpass.npcs;

import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.PlayerKilledNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerKilledNpcExecutiveListener;

public class UndergroundPassKalrag implements PlayerKilledNpcListener, PlayerKilledNpcExecutiveListener {

    public static int KALRAG = 641;
    @Override
    public boolean blockPlayerKilledNpc(Player p, NPC n) {
        if(n.getID() == KALRAG) {
            return true;
        }
        return false;
    }

    @Override
    public void onPlayerKilledNpc(Player p, NPC n) {
        if(n.getID() == KALRAG) {
            n.killedBy(p);
            message(p, "kalrag slumps to the floor",
                    "poison flows from the corpse over the soil");
            if(!p.getCache().hasKey("poison_on_doll") && p.getQuestStage(Plugin.UNDERGROUND_PASS) == 6) {
                if(hasItem(p, 1004)) {
                    message(p, "you smear the doll of iban in the poisoned blood");
                    p.message("it smells horrific");
                    p.getCache().store("poison_on_doll", true);
                } else {
                    message(p, "it quikly seeps away into the earth");
                    p.message("you dare not collect any without ibans doll");
                }
            }
        }
    }
}
