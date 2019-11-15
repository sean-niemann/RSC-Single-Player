package org.nemotech.rsc.model;

import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.external.definition.DoorDef;
import org.nemotech.rsc.external.definition.GameObjectDef;
import org.nemotech.rsc.external.location.GameObjectLoc;

public class GameObject extends Entity {
    
    private int containsItem = -1;
    
    public int getContainingItem() {
        return containsItem;
    }

    public void setContainsItem(int item) {
        containsItem = item;
    }

    private int direction;
    /**
     * The type of object
     */
    private int type;
    /**
     * Location definition of the object
     */
    private GameObjectLoc loc = null;
    /**
     * Set when the item has been destroyed to alert players
     */
    private boolean removed = false;

    public GameObject(GameObjectLoc loc) {
        direction = loc.direction;
        type = loc.type;
        this.loc = loc;
        super.setID(loc.id);
        super.setLocation(new Point(loc.x, loc.y));
    }

    public GameObject(Point location, int id, int direction, int type) {
        this(new GameObjectLoc(id, location.getX(), location.getY(), direction, type));
    }

    public GameObject(Point location, int id, int direction, int type, String owner) {
        this(new GameObjectLoc(id, location.getX(), location.getY(), direction, type, owner));
    }

    public String getOwner() {
        return loc.owner;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void remove() {
        removed = true;
    }

    public GameObjectLoc getLoc() {
        return loc;
    }

    public GameObjectDef getGameObjectDef() {
        return EntityManager.getGameObjectDef(super.getID());
    }

    public DoorDef getDoorDef() {
        return EntityManager.getDoor(super.getID());
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public int getGroundItemVar() {
        return EntityManager.getGameObjectDef(id).getItemHeight();
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof GameObject) {
            GameObject go = (GameObject)o;
            return go.getLocation().equals(getLocation()) && go.getID() == getID() && go.getDirection() == getDirection() && go.getType() == getType();
        }
        return false;
    }

    public boolean isOn(int x, int y) {
            int width, height;
            if(type == 1) {
                width = height = 1;
            }
            else if(direction == 0 || direction == 4) {
                width = getGameObjectDef().getWidth();
                height = getGameObjectDef().getHeight();
            }
            else {
                height = getGameObjectDef().getWidth();
                width = getGameObjectDef().getHeight();
            }
        if(type == 0) { // Object
            return x >= getX() && x <= (getX() + width) && y >= getY() && y <= (getY() + height);
        }
        else { // Door
            return x == getX() && y == getY();
        }
    }
    
    public final Point[] getObjectBoundary() {
        int dir = getDirection();
        int minX = getX();
        int minY = getY();
        int maxX = minX;
        int maxY = minY;
        if (getType() == 0) {
            int worldWidth;
            int worldHeight;
            if (dir != 0 && dir != 4) {
                worldWidth = getGameObjectDef().getHeight();
                worldHeight = getGameObjectDef().getWidth();
            } else {
                worldHeight = getGameObjectDef().getHeight();
                worldWidth = getGameObjectDef().getWidth();
            }
            maxX = worldWidth + getX() - 1;
            maxY = worldHeight + getY() - 1;

            if (getGameObjectDef().getType() == 2 || getGameObjectDef().getType() == 3) {
                if (dir == 0) {
                    ++worldWidth;
                    --minX;
                }
                if (dir == 2) {
                    ++worldHeight;
                }
                if (dir == 6) {
                    --minY;
                    ++worldHeight;
                }
                if (dir == 4) {// here.
                    ++worldWidth;
                }
                maxX = worldWidth + getX() - 1;
                maxY = worldHeight + getY() - 1;
            }
        } else if (getType() == 1) {
            
            if (dir == 0) {
                minX = getX();
                minY = getY() - 1;
                maxX = getX();
                maxY = getY();
            } else if (dir != 1) {
                
                minX = getX();
                minY = getY();
                maxX = getX();
                maxY = getY();
                if(dir == 3 || dir == 2) {
                    minX = getX() - 1;
                    minY = getY() - 1;
                    maxX = getX() + 1;
                    maxY = getY() + 1;
                }
            } else {
                minX = getX() - 1;
                minY = getY();
                maxX = getX();
                maxY = getY();
            }
        }
        return new Point[] { Point.getLocation(minX, minY), Point.getLocation(maxX, maxY)};
    }

    @Override
    public String toString() {
        return (type == 0 ? "GameObject" : "WallObject") + ":id = " + id + "; dir = " + direction + "; location = " + location.toString() + ";";
    }

}