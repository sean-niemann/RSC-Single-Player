package org.nemotech.rsc.model.landscape;

import java.util.Collection;
import java.util.HashSet;

import org.nemotech.rsc.model.Point;
import org.nemotech.rsc.model.Entity;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

public class Region {
    /**
     * A list of players in this region.
     */
    private HashSet<Player> players = new HashSet<>();

    /**
     * A list of NPCs in this region.
     */
    private HashSet<NPC> npcs = new HashSet<>();

    /**
     * A list of objects in this region.
     */
    private HashSet<GameObject> objects = new HashSet<>();

    /**
     * A list of objects in this region.
     */
    private HashSet<Item> items = new HashSet<>();

    /**
     * Gets the list of players.
     * 
     * @return The list of players.
     */
    public Collection<Player> getPlayers() {
        synchronized (players) {
            return players;
        }
    }

    /**
     * Gets the list of NPCs.
     * 
     * @return The list of NPCs.
     */
    public Collection<NPC> getNpcs() {
        synchronized (npcs) {
            return npcs;
        }
    }

    /**
     * Gets the list of objects.
     * 
     * @return The list of objects.
     */
    public Collection<GameObject> getGameObjects() {
        synchronized (objects) {
            return objects;
        }
    }

    public Iterable<Item> getGroundItems() {
        synchronized (items) {
            return items;
        }
    }

    public void removeEntity(Entity e) {

        if (e instanceof Player) {
            synchronized (players) {
                players.remove((Player) e);
            }
        } else if (e instanceof NPC) {
            synchronized (npcs) {
                npcs.remove((NPC) e);
            }
        } else if (e instanceof GameObject) {
            synchronized (objects) {
                objects.remove((GameObject) e);
            }
        } else if (e instanceof Item) {
            synchronized (items) {
                items.remove((Item) e);
            }
        }
    }

    public void addEntity(Entity e) {
        /*if (e.isRemoved()) {
            return;
        }*/
        if (e instanceof Player) {
            synchronized (players) {
                players.add((Player) e);
            }
        } else if (e instanceof NPC) {
            synchronized (npcs) {
                npcs.add((NPC) e);
            }
        } else if (e instanceof GameObject) {
            synchronized (objects) {
                objects.add((GameObject) e);
            }
        } else if (e instanceof Item) {
            synchronized (items) {
                items.add((Item) e);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(2000);
        sb.append("Players:\n");
        for (Player p : players) {
            sb.append("\t").append(p).append("\n");
        }

        sb.append("\nNpcs:\n");
        for (NPC n : npcs) {
            sb.append("\t").append(n).append("\n");
        }

        sb.append("\nItems:\n");
        for (Item i : items) {
            sb.append("\t").append(i).append("\n");
        }

        sb.append("\nObjects:\n");
        for (Object o : objects) {
            sb.append("\t").append(o).append("\n");
        }

        return sb.toString();
    }

    public String toString(boolean debugPlayers, boolean debugNpcs, boolean debugItems, boolean debugObjects) {
        StringBuilder sb = new StringBuilder(2000);
        if (debugPlayers) {
            sb.append("Players:\n");
            for (Player p : players) {
                sb.append("\t").append(p).append("\n");
            }
        }
        if (debugNpcs) {
            sb.append("\nNpcs:\n");
            for (NPC n : npcs) {
                sb.append("\t").append(n).append("\n");
            }
        }
        if (debugItems) {
            sb.append("\nItems:\n");
            for (Item i : items) {
                sb.append("\t").append(i).append("\n");
            }
        }
        if (debugObjects) {
            sb.append("\nObjects:\n");
            for (Object o : objects) {
                sb.append("\t").append(o).append("\n");
            }
        }

        return sb.toString();
    }

    public GameObject getGameObject(int x, int y) {
        synchronized (objects) {
            for (GameObject o : objects) {
                if (o.getX() == x && o.getY() == y) {
                    return o;
                }
            }
        }
        return null;
    }

    public GameObject getGameObject(Point point) {
        synchronized (objects) {
            for (GameObject o : objects) {
                if (o.getLocation().getX() == point.getX() && o.getLocation().getY() == point.getY() && o.getType() == 0) {
                    return o;
                }
            }
        }
        return null;
    }

    public GameObject getWallGameObject(Point point, int direction) {
        synchronized (objects) {
            for (GameObject o : objects) {
                if (o.getLocation().getX() == point.getX() && o.getLocation().getY() == point.getY() && o.getType() == 1 && o.getDirection() == direction) {
                    return o;
                }
            }
        }
        return null;
    }

    public NPC getNpc(int x, int y) {
        synchronized (npcs) {
            for (NPC n : npcs) {
                if (n.getLocation().getX() == x && n.getLocation().getY() == y) {
                    return n;
                }
            }
        }
        return null;
    }

    public Player getPlayer(int x, int y) {
        synchronized (players) {
            for (Player p : players) {
                if (p.getX() == x && p.getY() == y) {
                    return p;
                }
            }
        }
        return null;
    }
}