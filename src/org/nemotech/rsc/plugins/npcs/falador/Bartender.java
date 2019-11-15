package org.nemotech.rsc.plugins.npcs.falador;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import static org.nemotech.rsc.plugins.Plugin.GOBLIN_DIPLOMACY;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;
import org.nemotech.rsc.plugins.menu.Menu;
import org.nemotech.rsc.plugins.menu.Option;

public class Bartender implements TalkToNpcExecutiveListener, TalkToNpcListener {

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        Menu defaultMenu = new Menu();
        defaultMenu.addOption(new Option("Could i buy a beer please?") {
            @Override
            public void action() {
                npcTalk(p, n, "Sure that will be 2 gold coins please");
                if (p.getInventory().remove(10, 2) > -1) {
                    addItem(p, 193, 1);
                } else {
                    p.message("You dont have enough coins for the beer");
                }
            }
        });
        if (p.getQuestStage(GOBLIN_DIPLOMACY) == 0) {
            defaultMenu.addOption(new Option(
                    "Not very busy in here today is it?") {
                @Override
                public void action() {
                    npcTalk(p,
                            n,
                            "No it was earlier",
                            "There was a guy in here saying the goblins up by the mountain",
                            "Are arguing again",
                            "Of all things about the colour of their armour",
                            "Knowing the goblins,it could easily turn into a full blown war",
                            "Which wouldn't be good",
                            "Goblin wars make such a mess of the countryside");
                    playerTalk(p, n,
                            "Well if I have time I'll see if i can go and knock some sense into them");
                    p.updateQuestStage(GOBLIN_DIPLOMACY, 1); // remember
                                                                    // quest
                                                                    // starts
                                                                    // here.
                }
            });
        } else if (p.getQuestStage(GOBLIN_DIPLOMACY) >= 1
                || p.getQuestStage(GOBLIN_DIPLOMACY) == -1) { // TODO
            defaultMenu.addOption(new Option(
                    "Have you heard any more rumours in here?") {
                @Override
                public void action() {
                    npcTalk(p, n, "No it hasn't been very busy lately");
                }
            });
        }
        if (p.getCache().hasKey("barcrawl") && !p.getCache().hasKey("barsix")) {
            defaultMenu.addOption(new Option(
                    "I'm doing Alfred Grimhand's barcrawl") {
                @Override
                public void action() {
                    npcTalk(p, n, "Are you sure you look a bit skinny for that");
                    playerTalk(p, n,
                            "Just give me whatever drink I need to drink here");
                    npcTalk(p, n,
                            "Ok one black skull ale coming up, 8 coins please");
                    if (hasItem(p, 10, 8)) {
                        p.getInventory().remove(10, 8);
                        message(p, "You buy a black skull ale",
                                "You drink your black skull ale",
                                "Your vision blurs",
                                "The bartender signs your card");
                        p.getCache().store("barsix", true);
                        playerTalk(p, n, "hiccup", "hiccup");
                    } else {
                        playerTalk(p, n, "I don't have 8 coins right now");
                    }
                }
            });
        }
        defaultMenu.showMenu(p, n);
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 150;
    }

}