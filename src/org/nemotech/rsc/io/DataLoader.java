package org.nemotech.rsc.io;

import org.nemotech.rsc.external.definition.*;
import org.nemotech.rsc.external.definition.extra.*;
import org.nemotech.rsc.external.location.*;
import org.nemotech.rsc.Constants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
//import java.util.zip.GZIPInputStream;

/**
 * 
 * Loads data files in the (GZIP) compressed JSON format into memory.
 * 
 */
public class DataLoader {
    
    private Gson gson;
    
    private Map<String, File> files;
    
    public DataLoader() {
        gson = new GsonBuilder().setPrettyPrinting().generateNonExecutableJson().create();
        files = new HashMap<>();
        populate();
    }

    private <T> T load(String ident, Type type) throws Exception {
        File dataFile = files.get(ident);
        //BufferedInputStream inputStream = new BufferedInputStream(new GZIPInputStream(new FileInputStream(dataFile)));
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(dataFile)); 
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        //System.out.print("loaded: " + ident);
        return (T) gson.fromJson(new JsonReader(inputStreamReader), type);
    }
    
    private void addToFileMap(String ident) {
        files.put(ident, wrap(ident));
    }

    private File wrap(String file) { // shortens code and helps prevent typos
        return new File(Constants.CACHE_DIRECTORY + "data" + File.separator, file);// + ".dat");
    }
    
    private void populate() {
        addToFileMap("object_loc");
        addToFileMap("item_loc");
        addToFileMap("npc_loc");
        addToFileMap("certer_def");
        addToFileMap("tile_def");
        addToFileMap("elev_def");
        addToFileMap("object_def");
        addToFileMap("door_def");
        addToFileMap("item_def");
        addToFileMap("prayer_def");
        addToFileMap("spell_def");
        addToFileMap("npc_def");
        addToFileMap("npc_drop_def");
        addToFileMap("anim_def");
        addToFileMap("texture_def");
        addToFileMap("item_affected_def");
        addToFileMap("wieldable_def");
        addToFileMap("food_def");
        addToFileMap("smithing_def");
        addToFileMap("smelting_def");
        addToFileMap("mining_def");
        addToFileMap("crafting_def");
        addToFileMap("fishing_def");
        addToFileMap("cooking_def");
        addToFileMap("herb_def");
        addToFileMap("herb_second_def");
        addToFileMap("herb_unid_def");
        addToFileMap("arrowhead_def");
        //addToFileMap("bowstring_def"); // todo: refer to EntityManager.java 'todo'
        addToFileMap("darthead_def");
        addToFileMap("logcut_def");
        addToFileMap("gem_def");
    }
    
    public ItemCraftingDef[] loadItemCraftingDefs() throws Exception {
        return load("crafting_def", new TypeToken<ItemCraftingDef[]>() {}.getType());
    }
    
    public Map<Integer, ObjectFishingDef[]> loadObjectFishDefs() throws Exception {
        return load("fishing_def", new TypeToken<Map<Integer, ObjectFishingDef[]>>() {}.getType());
    }
    
    public Map<Integer, ItemCookingDef> loadItemCookingDefs() throws Exception {
        return load("cooking_def", new TypeToken<Map<Integer, ItemCookingDef>>() {}.getType());
    }
    
    public ItemHerbSecond[] loadItemHerbSecondDefs() throws Exception {
        return load("herb_second_def", new TypeToken<ItemHerbSecond[]>() {}.getType());
    }
    
    public Map<Integer, ItemHerbDef> loadItemHerbDefs() throws Exception {
        return load("herb_def", new TypeToken<Map<Integer, ItemHerbDef>>() {}.getType());
    }
    
    public Map<Integer, ItemUnIdentHerbDef> loadItemUnIdentHerbDefs() throws Exception {
        return load("herb_unid_def", new TypeToken<Map<Integer, ItemUnIdentHerbDef>>() {}.getType());
    }
    
    public Map<Integer, ItemArrowHeadDef> loadItemArrowHeadDefs() throws Exception {
        return load("arrowhead_def", new TypeToken<Map<Integer, ItemArrowHeadDef>>() {}.getType());
    }
    
    //public Map<Integer, ItemBowStringDef> loadItemBowStringDefs() throws Exception {
    //  return load("bowstring_def", new TypeToken<Map<Integer, ItemBowStringDef>>() {}.getType());
    //}
    
    public Map<Integer, ItemDartTipDef> loadItemDartTipDefs() throws Exception {
        return load("darthead_def", new TypeToken<Map<Integer, ItemDartTipDef>>() {}.getType());
    }
    
    public Map<Integer, ItemLogCutDef> loadItemLogCutDefs() throws Exception {
        return load("logcut_def", new TypeToken<Map<Integer, ItemLogCutDef>>() {}.getType());
    }
    
    public Map<Integer, ItemGemDef> loadItemGemDefs() throws Exception {
        return load("gem_def", new TypeToken<Map<Integer, ItemGemDef>>() {}.getType());
    }
    
    public GameObjectLoc[] loadGameObjectLocs() throws Exception {
        return load("object_loc", new TypeToken<GameObjectLoc[]>() {}.getType());
    }

    public ItemLoc[] loadItemLocs() throws Exception {
        return load("item_loc", new TypeToken<ItemLoc[]>() {}.getType());
    }

    public NPCLoc[] loadNPCLocs() throws Exception {
        return load("npc_loc", new TypeToken<NPCLoc[]>() {}.getType());
    }

    public Map<Integer, CerterDef> loadCerterDefs() throws Exception {
        return load("certer_def", new TypeToken<Map<Integer, CerterDef>>() {}.getType());
    }

    public TileDef[] loadTileDefs() throws Exception {
        return load("tile_def", new TypeToken<TileDef[]>() {}.getType());
    }

    public ElevationDef[] loadElevationDefs() throws Exception {
        return load("elev_def", new TypeToken<ElevationDef[]>() {}.getType());
    }

    public GameObjectDef[] loadGameObjectDefs() throws Exception {
        return load("object_def", new TypeToken<GameObjectDef[]>() {}.getType());
    }

    public DoorDef[] loadDoorDefs() throws Exception {
        return load("door_def", new TypeToken<DoorDef[]>() {}.getType());
    }

    public ItemDef[] loadItemDefs() throws Exception {
        return load("item_def", new TypeToken<ItemDef[]>() {}.getType());
    }

    public PrayerDef[] loadPrayerDefs() throws Exception {
        return load("prayer_def", new TypeToken<PrayerDef[]>() {}.getType());
    }

    public SpellDef[] loadSpellDefs() throws Exception {
        return load("spell_def", new TypeToken<SpellDef[]>() {}.getType());
    }

    public NPCDef[] loadNPCDefs() throws Exception {
        return load("npc_def", new TypeToken<NPCDef[]>() {}.getType());
    }
    
    public NPCDropDef[] loadNPCDropDefs() throws Exception {
        return load("npc_drop_def", new TypeToken<NPCDropDef[]>() {}.getType());
    }

    public AnimationDef[] loadAnimationDefs() throws Exception {
        return load("anim_def", new TypeToken<AnimationDef[]>() {}.getType());
    }

    public TextureDef[] loadTextureDefs() throws Exception {
        return load("texture_def", new TypeToken<TextureDef[]>() {}.getType());
    }

    public Map<Integer, int[]> loadItemAffectedTypes() throws Exception {
        return load("item_affected_def", new TypeToken<Map<Integer, int[]>>() {}.getType());
    }

    public Map<Integer, ItemWieldableDef> loadItemWieldableDefs() throws Exception {
        return load("wieldable_def", new TypeToken<Map<Integer, ItemWieldableDef>>() {}.getType());
    }

    public Map<Integer, Integer> loadItemEdibleHeals() throws Exception {
        return load("food_def", new TypeToken<Map<Integer, Integer>>() {}.getType());
    }

    public Map<Integer, ItemSmeltingDef> loadItemSmeltingDefs() throws Exception {
        return load("smelting_def", new TypeToken<Map<Integer, ItemSmeltingDef>>() {}.getType());
    }

    public ItemSmithingDef[] loadItemSmithingDefs() throws Exception {
        return load("smithing_def", new TypeToken<ItemSmithingDef[]>() {}.getType());
    }

    public Map<Integer, ObjectMiningDef> loadObjectMiningDefs() throws Exception {
        return load("mining_def", new TypeToken<Map<Integer, ObjectMiningDef>>() {}.getType());
    }

    public void dispose() {
        gson = null;
    }

}
