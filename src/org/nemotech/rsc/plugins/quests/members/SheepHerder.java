package org.nemotech.rsc.plugins.quests.members;

import org.nemotech.rsc.client.sound.SoundEffect;
import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.QuestInterface;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnNpcListener;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnObjectListener;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnObjectExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class SheepHerder extends Plugin implements QuestInterface,TalkToNpcListener,
    TalkToNpcExecutiveListener, ObjectActionListener,
    ObjectActionExecutiveListener, InvUseOnNpcListener,
    InvUseOnNpcExecutiveListener, InvUseOnObjectListener,
    InvUseOnObjectExecutiveListener {

    public static final int HALGRIVE = 436;
    public static final int FARMER_BRUMTY = 434;
    public static final int PLAGUE_SHEEP_1ST = 430;
    public static final int PLAGUE_SHEEP_2ND = 431;
    public static final int PLAGUE_SHEEP_3RD = 432;
    public static final int PLAGUE_SHEEP_4TH = 433;

    public static final int POISON = 759;
    public static final int CATTLE_PROD = 757;
    public static final int PROTECTIVE_JACKET = 760;
    public static final int PROTECTIVE_TROUSERS = 761;
    
    public static final int GATE = 443;
    public static final int CATTLE_FURNACE = 444;

    @Override
    public int getQuestID() {
        return Plugin.SHEEP_HERDER;
    }

    @Override
    public String getQuestName() {
        return "Sheep Herder (members)";
    }

    @Override
    public boolean isMembers() {
        return true;
    }

    @Override
    public void handleReward(Player p) {
        p.message("well done, you have completed the Plaguesheep quest");
        p.incQuestPoints(4);
        p.message("@gre@You have gained 4 quest points!");
        addItem(p, 10, 3100);
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if (n.getID() == HALGRIVE) {
            return true;
        }
        if (n.getID() == FARMER_BRUMTY) {
            return true;
        }
        return false;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if (n.getID() == FARMER_BRUMTY) {
            switch (p.getQuestStage(this)) {
            case 2:
                playerTalk(p, n, "hello");
                npcTalk(p, n, "hello adventurer",
                        "be careful rounding up those sheep",
                        "i don't think they've wandered far",
                        "but if you touch them you'll become infected as well",
                        "there should be a cattle prod in the barn",
                        "you can use it to herd up the sheep");
                break;
            case -1:
                playerTalk(p, n, "hello there", "i'm sorry about your sheep");
                npcTalk(p, n, "that's ok, it had to be done",
                        "i just hope none of my other livestock becomes infected");
                break;
            }
        }
        if (n.getID() == HALGRIVE) {
            switch (p.getQuestStage(this)) {
            case 0:
                playerTalk(p, n, "how are you?");
                npcTalk(p, n, "I've been better");
                int menu = showMenu(p, n, "What's wrong?",
                        "That's life for you");
                if (menu == 0) {
                    npcTalk(p, n, "a plague has spread over west ardounge",
                            "apparently it's reasonably contained",
                            "but four infected sheep have escaped",
                            "they're roaming free in and around east ardounge",
                            "the whole city could be infected in days",
                            "i need someone to gather the sheep",
                            "herd them into a safe enclosure",
                            "then kill the sheep",
                            "their remains will also need to be disposed of safely in a furnace");
                    int menu2 = showMenu(p, n, "I can do that for you",
                            "That's not a job for me");
                    if (menu2 == 0) {
                        npcTalk(p,
                                n,
                                "good, the enclosure is to the north of the city",
                                "On farmer Brumty's farm",
                                "the four sheep should still be close to it",
                                "before you go into the enclosure",
                                "make sure you have protective clothing on",
                                "otherwise you'll catch the plague");
                        playerTalk(p, n, "where do I get protective clothing?");
                        npcTalk(p,
                                n,
                                "Doctor Orbon wears it when trying to save the infected",
                                "you'll find him in the chapel",
                                "take this poisoned animal feed",
                                "give it to the four sheep and they'll peacefully fall asleep");
                        message(p, "The councillor gives you some sheep poison");
                        addItem(p, POISON, 1);
                        p.updateQuestStage(getQuestID(), 1);
                    } else if (menu2 == 1) {
                        npcTalk(p, n, "fair enough, it's not nice work");
                    }
                } 
                break;
            case 1:
                npcTalk(p, n,
                        "please find those four sheep as soon as you can",
                        "every second counts");
                if (!hasItem(p, POISON)) {
                    playerTalk(p, n, "Some more sheep poison might be useful");
                    message(p, "The councillor gives you some sheep poison");
                    addItem(p, POISON, 1);
                }
                break;
            case 2:
                npcTalk(p, n,
                        "have you managed to dispose of those four sheep?");
                if (p.getCache().hasKey("plagueremain1st")
                        && p.getCache().hasKey("plagueremain2nd")
                        && p.getCache().hasKey("plagueremain3th")
                        && p.getCache().hasKey("plagueremain4th")) {
                    playerTalk(p, n, "yes i have");
                    p.getCache().remove("plague1st");
                    p.getCache().remove("plague2nd");
                    p.getCache().remove("plague3th");
                    p.getCache().remove("plague4th");
                    p.getCache().remove("plagueremain1st");
                    p.getCache().remove("plagueremain2nd");
                    p.getCache().remove("plagueremain3th");
                    p.getCache().remove("plagueremain4th");
                    p.sendQuestComplete(Plugin.SHEEP_HERDER);
                } else {
                    playerTalk(p, n, "erm not quite");
                    npcTalk(p, n, "not quite's not good enough",
                            "all four sheep must be captured, slain and their remains burnt");
                    playerTalk(p, n, "ok i'll get to it");
                    if(!hasItem(p, POISON)) {
                        playerTalk(p,n, "Some more sheep poison might be useful");
                        p.message("The councillor gives you some more sheep poison");
                        addItem(p, POISON, 1);
                    }
                }
                break;
            case -1:
                playerTalk(p, n, "hello again halgrive");
                npcTalk(p, n, "well hello again traveller", "how are you");
                playerTalk(p, n, "good thanks and yourself?");
                npcTalk(p, n,
                        "much better now i don't have to worry about those sheep");
                break;
            }
        }
    }

    @Override
    public boolean blockObjectAction(GameObject obj, String command,
            Player player) {
        if (obj.getID() == GATE) {
            return true;
        }
        return false;
    }

    @Override
    public void onObjectAction(GameObject obj, String command, Player p) {
        if (obj.getID() == GATE) {
            if (p.getInventory().wielding(PROTECTIVE_JACKET)
                    && p.getInventory().wielding(PROTECTIVE_TROUSERS)) {
                openGatey(obj, p);
                if (p.getX() <= 588) {
                    p.teleport(589, 541, false);
                } else {
                    p.teleport(588, 540, false);
                }
            } else {
                message(p, "this is a restricted area",
                        "you cannot enter without protective clothing");
            }
        }

    }

    public void openGatey(GameObject object, Player player) {
        player.getSender().sendSound(SoundEffect.OPEN_DOOR);
        player.message("you open the gate and walk through");
        World.getWorld().replaceGameObject(object,
                new GameObject(object.getLocation(), 442,
                        object.getDirection(), object.getType()));
        World.getWorld().delayedSpawnObject(object.getLoc(), 3000);
    }

    private void sheepYell(Player p) {
        sleep(600);
        p.message("@yel@:Baaaaaaaaa!!!");
    }

    @Override
    public boolean blockInvUseOnNpc(Player player, NPC npc, InvItem item) {
        if (npc.getID() == PLAGUE_SHEEP_1ST || npc.getID() == PLAGUE_SHEEP_2ND || npc.getID() == PLAGUE_SHEEP_3RD
                || npc.getID() == PLAGUE_SHEEP_4TH) {
            return true;
        }
        return false;
    }

    @Override
    public void onInvUseOnNpc(Player p, final NPC plagueSheep, InvItem item) {
        if (plagueSheep.getID() == PLAGUE_SHEEP_1ST || plagueSheep.getID() == PLAGUE_SHEEP_2ND
                || plagueSheep.getID() == PLAGUE_SHEEP_3RD || plagueSheep.getID() == PLAGUE_SHEEP_4TH) {
            if (item.getID() == CATTLE_PROD) {
                if ((p.getInventory().wielding(PROTECTIVE_TROUSERS) && p.getInventory()
                        .wielding(PROTECTIVE_JACKET))
                        && p.getQuestStage(getQuestID()) != -1) {
                    if (plagueSheep.getLocation().inBounds(589, 543, 592, 548)) {
                        p.message("The sheep is already in the pen");
                        return;
                    }
                    p.message("you nudge the sheep forward");
                    switch(plagueSheep.getID()) {
                    case PLAGUE_SHEEP_1ST:
                        if (p.getY() >= 563) {
                            plagueSheep.teleport(580, 558);
                        } else if(p.getY() >= 559) {
                            plagueSheep.teleport(585, 553);
                        } else if (p.getY() <= 558 && p.getY() > 542) {
                            plagueSheep.teleport(594, 538);
                            // TODO: walkMob(plagueSheep, new Point(588, 538), new Point(578, 546));
                        } else if (p.getY() < 543) {
                            sheepYell(p);
                            p.message("the sheep jumps the gate into the enclosure");
                            plagueSheep.teleport(590, 546);
                            World.getWorld().getDelayedEventHandler().add(
                                    new DelayedEvent(p, 1000) {
                                        int timesRan = 0;
                                        @Override
                                        public void run() {
                                            if (timesRan > 60) {
                                                plagueSheep.remove();
                                                interrupt();
                                            }
                                            timesRan++;
                                        }
                                    });
                            return;
                        }
                        p.message("the sheep runs to the north");
                        sheepYell(p);
                        break;
                    case PLAGUE_SHEEP_2ND:
                        if (p.getY() >= 563) {
                            plagueSheep.teleport(580, 558);
                            // TODO: walkMob(plagueSheep, new Point(580, 561));
                        } else if(p.getY() >= 562) {
                            plagueSheep.teleport(585, 553);
                        } else if (p.getY() >= 559) {
                            plagueSheep.teleport(585, 553);
                            sleep(1200);
                            plagueSheep.teleport(597, 537);
                            // TODO: walkMob(plagueSheep, new Point(588, 538));
                        } else if(p.getY() >= 547) {
                            plagueSheep.teleport(597, 537);
                            // TODO: walkMob(plagueSheep, new Point(588, 538));
                        } else if (p.getY() <= 545) {
                            sheepYell(p);
                            p.message("the sheep jumps the gate into the enclosure");
                            plagueSheep.teleport(590, 546);
                            World.getWorld().getDelayedEventHandler().add(
                                    new DelayedEvent(p, 1000) {
                                        int timesRan = 0;
                                        @Override
                                        public void run() {
                                            if (timesRan > 60) {
                                                plagueSheep.remove();
                                                interrupt();
                                            }
                                            timesRan++;
                                        }
                                    });
                            return;
                        }
                        p.message("the sheep runs to the north");
                        sheepYell(p);
                        break;
                    case PLAGUE_SHEEP_3RD:
                        if (plagueSheep.getX() > 618) {
                            p.message("the sheep runs to the east");
                            plagueSheep.teleport(614, 531);
                        } else if (plagueSheep.getX() < 619
                                && plagueSheep.getX() > 612) {
                            p.message("the sheep runs to the east");
                            plagueSheep.teleport(604, 531);
                        } else if (plagueSheep.getX() < 613
                                && plagueSheep.getX() > 602) {
                            p.message("the sheep runs to the east");
                            plagueSheep.teleport(594, 531);
                        } else if (plagueSheep.getX() < 603
                                && plagueSheep.getX() > 592) {
                            p.message("the sheep runs to the east");
                            plagueSheep.teleport(584, 531);
                        } else if (plagueSheep.getX() < 593
                                && plagueSheep.getX() > 582) {
                            p.message("the sheep runs to the southeast");
                            plagueSheep.teleport(579, 543);
                        } else if (plagueSheep.getX() < 586) {
                            sheepYell(p);
                            p.message("the sheep jumps the gate into the enclosure");
                            plagueSheep.teleport(590, 546);
                            World.getWorld().getDelayedEventHandler().add(
                                    new DelayedEvent(p, 1000) {
                                        int timesRan = 0;
                                        @Override
                                        public void run() {
                                            if (timesRan > 60) {
                                                plagueSheep.remove();
                                                interrupt();
                                            }
                                            timesRan++;
                                        }
                                    });
                            return;
                        }
                        sheepYell(p);
                        break;
                    case PLAGUE_SHEEP_4TH:
                        if (plagueSheep.getX() == 603
                        && plagueSheep.getY() < 589) {
                            p.message("the sheep runs to the south");
                            plagueSheep.teleport(603, 595);
                        } else if (plagueSheep.getY() > 589
                                && plagueSheep.getY() < 599) {
                            p.message("the sheep runs to the southeast");
                            plagueSheep.teleport(591, 603);
                            // TODO: walkMob(plagueSheep, new Point(596, 603), new Point(598, 599));
                        } else if (plagueSheep.getY() > 598
                                && plagueSheep.getY() < 604) {
                            p.message("the sheep runs over the river to the northeast");
                            plagueSheep.teleport(587, 596);
                            // TODO: walkMob(plagueSheep, new Point(589, 595), new Point(593, 595), new Point(595, 587));
                        } else if (plagueSheep.getY() > 583
                                && plagueSheep.getY() < 588) {
                            p.message("the sheep runs to the north");
                            plagueSheep.teleport(588, 578);
                            // TODO: walkMob(plagueSheep, new Point(594, 584), new Point(594, 586));
                        } else if (plagueSheep.getY() > 575
                                && plagueSheep.getY() < 585) {
                            p.message("the sheep runs to the north");
                            plagueSheep.teleport(588, 572);
                            // TODO: walkMob(plagueSheep, new Point(594, 578), new Point(595, 578));
                        } else if (plagueSheep.getY() > 567
                                && plagueSheep.getY() < 576) {
                            p.message("the sheep runs to the northeast");
                            plagueSheep.teleport(589, 562);
                            // TODO: walkMob(plagueSheep, new Point(594, 567), new Point(595, 567));
                        } else if (plagueSheep.getY() > 565
                                && plagueSheep.getY() < 568) {
                            p.message("the sheep runs to the northeast");
                            plagueSheep.teleport(587, 552);
                            // TODO: walkMob(plagueSheep, new Point(596, 567));
                        } else if (plagueSheep.getY() > 551
                                && plagueSheep.getY() < 562) {
                            p.message("the sheep runs to the northeast");
                            plagueSheep.teleport(586, 547);
                        } else if (plagueSheep.getY() > 547
                                && plagueSheep.getY() < 552) {
                            p.message("the sheep runs to the northeast");
                            plagueSheep.teleport(586, 539);
                            // TODO: walkMob(plagueSheep, new Point(588, 549));
                        } else if (plagueSheep.getY() < 547) {
                            sheepYell(p);
                            p.message("the sheep jumps the gate into the enclosure");
                            plagueSheep.teleport(590, 546);
                            World.getWorld().getDelayedEventHandler().add(
                                    new DelayedEvent(p, 1000) {
                                        int timesRan = 0;
                                        @Override
                                        public void run() {
                                            if (timesRan > 60) {
                                                plagueSheep.remove();
                                                interrupt();
                                            }
                                            timesRan++;
                                        }
                                    });
                            return;
                        }
                        sheepYell(p);
                        break;

                    }
                } else {
                    message(p, "this sheep has the plague",
                            "you better not touch it");
                }
            }
            if (item.getID() == POISON) {
                if (plagueSheep.getLocation().inBounds(589, 543, 592, 548)) {
                    if (plagueSheep.getID() == PLAGUE_SHEEP_1ST) {
                        if(p.getCache().hasKey("plagueremain1st")) {
                            message(p,
                                    "You have already disposed of this sheep",
                                    "Find a different sheep");
                            return;
                        }
                    } else if (plagueSheep.getID() == PLAGUE_SHEEP_2ND) {
                        if(p.getCache().hasKey("plagueremain2nd")) {
                            message(p,
                                    "You have already disposed of this sheep",
                                    "Find a different sheep");
                            return;
                        }
                    } else if (plagueSheep.getID() == PLAGUE_SHEEP_3RD) {
                        if(p.getCache().hasKey("plagueremain3th")) {
                            message(p,
                                    "You have already disposed of this sheep",
                                    "Find a different sheep");
                            return;
                        }
                    } else if (plagueSheep.getID() == PLAGUE_SHEEP_4TH) {
                        if(p.getCache().hasKey("plagueremain4th")) {
                            message(p,
                                    "You have already disposed of this sheep",
                                    "Find a different sheep");
                            return;
                        }
                    }
                    message(p, "you give the sheep poisoned sheep feed");
                    p.message("the sheep collapses to the floor and dies");
                    plagueSheep.killedBy(p);
                } else {
                    message(p, "you can't kill the sheep out here",
                            "you might spread the plague");
                }
            }
        }
    }

    @Override
    public boolean blockInvUseOnObject(GameObject obj, InvItem item,
            Player player) {
        if (obj.getID() == CATTLE_FURNACE) {
            return true;
        }
        return false;
    }

    @Override
    public void onInvUseOnObject(GameObject obj, InvItem item, Player p) {
        if (obj.getID() == CATTLE_FURNACE) {
            if (item.getID() == 758) {
                removeItem(p, 758, 1);
                if(!p.getCache().hasKey("plagueremain1st")) {
                    p.getCache().store("plagueremain1st", true);
                }
            }
            if (item.getID() == 762) {
                removeItem(p, 762, 1);
                if(!p.getCache().hasKey("plagueremain2nd")) {
                    p.getCache().store("plagueremain2nd", true);
                }
            }
            if (item.getID() == 763) {
                removeItem(p, 763, 1);
                if(!p.getCache().hasKey("plagueremain3th")) {
                    p.getCache().store("plagueremain3th", true);
                }
            }
            if (item.getID() == 764) {
                removeItem(p, 764, 1);
                if(!p.getCache().hasKey("plagueremain4th")) {
                    p.getCache().store("plagueremain4th", true);
                }
            }
            message(p, "you put the sheep remains in the furnace",
                    "the remains burn to dust");
        }
    }

}