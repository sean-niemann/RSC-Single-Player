package org.nemotech.rsc;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.nemotech.rsc.model.Point3D;

public class Constants {
    
    public static final String VERSION = "2.4 Beta";
    
    public static int EXPERIENCE_MULTIPLIER = 8;

    public static final boolean MEMBER_WORLD = true;
    
    public static final boolean DEBUG_PLUGINS = false;

    public static final String MEMBERS_ONLY_MESSAGE = "This feature is only available on a members server";
    
    public static final String APPLICATION_TITLE = "RSC Single Player " + VERSION;

    public static final int APPLICATION_WIDTH = 950, APPLICATION_HEIGHT = 500;
    
    public static final boolean APPLICATION_RESIZABLE = true;
    
    public static final String CACHE_DIRECTORY = System.getProperty("user.dir") + File.separator + "cache" + File.separator; // Java only
    
    //public static final String CACHE_DIRECTORY = "/app/cache/"; // JS only
    
    public static final Map<Point3D, String> MUSIC_REGION_MAP = new HashMap<Point3D, String>() {{
        put(new Point3D(49, 50, 0), "arabian");
        put(new Point3D(49, 49, 0), "arabian_2");
        put(new Point3D(49, 51, 0), "al_kharid");
        put(new Point3D(50, 50, 0), "harmony");
        put(new Point3D(50, 49, 0), "autumn_voyage");
        put(new Point3D(50, 51, 0), "yesteryear");
        put(new Point3D(51, 50, 0), "dream");
        put(new Point3D(52, 50, 0), "unknown_land");
        put(new Point3D(51, 49, 0), "flute_salad");
        put(new Point3D(51, 51, 0), "book_of_spells");
        put(new Point3D(49, 52, 0), "egypt");
        put(new Point3D(49, 53, 0), "desert_voyage");
        put(new Point3D(50, 52, 0), "arabian_3");
        put(new Point3D(50, 53, 0), "the_desert");
        put(new Point3D(51, 53, 0), "the_desert");
        put(new Point3D(52, 51, 0), "vision");
        put(new Point3D(52, 52, 0), "newbie_melody");
        put(new Point3D(53, 51, 0), "tomorrow");
        put(new Point3D(53, 50, 0), "sea_shanty_2");
        put(new Point3D(54, 50, 0), "long_way_home");
        put(new Point3D(54, 51, 0), "attention");
        put(new Point3D(55, 50, 0), "emperor");
        put(new Point3D(55, 49, 0), "miles_away");
        put(new Point3D(54, 49, 0), "nightfall");
        put(new Point3D(53, 49, 0), "wander");
        put(new Point3D(52, 49, 0), "start");
        put(new Point3D(49, 48, 0), "still_night");
        put(new Point3D(48, 48, 0), "venture");
        put(new Point3D(50, 48, 0), "expanse");
        put(new Point3D(51, 48, 0), "greatness");
        put(new Point3D(52, 48, 0), "spooky");
        put(new Point3D(53, 48, 0), "workshop");
        put(new Point3D(54, 48, 0), "fanfare");
        put(new Point3D(55, 48, 0), "arrival");
        put(new Point3D(55, 47, 0), "horizon");
        put(new Point3D(54, 47, 0), "scape_soft");
        put(new Point3D(53, 47, 0), "scape_soft");
        put(new Point3D(52, 47, 0), "barbarianism");
        put(new Point3D(51, 47, 0), "spirit");
        put(new Point3D(50, 47, 0), "garden");
        put(new Point3D(49, 47, 0), "medieval");
        put(new Point3D(48, 47, 0), "lullaby");
        put(new Point3D(49, 46, 0), "parade");
        put(new Point3D(50, 46, 0), "adventure");
        put(new Point3D(51, 46, 0), "doorways");
        put(new Point3D(52, 46, 0), "forever");
        put(new Point3D(53, 46, 0), "alone");
        put(new Point3D(54, 46, 0), "alone");
        put(new Point3D(55, 46, 0), "splendour");
    }};

    // IDs of all undead-type of NPCs. (Used for undead sounds & crumble undead spell)
    public static final int[] UNDEAD_NPCS = { 15, 53, 80, 178, 664, 41, 52, 68, 180, 214, 319, 40, 45, 46, 50, 179, 195 };

    // IDs of all armor-type NPCs. (Used for armor hitting sounds)
    public static final int[] ARMOR_NPCS = { 66, 102, 189, 277, 322, 401, 324, 323, 632, 633, 518, 158 };

    // Combat spells - 30+ magic damage gives you +1 damage, so these damages are -1 the absolute max.. (level, max)
    public static final int[][] COMBAT_SPELLS = {
        { 1, 1 }, { 4, 2 }, { 9, 2 }, { 13, 3 }, { 17, 3 }, { 23, 4 },
        { 29, 4 }, { 35, 5 }, { 41, 5 }, { 47, 6 }, { 53, 6 },
        { 59, 7 }, { 62, 8 }, { 65, 9 }, { 70, 10 }, { 75, 11 }
    };

    // These NPCs are attackable, but do not run on low health such as guards etc.
    public static final int[] NPCS_THAT_DONT_RETREAT = { 65, 102, 100, 127, 258 };
    
}