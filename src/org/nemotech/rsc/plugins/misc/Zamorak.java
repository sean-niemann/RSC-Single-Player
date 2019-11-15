package org.nemotech.rsc.plugins.misc;

import static org.nemotech.rsc.plugins.Plugin.getMultipleNpcsInArea;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.ChatMessage;
import org.nemotech.rsc.plugins.listeners.action.PickupListener;
import org.nemotech.rsc.plugins.listeners.action.PlayerAttackNpcListener;
import org.nemotech.rsc.plugins.listeners.action.PlayerMageNpcListener;
import org.nemotech.rsc.plugins.listeners.action.PlayerRangeNpcListener;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.PickupExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerAttackNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerMageNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerRangeNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;


/**
 * 
 * @author n0m, Fate
 *
 */
public class Zamorak implements TalkToNpcListener, TalkToNpcExecutiveListener, PickupListener, PickupExecutiveListener, PlayerAttackNpcExecutiveListener, PlayerAttackNpcListener, PlayerRangeNpcExecutiveListener, PlayerRangeNpcListener, PlayerMageNpcExecutiveListener, PlayerMageNpcListener {

    @Override
    public void onPickup(Player owner, Item item) {
        if (item.getID() == 501 && item.getX() == 333 && item.getY() == 434) {
            NPC zam = getMultipleNpcsInArea(owner, 7, 140, 139);
            if (zam != null && !zam.inCombat()) {
                owner.face(zam);
                zam.face(owner);
                applyCurse(owner, zam);
                return;
            }
        }
    }

    @Override
    public boolean blockPickup(Player p, Item i) {
        if(i.getID() == 501) {
            NPC zam = getMultipleNpcsInArea(p, 7, 140, 139);
            if(zam == null || zam.inCombat())
                return false;
            else 
                return true;
        }
        return false;
    }

    @Override
    public boolean blockPlayerAttackNpc(Player p, NPC n) {
        if(n.getID() == 140 || n.getID() == 139) {
            return true;
        }   
        return false;
    }

    @Override
    public void onPlayerAttackNpc(Player p, NPC zamorak) {
        if(zamorak.getID() == 140 || zamorak.getID() == 139) {
            applyCurse(p, zamorak);
        }   
    }

    @Override
    public boolean blockPlayerMageNpc(Player p, NPC n, Integer spell) {
        if(n.getID() == 140 || n.getID() == 139) {
            return true;
        }   
        return false;
    }

    @Override
    public void onPlayerMageNpc(Player p, NPC zamorak, Integer spell) {
        if(zamorak.getID() == 140 || zamorak.getID() == 139) {
            applyCurse(p, zamorak);
        }
    }

    @Override
    public boolean blockPlayerRangeNpc(Player p, NPC n) {
        if(n.getID() == 140 || n.getID() == 139) {
            return true;
        }   
        return false;
    }

    @Override
    public void onPlayerRangeNpc(Player p, NPC zamorak) {
        if(zamorak.getID() == 140 || zamorak.getID() == 139) {
            applyCurse(p, zamorak);
        }
    }

    public void applyCurse(Player owner, NPC zam) {
        owner.setBusy(true);
        owner.informOfNPCMessage(new ChatMessage(zam, "A curse be upon you", owner));
        sleep(2200);
        owner.message("You feel slightly weakened");
        if(owner.getCurStat(3) > 10) {
            owner.damage((int) (owner.getCurStat(3) * (double) 0.08D));
        } else {
            owner.damage(1);
        }
        for (int i = 0; i < 3; i++) {
            int stat = owner.getCurStat(i);
            if (stat < 3)
                owner.setCurStat(i, 0);
            else
                owner.setCurStat(i, stat - 3);
        }
        sleep(500);
        zam.setChasing(owner);
        owner.setBusy(false);
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if(n.getID() == 140 || n.getID() == 139) {
            return true;
        }
        return false;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        if(n.getID() == 140 || n.getID() == 139) {
            if(n.getID() == 140) {
                npcTalk(p, n, "Save your speech for the altar");
            } else {
                npcTalk(p, n, "Who are you to dare speak to the servants of Zamorak ?");
            }
            n.setChasing(p);
        }   
    }
}
