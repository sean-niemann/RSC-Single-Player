package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.event.impl.FightEvent;
import org.nemotech.rsc.event.MiniEvent;
import org.nemotech.rsc.event.WalkMobToMobEvent;
import org.nemotech.rsc.model.Mob;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.landscape.Path;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.player.states.Action;
import org.nemotech.rsc.model.player.states.CombatState;
import org.nemotech.rsc.client.sound.SoundEffect;
import org.nemotech.rsc.client.action.ActionHandler;

public class WalkHandler implements ActionHandler {

    public void handleWalk(int x, int y, byte[] xOffsets, byte[] yOffsets, boolean walkToAction) {
        Player player = World.getWorld().getPlayer();
        if (player.inCombat()) {
            if (!walkToAction) {
                Mob opponent = player.getOpponent();
                if (opponent == null) { // This shouldn't happen
                    return;
                }
                if (opponent.getHitsMade() >= 3) {
                    player.setLastRun(System.currentTimeMillis());
                    player.resetCombat(CombatState.RUNNING);
                    
                    if (opponent instanceof NPC) {
                        NPC n = (NPC) opponent;
                        n.unblock();
                        opponent.resetCombat(CombatState.WAITING);
                        if (n.getDef().isAggressive() || n.getLocation().inWilderness()) {
                            player.lastNpcChasingYou = n;
                            World.getWorld().getDelayedEventHandler().add(new MiniEvent(player, 2000) {
                                @Override
                                public void action() {
                                    final NPC npc = owner.lastNpcChasingYou;
                                    owner.lastNpcChasingYou = null;
                                    if (npc.isBusy() || npc.getChasing() != null)
                                        return;

                                    npc.resetPath();
                                    npc.setChasing(owner);

                                    World.getWorld().getDelayedEventHandler().add(new WalkMobToMobEvent(npc, owner, 0) {
                                        @Override
                                        public void arrived() {
                                            if (affectedMob.isBusy() || owner.isBusy()) {
                                                npc.setChasing(null);
                                                return;
                                            }
                                            if (affectedMob.inCombat() || owner.inCombat()) {
                                                npc.setChasing(null);
                                                return;
                                            }
                                            Player player = (Player) affectedMob;
                                            player.resetPath();
                                            player.setBusy(true);
                                            npc.resetPath();
                                            player.reset();
                                            player.setStatus(Action.FIGHTING_MOB);
                                            player.getSender().sendSound(SoundEffect.UNDER_ATTACK);
                                            player.getSender().sendMessage("You are under attack!");

                                            npc.setLocation(player.getLocation(), true);
                                            for (Player p : npc.getViewArea().getPlayersInView())
                                                p.removeWatchedNpc(npc);
                                            player.setBusy(true);
                                            player.setSprite(9);
                                            player.setOpponent(npc);
                                            player.setCombatTimer();

                                            npc.setBusy(true);
                                            npc.setSprite(8);
                                            npc.setOpponent(player);
                                            npc.setCombatTimer();
                                            npc.setChasing(null);
                                            FightEvent fighting = new FightEvent(player, npc, true);
                                            fighting.setLastRun(0);
                                            world.getDelayedEventHandler().add(fighting);
                                        }

                                        @Override
                                        public void failed() {
                                            npc.setChasing(null);
                                        }
                                    });
                                }
                            });
                        }
                    } else {
                        opponent.resetCombat(CombatState.WAITING);
                    }
                } else {
                    player.getSender().sendMessage("You cannot retreat in the first 3 rounds of battle");
                    return;
                }
            } else {
                return;
            }
        }

        if(player.isBusy()) {
            return;
        }
        player.reset();

        Path path = new Path(x, y, xOffsets, yOffsets);
        player.setStatus(Action.IDLE);
        player.setPath(path);
    }
    
}