package org.nemotech.rsc.plugins.npcs.yanille;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class SigbertTheAdventurer implements TalkToNpcListener, TalkToNpcExecutiveListener {

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 573;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == 573) {
            npcTalk(p,n, "I'd be very careful going up there friend");
            int menu = showMenu(p,n,
            "Why what's up there?",
            "Fear not I am very strong");
            if(menu == 0) {
                npcTalk(p,n, "Salarin the twisted",
                        "One of Kanadarin's most dangerous chaos druids",
                        "I tried to take him on and then suddenly felt immensly week",
                        "I here he's susceptable to attacks from the mind",
                        "However I have no idea what that means",
                        "So it's not much help to me");
            } else if(menu == 1) {
                npcTalk(p,n, "You might find you are not so strong shortly");
            }
        }
    }
}
