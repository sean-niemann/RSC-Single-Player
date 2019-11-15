package org.nemotech.rsc.plugins.npcs.dwarvenmine;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import static org.nemotech.rsc.plugins.Plugin.FAMILY_CREST;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;
import org.nemotech.rsc.plugins.menu.Menu;
import org.nemotech.rsc.plugins.menu.Option;

public class Boot implements TalkToNpcExecutiveListener, TalkToNpcListener {

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        npcTalk(p,n, "Hello tall person");
        Menu defaultMenu = new Menu();
        if (p.getQuestStage(FAMILY_CREST) == 5) {
            defaultMenu.addOption(new Option("Hello I'm in search of very high quality gold") {
                @Override
                public void action() {
                    npcTalk(p,n, "Hmm well the best gold I know of",
                            "is east of the great city of Ardougne",
                            "In some certain rocks underground there",
                            "Its not the easiest of rocks to get to though I've heard");
                    p.updateQuestStage(FAMILY_CREST, 6);
                    // THEY MUST TALK TO THIS DWARF AND GET STAGE 6 OTHERWISE THEY WON'T BE ABLE TO MINE THE GOLD IN THE DUNGEON.
                }
            });
        }
        defaultMenu.addOption(new Option("Hello short person") {
            @Override
            public void action() {
                //NOTHING
            }
        });
        defaultMenu.addOption(new Option("Why are you called boot?") {
            @Override
            public void action() {
                npcTalk(p,n, "Because when I was a very young dwarf",
                        "I used to sleep in a large boot");
            }
        });
        defaultMenu.showMenu(p, n);
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 313;
    }

}
