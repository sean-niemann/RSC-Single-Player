package org.nemotech.rsc.plugins.npcs.yanille;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;
import org.nemotech.rsc.plugins.menu.Menu;
import org.nemotech.rsc.plugins.menu.Option;

public final class BartenderDragonInn implements TalkToNpcExecutiveListener,
        TalkToNpcListener {

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        if (n.getID() == 529) {
            npcTalk(p, n, "What can I get you?");
            playerTalk(p, n, "What's on the menu?");
            npcTalk(p, n, "Dragon bitter and Greenmans ale");
            Menu defaultMenu = new Menu();
            defaultMenu.addOption(new Option("I'll give it a miss I think") {
                @Override
                public void action() {
                    npcTalk(p, n, "Come back when you're a little thirstier");
                }
            });
            defaultMenu.addOption(new Option("I'll try the dragon bitter") {
                @Override
                public void action() {
                    npcTalk(p, n, "Ok, that'll be two coins");
                    if (hasItem(p, 10, 2)) {
                        p.message("You buy a pint of dragon bitter");
                        addItem(p, 829, 1);
                        removeItem(p, 10, 2);
                    } else {
                        playerTalk(p, n,"Oh dear. I don't seem to have enough money");
                    }
                }
            });
            defaultMenu.addOption(new Option("Can I have some greenmans ale?") {
                    @Override
                    public void action() {
                        npcTalk(p, n, "Ok, that'll be ten coins");
                        if (hasItem(p, 10, 10)) {
                            p.message("You buy a pint of ale");
                            addItem(p, 830, 1);
                            removeItem(p, 10, 10);
                        } else {
                            playerTalk(p, n, "Oh dear. I don't seem to have enough money");
                        }
                    }
                });
            defaultMenu.showMenu(p, n);
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if (n.getID() == 529) {
            return true;
        }
        return false;
    }

}
