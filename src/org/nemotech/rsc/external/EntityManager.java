package org.nemotech.rsc.external;

import org.nemotech.rsc.io.DataLoader;

import java.util.ArrayList;
import java.util.Map;

import org.nemotech.rsc.external.definition.*;
import org.nemotech.rsc.external.definition.extra.*;
import org.nemotech.rsc.external.location.*;

/**
 * This class handles the loading of entities from the cache files,
 * and provides methods for relaying these entities to the user.
 */
public class EntityManager {
    
    private static ItemDef[] items;
    private static NPCDef[] npcs;
    private static NPCDropDef[] npcDrops;
    private static GameObjectDef[] objects;
    private static DoorDef[] doors;
    private static PrayerDef[] prayers;
    private static SpellDef[] spells;
    private static TileDef[] tiles;
    private static ElevationDef[] elevations;
    private static AnimationDef[] animations;
    private static TextureDef[] textures;

    private static GameObjectLoc[] objectLocs;
    private static ItemLoc[] itemLocs;
    private static NPCLoc[] npcLocs;
    
    private static ItemCraftingDef[] itemCrafting;
    private static Map<Integer, ObjectFishingDef[]> objectFishing;
    private static Map<Integer, ItemCookingDef> itemCooking;
    
    private static ItemHerbSecond[] herbSeconds;
    private static Map<Integer, ItemHerbDef> itemHerb;
    private static Map<Integer, ItemUnIdentHerbDef> itemUnIdentHerb;
    
    private static Map<Integer, ItemArrowHeadDef> itemArrowHeads;
    //private static Map<Integer, ItemBowStringDef> itemBowStrings;
    private static Map<Integer, ItemDartTipDef> itemDartTips;
    private static Map<Integer, ItemLogCutDef> itemLogCuts;
    
    private static Map<Integer, ItemGemDef> itemGems;
    
    private static Map<Integer, CerterDef> certers;
    private static Map<Integer, Integer> itemEdibleHeals;
    private static Map<Integer, ItemSmeltingDef> itemSmelting;
    private static ItemSmithingDef[] itemSmithing;
    private static Map<Integer, ItemWieldableDef> itemWieldable;
    private static Map<Integer, ObjectMiningDef> objectMining;
    private static Map<Integer, int[]> itemAffectedTypes;
    private static int itemSpriteCount = 0;

    public static void init() {
        try {
            DataLoader loader = new DataLoader();
            objectLocs = loader.loadGameObjectLocs();
            itemLocs = loader.loadItemLocs();
            npcLocs = loader.loadNPCLocs();
            doors = loader.loadDoorDefs();
            objects = loader.loadGameObjectDefs();
            models = new ArrayList<>();
            for (GameObjectDef object : objects) {
                object.modelID = storeModel(object.getModelName());
            }
            npcs = loader.loadNPCDefs();
            for (NPCDef n : npcs) {
                if (n.isAttackable()) {
                    n.respawnTime -= (n.respawnTime / 3);
                }
            }
            npcDrops = loader.loadNPCDropDefs();
            prayers = loader.loadPrayerDefs();
            items = loader.loadItemDefs();
            for (ItemDef item : items) {
                if (item.getSprite() + 1 > itemSpriteCount) {
                    itemSpriteCount = item.getSprite() + 1;
                }
            }
            
            itemCrafting = loader.loadItemCraftingDefs();
            objectFishing = loader.loadObjectFishDefs();
            itemCooking = loader.loadItemCookingDefs();
            
            herbSeconds = loader.loadItemHerbSecondDefs();
            itemHerb = loader.loadItemHerbDefs();
            itemUnIdentHerb = loader.loadItemUnIdentHerbDefs();
            
            itemArrowHeads = loader.loadItemArrowHeadDefs();
            //itemBowStrings = loader.loadItemBowStringDefs();
            itemDartTips = loader.loadItemDartTipDefs();
            itemLogCuts = loader.loadItemLogCutDefs();
            
            itemGems = loader.loadItemGemDefs();
            
            //
            spells = loader.loadSpellDefs();
            tiles = loader.loadTileDefs();
            elevations = loader.loadElevationDefs();
            animations = loader.loadAnimationDefs();
            textures = loader.loadTextureDefs();
            itemWieldable = loader.loadItemWieldableDefs();
            itemEdibleHeals = loader.loadItemEdibleHeals();
            itemSmelting = loader.loadItemSmeltingDefs();
            itemSmithing = loader.loadItemSmithingDefs();
            objectMining = loader.loadObjectMiningDefs();
            certers = loader.loadCerterDefs();
            itemAffectedTypes = loader.loadItemAffectedTypes();
            loader.dispose();
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Exception at data load");
            System.out.println(e.getMessage());
            //System.exit(1);
        }
        //System.out.println("\t[Entity Manager] Done loading entity definitions and locations");
    }
    
    public static ItemCraftingDef getCraftingDef(int id) {
        if (id < 0 || id >= itemCrafting.length) {
            return null;
        }
        return itemCrafting[id];
    }
    
    public static ObjectFishingDef getObjectFishingDef(int id, int click) {
        ObjectFishingDef[] defs = objectFishing.get(id);
        if (defs == null) {
            return null;
        }
        return defs[click];
    }
    
    public static ItemCookingDef getItemCookingDef(int id) {
        return itemCooking.get(id);
    }
    
    /**
     * @param id the entities ID
     * @return the ItemHerbDef with the given ID
     */
    public static ItemHerbDef getItemHerbDef(int id) {
        return itemHerb.get(id);
    }

    /**
     * @return the ItemHerbSecond for the given second ingredient
     */
    public static ItemHerbSecond getItemHerbSecond(int secondID, int unfinishedID) {
        for (ItemHerbSecond def : herbSeconds) {
            if (def.getSecondID() == secondID && def.getUnfinishedID() == unfinishedID) {
                return def;
            }
        }
        return null;
    }
    
    /**
     * @param id the entities ID
     * @return the ItemUnIdentHerbDef with the given ID
     */
    public static ItemUnIdentHerbDef getItemUnIdentHerbDef(int id) {
        return itemUnIdentHerb.get(id);
    }
    
    //
    
    public static ItemArrowHeadDef getItemArrowHeadDef(int id) {
        return itemArrowHeads.get(id);
    }
    
    // TODO: BOWSTRINGS JSON NOT WORKING ON WINDOWS
    //public static ItemBowStringDef getItemBowStringDef(int id) {
    //  return itemBowStrings.get(id);
    //}
    
    public static ItemDartTipDef getItemDartTipDef(int id) {
        return itemDartTips.get(id);
    }
    
    public static ItemLogCutDef getItemLogCutDef(int id) {
        return itemLogCuts.get(id);
    }
    
    public static ItemGemDef getItemGemDef(int id) {
        return itemGems.get(id);
    }
    
    //

    public static GameObjectLoc[] getObjectLocs() {
        return objectLocs;
    }

    public static ItemLoc[] getItemLocs() {
        return itemLocs;
    }

    public static NPCLoc[] getNPCLocs() {
        return npcLocs;
    }
    
    public static CerterDef getCerterDef(int id) {
        return certers.get(id);
    }
    
    public static DoorDef getDoor(int id) {
        if (id < 0 || id >= doors.length) {
            return null;
        }
        return doors[id];
    }
    
    public static GameObjectDef getGameObjectDef(int id) {
        if (id < 0 || id >= objects.length) {
            return null;
        }
        return objects[id];
    }
    
    public static GameObjectDef[] getGameObjectDefs() {
        return objects;
    }
    
    public static DoorDef[] getDoorDefs() {
        return doors;
    }
    
    public static int getItemSpriteCount() {
        return itemSpriteCount;
    }
    
    public static ArrayList<String> models;
    
    public static int getModelCount() {
        return models.size();
    }

    public static String getModelName(int id) {
        if (id < 0 || id >= models.size()) {
            return null;
        }
        return models.get(id);
    }
    
    public static int storeModel(String name) {
        if(name == null) return 5;
        if (name.equalsIgnoreCase("na")) {
            return 0;
        }
        int index = models.indexOf(name);
        if (index < 0) {
            models.add(name);
            return models.size() - 1;
        }
        return index;
    }
    
    public static int[] getItemAffectedTypes(int type) {
        return itemAffectedTypes.get(type);
    }
    
    public static ItemDef[] getItems() {
        return items;
    }
    
    public static NPCDef[] getNPCs() {
        return npcs;
    }
    
    public static NPCDropDef[] getNPCDropsForID(int id) {
        NPCDropDef[] drops = new NPCDropDef[50];
        int i = 0;
        for(NPCDropDef drop : npcDrops) {
            if(drop.getNpcID() == id) {
                drops[i++] = drop;
            }
        }
        return drops;
    }
    
    public static ItemDef getItem(int id) {
        if (id < 0 || id >= items.length) {
            return null;
        }
        return items[id];
    }
    
    public static int getItemEdibleHeals(int id) {
        Integer heals = itemEdibleHeals.get(id);
        if (heals != null) {
            return heals;
        }
        return 0;
    }
    
    public static ItemSmeltingDef getItemSmeltingDef(int id) {
        return itemSmelting.get(id);
    }
    
    public static ItemWieldableDef getItemWieldableDef(int id) {
        return itemWieldable.get(id);
    }
    
    public static NPCDef getNPC(int id) {
        if (id < 0 || id >= npcs.length) {
            return null;
        }
        return npcs[id];
    }
    
    public static NPCDropDef getNPCDrop(int id) {
        if (id < 0 || id >= npcDrops.length) {
            return null;
        }
        return npcDrops[id];
    }
    
    public static ObjectMiningDef getObjectMiningDef(int id) {
        return objectMining.get(id);
    }
    
    public static PrayerDef[] getPrayers() {
        return prayers;
    }
    
    public static PrayerDef getPrayer(int id) {
        if (id < 0 || id >= prayers.length) {
            return null;
        }
        return prayers[id];
    }
    
    public static ItemSmithingDef getSmithingDef(int id) {
        if (id < 0 || id >= itemSmithing.length) {
            return null;
        }
        return itemSmithing[id];
    }
    
    public static ItemSmithingDef getSmithingDefbyID(int itemID) {
        for (ItemSmithingDef i : itemSmithing) {
            if (i.itemID == itemID)
                return i;
        }
        return null;
    }
    
    public static SpellDef[] getSpells() {
        return spells;
    }
    
    public static SpellDef getSpell(int id) {
        if (id < 0 || id >= spells.length) {
            return null;
        }
        return spells[id];
    }
    
    public static TileDef getTile(int id) {
        if (id < 0 || id >= tiles.length) {
            return null;
        }
        return tiles[id];
    }
    
    public static ElevationDef getElevation(int id) {
        if (id < 0 || id >= elevations.length) {
            return null;
        }
        return elevations[id];
    }
    
    public static AnimationDef[] getAnimations() {
        return animations;
    }
    
    public static AnimationDef getAnimation(int id) {
        if (id < 0 || id >= animations.length) {
            return null;
        }
        return animations[id];
    }
    
    public static TextureDef[] getTextures() {
        return textures;
    }
    
    public static TextureDef getTexture(int id) {
        if (id < 0 || id >= textures.length) {
            return null;
        }
        return textures[id];
    }
    
}