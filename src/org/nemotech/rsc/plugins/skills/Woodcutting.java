package org.nemotech.rsc.plugins.skills;

import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.event.impl.BatchEvent;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;
import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.util.Formulae;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;

public class Woodcutting extends Plugin implements ObjectActionListener, ObjectActionExecutiveListener {

    Trees TYPE;

    enum Trees {

        REGULAR_TREE(1, 14, 25, 100, 30),
        OAK_TREE(15, 632, 37, 10, 60),
        WILLOW_TREE(30, 633, 62, 10, 60),
        MAPLE_TREE(45, 634, 100, 15, 120),
        YEW_TREE(60, 635, 175, 20, 120),
        MAGIC_TREE(75, 636, 300, 20, 180);

        int level, log, experience, chance, respawn;

        Trees(int level, int log, int experience, int chance, int respawn) {
            this.level = level;
            this.log = log;
            this.experience = experience;
            this.chance = chance;
            this.respawn = respawn;
        }

    }
    
    @Override
    public boolean blockObjectAction(final GameObject obj, final String command, final Player player) {
        if (command.equals("chop") && obj.getID() != 245 && obj.getID() != 204) {
            return true;
        }
        return false;
    }
    
    @Override
    public void onObjectAction(final GameObject object, String command, Player owner) {
        if (command.equals("chop") && object.getID() != 245 && object.getID() != 204) {
            if(Util.inArray(new int[] { 0, 1, 70 }, object.getID())) {
                TYPE = Trees.REGULAR_TREE;
            } else if(object.getID() == 306) {
                TYPE = Trees.OAK_TREE;
            } else if(object.getID() == 307) {
                TYPE = Trees.WILLOW_TREE;
            } else if(object.getID() == 308) {
                TYPE = Trees.MAPLE_TREE;
            } else if(object.getID() == 309) {
                TYPE = Trees.YEW_TREE;
            } else if(object.getID() == 310) {
                TYPE = Trees.MAGIC_TREE;
            }
            handleWoodcutting(owner, object);
        }
    }

    void handleWoodcutting(Player owner, GameObject object) {
        int retries = (int) Math.ceil(owner.getMaxStat(WOODCUTTING) / 10);
        handleWoodcutting(owner, object, retries);
    }

    void handleWoodcutting(Player owner, final GameObject object, int passedvalue) {
        final int tries = --passedvalue;
        if (owner.isBusy()) {
            return;
        }
        if (!owner.withinRange(object, 2)) {
            return;
        }
        if (owner.getCurStat(WOODCUTTING) < TYPE.level) {
            owner.getSender().sendMessage("You need a woodcutting level of " + TYPE.level + " to axe this tree");
            return;
        }
        int axeID = -1;
        for (int axe : Formulae.HATCHETS) {
            if (owner.getInventory().countId(axe) > 0) {
                axeID = axe;
                break;
            }
        }
        if (axeID < 0) {
            owner.getSender().sendMessage("You need an axe to chop this tree down");
            return;
        }
        if(owner.getFatigue() >= 7500) {
            owner.getSender().sendMessage("You are too tired to chop wood");
            return;
        }
        int batchTimes = 1;
        switch (axeID) {
        case 87:
            batchTimes = 1;
            break;
        case 12:
            batchTimes = 2;
            break;
        case 88:
            batchTimes = 3;
            break;
        case 428:
            batchTimes = 4;
            break;
        case 203:
            batchTimes = 5;
            break;
        case 204:
            batchTimes = 8;
        case 405:
            batchTimes = 12;
            break;
        }
        owner.setBusy(true);
        showBubble(owner, new InvItem(axeID));
        owner.getSender().sendMessage("You swing your " + EntityManager.getItem(axeID).getName() + " at the tree...");
        final int finalAxeID = axeID;
        owner.setBatchEvent(new BatchEvent(owner, 1600, batchTimes) {
            @Override
            public void action() {
                if (Formulae.getLog(TYPE.level, owner.getCurStat(WOODCUTTING), finalAxeID)) {
                    InvItem log = new InvItem(TYPE.log);
                    owner.getInventory().add(log);
                    owner.getSender().sendMessage("You get some wood");
                    owner.getSender().sendInventory();
                    owner.incExp(WOODCUTTING, TYPE.experience, true);
                    owner.getSender().sendStat(WOODCUTTING);
                    if (Util.random(1, 100) <= TYPE.chance) {
                        world.registerGameObject(new GameObject(object.getLocation(), 4, object.getDirection(), object.getType()));
                        world.delayedSpawnObject(object.getLoc(), TYPE.respawn * 1000);
                        owner.setBusy(false);
                    } else {
                        owner.setBusy(false);
                        if(tries > 0) {
                            handleWoodcutting(owner, object, tries);
                        }
                    }
                } else {
                    owner.getSender().sendMessage("You slip and fail to hit the tree");
                    owner.setBusy(false);
                    if(tries > 0) {
                        handleWoodcutting(owner, object, tries);
                    }
                }
            }
        });
    }
    
}