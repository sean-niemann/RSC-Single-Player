package org.nemotech.rsc.event.impl;

import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.model.Mob;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.landscape.Path;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.player.states.CombatState;
import org.nemotech.rsc.util.Formulae;
import org.nemotech.rsc.client.sound.SoundEffect;

public final class FightEvent extends DelayedEvent {

    private Mob affectedMob;
    private int firstHit, hits;
    private boolean attacked = false, invincibleMode = false;
    boolean delay = false;
    int dela = 0;

    public FightEvent(Player owner, Mob affectedMob) {
        this(owner, affectedMob, false);
    }

    public FightEvent(Player owner, Mob affectedMob, boolean attacked) {
        super(owner, 1400);
        this.affectedMob = affectedMob;
        firstHit = attacked ? 1 : 0;
        this.attacked = attacked;
        hits = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FightEvent) {
            FightEvent e = (FightEvent) o;
            return e.belongsTo(owner) && e.getAffectedMob().equals(affectedMob);
        }
        return false;
    }

    public Mob getAffectedMob() {
        return affectedMob;
    }

    public void setOpponentInvincible(boolean invincibleMode) {
        this.invincibleMode = invincibleMode;
    }

    @Override
    public void run() {
        if (!owner.isLoggedIn() || (affectedMob instanceof Player && !((Player) affectedMob).isLoggedIn())) {
            owner.resetCombat(CombatState.ERROR);
            affectedMob.resetCombat(CombatState.ERROR);
            return;
        }
        /* Disabled due to overturned
        if (owner.isSleeping() && !owner.isPrayerActivated(12)) {
            owner.getActionSender().sendWakeUp(false, false);
            owner.getActionSender().sendFatigue(owner.getFatigue());
        } */

        Mob attacker, opponent;

        if (hits++ % 2 == firstHit) {
            attacker = owner;
            opponent = affectedMob;
        } else {
            attacker = affectedMob;
            opponent = owner;
        }

        if (attacker instanceof NPC) {
            NPC n = (NPC) attacker;
            if (attacker.getHits() <= 0) {
                n.resetCombat(CombatState.ERROR);
            }
        }
        if (opponent instanceof NPC) {
            NPC n = (NPC) opponent;
            if (opponent.getHits() <= 0) {
                n.resetCombat(CombatState.ERROR);
            }
        }

        attacker.incHitsMade();
        if (attacker instanceof NPC && opponent.isPrayerActivated(12)) {
            return;
        }
        int damage = (attacker instanceof Player && opponent instanceof Player ? Formulae.calcFightHit(attacker, opponent) : Formulae.calcFightHitWithNPC(attacker, opponent));
        if (opponent instanceof NPC && attacker instanceof Player) {
            NPC n = (NPC) opponent;
            Player p = ((Player) attacker);
            n.addCombatDamage(p, damage);
        } else if (opponent instanceof Player && attacker instanceof NPC) {
            NPC n = (NPC) attacker;
            Player p = ((Player) opponent);
            n.addCombatDamage(p, damage);
        }
        if (attacker instanceof NPC && opponent instanceof Player && attacker.getHitsMade() >= (attacked ? 4 : 3)) {
            NPC npc = (NPC) attacker;
            Player player = (Player) opponent;
            if (npc.getCurHits() <= npc.getDef().getHitpoints() * 0.10 && npc.getCurHits() > 0) {
                if (!npc.getLocation().inWilderness() && npc.getDef().isAttackable() && !npc.getDef().isAggressive()) {
                    if (!Util.inArray(Constants.NPCS_THAT_DONT_RETREAT, npc.getID())) {
                        player.getSender().sendSound(SoundEffect.RETREAT);
                        npc.unblock();
                        npc.resetCombat(CombatState.RUNNING);
                        player.resetCombat(CombatState.WAITING);
                        npc.setRan(true);
                        npc.setPath(new Path(attacker.getX(), attacker.getY(), Util.random(npc.getLoc().getMinX(), npc.getLoc().getMaxX()), Util.random(npc.getLoc().getMinY(), npc.getLoc().getMaxY())));
                        player.reset();
                        player.getSender().sendMessage("Your opponent is retreating");
                        return;
                    }
                }
            }
        }

        /*if(attacker instanceof Npc) {
            if(Util.inArray(Formulae.POISONOUS_NPCS, attacker.getID()) && !owner.poisoned){
                if(Util.chance(5.42)){
                    owner.getSender().sendMessage("@gr2@You have been poisoned!");
                    owner.poisoned = true;
                    owner.isPoisoned();
                }
            }
        }*/

        opponent.setLastDamage(damage);
        int newHp = opponent.getHits() - damage;
        opponent.setHits(newHp);

        if (opponent instanceof NPC && newHp > 0) {
            NPC n = (NPC) opponent;
            double max = n.getDef().getHitpoints();
            double cur = n.getHits();
            int percent = (int) ((cur / max) * 100);
            if(PluginManager.getInstance().blockDefaultAction("NpcHealthPercentage", new Object[] { n, percent })) {
                return;
            }
        }

        SoundEffect combatSound = null;
        combatSound = damage > 0 ? SoundEffect.COMBAT_1B : SoundEffect.COMBAT_1A;
        if (opponent instanceof Player) {
            if (attacker instanceof NPC) {
                NPC n = (NPC) attacker;
                if (n.hasArmor) {
                    combatSound = damage > 0 ? SoundEffect.COMBAT_2B : SoundEffect.COMBAT_2A;
                } else if (n.undead) {
                    combatSound = damage > 0 ? SoundEffect.COMBAT_3B : SoundEffect.COMBAT_3A;
                } else {
                    combatSound = damage > 0 ? SoundEffect.COMBAT_1B : SoundEffect.COMBAT_1A;
                }
            }
            Player opponentPlayer = ((Player) opponent);
            opponentPlayer.getSender().sendStat(3);
            opponentPlayer.getSender().sendSound(combatSound);
        }
        if (attacker instanceof Player) {
            if (opponent instanceof NPC) {
                NPC n = (NPC) opponent;
                if (n.hasArmor) {
                    combatSound = damage > 0 ? SoundEffect.COMBAT_2B : SoundEffect.COMBAT_2A;
                } else if (n.undead) {
                    combatSound = damage > 0 ? SoundEffect.COMBAT_3B : SoundEffect.COMBAT_3A;
                } else {
                    combatSound = damage > 0 ? SoundEffect.COMBAT_1B : SoundEffect.COMBAT_1A;
                }
            }
            Player attackerPlayer = (Player) attacker;
            attackerPlayer.getSender().sendSound(combatSound);
        }

        if (newHp <= 0) {
            onDeath(opponent, attacker);
            /*if (opponent instanceof Player) {
                Player p = (Player) opponent;
            }
            if (opponent instanceof Npc) {
                Npc n = (Npc) opponent;

                if(invincibleMode) {
                    n.setHits(n.getDef().getHitpoints());
                    return;
                }
            }
            opponent.killedBy(attacker);

            attacker.resetCombat(CombatState.WON);
            opponent.resetCombat(CombatState.LOST);*/
        } else { // show hits
            owner.informOfModifiedHits(opponent);
        }
    }
    
    protected static void onDeath(Mob killed, Mob killer) {
        if (killer instanceof Player && killed instanceof NPC) {
            if (PluginManager.getInstance().blockDefaultAction("PlayerKilledNpc", new Object[] { ((Player) killer), ((NPC) killed) })) {
                return;
            }
        }
        killed.killedBy(killer);
        killed.resetCombat(CombatState.LOST);
        killer.resetCombat(CombatState.WON);
        //killer.setKillType(0);
    }
    
}