package org.nemotech.rsc.plugins.quests.members.legendsquest.mechanism;

import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.event.SingleEvent;
import org.nemotech.rsc.util.Util;

import static org.nemotech.rsc.plugins.Plugin.*;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.plugins.listeners.action.InvActionListener;
import org.nemotech.rsc.plugins.listeners.executive.InvActionExecutiveListener;
import org.nemotech.rsc.plugins.npcs.shilo.JungleForester;
import org.nemotech.rsc.plugins.quests.members.legendsquest.npcs.LegendsQuestGujuo;

public class LegendsQuestBullRoarer implements InvActionListener, InvActionExecutiveListener {

    public static final int BULL_ROARER = 1177;

    private boolean inKharaziJungle(Player p) {
        return p.getLocation().inBounds(338, 869, 477, 908);
    }

    @Override
    public boolean blockInvAction(InvItem item, Player p) {
        if(item.getID() == BULL_ROARER) {
            return true;
        }
        return false;
    }

    @Override
    public void onInvAction(InvItem item, Player p) {
        if(item.getID() == BULL_ROARER) {
            message(p, 1300, "You start to swing the bullroarer above your head.",
                    "You feel a bit silly at first, but soon it makes an interesting sound.");
            if(inKharaziJungle(p)) {
                message(p, 1300, "You see some movement in the trees...");
                attractNatives(p);
            } else {
                message(p, 1300, "Nothing much seems to happen though.");
                NPC forester = getNearestNpc(p, JungleForester.JUNGLE_FORESTER, 10);
                if(forester != null) {
                    npcTalk(p, forester, "You might like to use that when you get into the ",
                            "Kharazi jungle, it might attract more natives...");
                }
            }
        }
    }

    void attractNatives(Player p) {
        int controlRandom = Util.getRandom().nextInt(4);
        if(controlRandom == 0) {
            message(p, 1300, "...but nothing else much seems to happen.");
        } else if(controlRandom >= 1 && controlRandom <= 2) {
            message(p, 1300, "...and a tall, dark, charismatic looking native approaches you.");
            NPC gujuo = getNearestNpc(p, LegendsQuestGujuo.GUJUO, 15);
            if(gujuo == null) {
                gujuo = spawnNpc(LegendsQuestGujuo.GUJUO, p.getX(), p.getY());
                delayedRemoveGujuo(p, gujuo);
            }
            if(gujuo != null) {
                gujuo.resetPath();
                gujuo.teleport(p.getX(), p.getY());
                gujuo.initializeTalkScript(p);
                sleep(650);
                npcWalkFromPlayer(p, gujuo);
            }
        } else if(controlRandom == 3) {
            NPC nativeNpc = getMultipleNpcsInArea(p, 5, 777, 775, 521, 776);
            if(nativeNpc != null) {
                message(p, 1300, "...and a nearby " + (nativeNpc.getDef().getName().contains("bird") ? nativeNpc.getDef().getName() : "Kharazi " + nativeNpc.getDef().getName().toLowerCase()) + " takes a sudden dislike to you.");
                nativeNpc.setChasing(p);
                message(p, 0, "And attacks...");
            } else {
                attractNatives(p);
            }
        }
    }

    void delayedRemoveGujuo(Player p, final NPC n) {
        try {
            World.getWorld().getDelayedEventHandler().add(new DelayedEvent(null, 60000 * 3) {
                @Override
                public void run() {
                    if (owner.isLoggedIn() || owner.isRemoved()) {
                        n.remove();
                        interrupt();
                        return;
                    }
                    if (n.isRemoved()) {
                        interrupt();
                        return;
                    }
                    if(!inKharaziJungle(owner)) {
                        n.remove();
                        interrupt();
                        return;
                    }
                    int yell = Util.random(0, 3);
                    if(yell == 1) {
                        npcTalk(owner, n, "I must visit my people now...");
                    } else if(yell == 2) {
                        npcTalk(owner, n, "I must go and hunt now Bwana..");
                    } else if(yell == 3) {
                        npcTalk(owner, n, "I have to collect herbs now Bwana...");
                    } else {
                        npcTalk(owner, n, "I have work to do Bwana, I may see you again...");
                    }
                    World.getWorld().getDelayedEventHandler().add(new SingleEvent(null, 1900) {
                        @Override
                        public void action() {
                            owner.message("Gujuo disapears into the Kharazi jungle as swiftly as he appeared...");
                            n.remove();
                        }
                    });
                    interrupt();
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
