package org.nemotech.rsc.plugins.quests.members.watchtower;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.PlayerAttackNpcListener;
import org.nemotech.rsc.plugins.listeners.action.PlayerKilledNpcListener;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerAttackNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerKilledNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;
/**
 * 
 * @author Imposter/Fate
 *
 */
public class WatchTowerGorad implements TalkToNpcListener,
TalkToNpcExecutiveListener, PlayerKilledNpcListener, PlayerKilledNpcExecutiveListener, PlayerAttackNpcListener, PlayerAttackNpcExecutiveListener {

    public static int OGRE_TOOTH = 1043;

    public static int GORAD = 683;

    @Override
    public boolean blockPlayerKilledNpc(Player p, NPC n) {
        return n.getID() == GORAD;
    }

    @Override
    public void onPlayerKilledNpc(Player p, NPC n) {
        if(n.getID() == GORAD) {
            n.killedBy(p);
            p.message("Gorad has gone");
            p.message("He's dropped a tooth, I'll keep that!");
            addItem(p, OGRE_TOOTH, 1);
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == GORAD;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == GORAD) {
            if(p.getCache().hasKey("ogre_grew_p1")) {
                playerTalk(p,n, "Hello");
                npcTalk(p,n, "Do you know who you are talking to ?");
                int menu = showMenu(p,n,
                        "A big ugly brown creature...",
                        "I don't know who you are");
                if(menu == 0) {
                    npcTalk(p,n, "The impudence! take that...");
                    p.damage(16);
                    playerTalk(p,n, "Ouch!");
                    p.message("The ogre punched you hard in the face!");

                } else if(menu == 1) {
                    npcTalk(p,n, "I am Gorad - who you are dosen't matter",
                            "Go now and you may live another day!");
                }
            } else if(p.getCache().hasKey("ogre_grew")) {
                playerTalk(p,n, "I've come to knock your teeth out!");
                npcTalk(p,n, "How dare you utter that foul language in my prescence!",
                        "You shall die quickly vermin");
                n.startCombat(p);
            } else {
                p.message("Gorad is busy, try again later");
            }
        }
    }

    @Override
    public boolean blockPlayerAttackNpc(Player p, NPC n) {
        return n.getID() == GORAD;
    }

    @Override
    public void onPlayerAttackNpc(Player p, NPC affectedmob) {
        if(affectedmob.getID() == GORAD) {
            npcTalk(p,affectedmob, "Ho Ho! why would I want to fight a worm ?",
                    "Get lost!");
        }
    }
}