package org.nemotech.rsc.plugins.npcs.seers;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Stankers implements TalkToNpcExecutiveListener, TalkToNpcListener {
    
    public static int STANKERS = 389; 

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == STANKERS) {
            npcTalk(p,n, "Hello bold adventurer");
            int menu = showMenu(p,n,
                    "Are these your trucks?",
                    "Hello Mr Stankers");
            if(menu == 0) {
                npcTalk(p,n, "Yes, I use them to transport coal over the river",
                        "I will let other people use them too",
                        "I'm a nice person like that",
                        "Just put coal in a truck and I'll move it down to my depot over the river");
            } else if(menu == 1) {
                npcTalk(p,n, "Would you like a poison chalice?");
                int subMenu = showMenu(p,n,
                "Yes please",
                "what's a poison chalice?",
                "no thankyou");
                if(subMenu == 0) {
                    p.message("Stankers hands you a glass of strangely coloured liquid");
                    addItem(p, 737, 1);
                } else if(subMenu == 1) {
                    npcTalk(p,n, "It's an exciting drink I've invented",
                            "I don't know what it tastes like",
                            "I haven't tried it myself");
                }
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == STANKERS;
    }
}