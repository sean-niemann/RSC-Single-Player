package org.nemotech.rsc.plugins.quests.members.undergroundpass.obstacles;

import static org.nemotech.rsc.plugins.Plugin.HITS;
import static org.nemotech.rsc.plugins.Plugin.createGroundItem;
import static org.nemotech.rsc.plugins.Plugin.delayedSpawnObject;
import static org.nemotech.rsc.plugins.Plugin.getCurrentLevel;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.registerObject;
import static org.nemotech.rsc.plugins.Plugin.removeObject;
import static org.nemotech.rsc.plugins.Plugin.replaceObject;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.model.Point;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnObjectListener;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.action.WallObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnObjectExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.WallObjectActionExecutiveListener;

public class UndergroundPassDungeonFloor implements ObjectActionListener, ObjectActionExecutiveListener, WallObjectActionListener, WallObjectActionExecutiveListener, InvUseOnObjectListener, InvUseOnObjectExecutiveListener {

    /** ITEM IDs **/
    public static int EMPTY_BUCKET = 21;
    public static int DWARF_BREW = 1001;
    public static int TINDER_BOX = 166;
    public static int IBANS_ASHES = 1002;

    /** OBJECT IDs **/
    public static int SPIDER_NEST_RAILING = 171;
    public static int LADDER = 920;
    public static int TOMB_OF_IBAN = 878;
    public static int DWARF_BARREL = 880;
    public static int PILE_OF_MUD = 890;

    @Override
    public boolean blockObjectAction(GameObject obj, String command, Player p) {
        if(obj.getID() == LADDER) {
            return true;
        }
        if(obj.getID() == TOMB_OF_IBAN) {
            return true;
        }
        if(obj.getID() == DWARF_BARREL) {
            return true;
        }
        if(obj.getID() == PILE_OF_MUD) {
            return true;
        }
        return false;
    }

    @Override
    public void onObjectAction(GameObject obj, String command, Player p) {
        if(obj.getID() == LADDER) {
            message(p, "you climb the ladder");
            p.message("it leads to some stairs, you walk up...");
            p.teleport(782, 3549);
        }
        if(obj.getID() == TOMB_OF_IBAN) {
            message(p, "you try to open the door of the tomb");
            p.message("but the door refuses to open");
            message(p, "you hear a noise from below");
            p.message("@dre@leave me be");
            GameObject claws_of_iban = new GameObject(Point.getLocation(p.getX(), p.getY()), 879, 0, 0);
            registerObject(claws_of_iban);
            p.damage(((int) getCurrentLevel(p, HITS) / 5) + 5);
            playerTalk(p,null, "aaarrgghhh");
            sleep(1000);
            removeObject(claws_of_iban);
        }
        if(obj.getID() == DWARF_BARREL) {
            if(!hasItem(p, EMPTY_BUCKET)) {
                p.message("you need a bucket first");
            } else {
                p.message("you pour some of the strong brew into your bucket");
                p.getInventory().replace(EMPTY_BUCKET, DWARF_BREW);
            }
        }
        if(obj.getID() == PILE_OF_MUD) {
            message(p, "you climb the pile of mud");
            p.message("it leads to an old stair way");
            p.teleport(773, 3417);
        }
    }

    @Override
    public boolean blockInvUseOnObject(GameObject obj, InvItem item, Player p) {
        if(obj.getID() == TOMB_OF_IBAN && item.getID() == DWARF_BREW) {
            return true;
        }
        if(obj.getID() == TOMB_OF_IBAN && item.getID() == TINDER_BOX) {
            return true;
        }
        return false;
    }

    @Override
    public void onInvUseOnObject(GameObject obj, InvItem item, Player p) {
        if(obj.getID() == TOMB_OF_IBAN && item.getID() == DWARF_BREW) {
            if(p.getCache().hasKey("doll_of_iban") && p.getQuestStage(Plugin.UNDERGROUND_PASS) == 6) {
                p.message("you pour the strong alcohol over the tomb");
                if(!p.getCache().hasKey("brew_on_tomb") && !p.getCache().hasKey("ash_on_doll")) {
                    p.getCache().store("brew_on_tomb", true);
                }
                p.getInventory().replace(DWARF_BREW, EMPTY_BUCKET);
            } else {
                message(p, "you consider pouring the brew over the grave");
                p.message("but it seems such a waste");
            }
        }
        if(obj.getID() == TOMB_OF_IBAN && item.getID() == TINDER_BOX) {
            message(p, "you try to set alight to the tomb");
            if(p.getCache().hasKey("brew_on_tomb") && !p.getCache().hasKey("ash_on_doll")) {
                message(p, "it bursts into flames");
                replaceObject(obj, new GameObject(obj.getLocation(), 97, obj.getDirection(), obj
                        .getType()));
                delayedSpawnObject(obj.getLoc(), 10000);
                message(p, "you search through the remains");
                if(!hasItem(p, IBANS_ASHES)) {
                    p.message("and find the ashes of ibans corpse");
                    createGroundItem(IBANS_ASHES, 1, 726, 654, p);
                } else {
                    p.message("but find nothing");
                }
                p.getCache().remove("brew_on_tomb");
            } else {
                p.message("but it will not light");
            }
        }
    }

    @Override
    public boolean blockWallObjectAction(GameObject obj, Integer click, Player player) {
        if(obj.getID() == SPIDER_NEST_RAILING) {
            return true;
        }
        return false;
    }

    @Override
    public void onWallObjectAction(GameObject obj, Integer click, Player p) {
        if(obj.getID() == SPIDER_NEST_RAILING) {
            message(p, "you search the bars");
            if(p.getCache().hasKey("doll_of_iban") || p.getQuestStage(Plugin.UNDERGROUND_PASS) == 7 || p.getQuestStage(Plugin.UNDERGROUND_PASS) == -1) {
                message(p, "there's a gap big enough to squeeze through");
                p.message("would you like to try");
                int menu = showMenu(p,
                        "nope",
                        "yes, lets do it");
                if(menu == 1) {
                    p.message("you squeeze through the old railings");
                    if(obj.getDirection() == 0) {
                        if(obj.getY() == p.getY())
                            p.teleport(obj.getX(), obj.getY() - 1);
                        else
                            p.teleport(obj.getX(), obj.getY());
                    }
                }
            } else {
                p.message("but you can't quite squeeze through");
            }
        }
    }
}
