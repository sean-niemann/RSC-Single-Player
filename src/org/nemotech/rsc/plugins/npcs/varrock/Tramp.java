package org.nemotech.rsc.plugins.npcs.varrock;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;
import org.nemotech.rsc.plugins.menu.Menu;
import org.nemotech.rsc.plugins.menu.Option;

public class Tramp implements TalkToNpcExecutiveListener, TalkToNpcListener {
    public boolean blockTalkToNpc(final Player player, final NPC npc) {
        switch (npc.getID()) {
        case 28:
            return true;
        }
        return false;
    }

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        npcTalk(p, n, "Spare some change guy?");
        Menu defaultMenu = new Menu();
        defaultMenu.addOption(new Option("Sorry I haven't got any") {
            @Override
            public void action() {
                npcTalk(p, n, "Thanks anyway");
            }
        });
        defaultMenu.addOption(new Option("Ok, here you go") {
            @Override
            public void action() {
                npcTalk(p, n, "Thankyou, thats great.");
                new Menu()
                        .addOptions(
                                new Option("No problem") {
                                    @Override
                                    public void action() {
                                    }

                                },
                                new Option(
                                        "So don't I get some sort of quest hint or something now") {
                                    @Override
                                    public void action() {
                                        npcTalk(p,
                                                n,
                                                "No that's not why I'm asking for money",
                                                "I just need to eat");
                                    }
                                }).showMenu(p, n);
            }
        });
        defaultMenu.addOption(new Option("Get a job") {
            @Override
            public void action() {
                npcTalk(p, n, "You startin?");
            }
        });
        if (p.getQuestStage(Plugin.SHIELD_OF_ARRAV) == 2) {
            defaultMenu.addOption(new Option(
                    "Is there anything down this alleyway?") {
                @Override
                public void action() {
                    npcTalk(p, n, "Yes, there is actually",
                            "A notorious gang of thieves and hoodlums",
                            "Called the blackarm gang");
                    new Menu().addOptions(new Option("Thanks for the warning") {
                        public void action() {
                            npcTalk(p, n, "Don't worry about it");
                            p.updateQuestStage(Plugin.SHIELD_OF_ARRAV, 3);
                        }
                    }, new Option("Do you think they would let me join?") {
                        public void action() {
                            if (p.getCache().hasKey("arrav_gang")) {
                                /*if (p.getCache().getInt("arrav_gang") == ShieldOfArrav.BLACK_ARM) {
                                    npcTalk(p, n,
                                            "I was under the impression you were already a member");
                                } else if (p.getCache().getInt("arrav_gang") == ShieldOfArrav.PHOENIX_GANG) {
                                    npcTalk(p,
                                            n,
                                            "No",
                                            "You're a collaborator with the phoenix gang",
                                            "There's no way they'll let you join");
                                }*/
                            } else {
                                npcTalk(p,
                                        n,
                                        "You never know",
                                        "You'll find a lady down there called katrine",
                                        "Speak to her",
                                        "But don't upset her, she's pretty dangerous");
                                p.updateQuestStage(Plugin.SHIELD_OF_ARRAV, 3);
                            }
                        }
                    }).showMenu(p, n);
                }
            });
        }
        defaultMenu.showMenu(p, n);
    }
}
