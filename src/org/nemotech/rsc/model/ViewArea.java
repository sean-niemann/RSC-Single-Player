package org.nemotech.rsc.model;

import java.util.ArrayList;
import java.util.List;

import org.nemotech.rsc.model.landscape.ActiveTile;
import org.nemotech.rsc.model.player.Player;

public class ViewArea {
    
    private static World world = World.getWorld();
    private Mob mob;

    public ViewArea(Mob mob) {
        this.mob = mob;
    }

    public ActiveTile[][] getViewedArea(int x1, int y1, int x2, int y2) {
        int mobX = mob.getX();
        int mobY = mob.getY();
            int startX, startY, endX, endY;
            startX = mobX - x1;
            if (startX < 0) {
                startX = 0;
            }
            startY = mobY - y1;
            if (startY < 0) {
                startY = 0;
            }
            endX = mobX + x2;
            if (endX >= World.MAX_WIDTH) {
                endX = World.MAX_WIDTH - 1;
            }
            endY = mobY + y2;
            if (endY >= World.MAX_HEIGHT) {
                endY = World.MAX_HEIGHT - 1;
            }
            int xWidth;
            int yWidth;
            if (startX > endX) {
                xWidth = startX - endX;
            }
            else {
                xWidth = endX - startX;
            }
            if (startY > endY) {
                yWidth = startY - endY;
            }
            else {
                yWidth = endY - startY;
            }
            ActiveTile[][] temp = new ActiveTile[xWidth][yWidth];
            for (int x = 0; (x + startX) < endX; x++) {
                for (int y = 0; (y + startY) < endY; y++) {
                    temp[x][y] = world.tiles[x + startX][y + startY];
                }
            }
            return temp;
    }

    public List<Player> getPlayersInView() {
        List<Player> players = new ArrayList<Player>();
        ActiveTile[][] viewArea = getViewedArea(15,15,16,16);
        for (ActiveTile[] viewArea1 : viewArea) {
            for (ActiveTile t : viewArea1) {
                if (t != null) {
                    List<Player> temp = t.getPlayers();
                    if (temp != null) {
                        players.addAll(temp);
                    }
                }
            }
        }
        return players;
    }

    public List<Item> getItemsInView() {
        List<Item> items = new ArrayList<Item>();
        ActiveTile[][] viewArea = getViewedArea(21,21,21,21);
        for (ActiveTile[] viewArea1 : viewArea) {
            for (ActiveTile t : viewArea1) {
                if (t != null) {
                    items.addAll(t.getItems());
                }
            }
        }
        return items;
    }

    public List<GameObject> getGameObjectsInView() {
        List<GameObject> objects = new ArrayList<GameObject>();
        ActiveTile[][] viewArea = getViewedArea(21,21,21,21);
        for (ActiveTile[] viewArea1 : viewArea) {
            for (ActiveTile t : viewArea1) {
                if (t != null) {
                    if (t.hasGameObject()) {
                        objects.add(t.getGameObject());
                    }
                }
            }
        }
        return objects;
    }

    public List<NPC> getNpcsInView() {
        List<NPC> npcs = new ArrayList<NPC>();
        ActiveTile[][] viewArea = getViewedArea(15,15,16,16);
        for (ActiveTile[] viewArea1 : viewArea) {
            for (ActiveTile t : viewArea1) {
                if (t != null) {
                    List<NPC> temp = t.getNpcs();
                    if (temp != null) {
                        npcs.addAll(temp);
                    }
                }
            }
        }
        return npcs;
    }
    
    public GameObject getGameObject(Point location) {
        for(GameObject o : getGameObjectsInView()) {
            if(o.getLocation().equals(location) && o.getType() != 1) {
                return o;
            }
        }
        return null;
    }
    
    public GameObject getGameObject(int id, int x, int y) {
        for(GameObject o : getGameObjectsInView()) {
            if(o.getID() == id && o.getX() == x && o.getY() == y) {
                return o;
            }
        }
        return null;
    }
    
    /**
     * Experimental - to handle Doors on same X and Y as GameObjects.
     * @param location
     * @return
     * FACT: RSC uses direction for wall objects, so that it doesn't collapse.
     */
    public GameObject getWallObjectWithDir(Point location, int dir) {
        for(GameObject o : getGameObjectsInView()) {
            if(o.getDirection() == dir && o.getLocation().equals(location) && (o.getType() != 0)) {
                return o;
            }
        }
        return null;
    }
    
    public Item getGroundItem(Point location) {
        for(Item o : getItemsInView()) {
            if(o.getLocation().equals(location)) {
                return o;
            }
        }
        return null;
    }
    
    public Item getGroundItem(int id, Point location) {
        for(Item o : getItemsInView()) {
            if(o.getID() == id && o.getLocation().equals(location)) {
                return o;
            }
        }
        return null;
    }

}