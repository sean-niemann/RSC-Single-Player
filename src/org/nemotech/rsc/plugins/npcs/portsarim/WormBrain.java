package org.nemotech.rsc.plugins.npcs.portsarim;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.getNearestNpc;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.removeItem;

import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import static org.nemotech.rsc.plugins.Plugin.DRAGON_SLAYER;
import org.nemotech.rsc.plugins.listeners.action.WallObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.WallObjectActionExecutiveListener;
import org.nemotech.rsc.plugins.menu.Menu;
import org.nemotech.rsc.plugins.menu.Option;

public final class WormBrain implements WallObjectActionListener, WallObjectActionExecutiveListener {

    @Override
    public boolean blockWallObjectAction(GameObject obj, Integer click, Player p) {
        if (obj.getID() == 30 && obj.getX() == 283 && obj.getY() == 665) {
            return true;
        } 
        return false;
    }

    @Override
    public void onWallObjectAction(GameObject obj, Integer click, final Player p) {
        if (obj.getID() == 30 && obj.getX() == 283 && obj.getY() == 665) {
            final NPC n = getNearestNpc(p, 192, 10);
            message(p, "...you knock on the cell door");
            npcTalk(p, n, "Whut you want?");
            Menu defaultMenu = new Menu();
            if (p.getQuestStage(DRAGON_SLAYER) >= 2 && !hasItem(p, 416)) {
                defaultMenu.addOption(new Option("I believe you've got a piece of map that I need") {
                    @Override
                    public void action() {
                        npcTalk(p, n, "So? Why should I be giving it to you? What you do for Wormbrain?");
                        new Menu().addOptions(
                                new Option("I'm not going to do anything for you. Forget it") {
                                    public void action() {
                                        npcTalk(p, n, "Me keep map piece, you no get map piece");
                                    }
                                },
                                new Option("I'll let you live. I could just kill you") {
                                    @Override
                                    public void action() {
                                        npcTalk(p, n, "Ha! Me in here you out dere. You not get map piece");
                                    }
                                }, new Option("I suppose I could pay you for the map piece...") {
                                    @Override
                                    public void action() {
                                        playerTalk(p, n, "Say, 10,000 coins?");
                                        npcTalk(p, n, "Me not stoopid, it worth at least 1,000,000 coins!");
                                        new Menu().addOptions(
                                                new Option("You must be joking! Forget it") {
                                                    public void action() {
                                                        npcTalk(p, n, "Me keep map piece, you no get map piece");
                                                    }
                                                }, new Option("Alright then, 1,000,000 it is") {
                                                    @Override
                                                    public void action() {
                                                        if(hasItem(p, 10, 1000000)) {
                                                            removeItem(p, 10, 1000000);
                                                            p.message("You buy the map piece from Wormbrain");
                                                            npcTalk(p, n, "Fank you very much! Now me can bribe da guards, hehehe");
                                                            addItem(p, 416, 1);
                                                        } else {
                                                            playerTalk(p, n, "Oops, I don't have enough on me");
                                                            npcTalk(p, n, "Comes back when you has enough");
                                                        }
                                                    }
                                                }).showMenu(p, n);
                                    }
                                }, new Option("Where did you get the map piece from?") {
                                    @Override
                                    public void action() {
                                        npcTalk(p, n, "Found it when me pick some stuff up, me kept it");
                                    }
                                }).showMenu(p, n);
                    }
                });
            }
            defaultMenu.addOption(new Option("What are you in for?") {
                @Override
                public void action() {
                    npcTalk(p, n, "Me not sure. Me pick some stuff up and take it away");
                    playerTalk(p, n, "Well, did the stuff belong to you?");
                    npcTalk(p, n, "Umm...no");
                    playerTalk(p, n, "Well, that would be why then");
                    npcTalk(p, n, "Oh, right");
                }
            });
            defaultMenu.addOption(new Option("Sorry, thought this was a zoo") {
                @Override
                public void action() {
                    // Nothing
                }
            });
            defaultMenu.showMenu(p, n);
        }
    }
}