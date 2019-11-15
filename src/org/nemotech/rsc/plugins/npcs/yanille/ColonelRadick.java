package org.nemotech.rsc.plugins.npcs.yanille;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class ColonelRadick implements TalkToNpcListener, TalkToNpcExecutiveListener {

    public static int COLONEL_RADICK = 518;

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == COLONEL_RADICK;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == COLONEL_RADICK) {
            npcTalk(p,n, "Who goes there?",
                    "friend or foe?");
            int menu = showMenu(p,n,
                    "Friend",
                    "foe",
                    "Why's this town so heavily defended?");
            if(menu == 0) {
                npcTalk(p,n, "Ok good to hear it");
            } else if(menu == 1) {
                npcTalk(p,n, "Oh righty");
                n.startCombat(p);
            } else if(menu == 2) {
                npcTalk(p,n, "Yanille is on the southwest border of Kandarin",
                        "Beyond here you go into the feldip hills",
                        "Which is major ogre teritory",
                        "Our job is to defend Yanille from the ogres");
            }
        }
    }
}