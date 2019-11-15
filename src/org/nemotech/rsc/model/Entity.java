package org.nemotech.rsc.model;

import java.util.HashMap;
import java.util.Map;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.util.Formulae;

public class Entity {

    protected World world = World.getWorld();

    protected int id, index;

    protected Point location;
    
    protected final Map<String, Object> attributes = new HashMap<>();

    public final int getID() {
        return id;
    }

    public final int getIndex() {
        return index;
    }

    public final Point getLocation() {
        return location;
    }

    public final int getX() {
        return location.getX();
    }

    public final int getY() {
        return location.getY();
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String string) {
        return (T) attributes.get(string);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String string, T fail) {
        T object = (T) attributes.get(string);
        if (object != null) {
            return object;
        }
        return fail;
    }
    
    public void removeAttribute(String string) {
        attributes.remove(string);
    }

    public void setAttribute(String string, Object object) {
        attributes.put(string, object);
    }

    public boolean isBlocking(Entity e, int x, int y, int bit) {
        return isMapBlocking(e, x, y, (byte) bit) || isObjectBlocking(e, x, y, (byte) bit);
    }

    private boolean isMapBlocking(Entity e, int x, int y, byte bit) {
        byte val = world.getTileValue(x, y).mapValue;
        //System.out.println("[MAP] " + EntityManager.getGameObjectDef(e.getID()).getName() + " | " + val + " | (" + x + "," + y + ")");
        if ((val & bit) != 0) { // There is a wall in the way
            return true;
        }
        if ((val & 16) != 0) { // There is a diagonal wall here: \
            return true;
        }
        if ((val & 32) != 0) { // There is a diagonal wall here: /
            return true;
        }
        if ((val & 64) != 0 && (e instanceof NPC || e instanceof Player) || (e instanceof Item && !((Item) e).isOn(x, y)) || (e instanceof GameObject && !((GameObject) e).isOn(x, y))) { // There is an object here, doesn't block items (ontop of it) or the object itself though
            return true;
        }
        return false;
    }

    private boolean isObjectBlocking(Entity e, int x, int y, byte bit) {
        byte val = world.getTileValue(x, y).objectValue;
        if((val & bit) != 0) {
            return true;
        }
        if ((val & bit) != 0 && !Formulae.doorAtFacing(e, x, y, Formulae.bitToDoorDir(bit)) && !Formulae.objectAtFacing(e, x, y, Formulae.bitToObjectDir(bit))) {
            return true;
        }
        if((val & 16) != 0) {
            return true;
        }
        if ((val & 16) != 0 && !Formulae.doorAtFacing(e, x, y, 2) && !Formulae.objectAtFacing(e, x, y, 3)) {
            return true;
        }
        if((val & 32) != 0) {
            return true;
        }
        if ((val & 32) != 0 && !Formulae.doorAtFacing(e, x, y, 3) && !Formulae.objectAtFacing(e, x, y, 1)) {
            return true;
        }
        if((val & 64) != 0) {
            return true;
        }
        if ((val & 64) != 0 && (e instanceof NPC || e instanceof Player) || (e instanceof Item && !((Item) e).isOn(x, y)) || (e instanceof GameObject && !((GameObject) e).isOn(x, y))) {
            return true;
        }
        //System.out.println("object not blocking..continuing to attack");
        return false;
    }

    public int[] nextStep(int myX, int myY, Entity e) {
        if (myX == e.getX() && myY == e.getY()) {
            return new int[]{myX, myY};
        }
        int newX = myX, newY = myY;
        boolean myXBlocked = false, myYBlocked = false, newXBlocked = false, newYBlocked = false;

        if (myX > e.getX()) {
            myXBlocked = isBlocking(e, myX - 1, myY, 8); // Check right tiles
            // left wall
            newX = myX - 1;
        } else if (myX < e.getX()) {
            myXBlocked = isBlocking(e, myX + 1, myY, 2); // Check left tiles
            // right wall
            newX = myX + 1;
        }

        if (myY > e.getY()) {
            myYBlocked = isBlocking(e, myX, myY - 1, 4); // Check top tiles
            // bottom wall
            newY = myY - 1;
        } else if (myY < e.getY()) {
            myYBlocked = isBlocking(e, myX, myY + 1, 1); // Check bottom tiles
            // top wall
            newY = myY + 1;
        }

        // If both directions are blocked OR we are going straight and the
        // direction is blocked
        if ((myXBlocked && myYBlocked) || (myXBlocked && myY == newY) || (myYBlocked && myX == newX)) {
            return null;
        }

        if (newX > myX) {
            newXBlocked = isBlocking(e, newX, newY, 2); // Check dest tiles
            // right wall
        } else if (newX < myX) {
            newXBlocked = isBlocking(e, newX, newY, 8); // Check dest tiles left
            // wall
        }

        if (newY > myY) {
            newYBlocked = isBlocking(e, newX, newY, 1); // Check dest tiles top
            // wall
        } else if (newY < myY) {
            newYBlocked = isBlocking(e, newX, newY, 4); // Check dest tiles
            // bottom wall
        }

        // If both directions are blocked OR we are going straight and the
        // direction is blocked
        if ((newXBlocked && newYBlocked) || (newXBlocked && myY == newY) || (myYBlocked && myX == newX)) {
            return null;
        }

        // If only one direction is blocked, but it blocks both tiles
        if ((myXBlocked && newXBlocked) || (myYBlocked && newYBlocked)) {
            return null;
        }

        return new int[]{newX, newY};
    }
    
    public boolean nextTo(Entity e) {  // broken?
        int[] currentCoords = {getX(), getY()};
        while (currentCoords[0] != e.getX() || currentCoords[1] != e.getY()) {
            currentCoords = nextStep(currentCoords[0], currentCoords[1], e);
            if (currentCoords == null) {
                return false;
            } 
        } 
        return true;
    }

    public boolean nextTo(Mob mob, Entity e) {  // broken?
        Player p = null;
        NPC n = null;
        int x = 0;
        int y = 0;
        
        if(mob instanceof Player) {
            p = (Player) mob;
            x = p.getX();
            y = p.getY();
        } else if(mob instanceof NPC) {
            n = (NPC) mob;
            x = n.getX();
            y = n.getY();
        }
        
        int[] currentCoords = new int[]{x, y};
        while (currentCoords[0] != e.getX() || currentCoords[1] != e.getY()) {
            currentCoords = nextStep(currentCoords[0], currentCoords[1], e);
            if (currentCoords == null) {
                System.out.println("null coords");
                return false;
            } 
        } 
        return true;
    }

    public final void setID(int newid) {
        id = newid;
    }

    public final void setIndex(int newIndex) {
        index = newIndex;
    }

    public void setLocation(Point p) {
        world.setLocation(this, location, p);
        location = p;
    }

    public final boolean withinRange(Entity e, int radius) {
        return withinRange(e.getLocation(), radius);
    }

    public final boolean withinRange(Point p, int radius) {
        int xDiff = Math.abs(location.getX() - p.getX());
        int yDiff = Math.abs(location.getY() - p.getY());
        return xDiff <= radius && yDiff <= radius;
    }

}