package org.nemotech.rsc.plugins.skills;

import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.event.SingleEvent;
import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.event.impl.BatchEvent;
import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import static org.nemotech.rsc.plugins.Plugin.SMITHING;
import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.getCurrentLevel;
import static org.nemotech.rsc.plugins.Plugin.inArray;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.showBubble;
import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnObjectListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnObjectExecutiveListener;
import org.nemotech.rsc.util.Formulae;

public class Smelting implements InvUseOnObjectListener,
InvUseOnObjectExecutiveListener { 

    enum Smelt {
        COPPER_ORE(150, 6.25, 1, 1, 169, 202, 1),
        TIN_ORE(202, 6.25, 1, 1, 169, 150, 1),
        IRON_ORE(151, 12.5, 15, 1, 170, -1, -1),
        SILVER(383, 13.5, 20, 1, 384, -1, -1),
        GOLD(152, 22.5, 40, 1, 172, -1, -1),
        MITHRIL_ORE(153, 30, 50, 1, 173, 155, 4),
        ADAMANTITE_ORE(154, 37.5, 70, 1, 174, 155, 6),
        COAL(155, 17.5, 30, 2, 171, 151, 1),
        RUNITE_ORE(409, 50, 85, 1, 408, 155, 8);

        private final int id;
        private final double xp;
        private final int requiredLevel;
        private final int oreAmount;
        private final int smeltBarId;
        private final int requestedOreId;
        private final int requestedOreAmount;

        Smelt(int itemId, double exp, int req, int oreAmount, int barId, int reqOreId, int reqOreAmount) {
            this.id = itemId;
            this.xp = exp;
            this.requiredLevel = req;
            this.oreAmount = oreAmount;
            this.smeltBarId = barId;
            this.requestedOreId = reqOreId;
            this.requestedOreAmount = reqOreAmount;
        }

        public int getID() {
            return id;
        }

        public double getXp() {
            return xp;
        }

        public int getRequiredLevel() {
            return requiredLevel;
        }

        public int getOreAmount() {
            return oreAmount;
        }

        public int getSmeltBarId() {
            return smeltBarId;
        }

        public int getReqOreId() {
            return requestedOreId;
        }

        public int getReqOreAmount() {
            return requestedOreAmount;
        }
    }

    public static final int FURNACE = 118;

    public static final int CANNON_AMMO_MOULD = 1057;
    public static final int MULTI_CANNON_BALL = 1041;

    public static final int BRONZE_BAR = 169;
    public static final int IRON_BAR = 170;
    public static final int STEEL_BAR = 171;
    public static final int MITHRIL_BAR = 173;
    public static final int ADDY_BAR = 174;
    public static final int RUNE_BAR = 408;
    public static final int GOLD_BAR = 172;
    public static final int SILVER_BAR = 384;
    public static final int SAND = 625;
    public static final int GOLD_BAR_FAMILYCREST = 691;
    public static final int GAUNTLETS_OF_GOLDSMITHING = 699;

    @Override
    public void onInvUseOnObject(GameObject obj, InvItem item, Player p) {
        if (obj.getID() == FURNACE && !Util.inArray(new int[] { GOLD_BAR, SILVER_BAR, SAND, GOLD_BAR_FAMILYCREST }, item.getID())) {
            if(item.getID() == STEEL_BAR && p.getInventory().hasItemId(CANNON_AMMO_MOULD)) {
                if (getCurrentLevel(p, SMITHING) < 30) {
                    p.message("You need at least level 30 smithing to make cannon balls");
                    return;
                }
                if (p.getQuestStage(Plugin.DWARF_CANNON) != -1) {
                    p.message("You need to complete the dwarf cannon quest");
                    return;
                }
                showBubble(p, item);
                message(p, 1300, "you heat the steel bar into a liquid state",
                        "and pour it into your cannon ball mould",
                        "you then leave it to cool for a short while");
                p.setBatchEvent(new BatchEvent(p, 2000, Formulae.getRepeatTimes(p, SMITHING)) {
                    public void action() {
                        owner.incExp(SMITHING, 12.5, true);
                        owner.getInventory().replace(STEEL_BAR, MULTI_CANNON_BALL);
                        addItem(owner, MULTI_CANNON_BALL, 1);
                        owner.getSender().sendInventory();
                        sleep(2000);
                        owner.message("it's very heavy");
                        if (owner.getFatigue() >= 7500) {
                            owner.message("You are too tired to smelt cannon ball");
                            interrupt();
                            return;
                        }
                        if (owner.getInventory().countId(STEEL_BAR) < 1) {
                            owner.message("You have no steel bars left");
                            interrupt();
                            return;
                        }
                    }
                });
            } else {
                handleRegularSmelting(item, p, obj);
            }
        } 
    }

    private void handleRegularSmelting(final InvItem item, Player p, final GameObject obj) {
        if(!inArray(item.getID(), Smelt.ADAMANTITE_ORE.getID(),Smelt.COAL.getID(), Smelt.COPPER_ORE.getID(), Smelt.IRON_ORE.getID(),Smelt.GOLD.getID(), Smelt.MITHRIL_ORE.getID(),Smelt.RUNITE_ORE.getID(),Smelt.SILVER.getID(),Smelt.TIN_ORE.getID(), 690)) {
            p.message("Nothing interesting happens");
            return;
        }
        String formattedName = item.getDef().getName().toUpperCase().replaceAll(" ", "_");
        Smelt smelt;
        if(item.getID() == Smelt.IRON_ORE.getID() && getCurrentLevel(p, SMITHING) >= 30 && p.getInventory().countId(Smelt.COAL.getID()) >= 2) {
            String coalChange = EntityManager.getItem(Smelt.COAL.getID()).getName().toUpperCase();
            smelt = Smelt.valueOf(coalChange);
        }  else {
            smelt = Smelt.valueOf(formattedName);
        }

        showBubble(p, item);
        if (!p.getInventory().contains(item)) {
            return;
        }
        if (!p.withinRange(obj, 2)) {
            return;
        }
        if (p.getFatigue() >= 7500) {
            p.message("You are too tired to smelt this ore");
            return;
        }
        if (getCurrentLevel(p, SMITHING) < smelt.getRequiredLevel()) {
            p.message("You need to be at least level-" + smelt.getRequiredLevel() + " smithing to " + (smelt.getSmeltBarId() == SILVER_BAR || smelt.getSmeltBarId() == GOLD_BAR || smelt.getSmeltBarId() == GOLD_BAR_FAMILYCREST ? "work " : "smelt ") + EntityManager.getItem(smelt.getSmeltBarId()).getName().toLowerCase().replaceAll("bar", ""));
            return;
        }
        if (p.getInventory().countId(smelt.getReqOreId()) < smelt.getReqOreAmount() || (p.getInventory().countId(smelt.getID()) < smelt.getOreAmount() && smelt.getReqOreAmount() != -1)) {
            if(smelt.getID() == Smelt.TIN_ORE.getID() || item.getID() == Smelt.COPPER_ORE.getID()) {
                p.message("You also need some " + (item.getID() == Smelt.TIN_ORE.getID() ? "copper" : "tin") + " to make bronze");
                return;
            }
            if(smelt.getID() == Smelt.COAL.getID() && (p.getInventory().countId(Smelt.IRON_ORE.getID()) < 1 || p.getInventory().countId(Smelt.COAL.getID()) <= 1)) {
                p.message("You need 1 iron-ore and 2 coal to make steel");
                return;
            } else {
                p.message("You need " + smelt.getReqOreAmount() + " heaps of " + EntityManager.getItem(smelt.getReqOreId()).getName().toLowerCase()
                        + " to smelt "
                        + item.getDef().getName().toLowerCase().replaceAll("ore", ""));
                return;
            }
        }

        p.message(smeltString(smelt, item));
        p.setBatchEvent(new BatchEvent(p, 1600, Formulae.getRepeatTimes(p, SMITHING)) {
            public void action() {
                if (owner.getFatigue() >= 7500) {
                    owner.message("You are too tired to smelt this ore");
                    interrupt();
                    return;
                }
                if (owner.getInventory().countId(smelt.getReqOreId()) < smelt.getReqOreAmount() || (owner.getInventory().countId(smelt.getID()) < smelt.getOreAmount() && smelt.getReqOreAmount() != -1)) {
                    if(smelt.getID() == Smelt.COAL.getID() && (owner.getInventory().countId(Smelt.IRON_ORE.getID()) < 1 || owner.getInventory().countId(Smelt.COAL.getID()) <= 1)) {
                        owner.message("You need 1 iron-ore and 2 coal to make steel");
                        interrupt();
                        return;
                    }
                    if(smelt.getID() == Smelt.TIN_ORE.getID() || item.getID() == Smelt.COPPER_ORE.getID()) {
                        owner.message("You also need some " + (item.getID() == Smelt.TIN_ORE.getID() ? "copper" : "tin") + " to make bronze");
                        interrupt();
                        return;
                    } else {
                        owner.message("You need " + smelt.getReqOreAmount() + " heaps of " + EntityManager.getItem(smelt.getReqOreId()).getName().toLowerCase()
                                + " to smelt "
                                + item.getDef().getName().toLowerCase().replaceAll("ore", ""));
                        interrupt();
                        return;
                    }
                }
                showBubble(owner, item);
                if (owner.getInventory().countId(item.getID()) > 0) {
                    if(item.getID() == GOLD_BAR_FAMILYCREST - 1) 
                        removeItem(owner, GOLD_BAR_FAMILYCREST - 1, 1);
                    else 
                        removeItem(owner, smelt.getID(), smelt.getOreAmount());

                    for (int i = 0; i < smelt.getReqOreAmount(); i++) { 
                        owner.getInventory().remove(new InvItem(smelt.getReqOreId()));
                    }
                    if (smelt.getID() == Smelt.IRON_ORE.getID() && Util.random(0, 1) == 1) {
                        owner.message("The ore is too impure and you fail to refine it");
                    } else {
                        if(item.getID() == GOLD_BAR_FAMILYCREST - 1) 
                            addItem(owner, GOLD_BAR_FAMILYCREST, 1);
                        else 
                            addItem(owner, smelt.getSmeltBarId(), 1);

                        owner.message("You retrieve a bar of " + new InvItem(smelt.getSmeltBarId()).getDef().getName().toLowerCase().replaceAll("bar", ""));

                        /** Gauntlets of Goldsmithing provide an additional 23 experience when smelting gold ores **/
                        if(owner.getInventory().wielding(GAUNTLETS_OF_GOLDSMITHING) && new InvItem(smelt.getSmeltBarId()).getID() == GOLD_BAR) {
                            owner.incExp(SMITHING, smelt.getXp() + 11, true);   
                        } else {
                            owner.incExp(SMITHING, smelt.getXp(), true);    
                        }
                    }
                } else {
                    interrupt();
                }
            }
        });
    }

    private String smeltString(Smelt smelt, InvItem item) {
        String message = null;
        if(smelt.getSmeltBarId() == BRONZE_BAR) {
            message = "You smelt the copper and tin together in the furnace";
        } else if(smelt.getSmeltBarId() == MITHRIL_BAR || smelt.getSmeltBarId() == ADDY_BAR || smelt.getSmeltBarId() == RUNE_BAR) {
            message = "You place the " + item.getDef().getName().toLowerCase().replaceAll(" ore", "") + " and " + smelt.getReqOreAmount() + " heaps of " + EntityManager.getItem(smelt.getReqOreId()).getName().toLowerCase() + " into the furnace";
        } else if(smelt.getSmeltBarId() == STEEL_BAR) {
            message = "You place the iron and 2 heaps of coal into the furnace";
        } else if(smelt.getSmeltBarId() == IRON_BAR) {
            message = "You smelt the " + item.getDef().getName().toLowerCase().replaceAll(" ore", "") + " in the furnace";
        } else if(smelt.getSmeltBarId() == SILVER_BAR || smelt.getSmeltBarId() == GOLD_BAR || smelt.getSmeltBarId() == GOLD_BAR_FAMILYCREST) {
            message = "You place a lump of " + item.getDef().getName().toLowerCase().replaceAll(" ore", "") + " in the furnace";
        }
        return message;
    }

    @Override
    public boolean blockInvUseOnObject(GameObject obj, InvItem item, Player player) {
        if (obj.getID() == FURNACE && !Util.inArray(new int[] { GOLD_BAR, SILVER_BAR, SAND, GOLD_BAR_FAMILYCREST }, item.getID())) {
            return true;
        }
        return false;
    }
}