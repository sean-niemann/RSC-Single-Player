package org.nemotech.rsc;

import java.io.File;

public class Constants {
    
    public static final double VERSION = 2.4;
    
    public static int EXPERIENCE_MULTIPLIER = 8;

    public static final boolean MEMBER_WORLD = true;
    
    public static final boolean DEBUG_PLUGINS = false;

    public static final String MEMBERS_ONLY_MESSAGE = "This feature is only available on a members server";
    
    public static final String APPLICATION_TITLE = "RSC Single Player v" + VERSION;

    public static final int APPLICATION_WIDTH = 950, APPLICATION_HEIGHT = 500;
    
    public static final boolean APPLICATION_RESIZABLE = true;
    
    public static final String CACHE_DIRECTORY = System.getProperty("user.dir") + File.separator + "cache" + File.separator; // Java only
    
    //public static final String CACHE_DIRECTORY = "/app/cache/"; // JS only

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