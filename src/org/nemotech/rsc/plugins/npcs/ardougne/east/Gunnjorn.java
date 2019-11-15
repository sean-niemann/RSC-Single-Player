package org.nemotech.rsc.plugins.npcs.ardougne.east;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Gunnjorn implements TalkToNpcListener, TalkToNpcExecutiveListener {

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if (n.getID() == 588) {
            return true;
        }
        return false;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if (n.getID() == 588) {
            npcTalk(p, n, "Ahoy there!");
            int menu = showMenu(p, n, "What is this place?");
            if(menu == 0) {
                npcTalk(p, n, "Haha welcome to my obstacle course",
                        "Have fun, but remember this isn't a child's playground",
                        "People have died here", "The best way to train",
                        "Is to go round the course in a clockwise direction");
            }
        }
    }
}
