package org.nemotech.rsc.model;

import java.util.Random;

import org.nemotech.rsc.model.landscape.Path;
import org.nemotech.rsc.model.landscape.PathHandler;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.event.impl.FightEvent;
import org.nemotech.rsc.model.player.states.Action;
import org.nemotech.rsc.model.player.states.CombatState;
import org.nemotech.rsc.util.Formulae;
import org.nemotech.rsc.util.CollisionFlags;

public abstract class Mob extends Entity {
    
    public final boolean atObject(GameObject o) {
        Point[] boundaries = o.getObjectBoundary();
        Point low = boundaries[0];
        Point high = boundaries[1];
        if (o.getType() == 0) {
            if (o.getGameObjectDef().getType() == 2 || o.getGameObjectDef().getType() == 3) {
                if (getX() >= low.getX() && getX() <= high.getX() && getY() >= low.getY() && getY() <= high.getY()) {
                    return true;
                }
            } else {
                return canReach(low.getX(), high.getX(), low.getY(), high.getY());
            }
        } else if (o.getType() == 1) {
            if (getX() >= low.getX() && getX() <= high.getX() && getY() >= low.getY() && getY() <= high.getY()) {
                return true;
            }
        }
        return false;
    }

    private boolean canReach(int minX, int maxX, int minY, int maxY) {
        if (getX() >= minX && getX() <= maxX && getY() >= minY && getY() <= maxY) {
            return true;
        }
        if (minX <= getX() - 1 && maxX >= getX() - 1 && minY <= getY() && maxY >= getY()
                && (World.getWorld().getTileValue(getX() - 1, getY()).objectValue & CollisionFlags.WALL_WEST) == 0) {
            return true;
        }
        if (1 + getX() >= minX && getX() + 1 <= maxX && getY() >= minY && maxY >= getY()
                && (CollisionFlags.WALL_EAST & World.getWorld().getTileValue(getX() + 1, getY()).objectValue) == 0) {
            return true;
        }
        if (minX <= getX() && maxX >= getX() && getY() - 1 >= minY && maxY >= getY() - 1
                && (CollisionFlags.WALL_SOUTH & World.getWorld().getTileValue(getX(), getY() - 1).objectValue) == 0) {
            return true;
        }
        if (minX <= getX() && getX() <= maxX && minY <= getY() + 1 && maxY >= getY() + 1
                && (CollisionFlags.WALL_NORTH & World.getWorld().getTileValue(getX(), getY() + 1).objectValue) == 0) {
            return true;
        }
        return false;
    }

    public final boolean canReach(Entity e) {
        int[] currentCoords = { getX(), getY() };
        while (currentCoords[0] != e.getX() || currentCoords[1] != e.getY()) {
            currentCoords = nextStep(currentCoords[0], currentCoords[1], e);
            if (currentCoords == null) {
                return false;
            }
        }
        return true;
    }
    
    public boolean withinRange(Entity e) {
        if (e != null) {
            int xDiff = getLocation().getX() - e.getLocation().getX();
            int yDiff = getLocation().getY() - e.getLocation().getY();
            return xDiff <= 15 && xDiff >= -15 && yDiff <= 15 && yDiff >= -15;
        }
        return false;
    }
    
    private final Random random = new Random();
    
    public Random getRandom() {
        return random;
    }
    
    public void face(Entity entity) {
        if (entity != null && entity.getLocation() != null) {
            final int dir = Formulae.getDirection(this, entity.getX(), entity.getY());
            if (dir != -1) {
                setSprite(dir);
            }
        }
    }

    public void face(int x, int y) {
        final int dir = Formulae.getDirection(this, x, y);
        if (dir != -1) {
            setSprite(dir);
        }
    }
    
    /**
     * Sets the time when player should be freed from the busy mode.
     * 
     * @param i
     */
    public void setBusyTimer(int i) {
        this.busyTimer = System.currentTimeMillis() + i;
    }
    
    public boolean inArea(int x1, int x2, int y1, int y2) {
        return getX() >= x1 && getX() <= x2 && getY() >= y1 && getY() <= y2;
    }

    /**
     * Prayers that are currently turned on
     */
    protected boolean[] activatedPrayers = new boolean[14];
    /**
     * ID for our current appearance, used client side to detect changed
     */
    protected int appearanceID = 0;
    /**
     * Used to block new requests when we are in the middle of one
     */
    private boolean busy = false;
    /**
     * Our combat level
     */
    protected int combatLevel = 3;
    /**
     * Timer used to track start and end of combat
     */
    private long combatTimer = 0;
    /**
     * Who they are in combat with
     */
    private Mob combatWith = null;
    /**
     * Have we moved since last update?
     */
    protected boolean hasMoved;
    /**
     * How many times we have hit our opponent
     */
    private int hitsMade = 0;
    /**
     * The end state of the last combat encounter
     */
    private CombatState lastCombatState = CombatState.WAITING;
    /**
     * Amount of damage last dealt to the player
     */
    private int lastDamage = 0;
    /**
     * Time of last movement, used for timeout
     */
    protected long lastMovement = System.currentTimeMillis();
    public long lastTimeShot = System.currentTimeMillis();
    protected int mobSprite = 1;
    private int[][] mobSprites = new int[][]{{3, 2, 1}, {4, -1, 0}, {5, 6, 7}};
    /**
     * Has our appearance changed since last update?
     */
    protected boolean ourAppearanceChanged = true;
    /**
     * The path we are walking
     */
    protected final PathHandler pathHandler = new PathHandler(this);
    /**
     * Set when the mob has been destroyed to alert players
     */
    public boolean removed = false;
    /**
     * Has the sprite changed?
     */
    private boolean spriteChanged = false;
    /**
     * Tiles around us that we can see
     */
    protected ViewArea viewArea = new ViewArea(this);
    /**
     * If we are warned to move
     */
    protected boolean warnedToMove = false;
    
    public PathHandler getPathHandler() {
        return pathHandler;
    }

    public boolean finishedPath() {
        return pathHandler.finishedPath();
    }

    public int getAppearanceID() {
        return appearanceID;
    }

    public abstract int getArmourPoints();

    public abstract int getAttack();

    public int getCombatLevel() {
        return combatLevel;
    }

    public CombatState getCombatState() {
        return lastCombatState;
    }

    public abstract int getCombatStyle();

    public long getCombatTimer() {
        return combatTimer;
    }

    public abstract int getDefense();

    public abstract int getHits();

    public int getHitsMade() {
        return hitsMade;
    }

    public int getLastDamage() {
        return lastDamage;
    }

    public long getLastMoved() {
        return lastMovement;
    }

    public Mob getOpponent() {
        return combatWith;
    }

    public int getSprite() {
        return mobSprite;
    }

    public abstract int getStrength();

    public ViewArea getViewArea() {
        return viewArea;
    }

    public abstract int getWeaponAimPoints();

    public abstract int getWeaponPowerPoints();

    public boolean hasMoved() {
        return hasMoved;
    }

    public void incHitsMade() {
        hitsMade++;
    }

    public boolean inCombat() {
        return (mobSprite == 8 || mobSprite == 9) && combatWith != null;
    }
    
    /**
     * Time in MS when we are freed from the 'busy' mode.
     */
    protected long busyTimer;
    
    public boolean isBusy() {
        return busyTimer - System.currentTimeMillis() > 0 || busy;
    }

    public boolean isPrayerActivated(int pID) {
        return activatedPrayers[pID];
    }

    public boolean isRemoved() {
        return removed;
    }

    public abstract void killedBy(Mob mob);

    public abstract void remove();

    public void resetCombat(CombatState state) {
        for (DelayedEvent event : World.getWorld().getDelayedEventHandler().getEvents()) {
            if (event instanceof FightEvent) {
                FightEvent fighting = (FightEvent) event;
                if (fighting.getOwner().equals(this) || fighting.getAffectedMob().equals(this)) {
                    fighting.interrupt();
                    break;
                }
            }
        }
        setBusy(false);
        setSprite(4);
        setOpponent(null);
        setCombatTimer();
        hitsMade = 0;
        if (this instanceof Player) {
            Player player = (Player) this;
            player.setStatus(Action.IDLE);
        }
        lastCombatState = state;
    }

    public void resetMoved() {
        hasMoved = false;
    }

    public void resetPath() {
        pathHandler.resetPath();
    }

    public void resetSpriteChanged() {
        spriteChanged = false;
    }

    public void setAppearnceChanged(boolean b) {
        ourAppearanceChanged = b;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public void setCombatLevel(int level) {
        combatLevel = level;
        ourAppearanceChanged = true;
    }

    public void setCombatTimer() {
        combatTimer = System.currentTimeMillis();
    }

    public abstract void setHits(int lvl);

    public void setLastDamage(int d) {
        lastDamage = d;
    }

    public void setLastMoved() {
        lastMovement = System.currentTimeMillis();
    }

    @Override
    public void setLocation(Point p) {
        setLocation(p, false);
    }

    public void setLocation(Point p, boolean teleported) {
        if (!teleported) {
            updateSprite(p);
            hasMoved = true;
        }
        setLastMoved();
        warnedToMove = false;

        super.setLocation(p);
    }

    public void setOpponent(Mob opponent) {
        combatWith = opponent;
    }

    public void setPath(Path path) {
        pathHandler.setPath(path);
    }

    public void setPrayer(int pID, boolean b) {
        activatedPrayers[pID] = b;
    }

    public void setSprite(int x) {
        spriteChanged = true;
        mobSprite = x;
    }

    public boolean spriteChanged() {
        return spriteChanged;
    }

    public void updateAppearanceID() {
        if (ourAppearanceChanged) {
            appearanceID++;
        }
    }

    public void updatePosition() {
        pathHandler.updatePosition();
    }

    protected void updateSprite(Point newLocation) {
        try {
            int xIndex = getLocation().getX() - newLocation.getX() + 1;
            int yIndex = getLocation().getY() - newLocation.getY() + 1;
            setSprite(mobSprites[xIndex][yIndex]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean warnedToMove() {
        return warnedToMove;
    }

    public void warnToMove() {
        warnedToMove = true;
    }
    
}