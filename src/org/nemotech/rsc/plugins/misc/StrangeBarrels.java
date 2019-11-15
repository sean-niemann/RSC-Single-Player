package org.nemotech.rsc.plugins.misc;

import org.nemotech.rsc.event.SingleEvent;
import org.nemotech.rsc.util.Util;

import static org.nemotech.rsc.plugins.Plugin.*;
import org.nemotech.rsc.model.Point;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.landscape.ActiveTile;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;

public class StrangeBarrels extends Plugin implements ObjectActionListener, ObjectActionExecutiveListener {

    /**
     * @author Davve
     * What I have discovered from barrels is the food, potions, misc, weapon, runes, certificates and monsters.
     * There are 8 ways that the barrel behave - more comment below.
     * The barrel is smashed and then after 40 seconds it adds on a new randomized coord in the cave area.
     */

    public static final int STRANGE_BARREL = 1178;

    public static final int[] FOOD = { 138, 179, 335, 261, 319, 262, 263, 861, };

    public static final int[] POTION = { 482, 224, 485, 476 };

    public static final int[] OTHER = { 237, 986, 988, 815, 10, 676, 156, 549, 172, 14, 987, 1259, 166, 774 };

    public static final int[] WEAPON = { 1013, 1076, 1080, 1078, 1024, 1015, 1068, 1075, 1077, 1079, 1069 };

    public static final int[] RUNES = { 34, 32, 33, 31 };

    public static final int[] CERTIFICATE = { 518, 519, 629, 534, 535, 631, 630, 713, 711 };

    public static final int[] MONSTER = { 190, 199, 57, 99, 768, 61, 43, 21, 23, 41, 46, 40, 47, 67, 104, 66, 45, 19, 70 };


    @Override
    public boolean blockObjectAction(GameObject obj, String command, Player p) {
        if(obj.getID() == STRANGE_BARREL) {
            return true;
        }
        return false;
    }

    @Override
    public void onObjectAction(GameObject obj, String command, Player player) {
        if(obj.getID() == STRANGE_BARREL) {
            player.setBusyTimer(600);
            int action = Util.random(0, 4);
            if(action != 0) {
                player.message("You smash the barrel open.");
                removeObject(obj);
                World.getWorld().getDelayedEventHandler().add(new SingleEvent(null, 40000) { // 40 seconds
                    public void action() {
                        int newObjectX = Util.random(467, 476);
                        int newObjectY = Util.random(3699, 3714);
                        ActiveTile tile = world.getTile(owner.getLocation());
                        if(tile.hasGameObject()) {
                            registerObject(new GameObject(Point.getLocation(newObjectX, newObjectY), 1178, 0, 0));
                        } else {
                            registerObject(new GameObject(obj.getLoc()));
                        }
                    }
                });
                /**
                 * Out comes a NPC only.
                 */
                if(action == 1) {
                    spawnMonster(player, obj.getX(), obj.getY());
                }
                /**
                 * Out comes an item only.
                 */
                else if(action == 2) {
                    spawnItem(player, obj.getX(), obj.getY());
                }
                /**
                 * Out comes both a NPC and an ITEM.
                 */
                else if(action == 3) {
                    spawnItem(player, obj.getX(), obj.getY());
                    spawnMonster(player, obj.getX(), obj.getY());
                }
                /**
                 * Smash the barrel and get randomly hit from 0-14 damage.
                 */
                else if(action == 4) {
                    player.message("The barrel explodes...");
                    player.message("...you take some damage...");
                    displayTeleportBubble(player, obj.getX(), obj.getY(), true);
                    player.damage((int) Util.random(0, 14));
                }
            } else {
                /**
                 * Smash the barrel open but nothing happens.
                 */
                if(Util.random(0, 1) != 1) {
                    player.message("You smash the barrel open.");
                    removeObject(obj);
                    delayedSpawnObject(obj.getLoc(), 40000); // 40 seconds
                } else {
                    if(Util.random(0, 1) != 0) {
                        player.message("You were unable to smash this barrel open.");
                        message(player, 1300, "You hit the barrel at the wrong angle.",
                                "You're heavily jarred from the vibrations of the blow.");
                        int reduceAttack = Util.random(1, 3);
                        player.message("Your attack is reduced by " + reduceAttack + ".");
                        player.setCurStat(ATTACK, player.getCurStat(ATTACK) - reduceAttack);
                    } else {
                        player.message("You were unable to smash this barrel open.");
                    }
                } 
            }
        } 
    }

    private void spawnMonster(Player p, int x, int y) {
        int randomizeMonster = Util.random(0, (MONSTER.length - 1));
        int selectedMonster = MONSTER[randomizeMonster];
        NPC monster = spawnNpc(selectedMonster, x, y, 60000 * 3); // 3 minutes
        sleep(600);
        if(monster != null) {
            monster.startCombat(p);
        }
    }

    private void spawnItem(Player p, int x, int y) {
        int randomizeReward = Util.random(0, 100);
        int[] selectItemArray = OTHER;
        if(randomizeReward >= 0 && randomizeReward <= 14) { // 15%
            selectItemArray = FOOD;
        } else if(randomizeReward >= 15 && randomizeReward <= 29) { // 15%
            selectItemArray = POTION;
        } else if(randomizeReward >= 30 && randomizeReward <= 44) { // 15%
            selectItemArray = RUNES;
        } else if(randomizeReward >= 45 && randomizeReward <= 59) { // 15%
            selectItemArray = CERTIFICATE;
        } else if(randomizeReward >= 60 && randomizeReward <= 89) { // 30%
            selectItemArray = OTHER;
        } else if(randomizeReward >= 90 && randomizeReward <= 100) { // 11%
            selectItemArray = WEAPON;
        }
        int randomizeItem = Util.random(0, (selectItemArray.length - 1));
        int selectedItem = selectItemArray[randomizeItem];
        if(selectedItem == 10) {
            createGroundItem(selectedItem, 100, x, y, p);
        } else {
            createGroundItem(selectedItem, 1, x, y, p);
        }
    }
}