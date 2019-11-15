package org.nemotech.rsc.plugins.npcs.barbarian;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import static org.nemotech.rsc.plugins.Plugin.DRAGON_SLAYER;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;
import org.nemotech.rsc.plugins.menu.Menu;
import org.nemotech.rsc.plugins.menu.Option;

public class Oracle implements TalkToNpcExecutiveListener, TalkToNpcListener {

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        Menu defaultMenu = new Menu();
        if (p.getQuestStage(DRAGON_SLAYER) == 2) {
            defaultMenu.addOption(new Option("I seek a piece of the map of the isle of Crandor") {
                @Override
                public void action() {
                    npcTalk(p, n, "The map's behind a door below",
                            "But entering is rather tough",
                            "And this is what you need to know",
                            "You must hold the following stuff",
                            "First a drink used by the mage",
                            "Next some worm string, changed to sheet",
                            "Then a small crustacean cage",
                            "Last a bowl that's not seen heat");
                }
            });
        }
        defaultMenu.addOption(new Option("Can you impart your wise knowledge to me oh oracle") {
            @Override
            public void action() {
                npcTalk(p, n, "You must search from within to find your true destiny");
            }
        });
        defaultMenu.showMenu(p, n);
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getDef().getName().equals("Oracle");
    }
}
