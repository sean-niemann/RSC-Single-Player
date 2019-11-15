package org.nemotech.rsc.model.landscape;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.nemotech.rsc.model.Point;
import org.nemotech.rsc.model.Entity;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.Mob;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;

public class RegionManager {

    public static final int REGION_SIZE = 48;

    private static final int HORIZONTAL_PLANES = (World.MAX_WIDTH / REGION_SIZE) + 1;

    private static final int VERTICAL_PLANES = (World.MAX_HEIGHT / REGION_SIZE) + 1;

    private static final Region[][] regions = new Region[HORIZONTAL_PLANES][VERTICAL_PLANES];

    static {
        for (int x = 0; x < HORIZONTAL_PLANES; x++) {
            for (int y = 0; y < VERTICAL_PLANES; y++) {
                regions[x][y] = new Region();
            }
        }
    }

    /**
     * Gets the local players around an entity.
     * 
     * @param entity
     *            The entity.
     * @return The collection of local players.
     */
    public static Collection<Player> getLocalPlayers(final Entity entity) {
        LinkedHashSet<Player> localPlayers = new LinkedHashSet<>();
        for (Region region : getSurroundingRegions(entity.getLocation())) {
            for (Player player : region.getPlayers()) {
                if (player.withinRange(entity)) {
                    localPlayers.add(player);
                }
            }
        }
        return localPlayers;
    }

    /**
     * Gets the local NPCs around an entity.
     * 
     * @param entity
     *            The entity.
     * @return The collection of local NPCs.
     */
    public static Collection<NPC> getLocalNpcs(Entity entity) {
        LinkedHashSet<NPC> localNpcs = new LinkedHashSet<>();
        for (Region region : getSurroundingRegions(entity.getLocation())) {
            for (NPC npc : region.getNpcs()) {
                if (npc.withinRange(entity)) {
                    localNpcs.add(npc);
                }
            }
        }
        return localNpcs;
    }

    public static Collection<GameObject> getLocalObjects(Mob entity) {
        LinkedHashSet<GameObject> localObjects = new LinkedHashSet<>();
        for (Region region : getSurroundingRegions(entity.getLocation())) {
            for (GameObject o : region.getGameObjects()) {
                if (o.getLocation().withinRange(entity.getLocation(), 15)) {
                    localObjects.add(o);
                }
            }
        }
        return localObjects;
    }

    public static Collection<Item> getLocalGroundItems(Mob entity) {
        LinkedHashSet<Item> localItems = new LinkedHashSet<>();
        for (Region region : getSurroundingRegions(entity.getLocation())) {
            for (Item o : region.getGroundItems()) {
                if (o.getLocation().withinRange(entity.getLocation(), 15)) {
                    localItems.add(o);
                }
            }
        }
        return localItems;
    }

    /**
     * Gets the regions surrounding a location.
     * 
     * @param location
     *            The location.
     * @return The regions surrounding the location.
     */
    public static LinkedHashSet<Region> getSurroundingRegions(Point location) {
        int regionX = location.getX() / REGION_SIZE;
        int regionY = location.getY() / REGION_SIZE;

        LinkedHashSet<Region> surrounding = new LinkedHashSet<>();
        surrounding.add(regions[regionX][regionY]);
        int[] xMod = {-1, +1, -1, 0, +1, 0, -1, +1};
        int[] yMod = {-1, +1, 0, -1, 0, +1, +1, -1};
        for(int i = 0; i < xMod.length;i++) {
            Region tmpRegion = getRegionFromSectorCoordinates(regionX + xMod[i], regionY+ yMod[i]);
            if(tmpRegion != null) {
                surrounding.add(tmpRegion);
            }
        }
        return surrounding;
    }

    private static Region getRegionFromSectorCoordinates(int regionX, int regionY) {
        if(regionX < 0 || regionY < 0 || regionX >= regions.length || regionY >= regions[regionX].length) {
            return null;
        }
        return regions[regionX][regionY];
    }

    public static Region getRegion(int x, int y) {
        int regionX = x / REGION_SIZE;
        int regionY = y / REGION_SIZE;

        return regions[regionX][regionY];
    }

    public static Region getRegion(Point objectCoordinates) {
        return getRegion(objectCoordinates.getX(), objectCoordinates.getY());
    }
    
}
