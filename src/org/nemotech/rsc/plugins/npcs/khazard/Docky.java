package org.nemotech.rsc.plugins.npcs.khazard;

import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Docky implements TalkToNpcExecutiveListener, TalkToNpcListener {

    public static final int DOCKY = 390;

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == DOCKY) {
            playerTalk(p, n, "hello there");
            npcTalk(p, n, "ah hoy there, wanting",
                    "to travel on the beatiful",
                    "lady valentine are we");
            int menu = showMenu(p, n, "not really, just looking around", "where are you travelling to");
            if(menu == 0) {
                npcTalk(p, n, "o.k land lover");
            } else if(menu == 1) {
                npcTalk(p, n, "we sail direct to Birmhaven port",
                        "it really is a speedy crossing",
                        "so would you like to come",
                        "it cost's 30 gold coin's");
                int travel = showMenu(p, n, "no thankyou", "ok");
                if(travel == 1) {
                    if(hasItem(p, 10, 30)) {
                        message(p, 1900, "You pay 30 gold");
                        removeItem(p, 10, 30);
                        message(p, 3000, "You board the ship");
                        p.teleport(467, 647);
                        sleep(2000);
                        p.message("The ship arrives at Port Birmhaven");
                    } else {
                        playerTalk(p,n, "Oh dear I don't seem to have enough money");
                    }
                }
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == DOCKY;
    }
}
