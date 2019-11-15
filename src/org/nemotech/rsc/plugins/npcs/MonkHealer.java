package org.nemotech.rsc.plugins.npcs;

import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class MonkHealer implements TalkToNpcListener, TalkToNpcExecutiveListener {
    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        npcTalk(p, n, "Greetings traveller");
        int option = showMenu(p,n, "Can you heal me? I'm injured", "Greetings");
        if(option == 0) {
            npcTalk(p,n, "Ok");
            message(p, "The monk places his hands on your head", "You feel a little better");
            int newHp = p.getCurStat(3) + 10;
            if (newHp > p.getMaxStat(3)) {
                newHp = p.getMaxStat(3);
            }
            p.setCurStat(3, newHp);
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 93 || n.getID() == 174;
    }
}
