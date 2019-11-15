package org.nemotech.rsc.plugins.misc;

import static org.nemotech.rsc.plugins.Plugin.*;

import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.listeners.action.InvActionListener;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnNpcListener;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnObjectListener;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.InvActionExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnObjectExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;

public class Panning extends Plugin implements ObjectActionListener, ObjectActionExecutiveListener, InvUseOnObjectListener, InvUseOnObjectExecutiveListener, InvUseOnNpcListener, InvUseOnNpcExecutiveListener, InvActionListener, InvActionExecutiveListener {

    public static int PANNING_POINT = 1058;

    public static int PANNING_TRAY = 1111;
    public static int FULL_PANNING_TRAY = 1113;
    public static int CUP_OF_TEA = 739;

    public static int COINS = 10;
    public static int GOLD_NUGGET = 1118;
    public static int GOLD_NUGGET_TRAY = 1112;
    public static int ORANGE_ROCK_SAMPLE = 1148;
    public static int UNCUT_OPAL = 891;
    public static int UNCUT_JADE = 890;
    public static int UNCUT_SAPPHIRE = 160;

    public static int GUIDE = 726;

    @Override
    public boolean blockObjectAction(GameObject obj, String command, Player p) {
        if(obj.getID() == PANNING_POINT) {
            return true;
        }
        return false;
    }

    @Override
    public void onObjectAction(GameObject obj, String command, Player p) {
        if(obj.getID() == PANNING_POINT) {
            p.message("If I had a panning tray I could pan here");
        }
    }

    @Override
    public boolean blockInvUseOnObject(GameObject obj, InvItem item, Player p) {
        if(obj.getID() == PANNING_POINT) {
            return true;
        }
        return false;
    }

    private boolean handlePanning(GameObject obj, InvItem item, Player p) {
        if(!p.getCache().hasKey("unlocked_panning")) {
            return false;
        }
        p.setBusy(true);
        showBubble(p, new InvItem(PANNING_TRAY));
        p.message("You scrape the tray along the bottom");
        message(p, "You swirl away the excess water");
        sleep(1500);
        showBubble(p, new InvItem(FULL_PANNING_TRAY));
        p.message("You lift the full tray from the water");
        p.getInventory().replace(PANNING_TRAY, FULL_PANNING_TRAY);
        p.incExp(MINING, (int) 5.0, true);
        p.setBusy(false);
        return false;
    }

    @Override
    public void onInvUseOnObject(GameObject obj, InvItem item, Player p) {
        if(obj.getID() == PANNING_POINT) {
            if(item.getID() == PANNING_TRAY) {
                NPC guide = getNearestNpc(p, GUIDE, 15);
                if(guide != null) {
                    // NOT SURE? if(p.getQuestStage(Constants.Quests.DIGSITE) < 2) {
                    if(!p.getCache().hasKey("unlocked_panning")) {
                        npcTalk(p, guide, "Hey! you can't pan yet!");
                        playerTalk(p, guide, "Why not ?");
                        npcTalk(p, guide, "We do not allow the uninvited to pan here");
                        int menu = showMenu(p, guide,
                                "Okay, forget it",
                                "So how do I become invited then ?");
                        if(menu == 0) {
                            npcTalk(p, guide, "You can of course use this place when you know what you are doing");
                        } else if(menu == 1) {
                            npcTalk(p, guide, "I'm not supposed to let people pan here",
                                    "Unless they have permission from the authorities first",
                                    "Mind you I could let you have a go...",
                                    "If you're willing to do me a favour");
                            playerTalk(p, guide, "What's that ?");
                            npcTalk(p, guide, "Well...to be honest...",
                                    "What I would really like...",
                                    "Is a nice cup of tea !");
                            playerTalk(p, guide, "Tea !?");
                            npcTalk(p, guide, "Absolutely, I'm parched !",
                                    "If you could bring me one of those...",
                                    "I would be more than willing to let you pan here");
                        }
                    } else {
                        handlePanning(obj, item, p);
                    }
                }
            }
            if(item.getID() == FULL_PANNING_TRAY) {
                p.message("This panning tray already contains something");
            }
            if(item.getID() == GOLD_NUGGET_TRAY) {
                p.message("This panning tray already contains gold");
            }
        }
    }

    @Override
    public boolean blockInvUseOnNpc(Player p, NPC npc, InvItem item) {
        if(npc.getID() == GUIDE) {
            return true;
        }
        return false;
    }

    @Override
    public void onInvUseOnNpc(Player p, NPC npc, InvItem item) {
        if(npc.getID() == GUIDE) {
            if(item.getID() == PANNING_TRAY) {
                p.message("You give the panning tray to the guide");
                npcTalk(p, npc, "Yes, this is a panning tray...");
            }
            if(item.getID() == FULL_PANNING_TRAY) {
                p.message("You give the full panning tray to the guide");
                npcTalk(p, npc, "This is no good to me",
                        "I don't deal with finds");
            }
            if(item.getID() == GOLD_NUGGET_TRAY) {
                p.message("You give the full panning tray to the guide");
                npcTalk(p, npc, "I am afraid I don't deal with finds",
                        "That's not my job");
            }
            if(item.getID() == CUP_OF_TEA) {
                if(p.getCache().hasKey("unlocked_panning")) {
                    npcTalk(p, npc, "No thanks, I've had enough!");
                } else {
                    npcTalk(p, npc, "Ah! Lovely!",
                            "You can't beat a good cuppa...",
                            "You're free to pan all you want");
                    playerTalk(p, npc, "Thanks");
                    removeItem(p, CUP_OF_TEA, 1);
                    p.getCache().store("unlocked_panning", true);
                }
            }
        }
    }

    @Override
    public boolean blockInvAction(InvItem item, Player p) {
        if(item.getID() == PANNING_TRAY) {
            return true;
        }
        if(item.getID() == FULL_PANNING_TRAY) {
            return true;
        }
        if(item.getID() == GOLD_NUGGET_TRAY) {
            return true;
        }
        return false;
    }


    @Override
    public void onInvAction(InvItem item, Player p) {
        if(item.getID() == PANNING_TRAY) {
            p.message("You search the contents of the tray");
            playerTalk(p, null, "Err, why am I searching an empty tray ?");
        }
        if(item.getID() == FULL_PANNING_TRAY) {
            p.setBusy(true);
            message(p, "You search the contents of the tray...");
            sleep(1500);
            int randomNumber = Util.random(0, 100);
            int addItem = -1;
            int addAmount = 1;
            if(randomNumber < 40) { // 40%
                addItem = -1;
            } else if(randomNumber >= 40 && randomNumber < 50) { // 10%
                addItem = UNCUT_OPAL;
            } else if(randomNumber >= 50 && randomNumber < 60) { // 10%
                addItem = UNCUT_JADE;
            } else if(randomNumber >= 60 && randomNumber < 70) { // 10%
                addItem = COINS;
                int[] randomCoins = { 1, 2, 5, 10 };
                addAmount = randomCoins[Util.random(0, (randomCoins.length - 1))];
            } else if(randomNumber >= 70 && randomNumber < 80) { // 10%
                addItem = ORANGE_ROCK_SAMPLE;
            } else if(randomNumber >= 80 && randomNumber < 90) { // 10%
                addItem = GOLD_NUGGET_TRAY;
            } else if(randomNumber >= 90 && randomNumber < 100) { // 10%
                addItem = UNCUT_SAPPHIRE;
            }
            p.getInventory().replace(FULL_PANNING_TRAY, PANNING_TRAY);
            if(addItem != -1) {
                if(addItem == COINS) {
                    p.message("You find some coins within the mud");
                } else if(addItem == ORANGE_ROCK_SAMPLE) {
                    p.message("You find a rock sample covered in mud");
                } else if(addItem == UNCUT_OPAL || addItem == UNCUT_JADE || addItem == UNCUT_SAPPHIRE) {
                    p.message("You find a gem within the mud!");
                } else if(addItem == GOLD_NUGGET_TRAY) {
                    p.message("You find some gold nuggets within the mud!");
                }
                addItem(p, addItem, addAmount);
            } else {
                p.message("The tray contains only plain mud");
            }
            p.setBusy(false);
        }
        if(item.getID() == GOLD_NUGGET_TRAY) {
            p.getInventory().replace(GOLD_NUGGET_TRAY, PANNING_TRAY);
            addItem(p, GOLD_NUGGET, 1);
            p.message("You take the gold form the panning tray");
            p.message("You have a handful of gold nuggets");
        }
    }
}
