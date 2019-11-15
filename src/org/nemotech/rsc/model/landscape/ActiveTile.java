package org.nemotech.rsc.model.landscape;

import java.util.LinkedList;
import java.util.List;
import org.nemotech.rsc.model.Entity;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.NPC;

import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;

public class ActiveTile {
    
    // World instance
    private final World world = World.getWorld();
    
    // A list of all items currently on this tile
    private final List<Item> items = new LinkedList<>();
    
    // A list of all npcs currently on this tile
    private final List<NPC> npcs = new LinkedList<>();
    
    // A list of all players currently on this tile
    private final List<Player> players = new LinkedList<>();
    
    // The object currently on this tile (can only have 1 at a time)
    private GameObject object = null;
    
    // The x and y coordinates of this tile
    private final int x, y;
    
    // Constructs a new tile at the given coordinates
    public ActiveTile(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public boolean remove = false;
    
    // Add an entity to the tile
    public void add(Entity entity) {
        if (entity instanceof Player) {
            players.add((Player) entity);
        } else if (entity instanceof NPC) {
            npcs.add((NPC) entity);
        } else if (entity instanceof Item) {
            items.add((Item) entity);
        } else if (entity instanceof GameObject) {
            if (object != null) {
                remove = true;
                world.unregisterGameObject(object);
                remove = false;
            }
            object = (GameObject) entity;
        }
    }

    public GameObject getGameObject() {
        return object;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<NPC> getNpcs() {
        return npcs;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasGameObject() {
        return object != null;
    }

    public boolean hasItem(Item item) {
        return items.contains(item);
    }

    public boolean hasItems() {
        return items != null && items.size() > 0;
    }

    public boolean hasNpcs() {
        return npcs != null && npcs.size() > 0;
    }

    public boolean hasPlayers() {
        return players != null && players.size() > 0;
    }

    public boolean specificArea() {
        //return Util.inPointArray(Formulae.NO_REMOVE_TILES, new Point(getX(), getY()));
        return false;
    }
    
    public Item getItem(int id, Player player) {
        for (Item i : getItems()) {
            if (i.getID() == id && i.visibleTo(player)) {
                return i;
            }
        }
        return null;
    }
    
    // Remove an entity from the tile
    public void remove(Entity entity) {
        if (entity instanceof Player) {
            players.remove((Player)entity);
            if (!hasGameObject() && !hasItems() && !hasNpcs() && !hasPlayers() && !specificArea()) {
                World.getWorld().tiles[getX()][getY()] = null;
            }
        } else if (entity instanceof NPC) {
            npcs.remove((NPC)entity);
            if (!hasGameObject() && !hasItems() && !hasNpcs() && !hasPlayers() && !specificArea()) {
                World.getWorld().tiles[getX()][getY()] = null;
            }
        } else if (entity instanceof Item) {
            items.remove((Item)entity);
            if (!hasGameObject() && !hasItems() && !hasNpcs() && !hasPlayers() && !specificArea()) {
                World.getWorld().tiles[getX()][getY()] = null;
            }
        } else if (entity instanceof GameObject) {
            object = null;
            if (!hasGameObject() && !hasItems() && !hasNpcs() && !hasPlayers() && !remove) {
                World.getWorld().tiles[getX()][getY()] = null;
            }
        }
    }
    
    public void clean() {
        if (!hasGameObject() && !hasItems() && !hasNpcs() && !hasPlayers() && !specificArea()) {
            World.getWorld().tiles[getX()][getY()] = null;
        }
    }
    
}