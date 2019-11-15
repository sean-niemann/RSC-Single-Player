package org.nemotech.rsc.model;

import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.external.definition.ItemDef;
import org.nemotech.rsc.external.location.ItemLoc;
import org.nemotech.rsc.event.DelayedEvent;

public class Item extends Entity {
    /**
     * World instance
     */
    private static final World world = World.getWorld();
    /**
     * Contains the player that the item belongs to, if any
     */
    private Player owner;
    /**
     * Amount (for stackables)
     */
    private int amount;
    /**
     * The time that the item was spawned
     */
    private long spawnedTime;
    /**
     * Set when the item has been destroyed to alert players
     */
    private boolean removed = false;
    /**
     * Location definition of the item
     */
    private ItemLoc loc = null;

    public Item(ItemLoc loc) {
        this.loc = loc;
        setID(loc.id);
        setAmount(loc.amount);
        spawnedTime = System.currentTimeMillis();
        setLocation(new Point(loc.x, loc.y));
    }

    public Item(int id, int x, int y, int amount, Player owner) {
        setID(id);
        setAmount(amount);
        this.owner = owner;
        spawnedTime = System.currentTimeMillis();
        setLocation(new Point(x, y));
    }

    public boolean visibleTo(Player p) {
        if(owner == null || p.equals(owner)) {
            return true;
        }
        return System.currentTimeMillis() - spawnedTime > 60000;
    }

    public ItemLoc getLoc() {
        return loc;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void remove() {
        if(!removed && loc != null && loc.getRespawnTime() > 0) {
            world.getDelayedEventHandler().add(new DelayedEvent(null, loc.getRespawnTime() * 1000) {
                public void run() {
                    world.registerItem(new Item(loc));
                    running = false;
                }
            });
        }
        removed = true;
    }

    public ItemDef getDef() {
        return EntityManager.getItem(id);
    }

    public void setAmount(int amount) {
        if(getDef().isStackable()) {
            this.amount = amount;
        }
        else {
            this.amount = 1;
        }
    }

    public boolean equals(Object o) {
        if(o instanceof Item) {
            Item item = (Item)o;
            return item.getID() == getID() && item.getAmount() == getAmount() && item.getSpawnedTime() == getSpawnedTime() && (item.getOwner() == null || item.getOwner().equals(getOwner())) && item.getLocation().equals(getLocation());
        }
        return false;
    }

    public long getSpawnedTime() {
        return spawnedTime;
    }

    public int getAmount() {
        return amount;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean isOn(int x, int y) {
        return x == getX() && y == getY();
    }

}
