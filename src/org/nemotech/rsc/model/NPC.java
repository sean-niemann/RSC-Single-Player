package org.nemotech.rsc.model;

import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.external.definition.NPCDef;
import org.nemotech.rsc.external.definition.NPCDropDef;
import org.nemotech.rsc.external.location.NPCLoc;
import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.event.impl.FightEvent;
import org.nemotech.rsc.event.WalkMobToMobEvent;
import org.nemotech.rsc.util.Formulae;

import java.util.*;
import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.client.sound.SoundEffect;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.player.states.Action;
import org.nemotech.rsc.model.player.states.CombatState;
import org.nemotech.rsc.model.landscape.ActiveTile;
import org.nemotech.rsc.model.landscape.Path;

public class NPC extends Mob {
    
    public NPC(int id, int x, int y) {
        this(new NPCLoc(id, x, y, x - 5, x + 5, y - 5, y + 5));
    }
    
    public NPC(int id, int x, int y, int radius) {
        this(new NPCLoc(id, x, y, x - radius, x + radius, y - radius, y + radius));
    }
    
    public void initializeTalkScript(final Player p) {
        //p.setBusyTimer(600);
        PluginManager.getInstance().blockDefaultAction("TalkToNpc", new Object[] { p, this });
    }
    
    public void teleport(int x, int y) {
        setLocation(Point.getLocation(x, y), true);
    }

    public void startCombat(final Player owner) {
        resetPath();
        setChasing(owner);
        World.getWorld().getDelayedEventHandler().add(new WalkMobToMobEvent(this, owner, 1) {
            public void arrived() {
                if (affectedMob.isBusy() || owner.isBusy()) {
                    setChasing(null);
                    return;
                }
                if(affectedMob instanceof Player && owner instanceof NPC) {
                    Player player = (Player)affectedMob;

                    if(player.isSleeping() && player.isPrayerActivated(12)) { // if player is sleeping and has the prayer activated, skip him
                        setChasing(null);
                        resetPath();
                        return;
                    }

                    NPC npc = (NPC)owner;
                    player.resetPath();
                    player.reset();
                    player.setStatus(Action.FIGHTING_MOB);
                    player.getSender().sendSound(SoundEffect.UNDER_ATTACK);
                    player.getSender().sendMessage("You are under attack!");

                    if (player.isSleeping()) {
                        //player.getSender().sendWakeUp(false, false);
                        player.getSender().sendFatigue(player.getFatigue());
                    }

                    setLocation(player.getLocation(), true);

                    for (Player p : getViewArea().getPlayersInView()) {
                        p.removeWatchedNpc(npc);
                    }

                    player.setBusy(true);
                    player.setSprite(9);
                    player.setOpponent(owner);
                    player.setCombatTimer();

                    setBusy(true);
                    setSprite(8);
                    setOpponent(player);
                    setCombatTimer();
                    FightEvent fighting = new FightEvent(player, owner, true);
                    fighting.setLastRun(0);
                    World.getWorld().getDelayedEventHandler().add(fighting);
                    resetPath();
                }

            }
        });


    }

    public void weakenDefense(int offset) {
        super.setCombatLevel(Formulae.getCombatLevel(def.getAttack(), (def.getDefense() - offset), def.getStrength(), def.getHitpoints(), 0, 0, 0));
    }

    public void weakenStrength(int offset) {
        super.setCombatLevel(Formulae.getCombatLevel(def.getAttack(), def.getDefense(), (def.getStrength() - offset), def.getHitpoints(), 0, 0, 0));
    }

    public void weakenAttack(int offset) {
        super.setCombatLevel(Formulae.getCombatLevel((def.getAttack() - offset), def.getDefense(), def.getStrength(), def.getHitpoints(), 0, 0, 0));
    }

    private int stage = 1;

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public boolean isHasArmor() {
        return hasArmor;
    }

    public void setHasArmor(boolean hasArmor) {
        this.hasArmor = hasArmor;
    }

    public boolean isUndead() {
        return undead;
    }

    public void setUndead(boolean undead) {
        this.undead = undead;
    }

    public boolean isRan() {
        return ran;
    }
    private boolean ran = false;
    /**
     * The identifier for the NPC block event
     */
    private static final int BLOCKED_IDENTIFIER = 69;
    /**
     * World instance
     */
    private static final World world = World.getWorld();
    /**
     * The player currently blocking this npc
     */
    private Player blocker = null;
    /**
     * DelayedEvent used for unblocking an npc after set time
     */
    private DelayedEvent chaseTimeout = null;
    /**
     * Player (if any) that this npc is chasing
     */
    private Player chasing = null;
    public boolean confused = false;
    /**
     * The npcs hitpoints
     */
    private int curHits;
    public boolean cursed = false;
    /**
     * The definition of this npc
     */
    private NPCDef def;
    private boolean goingToAttack = false;
    /**
     * The location of this npc
     */
    private NPCLoc loc;

    public boolean hasRan() {
        return ran;
    }

    public void setRan(boolean ran) {
        this.ran = ran;
    }

    public Player getBlocker() {
        return blocker;
    }

    public void setBlocker(Player blocker) {
        this.blocker = blocker;
    }

    public DelayedEvent getChaseTimeout() {
        return chaseTimeout;
    }

    public void setChaseTimeout(DelayedEvent chaseTimeout) {
        this.chaseTimeout = chaseTimeout;
    }

    public boolean isConfused() {
        return confused;
    }

    public void setConfused(boolean confused) {
        this.confused = confused;
    }

    public int getCurHits() {
        return curHits;
    }

    public void setCurHits(int curHits) {
        this.curHits = curHits;
    }

    public boolean isCursed() {
        return cursed;
    }

    public void setCursed(boolean cursed) {
        this.cursed = cursed;
    }

    public boolean isGoingToAttack() {
        return goingToAttack;
    }

    public void setGoingToAttack(boolean goingToAttack) {
        this.goingToAttack = goingToAttack;
    }

    public boolean isShouldRespawn() {
        return shouldRespawn;
    }

    public void setShouldRespawn(boolean shouldRespawn) {
        this.shouldRespawn = shouldRespawn;
    }

    public DelayedEvent getTimeout() {
        return timeout;
    }

    public void setTimeout(DelayedEvent timeout) {
        this.timeout = timeout;
    }

    public boolean isWeakend() {
        return weakened;
    }

    public void setWeakend(boolean weakend) {
        this.weakened = weakend;
    }

    public void setDef(NPCDef def) {
        this.def = def;
    }

    public void setLoc(NPCLoc loc) {
        this.loc = loc;
    }
    /**
     * Should this npc respawn once it has been killed?
     **/
    private boolean shouldRespawn = true;
    /**
     * DelayedEvent used for unblocking an npc after set time
     */
    private DelayedEvent timeout = null;

    public boolean weakened = false;

    public boolean special = false;

    public int itemid = -1;

    public int exp = -1; // used for events.

    public NPC(int id, int startX, int startY, int minX, int maxX, int minY, int maxY) {
        this(new NPCLoc(id, startX, startY, minX, maxX, minY, maxY));
    }

    public NPC(NPCLoc loc) {
        for (int i : Constants.UNDEAD_NPCS) {
            if (loc.getID() == i) {
                this.undead = true;
            }
        }
        for (int i : Constants.ARMOR_NPCS) {
            if (loc.getID() == i) {
                this.hasArmor = true;
            }
        }

        def = EntityManager.getNPC(loc.getID());
        curHits = def.getHitpoints();

        this.loc = loc;
        super.setID(loc.getID());
        super.setLocation(new Point(loc.getStartX(), loc.getStartY()), true);
        super.setCombatLevel(Formulae.getCombatLevel(def.getAttack(), def.getDefense(), def.getStrength(), def.getHitpoints(), 0, 0, 0));
        if (this.loc.getID() == 189 || this.loc.getID() == 53) {
            this.def.aggressive = 1;
        }
    }

    public synchronized void blockedBy(Player player) {
        blocker = player;
        player.setInteractingNpc(this);
        setBusy(true);
        boolean eventExists = false;

        if (timeout != null) {
            ArrayList<DelayedEvent> events = World.getWorld().getDelayedEventHandler().getEvents();

            // gettin threading problems here without it synced.
            try {
                synchronized (events) {
                    for (DelayedEvent e : events) {
                        if (e.is(timeout)) {
                            e.updateLastRun(); // If the event still exists, reset its delay time.
                            eventExists = true;
                        }
                    }
                    notifyAll();
                }
            } catch (ConcurrentModificationException cme) {

            }
        }

        if (eventExists) {
            return;
        }

        timeout = new DelayedEvent(player, 200000) {

            public Object getIdentifier() {
                return BLOCKED_IDENTIFIER;
            }

            public void run() {
                unblock();
                running = false;
            }
        };

        //World.getWorld().getDelayedEventHandler().add(timeout);
    }

    private Player findVictim() {
        if (goingToAttack) {
            return null;
        }
        if (hasRan()) {
            return null;
        }
        long now = System.currentTimeMillis();
        if (getChasing() != null) {
            return null;
        }
        ActiveTile[][] tiles = getViewArea().getViewedArea(2, 2, 2, 2);
        for (ActiveTile[] tile : tiles) {
            for (ActiveTile t : tile) {
                if (t != null) {
                    for (Player p : t.getPlayers()) {
                        if (p.inCombat()) {
                            continue;
                        }
                        if (p.isBusy() || now - p.getCombatTimer() < (p.getCombatState() == CombatState.RUNNING || p.getCombatState() == CombatState.WAITING ? 3000 : 1500) || !p.nextTo(this) || !p.getLocation().inBounds(loc.getMinX() - 4, loc.getMinY() - 4, loc.getMaxX() + 4, loc.getMaxY() + 4)) {
                            continue;
                        }
                        if(!(p.isBusy() || now - p.getCombatTimer() < (p.getCombatState() == CombatState.RUNNING || p.getCombatState() == CombatState.WAITING ? 3000 : 1500) || !p.nextTo(this))) {
                            if (this.getLocation().inWilderness()) {
                                return p;
                            }
                            if(p.getCombatLevel() <= ((this.getCombatLevel() * 2) + 1)) {
                                return p;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getArmourPoints() {
        return 1;
    }

    @Override
    public int getAttack() {
        return def.getAttack();
    }

    public Player getChasing() {
        return chasing;
    }

    @Override
    public int getCombatStyle() {
        return 0;
    }

    public NPCDef getDef() {
        return EntityManager.getNPC(getID());
    }

    @Override
    public int getDefense() {
        return def.getDefense();
    }

    @Override
    public int getHits() {
        return curHits;
    }

    public NPCLoc getLoc() {
        return loc;
    }

    @Override
    public int getStrength() {
        return def.getStrength();
    }

    @Override
    public int getWeaponAimPoints() {
        return 1;
    }

    @Override
    public int getWeaponPowerPoints() {
        return 1;
    }

    @Override
    public void killedBy(Mob mob) {
        //this.cure();
        Player owner = mob instanceof Player ? (Player) mob : null;
        if (owner != null) {
            owner.getSender().sendSound(SoundEffect.VICTORY);
            
            Mob opponent = super.getOpponent();
            if (opponent != null) {
                opponent.resetCombat(CombatState.WON);
            }

            owner = handleLootAndXpDistribution((Player) mob);
            
            NPCDropDef[] drops = EntityManager.getNPCDropsForID(id);

            int total = 0;
            for (NPCDropDef drop : drops) {
                if (drop == null) {
                    continue;
                }
                total += drop.getWeight();
            }
            //
            int hit = Util.random(0, total);
            total = 0;
            
            for (NPCDropDef drop : drops) {
                if (drop == null) {
                    continue;
                }
                if (drop.getWeight() == 0 && drop.getID() != -1) {
                    Item groundItem = new Item(drop.getID(), getX(), getY(), drop.getAmount(), owner);
                    groundItem.setAttribute("npcdrop", true);
                    world.registerItem(groundItem);
                    continue;
                }
                if (hit >= total && hit < (total + drop.getWeight())) {

                    if (drop.getID() != -1) {
                        if (EntityManager.getItem(drop.getID()).isMembers() && !Constants.MEMBER_WORLD) {
                            continue;
                        }
                        if (!EntityManager.getItem(drop.getID()).isStackable()) {
                            Item groundItem = new Item(drop.getID(), getX(), getY(), 1, owner);
                            groundItem.setAttribute("npcdrop", true);
                            for (int count = 0; count < drop.getAmount(); count++)
                                world.registerItem(groundItem);
                        } else {
                            int amount = drop.getAmount();
                            Item groundItem = new Item(drop.getID(), getX(), getY(), amount, owner);
                            groundItem.setAttribute("npcdrop", true);

                            world.registerItem(groundItem);
                        }
                    }
                }
                total += drop.getWeight();
            }
        }
        resetCombat(CombatState.LOST);
        //world.unregisterNpc(this);
        remove();
        /*if (mob instanceof Player) {
            Player player = (Player) mob;
            player.getSender().sendSound(SoundEffect.VICTORY);
        }

        Mob opponent = super.getOpponent();
        if (opponent != null) {
            opponent.resetCombat(CombatState.WON);
        }

        resetCombat(CombatState.LOST);
        world.unregisterNpc(this);
        remove();

        Player owner = null;
        if(mob instanceof Player) {
            owner = handleLootAndXpDistribution((Player) mob);
            if(PluginManager.getInstance().blockDefaultAction("PlayerKilledNpc", new Object[]{ owner, this })) {
                return;
            }
        }

        ItemDropDef[] drops = def.getDrops();

        int total = 0;
        for (ItemDropDef drop : drops) {
            total += drop.getWeight();
        }

        int hit = Util.random(0, total);
        total = 0;
        for (ItemDropDef drop : drops) {
            if (drop == null) {
                continue;
            }
            if (drop.getWeight() == 0) {
                world.registerItem(new Item(drop.getID(), getX(), getY(), drop.getAmount(), owner));
                continue;
            }
            if (hit >= total && hit < (total + drop.getWeight())) {
                if (drop.getID() != -1) {
                    if(new InvItem(drop.getID()).getDef().isMembers() && !Config.MEMBER_WORLD) {
                        continue;
                    } else {
                        world.registerItem(new Item(drop.getID(), getX(), getY(), drop.getAmount(), owner));
                        break;
                    }
                }
            }
            total += drop.getWeight();
        }*/
    }


    @Override
    public void remove() {
        if (!removed && shouldRespawn && def.getRespawnTime() > 0) {
            teleport(0, 0);
            World.getWorld().getDelayedEventHandler().add(new DelayedEvent(null, def.getRespawnTime() * 1000) {
                @Override
                public void run() {
                    setRespawning(false);
                    teleport(loc.startX, loc.startY);
                    setHits(def.getHitpoints());
                    running = false;
                }
            });
        setRespawning(true);
        } else if (!shouldRespawn) {
            teleport(0, 0);
            world.unregisterNpc(this);
            //removed = true;
        }
    }
    
    private boolean isRespawning = false;
    
    public boolean isRespawning() {
        return isRespawning;
    }

    public void setRespawning(boolean isRespawning) {
        this.isRespawning = isRespawning;
    }

    public void setChasing(Player player) {

        this.chasing = player;
        goingToAttack = true;

        if (player == null) {
            this.chasing = null;
            goingToAttack = false;
            return;
        }

        chaseTimeout = new DelayedEvent(null, 15000) {
            @Override
            public void run() {
                goingToAttack = false;
                setChasing(null);
                running = false;
            }
        };

        World.getWorld().getDelayedEventHandler().add(chaseTimeout);
    }

    @Override
    public void setHits(int lvl) {
        if (lvl <= 0) {
            lvl = 0;
        }

        curHits = lvl;
    }

    public void setRespawn(boolean respawn) {
        shouldRespawn = respawn;
    }

    public void unblock() {
        if (blocker != null) {
            blocker.setInteractingNpc(null);
            blocker = null;
        }

        if (timeout == null) {
            return;
        }

        goingToAttack = false;
        setBusy(false);
        timeout.interrupt();
        timeout = null;
    }

    @Override
    public void updatePosition() {
        long now = System.currentTimeMillis();
        Player victim = findVictim();
        if (!isBusy() && def.isAggressive() && now - getCombatTimer() > 3000 && victim != null) {
            resetPath();
            victim.resetPath();
            victim.reset();
            victim.setStatus(Action.FIGHTING_MOB);
            victim.getSender().sendSound(SoundEffect.UNDER_ATTACK);
            victim.getSender().sendMessage("You are under attack!");

            setLocation(victim.getLocation(), true);
            for (Player p : getViewArea().getPlayersInView()) {
                p.removeWatchedNpc(this);
            }

            victim.setBusy(true);
            victim.setSprite(9);
            victim.setOpponent(this);
            victim.setCombatTimer();

            setBusy(true);
            setSprite(8);
            setOpponent(victim);
            setCombatTimer();
            FightEvent fighting = new FightEvent(victim, this, true);
            fighting.setLastRun(0);
            World.getWorld().getDelayedEventHandler().add(fighting);
        }

        if (now - lastMovement > 2200) {
            lastMovement = now;
            int rand = Util.random(0, 1);
            if (!isBusy() && finishedPath() && rand == 1 && !this.isRemoved()) {
                int newX = Util.random(loc.getMinX(), loc.getMaxX());
                int newY = Util.random(loc.getMinY(), loc.getMaxY());
                super.setPath(new Path(getX(), getY(), newX, newY));
            }
        }

        super.updatePosition();
    }

    @Override
    public String toString() {
        return "[NPC:" + EntityManager.getNPC(id).getName() + "]";
    }

    public boolean hasArmor = false;
    public boolean undead = false;
    private int team = 2;
    public int getTeam() {
        return team;
    }
    public void setTeam(int team) {
        this.team = team;
    }

    /**
     * Holds players that did damage with combat
     */
    private Map<Long, Integer> combatDamagers = new HashMap<Long, Integer>();
    /**
     * Holds players that did damage with range
     */
    private Map<Long, Integer> rangeDamagers = new HashMap<Long, Integer>();
    /**
     * Holds players that did damage with mage
     */
    private Map<Long, Integer> mageDamagers = new HashMap<Long, Integer>();

    /**
     * Combat damage done by player p
     * @param p
     * @return
     */
    public Integer getCombatDamageDoneBy(Player p) {
        if(p == null) {
            return 0;
        }
        if(!combatDamagers.containsKey(p.getUsernameHash())) {
            return 0;
        }
        int dmgDone = combatDamagers.get(p.getUsernameHash());
        return (dmgDone > this.getDef().getHitpoints() ? this.getDef().getHitpoints() : dmgDone);
    }
    /**
     * Range damage done by player p
     * @param p
     * @return
     */
    public Integer getRangeDamageDoneBy(Player p) {
        if(p == null) {
            return 0;
        }
        if(!rangeDamagers.containsKey(p.getUsernameHash())) {
            return 0;
        }
        int dmgDone = rangeDamagers.get(p.getUsernameHash());
        return (dmgDone > this.getDef().getHitpoints() ? this.getDef().getHitpoints() : dmgDone);
    }
    /**
     * Mage damage done by player p
     * @param p
     * @return
     */
    public Integer getMageDamageDoneBy(Player p) {
        if(p == null) {
            return 0;
        }
        if(!mageDamagers.containsKey(p.getUsernameHash())) {
            return 0;
        }
        int dmgDone = mageDamagers.get(p.getUsernameHash());
        return (dmgDone > this.getDef().getHitpoints() ? this.getDef().getHitpoints() : dmgDone);
    }
    /**
     * Iterates over combatDamagers map and returns the keys
     * @return
     */
    public ArrayList<Long> getCombatDamagers() {
        return new ArrayList<Long>(combatDamagers.keySet());
    }
    /**
     * Iterates over rangeDamagers map and returns the keys
     * @return
     */
    public ArrayList<Long> getRangeDamagers() {
        return new ArrayList<Long>(rangeDamagers.keySet());
    }
    /**
     * Iterates over mageDamagers map and returns the keys
     * @return
     */
    public ArrayList<Long> getMageDamagers() {
        return new ArrayList<Long>(mageDamagers.keySet());
    }
    /**
     * Adds combat damage done by a player
     * @param p
     * @param damage
     */
    public void addCombatDamage(Player p, int damage) {
        if(combatDamagers.containsKey(p.getUsernameHash())) {
            combatDamagers.put(p.getUsernameHash(), combatDamagers.get(p.getUsernameHash()) + damage);
        }
        else {
            combatDamagers.put(p.getUsernameHash(), damage);
        }
    }
    /**
     * Adds range damage done by a player
     * @param p
     * @param damage
     */
    public void addRangeDamage(Player p, int damage) {
        if(rangeDamagers.containsKey(p.getUsernameHash())) {
            rangeDamagers.put(p.getUsernameHash(), rangeDamagers.get(p.getUsernameHash()) + damage);
        }
        else {
            rangeDamagers.put(p.getUsernameHash(), damage);
        }
    }
    /**
     * Adds mage damage done by a player
     * @param p
     * @param damage
     */
    public void addMageDamage(Player p, int damage) {
        if(mageDamagers.containsKey(p.getUsernameHash())) {
            mageDamagers.put(p.getUsernameHash(), mageDamagers.get(p.getUsernameHash()) + damage);
        }
        else {
            mageDamagers.put(p.getUsernameHash(), damage);
        }
    }

    /**
     * Distributes the XP from this monster and the loot
     * @param attacker the person that "finished off" the npc
     * @return the player who did the most damage / should get the loot
     */
    public Player handleLootAndXpDistribution(Player attacker) {
        Player toLoot = attacker;
        int mostDamageDone = 0;
        int exp = Util.roundUp(Formulae.combatExperience(this) / 4D);

        for (Long playerHash : getCombatDamagers()) {
            int newXP = 0;
            Player p = World.getWorld().getPlayer(playerHash);
            if(p == null)
                continue;
            int dmgDoneByPlayer = getCombatDamageDoneBy(p);


            if (dmgDoneByPlayer > mostDamageDone) {
                toLoot = p;
                mostDamageDone = dmgDoneByPlayer;
            }

            newXP = (exp * dmgDoneByPlayer) / this.getDef().getHitpoints();
            switch (p.getCombatStyle()) {
                case 0:
                    for (int x = 0; x < 3; x++) {
                        p.incExp(x, newXP, true);
                        p.getSender().sendStat(x);
                    }
                    break;
                case 1:
                    p.incExp(2, newXP * 3, true);
                    p.getSender().sendStat(2);
                    break;
                case 2:
                    p.incExp(0, newXP * 3, true);
                    p.getSender().sendStat(0);
                    break;
                case 3:
                    p.incExp(1, newXP * 3, true);
                    p.getSender().sendStat(1);
                    break;
            }
            p.incExp(3, newXP, true);
            p.getSender().sendStat(3);
        }
        for (Long playerHash : getRangeDamagers()) {

            int newXP = 0;
            Player p = World.getWorld().getPlayer();
            int dmgDoneByPlayer = getRangeDamageDoneBy(p);
            if(p == null)
                continue;

            if (dmgDoneByPlayer > mostDamageDone) {
                toLoot = p;
                mostDamageDone = dmgDoneByPlayer;
            }
            newXP = (exp * dmgDoneByPlayer) / this.getDef().getHitpoints();
            p.incExp(4, newXP * 4, true);
            p.getSender().sendStat(4);
        }
        for (Long playerHash : getMageDamagers()) {

            Player p = World.getWorld().getPlayer(playerHash);

            int dmgDoneByPlayer = getMageDamageDoneBy(p);
            if(p == null)
                continue;

            if (dmgDoneByPlayer > mostDamageDone) {
                toLoot = p;
                mostDamageDone = dmgDoneByPlayer;
            }
        }
        return toLoot;
    }

}