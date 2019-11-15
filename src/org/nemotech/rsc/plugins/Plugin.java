package org.nemotech.rsc.plugins;

import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.ChatMessage;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.MenuHandler;
import org.nemotech.rsc.model.Mob;
import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.event.impl.FightEvent;
import org.nemotech.rsc.event.SingleEvent;
import org.nemotech.rsc.event.impl.extra.UndergroundPassMessageEvent;
import org.nemotech.rsc.model.player.states.Action;
import org.nemotech.rsc.model.player.states.CombatState;
import org.nemotech.rsc.util.Formulae;
import org.nemotech.rsc.external.location.GameObjectLoc;
import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.client.sound.SoundEffect;
import org.nemotech.rsc.model.Point;
import org.nemotech.rsc.model.landscape.TileValue;

public class Plugin {
    
    public static final int ATTACK = 0;
    public static final int DEFENSE = 1;
    public static final int STRENGTH = 2;
    public static final int HITS = 3;
    public static final int RANGED = 4;
    public static final int PRAYER = 5;
    public static final int MAGIC = 6;
    public static final int COOKING = 7;
    public static final int WOODCUTTING = 8;
    public static final int FLETCHING = 9;
    public static final int FISHING = 10;
    public static final int FIREMAKING = 11;
    public static final int CRAFTING = 12;
    public static final int SMITHING = 13;
    public static final int MINING = 14;
    public static final int HERBLAW = 15;
    public static final int AGILITY = 16;
    public static final int THIEVING = 17;

    public static final int BLACK_KNIGHTS_FORTRESS = 0;
    public static final int COOKS_ASSISTANT = 1;
    public static final int DEMON_SLAYER = 2;
    public static final int DORICS_QUEST = 3;
    public static final int THE_RESTLESS_GHOST = 4;
    public static final int GOBLIN_DIPLOMACY = 5;
    public static final int ERNEST_THE_CHICKEN = 6;
    public static final int IMP_CATCHER = 7;
    public static final int PIRATES_TREASURE = 8;
    public static final int PRINCE_ALI_RESCUE = 9;
    public static final int ROMEO_N_JULIET = 10;
    public static final int SHEEP_SHEARER = 11;
    public static final int SHIELD_OF_ARRAV = 12;
    public static final int THE_KNIGHTS_SWORD = 13;
    public static final int VAMPIRE_SLAYER = 14;
    public static final int WITCHS_POTION = 15;
    public static final int DRAGON_SLAYER = 16;
    public static final int WITCHS_HOUSE = 17;
    public static final int LOST_CITY = 18;
    public static final int HEROS_QUEST = 19;
    public static final int DRUIDIC_RITUAL = 20;
    public static final int MERLINS_CRYSTAL = 21;
    public static final int SCORPION_CATCHER = 22;
    public static final int FAMILY_CREST = 23;
    public static final int TRIBAL_TOTEM = 24;
    public static final int FISHING_CONTEST = 25;
    public static final int MONKS_FRIEND = 26;
    public static final int TEMPLE_OF_IKOV = 27;
    public static final int CLOCK_TOWER = 28;
    public static final int THE_HOLY_GRAIL = 29;
    public static final int FIGHT_ARENA = 30;
    public static final int TREE_GNOME_VILLAGE = 31;
    public static final int THE_HAZEEL_CULT = 32;
    public static final int SHEEP_HERDER = 33;
    public static final int PLAGUE_CITY = 34;
    public static final int SEA_SLUG = 35;
    public static final int WATERFALL_QUEST = 36;
    public static final int BIOHAZARD = 37;
    public static final int JUNGLE_POTION = 38;
    public static final int GRAND_TREE = 39;
    public static final int SHILO_VILLAGE = 40;
    public static final int UNDERGROUND_PASS = 41;
    public static final int OBSERVATORY_QUEST = 42;
    public static final int TOURIST_TRAP = 43;
    public static final int WATCHTOWER = 44;
    public static final int DWARF_CANNON = 45;
    public static final int MURDER_MYSTERY = 46;
    public static final int DIGSITE = 47;
    public static final int GERTRUDES_CAT = 48;
    public static final int LEGENDS_QUEST = 49;
    
    public static void doWallMovePlayer(final GameObject object, final Player p, int replaceID, int delay, boolean removeObject) {
        p.setBusyTimer(650);
        /* For the odd looking walls. */
        if(removeObject) {
            GameObject newObject = new GameObject(object.getLocation(), replaceID, object.getDirection(), object.getType());
            if(object.getID() == replaceID) {
                p.message("Nothing interesting happens");
                return;
            }
            if (replaceID == -1) {
                removeObject(object);
            } else {
                replaceObject(object, newObject);
            }
            delayedSpawnObject(object.getLoc(), delay);
        }
        if (object.getDirection() == 0) {
            if (object.getLocation().equals(p.getLocation())) {
                movePlayer(p, object.getX(), object.getY() - 1);
            } else {
                movePlayer(p, object.getX(), object.getY());
            }
        }
        if (object.getDirection() == 1) {
            if (object.getLocation().equals(p.getLocation())) {
                movePlayer(p, object.getX() - 1, object.getY());
            } else {
                movePlayer(p, object.getX(), object.getY());
            }
        }
        if (object.getDirection() == 2) {
            // DIAGONAL
            // front
            if (object.getX() == p.getX() && object.getY() == p.getY() + 1) {
                movePlayer(p, object.getX(), object.getY() + 1);
            } 
            else if (object.getX() == p.getX() - 1 && object.getY() == p.getY()) {
                movePlayer(p, object.getX() - 1, object.getY());
            }
            // back
            else if (object.getX() == p.getX() && object.getY() == p.getY() - 1) {
                movePlayer(p, object.getX(), object.getY() - 1);
            } 
            else if (object.getX() == p.getX() + 1 && object.getY() == p.getY()) {
                movePlayer(p, object.getX() + 1, object.getY());
            }
            else if (object.getX() == p.getX() + 1 && object.getY() == p.getY() + 1) {
                movePlayer(p, object.getX() + 1, object.getY() + 1);
            } 
            else if (object.getX() == p.getX() - 1 && object.getY() == p.getY() - 1) {
                movePlayer(p, object.getX() - 1, object.getY() - 1);
            }
        }
        if (object.getDirection() == 3) {

            // front
            if (object.getX() == p.getX() && object.getY() == p.getY() - 1) {
                movePlayer(p, object.getX(), object.getY() - 1);
            } else if (object.getX() == p.getX() + 1 && object.getY() == p.getY()) {
                movePlayer(p, object.getX() + 1, object.getY());
            }

            // back
            else if (object.getX() == p.getX() && object.getY() == p.getY() + 1) {
                movePlayer(p, object.getX(), object.getY() + 1);
            } else if (object.getX() == p.getX() - 1 && object.getY() == p.getY()) {
                movePlayer(p, object.getX() - 1, object.getY());
            } else if(object.getX() == p.getX() - 1 && object.getY() == p.getY() + 1) {
                movePlayer(p, object.getX() - 1, object.getY() + 1);
            } else if(object.getX() == p.getX() + 1 && object.getY() == p.getY() - 1) {
                movePlayer(p, object.getX() + 1, object.getY() - 1);
            }
        }
    }
    
    public static NPC spawnNpc(int id, int x, int y, final int time, final Player spawnedFor) {
        final NPC npc = new NPC(id, x, y);
        npc.setShouldRespawn(false);
        npc.setAttribute("spawnedFor", spawnedFor);
        World.getWorld().registerNpc(npc);
        World.getWorld().getDelayedEventHandler().add(new SingleEvent(null, time) {
            @Override
            public void action() {
                npc.remove();
            }
        });
        return npc;
    }
    public static NPC spawnNpc(int id, int x, int y) {
        final NPC npc = new NPC(id, x, y);
        npc.setShouldRespawn(false);
        World.getWorld().registerNpc(npc);
        return npc;
    }
    
    public static NPC spawnNpcWithRadius(int id, int x, int y, int radius, final int time) {
        final NPC npc = new NPC(id, x, y, radius);
        npc.setShouldRespawn(false);
        World.getWorld().registerNpc(npc);
        World.getWorld().getDelayedEventHandler().add(new SingleEvent(null, time) {
            @Override
            public void action() {
                npc.remove();
            }
        });
        return npc;
    }

    public static NPC spawnNpc(int id, int x, int y, final int time) {
        final NPC npc = new NPC(id, x, y);
        npc.setShouldRespawn(false);
        World.getWorld().registerNpc(npc);
        World.getWorld().getDelayedEventHandler().add(new SingleEvent(null, time) {
            @Override
            public void action() {
                npc.remove();
            }
        });
        return npc;
    }
    
    public static void npcWalkFromPlayer(Player player, NPC n) {
        if (player.getLocation().equals(n.getLocation())) {
            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y <= 1; ++y) {
                    if(x == 0 || y == 0) 
                        continue;
                    Point destination = canWalk(n, player.getX() - x, player.getY() - y);
                    if (destination != null && destination.inBounds(n.getLoc().minX, n.getLoc().minY, n.getLoc().maxY, n.getLoc().maxY)) {
                        //n.walk(destination.getX(), destination.getY());
                        n.teleport(destination.getX(), destination.getY());
                        break;
                    }
                }
            }
        }
    }
    
    public static void doTentDoor(final GameObject object, final Player p) {
        p.setBusyTimer(650);
        if (object.getDirection() == 0) {
            if (object.getLocation().equals(p.getLocation())) {
                movePlayer(p, object.getX(), object.getY() - 1);
            } else {
                movePlayer(p, object.getX(), object.getY());
            }
        }
        if (object.getDirection() == 1) {
            if (object.getLocation().equals(p.getLocation())) {
                movePlayer(p, object.getX() - 1, object.getY());
            } else {
                movePlayer(p, object.getX(), object.getY());
            }
        }
        if (object.getDirection() == 2) {
            // DIAGONAL
            // front
            if (object.getX() == p.getX() && object.getY() == p.getY() + 1) {
                movePlayer(p, object.getX(), object.getY() + 1);
            } 
            else if (object.getX() == p.getX() - 1 && object.getY() == p.getY()) {
                movePlayer(p, object.getX() - 1, object.getY());
            }
            // back
            else if (object.getX() == p.getX() && object.getY() == p.getY() - 1) {
                movePlayer(p, object.getX(), object.getY() - 1);
            } 
            else if (object.getX() == p.getX() + 1 && object.getY() == p.getY()) {
                movePlayer(p, object.getX() + 1, object.getY());
            }
            else if (object.getX() == p.getX() + 1 && object.getY() == p.getY() + 1) {
                movePlayer(p, object.getX() + 1, object.getY() + 1);
            } 
            else if (object.getX() == p.getX() - 1 && object.getY() == p.getY() - 1) {
                movePlayer(p, object.getX() - 1, object.getY() - 1);
            }
        }
        if (object.getDirection() == 3) {

            // front
            if (object.getX() == p.getX() && object.getY() == p.getY() - 1) {

                movePlayer(p, object.getX(), object.getY() - 1);
            } else if (object.getX() == p.getX() + 1 && object.getY() == p.getY()) {
                movePlayer(p, object.getX() + 1, object.getY());
            }

            // back
            else if (object.getX() == p.getX() && object.getY() == p.getY() + 1) {
                movePlayer(p, object.getX(), object.getY() + 1);
            } else if (object.getX() == p.getX() - 1 && object.getY() == p.getY()) {
                movePlayer(p, object.getX() - 1, object.getY());
            }

        }
    }
    
    public static int[] coordModifier(Player player, boolean up, GameObject object) {
        if (object.getGameObjectDef().getHeight() <= 1) {
            return new int[] { player.getX(), Formulae.getNewY(player.getY(), up) };
        }
        int[] coords = { object.getX(), Formulae.getNewY(object.getY(), up) };
        switch (object.getDirection()) {
        case 0:
            coords[1] -= (up ? -object.getGameObjectDef().getHeight() : 1);
            break;
        case 2:
            coords[0] -= (up ? -object.getGameObjectDef().getHeight() : 1);
            break;
        case 4:
            coords[1] += (up ? -1 : object.getGameObjectDef().getHeight());
            break;
        case 6:
            coords[0] += (up ? -1 : object.getGameObjectDef().getHeight());
            break;
        }
        return coords;
    }
    
    public static void temporaryRemoveNpc(final NPC n) {
        n.setShouldRespawn(true);
        n.remove();
    }
    
    public static void setCurrentLevel(Player p, int skill, int level) {
        p.setCurStat(skill, level);
        p.getSender().sendStats();
    }
    
    public static boolean isWielding(Player p, int i) {
        return p.getInventory().wielding(i);
    }
    
    public static int getWoodcutAxe(Player p) {
        int axeId = -1;
        for (final int a : Formulae.HATCHETS) {
            if (p.getInventory().countId(a) > 0) {
                axeId = a;
                break;
            }
        }
        return axeId;
    }
    
    /**
     * Checks if this @param obj id is @param i
     * 
     * @param obj
     * @param i
     * @return
     */
    public static boolean isObject(GameObject obj, int i) {
        return obj.getID() == i;
    }
    
    public static void completeQuest(Player p, QuestInterface quest) {
        p.sendQuestComplete(quest.getQuestID());
    }
    
    public static boolean hasItemAtAll(Player p, int id) {
        return p.getBank().contains(new InvItem(id)) || p.getInventory().contains(new InvItem(id));
    }
    
    /**
     * Transforms npc into another please note that you will need to unregister
     * the transformed npc after using this method.
     * 
     * @param n
     * @param newID
     * @return
     */
    public static NPC transform(final NPC n, final int newID, boolean onlyShift) {
        final NPC newNpc = new NPC(newID, n.getX(), n.getY());
        newNpc.setShouldRespawn(false);
        World.getWorld().registerNpc(newNpc);
        if(onlyShift)  {
            n.setShouldRespawn(false);
        }
        n.remove();
        return newNpc;
    }
    
    public static void closeCupboard(GameObject obj, Player p, int cupboardID) {
        replaceObject(obj, new GameObject(obj.getLocation(), cupboardID, obj.getDirection(), obj.getType()));
        p.message("You close the cupboard");
    }

    public static void openCupboard(GameObject obj, Player p, int cupboardID) {
        replaceObject(obj, new GameObject(obj.getLocation(), cupboardID, obj.getDirection(), obj.getType()));
        p.message("You open the cupboard");
    }
    
    public static void doLedge(final GameObject object, final Player p, int damage) {
        p.setBusyTimer(650);
        p.message("you climb the ledge");
        boolean failLedge = false;
        int random = Util.getRandom().nextInt(10);
        if (random == 5) {
            failLedge = true;
        } else {
            failLedge = false;
        }
        if (object != null && !failLedge) {
            if (object.getDirection() == 2 || object.getDirection() == 6) {
                if (object.getX() == p.getX() - 1 && object.getY() == p.getY()) { // X
                    if (object.getID() == 753) {
                        p.message("and drop down to the cave floor");
                        movePlayer(p, object.getX() - 2, object.getY());
                    } else {
                        p.message("and drop down to the cave floor");
                        movePlayer(p, object.getX() - 1, object.getY());
                    }
                } else if (object.getX() == p.getX() + 1 && object.getY() == p.getY()) { // Y
                    if (object.getID() == 753) {
                        p.message("and drop down to the cave floor");
                        movePlayer(p, object.getX() + 2, object.getY());
                    } else {
                        p.message("and drop down to the cave floor");
                        movePlayer(p, object.getX() + 1, object.getY());
                    }
                }
            }
            if (object.getDirection() == 4 || object.getDirection() == 0) {
                if (object.getX() == p.getX() && object.getY() == p.getY() + 1) { // X
                    movePlayer(p, object.getX(), object.getY() + 1);
                    p.message("and drop down to the cave floor");
                } else if (object.getX() == p.getX() && object.getY() == p.getY() - 1) { // Y
                    movePlayer(p, object.getX(), object.getY() - 1);
                }
            }
        } else {
            p.message("but you slip");
            p.damage(damage);
            playerTalk(p, null, "aargh");
        }
    }

    public static void doRock(final GameObject object, final Player p, int damage, boolean eventMessage,
            int spikeLocation) {
        p.setBusyTimer(650);
        p.message("you climb onto the rock");
        boolean failRock = false;
        int random = Util.getRandom().nextInt(5);
        if (random == 4) {
            failRock = true;
        } else {
            failRock = false;
        }
        if (object != null && !failRock) {
            if (object.getDirection() == 1 || object.getDirection() == 2 || object.getDirection() == 4
                    || object.getDirection() == 3) {
                if (object.getX() == p.getX() - 1 && object.getY() == p.getY()) { // X
                    movePlayer(p, object.getX() - 1, object.getY());
                } else if (object.getX() == p.getX() + 1 && object.getY() == p.getY()) { // Y
                    movePlayer(p, object.getX() + 1, object.getY());
                } else if (object.getX() == p.getX() && object.getY() == p.getY() + 1) { // left
                    // side
                    if (object.getID() == 749) {
                        movePlayer(p, object.getX(), object.getY() + 1);
                    } else {
                        movePlayer(p, object.getX() + 1, object.getY());
                    }
                } else if (object.getX() == p.getX() && object.getY() == p.getY() - 1) { // right
                    // side.
                    if (object.getID() == 749) {
                        movePlayer(p, object.getX(), object.getY() - 1);
                    } else {
                        movePlayer(p, object.getX() + 1, object.getY());
                    }
                }
            }
            if (object.getDirection() == 6) {
                if (object.getX() == p.getX() && object.getY() == p.getY() + 1) { // left
                    // side
                    movePlayer(p, object.getX(), object.getY() + 1);
                } else if (object.getX() == p.getX() && object.getY() == p.getY() - 1) { // right
                    // side.
                    movePlayer(p, object.getX(), object.getY() - 1);
                } else if (object.getX() == p.getX() - 1 && object.getY() == p.getY()) {
                    movePlayer(p, object.getX() + 1, object.getY() + 1);
                } else if (object.getX() == p.getX() + 1 && object.getY() == p.getY()) {
                    movePlayer(p, object.getX(), object.getY() + 1);
                }
            }
            if (object.getDirection() == 0) {
                if (object.getX() == p.getX() - 1 && object.getY() == p.getY()) { // X
                    movePlayer(p, object.getX() - 1, object.getY());
                } else if (object.getX() == p.getX() + 1 && object.getY() == p.getY()) { // Y
                    movePlayer(p, object.getX() + 1, object.getY());
                } else if (object.getX() == p.getX() && object.getY() == p.getY() + 1) { // left
                    // side
                    movePlayer(p, object.getX(), object.getY() + 1);
                } else if (object.getX() == p.getX() && object.getY() == p.getY() - 1) { // right
                    // side.
                    movePlayer(p, object.getX(), object.getY() - 1);
                }
            }
            if (object.getDirection() == 7) {
                if (object.getX() == p.getX() - 1 && object.getY() == p.getY()) { // X
                    movePlayer(p, object.getX() - 1, object.getY() - 1);
                } else if (object.getX() == p.getX() + 1 && object.getY() == p.getY()) { // Y
                    movePlayer(p, object.getX() + 1, object.getY());
                } else if (object.getX() == p.getX() && object.getY() == p.getY() + 1) { // left
                    // side
                    movePlayer(p, object.getX(), object.getY() + 1);
                } else if (object.getX() == p.getX() && object.getY() == p.getY() - 1) { // right
                    // side.
                    movePlayer(p, object.getX() + 1, object.getY());
                }
            }
            p.message("and step down the other side");
        } else {
            p.message("but you slip");
            p.damage(damage);
            if (spikeLocation == 1) {
                p.teleport(743, 3475);
            } else if (spikeLocation == 2) {
                p.teleport(748, 3482);
            } else if (spikeLocation == 3) {
                p.teleport(738, 3483);
            } else if (spikeLocation == 4) {
                p.teleport(736, 3475);
            } else if (spikeLocation == 5) {
                p.teleport(730, 3478);
            }
            playerTalk(p, null, "aargh");
        }
        if (eventMessage) {
            World.getWorld().getDelayedEventHandler().add(new UndergroundPassMessageEvent(p, Util.random(2000, 10000)));
        }
    }
    
    public static int random(int low, int high) {
        return Util.random(low, high);
    }

    /**
     * Creates a new ground item
     * 
     * @param id
     * @param amount
     * @param x
     * @param y
     * @param owner
     */
    public static void createGroundItem(int id, int amount, int x, int y, Player owner) {
        World.getWorld().registerItem(new Item(id, x, y, amount, owner));
    }



    /**
     * Creates a new ground item
     * 
     * @param id
     * @param amount
     * @param x
     * @param y
     */
    public static void createGroundItem(int id, int amount, int x, int y) {
        createGroundItem(id, amount, x, y, null);
    }

    public static void createGroundItemDelayedRemove(final Item i, int time) {
        if (i.getLoc() == null) {
            world.getDelayedEventHandler().add(new SingleEvent(null, time) {
                public void action() {
                    World.getWorld().unregisterItem(i);
                }
            });
        }
    }
    
    public static void showBubble(final Player player, int itemID) {
        player.getSender().sendItemBubble(itemID);
    }
    
    public static void showBubble(final Player player, final InvItem item) {
        player.getSender().sendItemBubble(item.getID());
    }
    
    public static void resetGnomeCooking(Player p) {
        String[] caches = { 
            "cheese_on_batta", "tomato_on_batta", "tomato_cheese_batta", "leaves_on_batta",
            "complete_dish", "chocolate_on_bowl", "leaves_on_bowl", "chocolate_bomb", "cream_on_bowl",
            "choco_dust_on_bowl", "aqua_toad_legs", "gnomespice_toad_legs", "toadlegs_on_batta", 
            "kingworms_on_bowl", "onions_on_bowl", "gnomespice_on_bowl", "wormhole", "gnomespice_on_dough",
            "toadlegs_on_dough", "gnomecrunchie_dough", "gnome_crunchie_cooked", "gnomespice_on_worm",
            "worm_on_batta", "worm_batta", "onion_on_batta", "cabbage_on_batta", "dwell_on_batta",
            "veg_batta_no_cheese", "veg_batta_with_cheese", "chocolate_on_dough", "choco_dust_on_crunchies",
            "potato_on_bowl", "vegball", "toadlegs_on_bowl", "cheese_on_bowl", "dwell_on_bowl", "kingworm_on_dough",
            "leaves_on_dough", "spice_over_crunchies", "batta_cooked_leaves", "diced_orange_on_batta", "lime_on_batta",
            "pine_apple_batta", "spice_over_batta"
        };
        for(String s : caches) {
            if(p.getCache().hasKey(s)) {
                p.getCache().remove(s);
            }
        }
    }

    public static boolean checkAndRemoveBlurberry(Player p, boolean reset) { 
        String[] caches = { 
            "lemon_in_shaker", "orange_in_shaker", "pineapple_in_shaker", "lemon_slices_to_drink", 
            "drunk_dragon_base", "diced_pa_to_drink", "cream_into_drink", "dwell_in_shaker",
            "gin_in_shaker", "vodka_in_shaker", "fruit_blast_base", "lime_in_shaker", "sgg_base", 
            "leaves_into_drink", "lime_slices_to_drink", "whisky_in_shaker", "milk_in_shaker",
            "leaves_in_shaker", "choco_bar_in_drink", "chocolate_saturday_base", "heated_choco_saturday",
            "choco_dust_into_drink", "brandy_in_shaker", "diced_orange_in_drink", "blurberry_special_base",
            "diced_lemon_in_drink", "pineapple_punch_base", "diced_lime_in_drink", "wizard_blizzard_base"
        };
        for(String s : caches) {
            if(p.getCache().hasKey(s)) {
                if(reset) {
                    p.getCache().remove(s);
                    continue;
                }
                return true;
            }
        }
        return false;
    }
    
    private static boolean isBlocking(int objectValue, byte bit) {
        if ((objectValue & bit) != 0) { // There is a wall in the way
            return true;
        }
        if ((objectValue & 16) != 0) { // There is a diagonal wall here:
            // \
            return true;
        }
        if ((objectValue & 32) != 0) { // There is a diagonal wall here:
            // /
            return true;
        }
        if ((objectValue & 64) != 0) { // This tile is unwalkable
            return true;
        }
        return false;
    }
    
    private static boolean isBlocking(NPC npc, int x, int y, int bit) {
        TileValue t = World.getWorld().getTileValue(x, y);
        Point p = new Point(x, y);
        for(NPC n : npc.getViewArea().getNpcsInView()) {
            if(n.getLocation().equals(p)) {
                return true;
            }
        }
        for(Player areaPlayer : npc.getViewArea().getPlayersInView()) {
            if(areaPlayer.getLocation().equals(p)) {
                return true;
            }
        }
        return isBlocking(t.objectValue, (byte) bit);
    }
    
    private static Point canWalk(NPC n, int x, int y) {
        int myX = n.getX();
        int myY = n.getY();
        int newX = x;
        int newY = y;
        boolean myXBlocked = false, myYBlocked = false, newXBlocked = false, newYBlocked = false;
        if (myX > x) {
            myXBlocked = isBlocking(n, myX - 1, myY, 8); // Check right
            // tiles
            newX = myX - 1;
        } else if (myX < x) {
            myXBlocked = isBlocking(n, myX + 1, myY, 2); // Check left
            // tiles
            newX = myX + 1;
        }
        if (myY > y) {
            myYBlocked = isBlocking(n, myX, myY - 1, 4); // Check top tiles
            newY = myY - 1;
        } else if (myY < y) {
            myYBlocked = isBlocking(n, myX, myY + 1, 1); // Check bottom
            // tiles
            newY = myY + 1;
        }

        if ((myXBlocked && myYBlocked) || (myXBlocked && myY == newY) || (myYBlocked && myX == newX)) {
            return null;
        }

        if (newX > myX) {
            newXBlocked = isBlocking(n, newX, newY, 2);
        } else if (newX < myX) {
            newXBlocked = isBlocking(n, newX, newY, 8);
        }

        if (newY > myY) {
            newYBlocked = isBlocking(n, newX, newY, 1);
        } else if (newY < myY) {
            newYBlocked = isBlocking(n, newX, newY, 4);
        }
        if ((newXBlocked && newYBlocked) || (newXBlocked && myY == newY) || (myYBlocked && myX == newX)) {
            return null;
        }
        if ((myXBlocked && newXBlocked) || (myYBlocked && newYBlocked)) {
            return null;
        }
        return new Point(newX, newY);
    }
    
    /*public static void npcWalkFromPlayer(Player player, Npc n) {
        if (player.getLocation().equals(n.getLocation())) {
            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y <= 1; ++y) {
                    if(x == 0 || y == 0) 
                        continue;
                    Point destination = canWalk(n, player.getX() - x, player.getY() - y);
                    if (destination != null && destination.inBounds(n.getLoc().minX, n.getLoc().minY, n.getLoc().maxY, n.getLoc().maxY)) {
                        n.walk(destination.getX(), destination.getY());
                        break;
                    }
                }
            }
        }
    }*/
    
    public static InvItem getItem(int itemId) {
        return new InvItem(itemId, 1);
    }
    
    public static boolean inArray(Object o, Object... oArray) {
        for (Object object : oArray) {
            if (o.equals(object) || o == object) {
                return true;
            }
        }
        return false;
    }

    public static boolean inArray(int o, int[] oArray) {
        for (int object : oArray) {
            if (o == object) {
                return true;
            }
        }
        return false;
    }

    public static void kill(NPC mob, Player killedBy) {
        mob.killedBy(killedBy);
    }
    
    public static void removeNpc(final NPC npc) {
        world.unregisterNpc(npc);
    }
    
    public static int getCurrentLevel(Player p, int i) {
        return p.getCurStat(i);
    }

    public static int getMaxLevel(Player p, int i) {
        return p.getMaxStat(i);
    }
    
    public static void movePlayer(Player p, int x, int y) {
        movePlayer(p, x, y, false);
    }

    public static void movePlayer(Player p, int x, int y, boolean worldInfo) {
        if (worldInfo)
            p.teleport(x, y, false);
        else
            p.teleport(x, y);
    }
    
    public static void doDoor(final GameObject object, final Player p, int replaceID) {

        p.setBusyTimer(650);
        /* For the odd looking walls. */
        GameObject newObject = new GameObject(object.getLocation(), replaceID, object.getDirection(), object.getType());
        if(object.getID() == replaceID) {
            p.message("Nothing interesting happens");
            return;
        }
        if (replaceID == -1) {
            removeObject(object);
        } else {
            p.getSender().sendSound(SoundEffect.OPEN_DOOR);
            replaceObject(object, newObject);
        }
        delayedSpawnObject(object.getLoc(), 3000);

        if (object.getDirection() == 0) {
            if (object.getLocation().equals(p.getLocation())) {
                movePlayer(p, object.getX(), object.getY() - 1);
            } else {
                movePlayer(p, object.getX(), object.getY());
            }
        }
        if (object.getDirection() == 1) {
            if (object.getLocation().equals(p.getLocation())) {
                movePlayer(p, object.getX() - 1, object.getY());
            } else {
                movePlayer(p, object.getX(), object.getY());
            }
        }
        if (object.getDirection() == 2) {
            // front
            if (object.getX() == p.getX() && object.getY() == p.getY() + 1) {

                movePlayer(p, object.getX(), object.getY() + 1);
            } else if (object.getX() == p.getX() - 1 && object.getY() == p.getY()) {
                movePlayer(p, object.getX() - 1, object.getY());
            }

            // back
            else if (object.getX() == p.getX() && object.getY() == p.getY() - 1) {
                movePlayer(p, object.getX(), object.getY() - 1);
            } else if (object.getX() == p.getX() + 1 && object.getY() == p.getY()) {
                movePlayer(p, object.getX() + 1, object.getY());
            }
        }
        if (object.getDirection() == 3) {

            // front
            if (object.getX() == p.getX() && object.getY() == p.getY() - 1) {

                movePlayer(p, object.getX(), object.getY() - 1);
            } else if (object.getX() == p.getX() + 1 && object.getY() == p.getY()) {
                movePlayer(p, object.getX() + 1, object.getY());
            }

            // back
            else if (object.getX() == p.getX() && object.getY() == p.getY() + 1) {
                movePlayer(p, object.getX(), object.getY() + 1);
            } else if (object.getX() == p.getX() - 1 && object.getY() == p.getY()) {
                movePlayer(p, object.getX() - 1, object.getY());
            }

        }
    }
    
    /**
     * Returns true if you are in any stages provided.
     * 
     * @param p
     * @param quest
     * @param stage
     * @return
     */
    public static boolean atQuestStages(Player p, QuestInterface quest, int... stage) {
        boolean flag = false;
        for (int s : stage) {
            if (atQuestStage(p, quest, s)) {
                flag = true;
            }
        }
        return flag;
    }
    
    /**
     * Checks if players quest stage for this quest is @param stage
     * 
     * @param p
     * @param qID
     * @param stage
     * @return
     */
    public static boolean atQuestStage(Player p, int qID, int stage) {
        return getQuestStage(p, qID) == stage;
    }
    
    /**
     * Checks if players quest stage for this quest is @param stage
     * 
     * @param p
     * @param qID
     * @param stage
     * @return
     */
    public static boolean atQuestStage(Player p, QuestInterface quest, int stage) {
        return getQuestStage(p, quest) == stage;
    }
    
    /**
     * Returns the quest stage for @param quest
     * 
     * @param p
     * @param quest
     * @return
     */
    public static int getQuestStage(Player p, QuestInterface quest) {
        return p.getQuestStage(quest);
    }

    /**
     * Returns the quest stage for @param qID
     * 
     * @param p
     * @param qID
     * @param stage
     * @return
     */
    public static int getQuestStage(Player p, int questID) {
        return p.getQuestStage(questID);
    }
    
    public static void message(final Player player, int delay, final String... messages) {
        message(player, null, delay, messages);
    }
    public static void message(final Player player, final NPC npc, int delay, final String... messages) {
        for (final String message : messages) {
            if (!message.equalsIgnoreCase("null")) {
                if (npc != null) {
                    if(npc.isRemoved()) {
                        player.setBusy(false);
                        return;
                    }
                    npc.setBusyTimer(delay);
                }
                player.setBusy(true);
                player.message(message);
            }
            sleep(delay);
        }
        player.setBusy(false);
    }

    /**
     * Displays server message(s) with 2.2 second delay.
     * 
     * @param player
     * @param messages
     */
    public static void message(final Player player, final String... messages) {
        for (final String message : messages) {
            if (!message.equalsIgnoreCase("null")) {
                if (player.getInteractingNpc() != null) {
                    player.getInteractingNpc().setBusyTimer(1900);
                }
                player.message(message);
                player.setBusyTimer(1900);
            }
            sleep(1900);
        }
        player.setBusyTimer(0);
    }
    
    /**
     * Sets Quest with ID @param questID's stage to @parma stage
     * 
     * @param p
     * @param questID
     * @param stage
     */
    public static void setQuestStage(final Player p, final int questID, final int stage) {
        p.updateQuestStage(questID, stage);
    }

    /**
     * Sets @param quest 's stage to @param stage
     * 
     * @param p
     * @param questID
     * @param stage
     */
    public static void setQuestStage(Player p, QuestInterface quest, int stage) {
        p.updateQuestStage(quest, stage);
    }
    
    public static NPC getNearestNpc(Player p, final int npcId, final int radius) {
        final Iterable<NPC> npcsInView = p.getViewArea().getNpcsInView();
        NPC closestNpc = null;
        for (int next = 0; next < radius; next++) {
            for (final NPC n : npcsInView) {
                if(n.getID() == npcId) {

                }
                if (n.getID() == npcId && n.withinRange(p.getLocation(), next) && !n.isBusy()) {
                    closestNpc = n;
                }
            }
        }
        return closestNpc;
    }

    public static NPC getMultipleNpcsInArea(Player p, final int radius, final int... npcId) {
        final Iterable<NPC> npcsInView = p.getViewArea().getNpcsInView();
        NPC closestNpc = null;
        for (int next = 0; next < radius; next++) {
            for (final NPC n : npcsInView) {
                for (final int na : npcId) {
                    if (n.getID() == na && n.withinRange(p.getLocation(), next) && !n.isBusy()) {
                        closestNpc = n;
                    }
                }
            }
        }
        return closestNpc;
    }

    public static boolean isNpcNearby(Player p, int id) {
        for (NPC npc : p.getViewArea().getNpcsInView()) {
            if (npc.getID() == id) {
                return true;
            }
        }
        return false;
    }
    
    public static void removeObject(final GameObject o) {
        World.getWorld().unregisterGameObject(o);
    }

    public static void registerObject(final GameObject o) {
        World.getWorld().registerGameObject(o);
    }

    public static void replaceObject(final GameObject o, final GameObject newObject) {
        World.getWorld().replaceGameObject(o, newObject);
    }

    public static void delayedSpawnObject(final GameObjectLoc loc, final int time) {
        World.getWorld().delayedSpawnObject(loc, time);
    }
    
    public static void openChest(GameObject obj, int delay, int chestID) {
        GameObject chest = new GameObject(obj.getLocation(), chestID, obj.getDirection(), obj.getType());
        replaceObject(obj, chest);
        delayedSpawnObject(obj.getLoc(), delay);
    }

    public static void replaceObjectDelayed(GameObject obj, int delay, int replaceID) {
        GameObject replaceObj = new GameObject(obj.getLocation(), replaceID, obj.getDirection(), obj.getType());
        replaceObject(obj, replaceObj);
        delayedSpawnObject(obj.getLoc(), delay);
    }

    public static void openChest(GameObject obj, int delay) {
        openChest(obj, delay, 339);
    }

    public static void openChest(GameObject obj) {
        openChest(obj, 2000);
    }

    public static World world = World.getWorld();

    public static void decCurStat(Player player, int stat, int amount) {
        int level = player.getCurStat(stat);
        level -= amount;
        if (level < 0) {
            level = 0;
        }
        player.setCurStat(stat, level);
        player.getSender().sendStat(stat);
    }

    public static void incCurStat(Player player, int stat, int amount) {
        int level = player.getCurStat(stat);
        level -= amount;
        if (level < 0) {
            level = 0;
        }
        player.setCurStat(stat, level);
        player.getSender().sendStat(stat);
    }
    
    public static void replaceDoor(int newID, boolean open, Player player, GameObject door) {
        World.getWorld().unregisterGameObject(door);
        World.getWorld().registerGameObject(new GameObject(door.getLocation(), newID, door.getDirection(), door.getType()));
        player.getSender().sendMessage("The door " + (open ? "swings open" : "creaks shut"));
        player.getSender().sendSound(open ? SoundEffect.OPEN_DOOR : SoundEffect.CLOSE_DOOR);
    }

    public static void replaceGate(int newID, boolean open, Player owner, GameObject object) {
        world.unregisterGameObject(object);
        world.registerGameObject(new GameObject(object.getLocation(), newID, object.getDirection(), object.getType()));
        owner.getSender().sendSound(open ? SoundEffect.OPEN_DOOR : SoundEffect.CLOSE_DOOR);
    }

    public static void doGate(final Player p, final GameObject object) {
        doGate(p, object, 181);
    }
    
    public static void doGate(final Player p, final GameObject object, int replaceID) {
        p.setBusyTimer(650);
        // 0 - East
        // 1 - Diagonal S- NE
        // 2 - South
        // 3 - Diagonal S-NW
        // 4- West
        // 5 - Diagonal N-NE
        // 6 - North
        // 7 - Diagonal N-W
        // 8 - N->S
        p.getSender().sendSound(SoundEffect.OPEN_DOOR);
        removeObject(object);
        registerObject(new GameObject(object.getLocation(), replaceID, object.getDirection(), object.getType()));

        int dir = object.getDirection();
        if (dir == 0) {
            if (p.getX() >= object.getX()) {
                movePlayer(p, object.getX() - 1, object.getY());
            } else {
                movePlayer(p, object.getX(), object.getY());
            }
        } else if (dir == 2) {
            if (p.getY() <= object.getY()) {
                movePlayer(p, object.getX(), object.getY() + 1);
            } else {
                movePlayer(p, object.getX(), object.getY());
            }
        } else if (dir == 4) {
            if (p.getX() > object.getX()) {
                movePlayer(p, object.getX(), object.getY());
            } else {
                movePlayer(p, object.getX() + 1, object.getY());
            }
        } else if (dir == 6) {
            if (p.getY() >= object.getY()) {
                movePlayer(p, object.getX(), object.getY() - 1);
            } else {
                movePlayer(p, object.getX(), object.getY());
            }
        } else {
            p.message("Failure - Contact an administrator");
        }
        sleep(1000);
        registerObject(new GameObject(object.getLoc()));
    }

    public static void doDoor(final GameObject object, final Player p) {
        doDoor(object, p, 11);
    }
    
    public static void doDoor(GameObject object, Player player, boolean teleport) {
        player.getSender().sendSound(SoundEffect.OPEN_DOOR);
        world.registerGameObject(new GameObject(object.getLocation(), 11, object.getDirection(), object.getType()));
        world.delayedSpawnObject(object.getLoc(), 1000);
        if(!teleport) return;
        if (object.getDirection() == 0) {
            if (object.getLocation().equals(player.getLocation())) {
                player.teleport(object.getX(), object.getY() - 1);
            } else {
                player.teleport(object.getX(), object.getY());
            }
        }
        if (object.getDirection() == 1) {
            if (object.getLocation().equals(player.getLocation())) {
                player.teleport(object.getX() - 1, object.getY());
            } else {
                player.teleport(object.getX(), object.getY());
            }
        }
        if (object.getDirection() == 2) {
            // front
            if (object.getX() == player.getX() && object.getY() == player.getY() + 1) {

                player.teleport(object.getX(), object.getY() + 1);
            } else if (object.getX() == player.getX() - 1 && object.getY() == player.getY()) {
                player.teleport(object.getX() - 1, object.getY());
            }

            // back
            else if (object.getX() == player.getX() && object.getY() == player.getY() - 1) {
                player.teleport(object.getX(), object.getY() - 1);
            } else if (object.getX() == player.getX() + 1 && object.getY() == player.getY()) {
                player.teleport(object.getX() + 1, object.getY());
            }
        }
        if (object.getDirection() == 3) {

            // front
            if (object.getX() == player.getX() && object.getY() == player.getY() - 1) {

                player.teleport(object.getX(), object.getY() - 1);
            } else if (object.getX() == player.getX() + 1 && object.getY() == player.getY()) {
                player.teleport(object.getX() + 1, object.getY());
            }

            // back
            else if (object.getX() == player.getX() && object.getY() == player.getY() + 1) {
                player.teleport(object.getX(), object.getY() + 1);
            } else if (object.getX() == player.getX() - 1 && object.getY() == player.getY()) {
                player.teleport(object.getX() - 1, object.getY());
            }

        }
    }

    public static void faceNpc(Player player, NPC npc) {
        int dir = Formulae.getDirection(player, npc);
        if (dir != -1) {
            player.setSprite(Formulae.getDirection(npc, player));
            npc.setSprite(Formulae.getDirection(player, npc));
        }
    }

    public static void faceNpc(Player player, NPC npc, boolean npcFace) {
        int dir = Formulae.getDirection(player, npc);
        if (dir != -1) {
            player.setSprite(Formulae.getDirection(npc, player));
            if(npcFace) {
                npc.setSprite(Formulae.getDirection(player, npc));
            }
        }
    }

    public static NPC spawnNpc(int npcId, int x, int y, boolean respawn) {
        return spawnNpc(npcId, x, y, 0, respawn, false);
    }

    public static NPC spawnNpc(int npcId, int x, int y, int time, boolean persist, boolean aggressive) {
        if (EntityManager.getNPC(npcId) != null) {
            final NPC n = new NPC(npcId, x, y, x - 5, x + 5, y - 5, y + 5);
            n.setRespawn(persist);
            n.getDef().aggressive = 1;
            world.registerNpc(n);
            if(!persist) {
                world.getDelayedEventHandler().add(new SingleEvent(null, time == 0 ? 300000 : time) {
                    @Override
                    public void action() {
                        Mob opponent = n.getOpponent();
                        if (opponent != null) {
                            opponent.resetCombat(CombatState.ERROR);
                        }
                        n.resetCombat(CombatState.ERROR);
                        world.unregisterNpc(n);
                        n.remove();
                    }
                });
            }
            return n;
        }
        return null;
    }

    public static FightEvent fightNpc(Player player, NPC npc) {
        return fightNpc(player, npc, false);
    }

    public static FightEvent fightNpc(Player player, NPC npc, boolean invincibleMode) {
        player.reset();
        player.setStatus(Action.FIGHTING_MOB);
        npc.resetPath();

        player.setLocation(npc.getLocation(), true);

        for (Player p : player.getViewArea().getPlayersInView()) {
            p.removeWatchedPlayer(player);
        }

        player.setBusy(true);
        player.setSprite(9);
        player.setOpponent(npc);
        player.setCombatTimer();
        npc.setBusy(true);
        npc.setSprite(8);
        npc.setOpponent(player);
        npc.setCombatTimer();
        FightEvent fightEvent = new FightEvent(player, npc);
        fightEvent.setLastRun(0);
        fightEvent.setOpponentInvincible(invincibleMode);
        world.getDelayedEventHandler().add(fightEvent);
        return fightEvent;
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    public static void sleep() {
        sleep(2000);
    }

    public static boolean hasItem(Player player, int id, int amount) {
        if (EntityManager.getItem(id).isStackable()) {
            for (InvItem i : player.getInventory().getItems()) {
                if (i.getID() == id && i.getAmount() >= amount) {
                    return true;
                }
            }
        } else {
            int count = 0;
            for (InvItem i : player.getInventory().getItems()) {
                if (i.getID() == id) {
                    count++;
                }
            }
            if (count >= amount) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasItem(Player player, int id) {
        int count = 0;
        for (InvItem i : player.getInventory().getItems()) {
            if (i.getID() == id)
                count++;
        }
        return count >= 1;
    }

    public static void addItem(Player player, int id, int amount) {
        for(int i = 0; i < amount; i++) {
            player.getInventory().add(new InvItem(id));
        }
        player.getSender().sendInventory();
    }

    public static void addItem(Player player, int id) {
        addItem(player, id, 1);
    }
    
    public static boolean removeItem(final Player p, final int id, final int amt) {
        if (!hasItem(p, id, amt)) {
            return false;
        }
        final InvItem item = new InvItem(id, 1);
        if (!item.getDef().isStackable()) {
            for (int i = 0; i < amt; i++) {
                p.getInventory().remove(new InvItem(id, 1));
                p.getSender().sendInventory();
            }
        } else {
            p.getInventory().remove(new InvItem(id, amt));
            p.getSender().sendInventory();
        }
        return true;
    }
    
    public static boolean removeItem(final Player p, final InvItem... items) {
        for (InvItem i : items) {
            if (!p.getInventory().contains(i)) {
                return false;
            }
        }
        for (InvItem ir : items) {
            p.getInventory().remove(ir);
            p.getSender().sendInventory();
        }
        return true;
    }
    
    public static boolean removeItem(Player p, int id) {
        return removeItem(p, new InvItem(id));
    }

    public static void displayTeleportBubble(Player player, int x, int y, boolean small) {
        player.getSender().sendTeleBubble(player.getX(), player.getY(), small);
    }

    public static void spawnItem(int id, int x, int y, int amount, Player player) {
        world.registerItem(new Item(id, x, y, amount, player));
    }
    
    public static int showMenu(final Player player, final NPC npc, final String... options) {
        //final long start = System.currentTimeMillis();
        if (npc != null) {
            if(npc.isRemoved()) {
                player.resetMenuHandler();
                player.lastOption = -1;
                player.setBusy(false);
                return -1;
            }
            npc.setBusy(true);
        }
        player.resetMenuHandler();
        player.lastOption = -2;
        player.setMenuHandler(new MenuHandler(options) {
            @Override
            public void handleReply(final int option, final String reply) {
                if (option < 0 || option >= getOptions().length || option == 30) {
                    npc.unblock();
                    player.setBusy(false);
                    owner.lastOption = -1;
                } else {
                    owner.lastOption = option;
                }
            }
        });
        player.getSender().sendMenu(options);

        while (true) {
            if (player.lastOption != -1 && player.lastOption != -2) {
                if (options[player.lastOption] != null) {
                    npc.setBusy(false);
                    playerTalk(player, npc, options[player.lastOption]);
                }
                return player.lastOption;
            } else if (/*System.currentTimeMillis() - start > 19500 || */player.getMenuHandler() == null) {
                player.lastOption = -2;
                player.resetMenuHandler();
                npc.setBusy(false);
                player.setBusyTimer(0);
                return -1;
            }
            sleep(1);
        }
    }

    public static int showMenu(final Player player, String... options) {
        //final long start = System.currentTimeMillis();
        player.resetMenuHandler();
        player.lastOption = -2;
        player.setMenuHandler(new MenuHandler(options) {
            @Override
            public void handleReply(final int option, final String reply) {
                if (option < 0 || option >= getOptions().length || option == 30) {
                    player.setBusy(false);
                    owner.lastOption = -1;
                } else {
                    owner.lastOption = option;
                }
            }
        });
        player.getSender().sendMenu(options);

        while (true) {
            if (player.lastOption != -1 && player.lastOption != -2) {
                return player.lastOption;
            } else if (/*System.currentTimeMillis() - start > 19500 || */player.getMenuHandler() == null) {
                player.lastOption = -2;
                player.resetMenuHandler();
                player.setBusyTimer(0);
                return -1;
            }
            sleep(1);
        }
    }

    public static void sendChat(Player player, String... messages) {
        for (String message : messages) {
            player.informOfChatMessage(new ChatMessage(player, message, player));
            sleep(2200);
        }
    }
    
    public static void playerTalk(final Player player, final NPC npc, final String... messages) {
        for (final String message : messages) {
            if (!message.equalsIgnoreCase("null")) {
                if(npc != null) {
                    if(npc.isRemoved()) {
                        player.setBusy(false);
                        return;
                    }
                }
                if (npc != null) {
                    npc.resetPath();
                    npc.setBusyTimer(2500);
                    npc.face(player);
                    player.face(npc);
                }
                player.setBusyTimer(2500);
                player.resetPath();
                player.informOfChatMessage(new ChatMessage(player, message + "@que@", npc));
            }
            sleep(1900);
        }
    }
    
    public static void npcTalk(final Player player, final NPC npc, final int delay, final String... messages) {
        for (final String message : messages) {
            if (!message.equalsIgnoreCase("null")) {
                if(npc.isRemoved()) {
                    player.setBusy(false);
                    return;
                }
                npc.setBusy(true);
                player.setBusy(true);
                npc.resetPath();
                player.resetPath();

                player.informOfNPCMessage(new ChatMessage(npc, message, player));

                npc.face(player);
                if (!player.inCombat()) {
                    player.face(npc);
                }
            }

            sleep(delay);

        }
        npc.setBusy(false);
        player.setBusy(false);
    }

    public static void npcTalk(final Player player, final NPC npc, final String... messages) {
        npcTalk(player, npc, 1900, messages);
    }

    /**
     * Npc chat method not blocking
     * 
     * @param player
     * @param npc
     * @param messages
     *            - String array of npc dialogue lines.
     */
    public static void npcYell(final Player player, final NPC npc, final String... messages) {
        for (final String message : messages) {
            if (!message.equalsIgnoreCase("null")) {
                player.informOfNPCMessage(new ChatMessage(npc, message, player));
            }
        }
    }

}