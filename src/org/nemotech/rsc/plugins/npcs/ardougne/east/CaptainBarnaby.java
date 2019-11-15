package org.nemotech.rsc.plugins.npcs.ardougne.east;

import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class CaptainBarnaby implements TalkToNpcExecutiveListener,
        TalkToNpcListener {

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p, n, "Do you want to go on a trip to Karamja?",
                "The trip will cost you 30 gold");
        int karamja = showMenu(p, n, "Yes please", "No thankyou");
        if (karamja == 0) {
            if (p.getInventory().remove(10, 30) > -1) { // enough money
                message(p, "You pay 30 gold", "You board the ship");
                p.teleport(467, 651, false);
                sleep(1000);
                p.message("The ship arrives at Karamja");
            } else {
                playerTalk(p, n, "Oh dear I don't seem to have enough money");
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 316;
    }

}
