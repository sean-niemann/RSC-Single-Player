package org.nemotech.rsc.plugins.npcs.lostcity;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class FairyLunderwin implements TalkToNpcListener,
TalkToNpcExecutiveListener {

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if (n.getID() == 219) {
            return true;
        }
        return false;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if (n.getID() == 219) {
            npcTalk(p, n, "I am buying cabbage, we have no such thing where I come from",
                    "I pay hansomly for this wounderous object",
                    "Would 100 gold coins per cabbage be a fair price?");
            if(hasItem(p, 18)) {
                int menu = showMenu(p, n, "Yes, I will sell you all my cabbages",
                        "No, I will keep my cabbbages");
                if(menu == 0) {
                    p.message("You sell a cabbage");
                    removeItem(p, 18, 1);
                    addItem(p, 10, 100);
                    npcTalk(p, n, "Good doing buisness with you");
                }
            } else {
                playerTalk(p, n, "Alas I have no cabbages either");
            }
        }
    }
}
