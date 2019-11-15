package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.model.Point;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.Mob;
import org.nemotech.rsc.model.landscape.PathGenerator;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.event.impl.FightEvent;
import org.nemotech.rsc.event.impl.RangeEvent;
import org.nemotech.rsc.event.WalkToMobEvent;
import org.nemotech.rsc.model.player.states.Action;
import org.nemotech.rsc.util.Formulae;
import org.nemotech.rsc.external.definition.NPCDef;
import org.nemotech.rsc.client.action.ActionHandler;

public class NPCHandler implements ActionHandler {

    public void handleTalk(int idx) {
        Player player = World.getWorld().getPlayer();
        if (player.isBusy()) {
            player.resetPath();
            return;
        }
        if (System.currentTimeMillis() - player.lastNPCChat < 1500) {
            return;
        }
        player.lastNPCChat = System.currentTimeMillis();
        player.reset();
        final NPC npc = World.getWorld().getNpc(idx);
        if (npc == null) {
            return;
        }

        player.setFollowing(npc);
        player.setStatus(Action.TALKING_MOB);
        World.getWorld().getDelayedEventHandler().add(new WalkToMobEvent(player, npc, 1) {
            public void arrived() {
                owner.resetFollowing();
                owner.resetPath();
                if (owner.isBusy() || owner.isRanging() || !owner.nextTo(npc) || owner.getStatus() != Action.TALKING_MOB) {
                    return;
                }
                owner.reset();
                if (npc.isBusy()) {
                    owner.setSprite(Formulae.getDirection(npc, owner));
                    owner.getSender().sendMessage(npc.getDef().getName() + " is currently busy");
                    return;
                }
                npc.resetPath();

                // The following code moves the player one tile over from the npc if they are on the same tile when talking
                if(owner.withinRange(npc, 0)) {
                    if(!owner.isBlocking(npc, owner.getX(), owner.getY() + 1, 1)) {
                        owner.setLocation(new Point(owner.getX(), owner.getY() + 1), true);
                    } else if(!owner.isBlocking(npc, owner.getX(), owner.getY() - 1, 4)) {
                        owner.setLocation(new Point(owner.getX(), owner.getY() - 1), true);
                    } else if(!owner.isBlocking(npc, owner.getX() + 1, owner.getY(), 8)) {
                        owner.setLocation(new Point(owner.getX() + 1, owner.getY()), true);
                    } else if(!owner.isBlocking(npc, owner.getX() - 1, owner.getY(), 2)) {
                        owner.setLocation(new Point(owner.getX() - 1, owner.getY()), true);
                    }
                }

                if (Formulae.getDirection(owner, npc) != -1) {
                    npc.setSprite(Formulae.getDirection(owner, npc));
                    owner.setSprite(Formulae.getDirection(npc, owner));
                }
                
                /*for(Object o : PluginManager.getInstance().getActionPlugins().get("TalkToNPCListener")) {
                    for(Annotation a : o.getClass().getAnnotations()) {
                        ID id = (ID) a;
                        System.out.println(id.id());
                        if(id.id() == npc.getID()) {
                            if(PluginManager.getInstance().blockDefaultAction("TalkToNPC", new Object[] { owner, npc })) {
                                return;
                            }
                        } else {
                            owner.getSender().sendMessage("The " + npc.getDef().getName() + " doesn't appear interested in talking");
                        }
                    }
                }*/
                
                owner.setInteractingNpc(npc);
                if(PluginManager.getInstance().blockDefaultAction("TalkToNpc", new Object[] { owner, npc })) {
                    return;
                }
                
            }
        });
    }

    public void handleAttack(int idx) {
        Player player = World.getWorld().getPlayer();
        
        if (player.isBusy()) {
            player.resetPath();
            return;
        }
        
        player.reset();
        Mob affectedMob = null;
        
        affectedMob = World.getWorld().getNpc(idx);
        
        if (affectedMob == null || affectedMob.equals(player)) {
            player.resetPath();
            return;
        }
        
        player.setFollowing(affectedMob);
        player.setStatus(Action.ATTACKING_MOB);

        if (player.getRangeEquip() < 0) {
            World.getWorld().getDelayedEventHandler().add(new WalkToMobEvent(player, affectedMob, affectedMob instanceof NPC ? 1 : 2) {
                public void arrived() {
                    owner.resetPath();
                    owner.resetFollowing();
                    //!owner.nextTo(affectedMob)
                    if (owner.isBusy() || affectedMob.isBusy() || !owner.withinRange(affectedMob, affectedMob instanceof NPC ? 1 : 2) || owner.getStatus() != Action.ATTACKING_MOB) {
                        return;
                    }
                    if (affectedMob instanceof NPC) {
                        if (PluginManager.getInstance().blockDefaultAction("PlayerAttackNpc", new Object[] { owner, (NPC)affectedMob })) {
                            return;
                        }
                    }
                    
                    owner.reset();
                    owner.setStatus(Action.FIGHTING_MOB);
                    affectedMob.resetPath();

                    owner.setLocation(affectedMob.getLocation(), true);             
                    
                    for (Player p : owner.getViewArea().getPlayersInView()) {
                        p.removeWatchedPlayer(this.owner);
                    } 

                    owner.setBusy(true);
                    owner.setSprite(9);
                    owner.setOpponent(affectedMob);
                    owner.setCombatTimer();
                    affectedMob.setBusy(true);
                    affectedMob.setSprite(8);
                    affectedMob.setOpponent(owner);
                    affectedMob.setCombatTimer();
                    FightEvent fighting = new FightEvent(owner, affectedMob);
                    fighting.setLastRun(0);
                    World.getWorld().getDelayedEventHandler().add(fighting);
                }
            });
        } else {
            if (!new PathGenerator(player.getX(), player.getY(), affectedMob.getX(), affectedMob.getY()).isValid()) {
                player.getSender().sendMessage("I can't get a clear shot from here");
                player.resetPath();
                player.resetFollowing();
                return;
            }
            
            if(PluginManager.getInstance().blockDefaultAction("PlayerRangeNpc", new Object[] { player, affectedMob })) {
                return;
            }
            
            int radius = 7; // todo add members radius weapons
            
            if (player.getRangeEquip() == 59 || player.getRangeEquip() == 60)
                radius = 5;
            if (player.getRangeEquip() == 189)
                radius = 4;
            
            World.getWorld().getDelayedEventHandler().add(new WalkToMobEvent(player, affectedMob, radius) {
                public void arrived() {
                    owner.resetPath();
                    if (owner.isBusy() || owner.getStatus() != Action.ATTACKING_MOB) {
                        return;
                    }
                    if (!new PathGenerator(owner.getX(), owner.getY(), affectedMob.getX(), affectedMob.getY()).isValid()) {
                        owner.getSender().sendMessage("I can't get a clear shot from here");
                        owner.resetPath();
                        return;
                    }
                    owner.reset();
                    owner.setStatus(Action.RANGING_MOB);
                    if (Formulae.getRangeDirection(owner, affectedMob) != -1) {
                        owner.setSprite(Formulae.getRangeDirection(owner, affectedMob));
                    }
                    owner.setRangeEvent(new RangeEvent(owner, affectedMob));
                }
            });
        }
    }

    public void handleCommand(int idx) {
        Player player = World.getWorld().getPlayer();
        if (player.isBusy()) {
            player.resetPath();
            return;
        }
        final NPC npc = World.getWorld().getNpc(idx);
        if (npc == null) {
            return;
        }

        player.setFollowing(npc);
        World.getWorld().getDelayedEventHandler().add(new WalkToMobEvent(player, npc, 1) {
            public void arrived() {
                owner.resetFollowing();
                owner.resetPath();
                if (owner.isBusy() || owner.isRanging() || !owner.nextTo(npc)) {
                    return;
                }
                owner.reset();
                NPCDef def = npc.getDef();
                String command = def.getCommand();
                npc.resetPath();
                
                if(PluginManager.getInstance().blockDefaultAction("NpcCommand", new Object[] { npc, command, owner })) {
                    return;
                }
                
            }
        });
    }
    
}