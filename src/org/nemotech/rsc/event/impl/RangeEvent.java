package org.nemotech.rsc.event.impl;

import java.util.ArrayList;

import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.event.WalkMobToMobEvent;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.Mob;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.Projectile;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.landscape.PathGenerator;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.player.states.Action;
import org.nemotech.rsc.util.Formulae;
import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.client.sound.SoundEffect;

public class RangeEvent extends DelayedEvent {
    
    private Mob affectedMob;

    public int[][] allowedArrows = {
        { 189, 11, 638, 639 }, // Shortbow
        { 188, 11, 638, 639 }, // Longbow
        { 649, 11, 638, 639 }, // Oak Shortbow
        { 648, 11, 638, 639, 640, 641 }, // Oak Longbow
        { 651, 11, 638, 639, 640, 641 }, // Willow Shortbow
        { 650, 11, 638, 639, 640, 641, 642, 643 }, // Willow Longbow
        { 653, 11, 638, 639, 640, 641, 642, 643 }, // Maple Shortbow
        { 652, 11, 638, 639, 640, 641, 642, 643, 644, 645 }, // Maple Longbow
        { 655, 11, 638, 639, 640, 641, 642, 643, 644, 645, 723 }, // Yew Shortbow
        { 654, 11, 638, 639, 640, 641, 642, 643, 644, 645, 646, 647, 723 }, // Yew Longbow
        { 657, 11, 638, 639, 640, 641, 642, 643, 644, 645, 646, 647, 723 }, // Magic Shortbow
        { 656, 11, 638, 639, 640, 641, 642, 643, 644, 645, 646, 647, 723 } // Magic Longbow
    };

    public RangeEvent(Player owner, Mob victim) {
        super(owner, 2000);
        this.affectedMob = victim;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RangeEvent) {
            RangeEvent e = (RangeEvent) o;
            return e.belongsTo(owner);
        }
        return false;
    }

    public Mob getTarget() {
        return affectedMob;
    }

    private Item getArrows(int id) {
        return affectedMob.getViewArea().getGroundItem(id, affectedMob.getLocation());
    }

    @Override
    public void run() {
        int bowID = owner.getRangeEquip();
        if (!owner.isLoggedIn() || (affectedMob instanceof Player && !((Player) affectedMob).isLoggedIn()) || affectedMob.getHits() <= 0 || bowID < 0) {
            owner.resetRange();
            return;
        }
        if (owner.withinRange(affectedMob, 5)) { // within 5 squares
            if (owner.isFollowing()) {
                owner.resetFollowing();
            }
            if (!owner.finishedPath()) {
                owner.resetPath();
            }
        } else { // start
            owner.setFollowing(affectedMob);
            return;
        }
        if (!new PathGenerator(owner.getX(), owner.getY(), affectedMob.getX(), affectedMob.getY()).isValid()) {
            owner.getSender().sendMessage("I can't get a clear shot from here");
            owner.resetPath();
            owner.resetFollowing();
            return;
        }
        boolean xbow = Util.inArray(Formulae.CROSSBOWS, bowID);
        int arrowID = -1;
        for (int aID : (xbow ? Formulae.BOLTS : Formulae.ARROWS)) {
            int slot = owner.getInventory().getLastIndexById(aID);
            if (slot < 0) {
                continue;
            }
            InvItem arrow = owner.getInventory().get(slot);
            if (arrow == null) { // This shouldn't happen
                continue;
            }
            arrowID = aID;

            int newAmount = arrow.getAmount() - 1;
            if (!xbow && arrowID > 0) {
                int temp = -1;

                for (int i = 0; i < allowedArrows.length; i++)
                    if (allowedArrows[i][0] == owner.getRangeEquip())
                        temp = i;

                boolean canFire = false;
                for (int i = 0; i < allowedArrows[temp].length; i++)
                    if (allowedArrows[temp][i] == aID)
                        canFire = true;

                if (!canFire) {
                    owner.getSender().sendMessage("Your arrows are too powerful for your bow");
                            owner.resetRange();
                            return;
                }
            }

            if (newAmount <= 0) {
                owner.getInventory().remove(slot);
                owner.getSender().sendInventory();
            } else {
                arrow.setAmount(newAmount);
                owner.getSender().sendUpdateItem(slot);
            }
            break;
        }
        if (arrowID < 0) {
            owner.getSender().sendMessage("You have run out of " + (xbow ? "bolts" : "arrows"));
            owner.getSender().sendSound(SoundEffect.OUT_OF_AMMO);
            owner.resetRange();
            return;
        }

        int damage = Formulae.calcRangeHit(owner.getCurStat(4), owner.getRangePoints(), affectedMob.getArmourPoints(), arrowID);
        
        NPC npc = (NPC) affectedMob;
        if (damage > 1 && npc.getID() == 477)
            damage = damage / 2;
        if (npc.getID() == 196) {
            owner.message("The dragon breathes fire at you");
            int maxHit = 65;
            if (owner.getInventory().wielding(420)) {
                maxHit = 10;
                owner.message("Your shield prevents some of the damage from the flames");
            }
            owner.damage(Util.random(0, maxHit));
        }
        if (!Formulae.looseArrow(damage)) {
            Item arrows = getArrows(arrowID);
            if (arrows == null) {
                World.getWorld().registerItem(new Item(arrowID, affectedMob.getX(), affectedMob.getY(), 1, owner));
            } else {
                arrows.setAmount(arrows.getAmount() + 1);
            }
        }
        npc.addRangeDamage(owner, damage);
        Projectile projectile = new Projectile(owner, affectedMob, 2);

        ArrayList<Player> playersToInform = new ArrayList<>();
        playersToInform.addAll(Util.newArrayList(owner.getViewArea().getPlayersInView()));
        playersToInform.addAll(Util.newArrayList(affectedMob.getViewArea().getPlayersInView()));
        
        for (Player p : playersToInform) {
            p.informOfProjectile(projectile);
        }

        if (System.currentTimeMillis() - affectedMob.lastTimeShot > 500) {
            affectedMob.lastTimeShot = System.currentTimeMillis();
            affectedMob.setLastDamage(damage);
            int newHp = affectedMob.getHits() - damage;
            affectedMob.setHits(newHp);
            
            if (affectedMob instanceof NPC && newHp > 0) {
                NPC n = (NPC) affectedMob;
                double max = n.getDef().hits;
                double cur = n.getHits();
                int percent = (int) ((cur / max) * 100);
                /*if(PluginManager.getInstance().blockDefaultAction("NpcHealthPercentage", new Object[] { n, percent })) {
                    return;
                }*/
            }
            
            for (Player p : playersToInform) {
                p.informOfModifiedHits(affectedMob);
            }
            if (affectedMob instanceof Player) {
                Player affectedPlayer = (Player) affectedMob;
                affectedPlayer.getSender().sendStat(3);
            }
            owner.getSender().sendSound(SoundEffect.SHOOT);
            owner.setArrowFired();
            if (newHp <= 0) {
                affectedMob.killedBy(owner);
                owner.resetRange();
            } else {
                if (owner instanceof Player && affectedMob instanceof NPC) {
                    final Player player = (Player) owner;

                    if (npc.isBusy() || npc.getChasing() != null)
                        return;

                    npc.resetPath();
                    npc.setChasing(player);

                    // Radius is 0 to prevent wallhacking by NPCs. Easiest method I can come up with for now.
                    World.getWorld().getDelayedEventHandler().add(new WalkMobToMobEvent(affectedMob, owner, 0) {
                        @Override
                        public void arrived() {
                            if (affectedMob.isBusy() || player.isBusy()) {
                                npc.setChasing(null);
                                return;
                            }

                            npc.resetPath();
                            player.setBusy(true);
                            player.resetPath();
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
                            World.getWorld().getDelayedEventHandler().add(fighting);
                        }

                        @Override
                        public void failed() {
                            npc.setChasing(null);
                        }
                    });
                }
            }
        }
    }
    
}