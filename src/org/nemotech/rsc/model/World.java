package org.nemotech.rsc.model;

import java.util.*;
import org.nemotech.rsc.Constants;

import org.nemotech.rsc.client.mudclient;
import org.nemotech.rsc.core.DelayedEventHandler;
import org.nemotech.rsc.model.landscape.TileValue;
import org.nemotech.rsc.model.landscape.ActiveTile;

import org.nemotech.rsc.external.location.GameObjectLoc;
import org.nemotech.rsc.external.location.NPCLoc;
import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.event.SingleEvent;
import org.nemotech.rsc.io.WorldLoader;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.player.states.CombatState;
import org.nemotech.rsc.plugins.QuestInterface;
import org.nemotech.rsc.util.EntityList;

public class World {
    
    private Player player;
    
    public Player getPlayer() {
        if(player == null) {
            player = new Player();
        }
        return player;
    }
    
    private final List<QuestInterface> quests = new LinkedList<>();

    public static final int MAX_HEIGHT = 3776;

    public static final int MAX_WIDTH = 944;

    private static World worldInstance;

    public WorldLoader wl;
    
    public void replaceGameObject(GameObject old, GameObject _new) {
        //unregisterGameObject(old);
        registerGameObject(_new);
    }

    /**
     * returns the only instance of this world, if there is not already one,
     * makes it and loads everything
     */
    public static synchronized World getWorld() {
        if (worldInstance == null) {
            worldInstance = new World();
            try {
                worldInstance.wl = new WorldLoader();
                worldInstance.wl.loadWorld(worldInstance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return worldInstance;
    }

    private DelayedEventHandler delayedEventHandler;

    private final EntityList<NPC> npcs = new EntityList<>(4000); 

    private final EntityList<Player> players = new EntityList<>(3000);

    private final List<Shop> shops = new ArrayList<>();
    
    public ActiveTile[][] tiles = new ActiveTile[MAX_WIDTH][MAX_HEIGHT];

    private final TileValue[][] tileType = new TileValue[MAX_WIDTH][MAX_HEIGHT];

    public synchronized int countNpcs() {
        return npcs.size();
    }

    public synchronized int countPlayers() {
        return players.size();
    }
    
    public List<QuestInterface> getQuests() {
        return quests;
    }
    
    public void registerQuest(QuestInterface quest) {
        if (quest.getQuestName() == null) {
            throw new IllegalArgumentException("Quest name cannot be null");
        } else if (quest.getQuestName().length() > 40) {
            throw new IllegalArgumentException("Quest name cannot be longer then 40 characters");
        }
        for (QuestInterface q : quests) {
            if (q.getQuestID() == quest.getQuestID()) {
                throw new IllegalArgumentException("Quest ID must be unique");
            }
        }
        if(!Constants.MEMBER_WORLD && quest.isMembers()) {
            return;
        }
        quests.add(quest);
    }
    
    public void unregisterQuest(QuestInterface quest) {
        if(quests.contains(quest)) {
            quests.remove(quest);
        }
    }

    public void sendWorldMessage(String msg) {
        synchronized (players) {
            for (Player p : players) {
                p.getSender().sendMessage(msg);
            }
        }
    }

    public void delayedRemoveObject(final GameObject object, final int delay) {
        delayedEventHandler.add(new SingleEvent(null, delay) {
            public void action() {
                ActiveTile tile = getTile(object.getLocation());
                if (tile.hasGameObject() && tile.getGameObject().equals(object)) {
                    unregisterGameObject(object);
                }
            }
        });
    }

    /**
     * Adds a DelayedEvent that will spawn a GameObject
     */
    public void delayedSpawnObject(final GameObjectLoc loc, final int respawnTime) {
        delayedEventHandler.add(new SingleEvent(null, respawnTime) {
            public void action() {
                registerGameObject(new GameObject(loc));
            }
        }); 
    }
    
    public void delayedSpawnObject(final GameObjectLoc loc, final GameObject objToRemove, final int respawnTime) {
        delayedEventHandler.add(new SingleEvent(null, respawnTime) {
            public void action() {
                registerGameObject(new GameObject(loc));
                objToRemove.remove();
            }
        }); 
    }

    /**
     * Gets the DelayedEventHandler instance
     */
    public DelayedEventHandler getDelayedEventHandler() {
        return delayedEventHandler;
    }

    public NPC getNpcById(int id) {
        for (NPC npc : npcs) {
            if (npc.getID() == id) {
                return npc;
            }
        }
        return null;
    }
    
    public NPC getNpc(int idx) {
        return npcs.get(idx);
    }

    public NPC getNpc(int id, int minX, int maxX, int minY, int maxY) {
        for (NPC npc : npcs) {
            if (npc.getID() == id && npc.getX() >= minX && npc.getX() <= maxX && npc.getY() >= minY && npc.getY() <= maxY) {
                return npc;
            }
        }
        return null;
    }

    public NPC getNpc(int id, int minX, int maxX, int minY, int maxY, boolean notNull) {
        for (NPC npc : npcs) {
            if (npc.getID() == id && npc.getX() >= minX && npc.getX() <= maxX && npc.getY() >= minY && npc.getY() <= maxY) {
                if (!npc.inCombat()) {
                    return npc;
                }
            }
        }
        return null;
    }

    /**
     * Gets the list of npcs on the server
     */
    public synchronized EntityList<NPC> getNpcs() {
        return npcs;
    }

    /**
     * Gets a Player by their server index
     */
    public Player getPlayer(int idx) {
        try {
            Player p = players.get(idx);
            return p;
        } catch(Exception e) {
            return null;
        }
    }
    
    public void sendWorldAnnouncement(String msg) {
        for (Player p : getPlayers()) {
            p.getSender().sendMessage("@ann@" + msg);
        }
    }
    
    public Player getPlayer(long usernameHash) {
        for (Player p : players) {
            if (p.getUsernameHash() == usernameHash) {
                return p;
            }
        }
        return null;
    }

    public synchronized EntityList<Player> getPlayers() {
        return players;
    }

    public List<Shop> getShops() {
        return shops;
    }
    
    public ActiveTile getTile(int x, int y) {
        if (!withinWorld(x, y)) {
            return null;
        }
        ActiveTile t = tiles[x][y];
        if (t == null) {
            t = new ActiveTile(x, y);
            tiles[x][y] = t;
        }
        return t;
    }
    
    public ActiveTile getTile(Point p) {
        return getTile(p.getX(), p.getY());
    }

    public TileValue getTileValue(int x, int y) {
        if (!withinWorld(x, y)) {
            return new TileValue();
        }
        TileValue t = tileType[x][y];
        if (t == null) {
            t = new TileValue();
            tileType[x][y] = t;
        }
        return t;
    }
    
    public boolean hasNpc(NPC n) {
        return npcs.contains(n);
    }

    public boolean hasPlayer(Player p) {
        return players.contains(p);
    }

    public boolean isLoggedIn(long usernameHash) {
        Player friend = getPlayer(usernameHash);
        if (friend != null) {
            return friend.isLoggedIn();
        }
        return false;
    }

    private void registerDoor(GameObject o) {
        if (o.getDoorDef().getType() != 1) {
            return;
        }
        int dir = o.getDirection();
        int x = o.getX(), y = o.getY();
        if (dir == 0) {
            getTileValue(x, y).objectValue |= 1;
            getTileValue(x, y - 1).objectValue |= 4;
        } else if (dir == 1) {
            getTileValue(x, y).objectValue |= 2;
            getTileValue(x - 1, y).objectValue |= 8;
        } else if (dir == 2) {
            getTileValue(x, y).objectValue |= 0x10;
        } else if (dir == 3) {
            getTileValue(x, y).objectValue |= 0x20;
        }
    }
    
    public void registerGameObject(GameObject o, boolean initial) {
        /*if(!getTile(o.getLocation()).getGameObject().equals(o) && !initial) { TODO? IDFK
            System.out.println("removed: " + getTile(o.getLocation()).getGameObject());
            unregisterGameObject(getTile(o.getLocation()).getGameObject());
        }*/
        switch(o.getType()) {
            case 0:
                registerObject(o);
                break;
            case 1:
                registerDoor(o);
                break;
        }
    }

    public void registerGameObject(GameObject o) {
        registerGameObject(o, false);
    }

    public void registerItem(final Item i) {
        try {
            if (i.getLoc() == null) {
                delayedEventHandler.add(new DelayedEvent(null, 180000) {
                    public void run() {
                        ActiveTile tile = getTile(i.getLocation());
                        if (tile.hasItem(i)) {
                            unregisterItem(i);
                        }
                        running = false;
                    }
                });
            }
        } catch(Exception e) {
            i.remove();
            e.printStackTrace();
        }
    }
    
    public void registerNpc(NPC n) {
        NPCLoc npc = n.getLoc();
        if (npc.startX < npc.minX || npc.startX > npc.maxX || npc.startY < npc.minY || npc.startY > npc.maxY) {
            System.out.println("\t[NPC Registrar] Impossible coordinate range: [ID:" + npc.id + " X:" + npc.startX + " Y:" + npc.startY + "]");
        }
        if((getTileValue(npc.startX, npc.startY).mapValue & 64) != 0) {
            System.out.println("\t[NPC Registrar] Starting coordinates are unwalkable: [ID:" + npc.id + " X:" + npc.startX + " Y:" + npc.startY + "]");
        }
        if((getTileValue(npc.startX, npc.startY).mapValue & 64) != 0) {
            System.out.println("\t[NPC Registrar] Starting coordinates are on an object: [ID:" + npc.id + " X:" + npc.startX + " Y:" + npc.startY + "]");
        }
        npcs.add(n);
    }

    private void registerObject(GameObject o) {
        if (o.getGameObjectDef().getType() != 1 && o.getGameObjectDef().getType() != 2) {
            return;
        }
        int dir = o.getDirection();
        int width, height;
        if (dir == 0 || dir == 4) {
            width = o.getGameObjectDef().getWidth();
            height = o.getGameObjectDef().getHeight();
        } else {
            height = o.getGameObjectDef().getWidth();
            width = o.getGameObjectDef().getHeight();
        }
        for (int x = o.getX(); x < o.getX() + width; x++) {
            for (int y = o.getY(); y < o.getY() + height; y++) {
                if (o.getGameObjectDef().getType() == 1) {
                    getTileValue(x, y).objectValue |= 0x40;
                } else if (dir == 0) {
                    getTileValue(x, y).objectValue |= 2;
                    getTileValue(x - 1, y).objectValue |= 8;
                } else if (dir == 2) {
                    getTileValue(x, y).objectValue |= 4;
                    getTileValue(x, y + 1).objectValue |= 1;
                } else if (dir == 4) {
                    getTileValue(x, y).objectValue |= 8;
                    getTileValue(x + 1, y).objectValue |= 2;
                } else if (dir == 6) {
                    getTileValue(x, y).objectValue |= 1;
                    getTileValue(x, y - 1).objectValue |= 4;
                }
            }
        }
    }

    public void registerPlayer(Player p) {
        p.setInitialized();
        players.add(p);
        mudclient.getInstance().player = p;
    }
    
    public void unregisterPlayer(final Player player) {
        try {
            player.setLoggedIn(false);
            player.reset();
            player.save();
            Mob opponent = player.getOpponent();
            if (opponent != null) {
                player.resetCombat(CombatState.ERROR);
                opponent.resetCombat(CombatState.ERROR);
            }
            delayedEventHandler.removePlayersEvents(player);
            players.remove(player);
            setLocation(player, player.getLocation(), null);
            this.player = null;
            mudclient.getInstance().player = null;
            mudclient.getInstance().resetLoginVars();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void registerShop(Shop shop) {
        shops.add(shop);
    }

    public void registerShops(Shop... shop) {
        shops.addAll(Arrays.asList(shop));
    }

    public void setDelayedEventHandler(DelayedEventHandler delayedEventHandler) {
        this.delayedEventHandler = delayedEventHandler;
    }

    public void setLocation(Entity entity, Point oldPoint, Point newPoint) {
        ActiveTile t;
        if (oldPoint != null) {
            t = getTile(oldPoint);
            t.remove(entity);
        }
        if (newPoint != null) {
            t = getTile(newPoint);
            t.add(entity);
        }
    }

    /**
     * Removes a door from the map
     */
    private void unregisterDoor(GameObject o) {
        if (o.getDoorDef().getType() != 1) {
            return;
        }
        int dir = o.getDirection();
        int x = o.getX(), y = o.getY();
        if (dir == 0) {
            getTileValue(x, y).objectValue &= 0xfffe;
            getTileValue(x, y - 1).objectValue &= 65535 - 4;
        } else if (dir == 1) {
            getTileValue(x, y).objectValue &= 0xfffd;
            getTileValue(x - 1, y).objectValue &= 65535 - 8;
        } else if (dir == 2) {
            getTileValue(x, y).objectValue &= 0xffef;
        } else if (dir == 3) {
            getTileValue(x, y).objectValue &= 0xffdf;
        }
    }

    /**
     * Removes an object from the server
     */
    public void unregisterGameObject(GameObject o) {
        o.remove();
        setLocation(o, o.getLocation(), null);
        switch (o.getType()) {
        case 0:
            unregisterObject(o);
            break;
        case 1:
            unregisterDoor(o);
            break;
        }
    }

    /**
     * Removes an item from the server
     */
    public void unregisterItem(Item i) {
        i.remove();
        setLocation(i, i.getLocation(), null);
    }

    /**
     * Removes an npc from the server
     */
    public void unregisterNpc(NPC n) {
        if (hasNpc(n)) {
            npcs.remove(n);
        }
        setLocation(n, n.getLocation(), null);
    }

    /**
     * Removes an object from the map
     */
    private void unregisterObject(GameObject o) {
        if (o.getGameObjectDef().getType() != 1 && o.getGameObjectDef().getType() != 2) {
            return;
        }
        int dir = o.getDirection();
        int width, height;
        if (dir == 0 || dir == 4) {
            width = o.getGameObjectDef().getWidth();
            height = o.getGameObjectDef().getHeight();
        } else {
            height = o.getGameObjectDef().getWidth();
            width = o.getGameObjectDef().getHeight();
        }
        for (int x = o.getX(); x < o.getX() + width; x++) {
            for (int y = o.getY(); y < o.getY() + height; y++) {
                if (o.getGameObjectDef().getType() == 1) {
                    getTileValue(x, y).objectValue &= 0xffbf;
                } else if (dir == 0) {
                    getTileValue(x, y).objectValue &= 0xfffd;
                    getTileValue(x - 1, y).objectValue &= 65535 - 8;
                } else if (dir == 2) {
                    getTileValue(x, y).objectValue &= 0xfffb;
                    getTileValue(x, y + 1).objectValue &= 65535 - 1;
                } else if (dir == 4) {
                    getTileValue(x, y).objectValue &= 0xfff7;
                    getTileValue(x + 1, y).objectValue &= 65535 - 2;
                } else if (dir == 6) {
                    getTileValue(x, y).objectValue &= 0xfffe;
                    getTileValue(x, y - 1).objectValue &= 65535 - 4;
                }
            }
        }
    }

    /**
     * Are the given coords within the world boundaries
     */
    public boolean withinWorld(int x, int y) {
        return x >= 0 && x < MAX_WIDTH && y >= 0 && y < MAX_HEIGHT;
    }

    /**
     * Finds a specific quest by ID
     *
     * @param q
     * @return
     * @throws IllegalArgumentException when a quest by that ID isn't found
     */
    public QuestInterface getQuest(int q) {
        for (QuestInterface quest : this.getQuests()) {
            if (quest.getQuestID() == q) {
                return quest;
            }
        }
        return null;
    }
    
}