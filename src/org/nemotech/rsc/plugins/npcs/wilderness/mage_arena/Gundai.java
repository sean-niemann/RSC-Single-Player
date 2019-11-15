package org.nemotech.rsc.plugins.npcs.wilderness.mage_arena;

import static org.nemotech.rsc.plugins.Plugin.*;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;
import org.nemotech.rsc.plugins.menu.Menu;
import org.nemotech.rsc.plugins.menu.Option;

public class Gundai implements TalkToNpcExecutiveListener, TalkToNpcListener {
    
    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        playerTalk(p,n, "hello, what are you doing out here?");
        npcTalk(p,n, "why i'm a banker, the only one around these dangerous parts");
        Menu defaultMenu = new Menu();
        defaultMenu.addOption(new Option("cool, I'd like to access my bank account please") {
            @Override
            public void action() {
                npcTalk(p, n, "no problem");
                p.getSender().showBank();
            }
        });
        defaultMenu.addOption(new Option("Well, now i know") {
            @Override
            public void action() {
                npcTalk(p,n, "knowledge is power my friend");
            }
        });
        defaultMenu.showMenu(p, n);
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 792;
    }

}