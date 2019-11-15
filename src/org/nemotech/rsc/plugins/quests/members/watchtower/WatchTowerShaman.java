package org.nemotech.rsc.plugins.quests.members.watchtower;

import static org.nemotech.rsc.plugins.Plugin.*;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnNpcListener;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;
/**
 * 
 * @author Imposter/Fate
 *
 */
public class WatchTowerShaman implements TalkToNpcListener, TalkToNpcExecutiveListener, InvUseOnNpcListener, InvUseOnNpcExecutiveListener {

    public static int OGRE_SHAMAN = 673;
    public static int MAGIC_OGRE_POTION = 1054;

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if(n.getID() == OGRE_SHAMAN) {
            return true;
        }
        return false;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == OGRE_SHAMAN) {
            npcTalk(p, n, "Grr! how dare you talk to us",
                    "We will destroy you!");
            p.message("A magic blast comes from the shaman");
            //n.displayNpcTeleportBubble(n.getX(), n.getY());
            p.damage((int) (getCurrentLevel(p, HITS) * 0.2D + 10));
            p.message("You are badly injured by the blast");
        }
    }

    @Override
    public boolean blockInvUseOnNpc(Player player, NPC npc, InvItem item) {
        if(npc.getID() == OGRE_SHAMAN && item.getID() == MAGIC_OGRE_POTION) {
            return true;
        }
        return false;
    }

    @Override
    public void onInvUseOnNpc(Player p, NPC n, InvItem item) {
        if(n.getID() == OGRE_SHAMAN && item.getID() == MAGIC_OGRE_POTION) {
            p.setBusy(true);
            if(p.getMaxStat(6) < 14) {
                p.message("You need a level of 14 magic first");
                p.setBusy(false);
                return;
            }
            p.message("There is a bright flash");
            p.message("The ogre dissolves into spirit form");
            displayTeleportBubble(p, n.getX(), n.getY(), true);
            temporaryRemoveNpc(n);
            if(p.getCache().hasKey("shaman_count")) {
                int shaman_done = p.getCache().getInt("shaman_count");
                if(p.getCache().getInt("shaman_count") < 6) {
                    p.getCache().set("shaman_count", shaman_done + 1);
                }
                if(shaman_done == 1) {
                    playerTalk(p, null, "Thats the second one gone...");
                } else if(shaman_done == 2) {
                    playerTalk(p, null, "Thats the next one dealth with...");
                } else if(shaman_done == 3) {
                    playerTalk(p, null, "There goes another one...");
                } else if(shaman_done == 4) {
                    playerTalk(p, null, "Thats five, only one more left now...");
                } else if(shaman_done == 5 || p.getCache().getInt("shaman_count") == 6) {
                    p.message("You hear a scream...");
                    p.message("The shaman dissolves before your eyes!");
                    p.message("A crystal drops from the hand of the dissapearing ogre!");
                    p.message("You snatch it up quickly");
                    removeItem(p, 1054, 1);
                    addItem(p, 465, 1);
                    addItem(p, 1153, 1);
                    if(p.getQuestStage(Plugin.WATCHTOWER) == 8) {
                        p.updateQuestStage(Plugin.WATCHTOWER, 9);
                    }
                }
            } else {
                p.getCache().set("shaman_count", 1);
                playerTalk(p, null, "Thats one destroyed...");
            }
            p.setBusy(false);
        }
    }
}
