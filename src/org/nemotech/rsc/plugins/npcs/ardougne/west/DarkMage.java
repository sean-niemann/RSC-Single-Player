package org.nemotech.rsc.plugins.npcs.ardougne.west;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class DarkMage implements TalkToNpcExecutiveListener, TalkToNpcListener {
    public static int DARK_MAGE = 667;
    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == DARK_MAGE) {
            playerTalk(p,n, "hello there");
            npcTalk(p,n, "why do do you interupt me traveller?");
            playerTalk(p,n, "i just wondered what you're doing?");
            npcTalk(p,n, "i experiment with dark magic",
                    "it's a dangerous craft");
            if(hasItem(p, 1031) && p.getQuestStage(Plugin.UNDERGROUND_PASS) == -1) {
                playerTalk(p,n, "could you fix this staff?");
                p.message("you show the mage your staff of iban");
                npcTalk(p,n, "almighty zamorak! the staff of iban!");
                playerTalk(p,n, "can you fix it?");
                npcTalk(p,n, "this truly is dangerous magic traveller",
                        "i can fix it, but it will cost you",
                        "the process could kill me");
                playerTalk(p,n, "how much?");
                npcTalk(p,n, "200,000 gold pieces, not a penny less");
                int menu = showMenu(p,n,
                        "no chance, that's ridiculous",
                        "ok then");
                if(menu == 0) {
                    npcTalk(p,n, "fine by me");
                } else if(menu == 1) {
                    if(!hasItem(p, 10, 200000)) {
                        p.message("you don't have enough money");
                        playerTalk(p,n, "oops, i'm a bit short");
                    } else {
                        message(p, "you give the mage 200,000 coins",
                                "and the staff of iban");
                        removeItem(p, 10, 200000);
                        removeItem(p, 1031, 1);
                        p.message("the mage fixes the staff and returns it to you");
                        addItem(p, 1000, 1);
                        playerTalk(p,n, "thanks mage");
                        npcTalk(p,n, "you be carefull with that thing");
                    }
                }
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == DARK_MAGE;
    }

}
