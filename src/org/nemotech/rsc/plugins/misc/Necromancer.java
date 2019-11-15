package org.nemotech.rsc.plugins.misc;

import static org.nemotech.rsc.plugins.Plugin.getNearestNpc;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.plugins.listeners.action.PlayerAttackNpcListener;
import org.nemotech.rsc.plugins.listeners.action.PlayerKilledNpcListener;
import org.nemotech.rsc.plugins.listeners.action.PlayerMageNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerAttackNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerKilledNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerMageNpcExecutiveListener;
/**
 * 
 * @author Fate
 * 
 */
public class Necromancer implements PlayerAttackNpcListener, PlayerAttackNpcExecutiveListener, PlayerKilledNpcExecutiveListener, PlayerKilledNpcListener, PlayerMageNpcListener, PlayerMageNpcExecutiveListener {

    @Override
    public boolean blockPlayerAttackNpc(Player p, NPC n) {
        return n.getID() == 358;
    }
    
    private void necromancerFightSpawnMethod(Player p, NPC necromancer) {
        if(necromancer.getID() == 358) {
            NPC zombie = getNearestNpc(p, 359, 10);
            if(!p.getCache().hasKey("necroSpawn") || (p.getCache().hasKey("necroSpawn") && p.getCache().getInt("necroSpawn") < 7) || (p.getCache().hasKey("killedZomb") && p.getCache().getInt("killedZomb") != 0 && zombie == null)) {
                npcTalk(p, necromancer, "I summon the undead to smite you down");
                p.setBusyTimer(3000);
                NPC zombieSpawned = new NPC(359, necromancer.getX(), necromancer.getY());
                World.getWorld().registerNpc(zombieSpawned);
                zombieSpawned.setShouldRespawn(false);
                sleep(1600);
                if(!p.inCombat()) {
                    zombie.startCombat(p);
                }
                if(!p.getCache().hasKey("necroSpawn")) {
                    p.getCache().set("necroSpawn", 1);
                } else {
                    int spawn = p.getCache().getInt("necroSpawn");
                    if(spawn < 7) {
                        p.getCache().set("necroSpawn", spawn + 1);
                    }
                }
                if(!p.getCache().hasKey("killedZomb")) {
                    p.getCache().set("killedZomb", 7);
                } 
            } else if(p.getCache().getInt("necroSpawn") > 6 && p.getCache().hasKey("necroSpawn") && zombie != null && p.getCache().getInt("killedZomb") != 0) { 
                npcTalk(p,zombie, "Raargh");
                p.setBusyTimer(3000);
                zombie.startCombat(p);
            }/* WTF? else if(p.getCache().getInt("killedZomb") == 0 && p.getCache().hasKey("killedZomb")){
                p.attack(necromancer);
            }*/
        }   
    }
    
    private void necromancerOnKilledMethod(Player p, NPC n) {
        if(n.getID() == 358) {
            n.killedBy(p);
            p.getCache().remove("necroSpawn");
            p.getCache().remove("killedZomb");
            NPC newZombie = new NPC(359, p.getX(), p.getY());
            World.getWorld().registerNpc(newZombie);
            newZombie.setShouldRespawn(false);
            newZombie.setChasing(p);
        }
        if(n.getID() == 359) {
            n.killedBy(p);
            if(p.getCache().hasKey("killedZomb") && p.getCache().getInt("killedZomb") != 0) {
                int delete = p.getCache().getInt("killedZomb");
                p.getCache().set("killedZomb", delete - 1);
            }
        }
    }
    

    @Override
    public void onPlayerAttackNpc(Player p, NPC necromancer) {
        necromancerFightSpawnMethod(p, necromancer);
    }

    @Override
    public void onPlayerKilledNpc(Player p, NPC n) {
        necromancerOnKilledMethod(p,  n);
    }

    @Override
    public boolean blockPlayerKilledNpc(Player p, NPC n) {
        if(n.getID() == 358 || n.getID() == 359) {
            return true;
        }
        return false;
    }

    @Override
    public boolean blockPlayerMageNpc(Player p, NPC n, Integer spell) {
        if(n.getID() == 358 || n.getID() == 359) {
            return true;
        }
        return false;
    }

    @Override
    public void onPlayerMageNpc(Player p, NPC necromancer, Integer spell) {
        necromancerFightSpawnMethod(p, necromancer);
    }
}
