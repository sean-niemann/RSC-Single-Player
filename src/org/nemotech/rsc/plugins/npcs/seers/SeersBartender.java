package org.nemotech.rsc.plugins.npcs.seers;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;
import org.nemotech.rsc.plugins.menu.Menu;
import org.nemotech.rsc.plugins.menu.Option;

public final class SeersBartender implements TalkToNpcExecutiveListener,
        TalkToNpcListener {

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        if (n.getID() == 306) {
            npcTalk(p, n, "Good morning, what would you like?");
            Menu defaultMenu = new Menu();
            defaultMenu.addOption(new Option("What do you have?") {
                @Override
                public void action() {
                    npcTalk(p, n, "Well we have a beer",
                            "Or if you want some food, we have our home made stew and meat pies");
                    Menu def = new Menu();
                    def.addOption(new Option("Beer please") {
                        @Override
                        public void action() {
                            npcTalk(p, n, "one beer coming up",
                                    "Ok, that'll be two coins");
                            if (hasItem(p, 10, 2)) {
                                p.message("You buy a pint of beer");
                                addItem(p, 193, 1);
                                p.getInventory().remove(10, 18);
                            } else {
                                playerTalk(p, n,
                                        "Oh dear. I don't seem to have enough money");
                            }

                        }
                    });
                    def.addOption(new Option("I'll try the meat pie") {
                        @Override
                        public void action() {
                            npcTalk(p, n, "Ok, that'll be 16 gold");
                            if (hasItem(p, 10, 16)) {
                                p.message("You buy a nice hot meat pie");
                                addItem(p, 259, 1);
                                p.getInventory().remove(10, 18);
                            } else {
                                playerTalk(p, n,
                                        "Oh dear. I don't seem to have enough money");
                            }

                        }
                    });
                    def.addOption(new Option("Could I have some stew please") {
                        @Override
                        public void action() {
                            npcTalk(p, n,
                                    "A bowl of stew, that'll be 20 gold please");
                            if (hasItem(p, 10, 16)) {
                                p.message("You buy a bowl of home made stew");
                                addItem(p, 346, 1);
                                p.getInventory().remove(10, 18);
                            } else {
                                playerTalk(p, n,
                                        "Oh dear. I don't seem to have enough money");
                            }

                        }
                    });
                    def.addOption(new Option(
                            "I don't really want anything thanks") {
                        @Override
                        public void action() {
                        }
                    });
                    def.showMenu(p, n);
                }
            });
            defaultMenu.addOption(new Option("Beer please") {
                @Override
                public void action() {
                    npcTalk(p, n, "one beer coming up",
                            "Ok, that'll be two coins");
                    if (hasItem(p, 10, 2)) {
                        p.message("You buy a pint of beer");
                        addItem(p, 193, 1);
                        p.getInventory().remove(10, 18);
                    } else {
                        playerTalk(p, n,
                                "Oh dear. I don't seem to have enough money");
                    }
                }
            });
            if (p.getCache().hasKey("barcrawl")
                    && !p.getCache().hasKey("barfive")) {
                defaultMenu.addOption(new Option(
                        "I'm doing Alfred Grimhand's barcrawl") {
                    @Override
                    public void action() {
                        npcTalk(p,
                                n,
                                "Oh you're a barbarian then",
                                "Now which of these was the barrels contained the liverbane ale?",
                                "That'll be 18 coins please");
                        if (hasItem(p, 10, 18)) {
                            p.getInventory().remove(10, 18);
                            message(p,
                                    "The bartender gives you a glass of liverbane ale",
                                    "You gulp it down",
                                    "The room seems to be swaying",
                                    "The bartender scrawls his signature on your card");
                            p.getCache().store("barfive", true);
                        } else {
                            playerTalk(p, n, "I don't have 18 coins right now");
                        }
                    }
                });
            }
            defaultMenu.addOption(new Option(
                    "I don't really want anything thanks") {
                @Override
                public void action() {
                }
            });
            defaultMenu.showMenu(p, n);
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if (n.getID() == 306) {
            return true;
        }
        return false;
    }

}
