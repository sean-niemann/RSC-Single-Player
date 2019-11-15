package org.nemotech.rsc.plugins.skills;

import org.nemotech.rsc.event.MiniEvent;
import org.nemotech.rsc.event.ShortEvent;
import org.nemotech.rsc.util.Formulae;
import org.nemotech.rsc.external.definition.extra.ItemCraftingDef;
import org.nemotech.rsc.event.impl.BatchEvent;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.client.sound.SoundEffect;
import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.external.definition.extra.ItemGemDef;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.MenuHandler;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;

import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.showBubble;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnItemListener;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnObjectListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnItemExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnObjectExecutiveListener;
import org.nemotech.rsc.util.Util;

public class Crafting implements InvUseOnItemListener,
        InvUseOnItemExecutiveListener, InvUseOnObjectListener,
        InvUseOnObjectExecutiveListener {

    public static final World world = World.getWorld();

    @Override
    public void onInvUseOnObject(GameObject obj, final InvItem item, Player owner) {
        switch(obj.getID()) {
            case 118:
            case 813: // Furnace
                if(item.getID() == 172 || item.getID() == 691) {
                    World.getWorld().getDelayedEventHandler().add(new MiniEvent(owner) {
                        @Override
                        public void action() {
                            owner.message(
                                    "What would you like to make?");
                            String[] options = new String[]{"Ring", "Necklace",
                                "Amulet"};
                            owner.setMenuHandler(new MenuHandler(options) {
                                public void handleReply(int option, String reply) {
                                    if(owner.isBusy() || option < 0 || option > 2) {
                                        return;
                                    }
                                    final int[] moulds = {293, 295, 294};
                                    final int[] gems = {-1, 164, 163, 162, 161,
                                        523};
                                    String[] options = {"Gold", "Sapphire",
                                        "Emerald", "Ruby", "Diamond"};
                                    if(Constants.MEMBER_WORLD) {
                                        options = new String[]{"Gold",
                                            "Sapphire", "Emerald", "Ruby",
                                            "Diamond", "Dragonstone"};
                                    }
                                    final int craftType = option;
                                    if(owner.getInventory().countId(
                                            moulds[craftType]) < 1) {
                                        owner.message(
                                                "You need a "
                                                + EntityManager.getItem(
                                                        moulds[craftType])
                                                        .getName()
                                                + " to make a " + reply);
                                        return;
                                    }
                                    owner.message(
                                            "What type of " + reply
                                            + " would you like to make?");
                                    owner.setMenuHandler(new MenuHandler(options) {
                                        public void handleReply(final int option,
                                                final String reply) {
                                            owner.setBatchEvent(new BatchEvent(
                                                    owner, 1400,
                                                    Formulae.getRepeatTimes(owner, 12)) {
                                                public void action() {
                                                    if(option < 0 || option > (Constants.MEMBER_WORLD ? 5 : 4)) {
                                                        owner.checkAndInterruptBatchEvent();
                                                        return;
                                                    }
                                                    if(owner.getFatigue() >= 7500) {
                                                        owner.message("You are too tired to craft");
                                                        interrupt();
                                                        return;
                                                    }
                                                    if(option != 0
                                                            && owner.getInventory()
                                                                    .countId(
                                                                            gems[option]) < 1) {
                                                        owner.message("You don't have a "
                                                                + reply
                                                                + ".");
                                                        owner.checkAndInterruptBatchEvent();
                                                        return;
                                                    }
                                                    ItemCraftingDef def = EntityManager.getCraftingDef((option * 3) + craftType);
                                                    if(def == null) {
                                                        owner.message("Nothing interesting happens");
                                                        owner.checkAndInterruptBatchEvent();
                                                        return;
                                                    }
                                                    if(owner.getCurStat(12) < def.getReqLevel()) {
                                                        owner.message("You need a crafting skill level of "
                                                                + def.getReqLevel()
                                                                + " to make this");
                                                        owner.checkAndInterruptBatchEvent();
                                                        return;
                                                    }
                                                    if(owner.getInventory()
                                                            .remove(item) > -1
                                                            && (option == 0 || owner
                                                                    .getInventory()
                                                                    .remove(gems[option],
                                                                            1) > -1)) {
                                                        showBubble(owner, item);
                                                        InvItem result = null;
                                                        if(item.getID() == 691
                                                                && option == 3
                                                                && craftType == 0) {
                                                            result = new InvItem(
                                                                    692, 1);
                                                        } else if(item.getID() == 691
                                                                && option == 3
                                                                && craftType == 1) {
                                                            result = new InvItem(
                                                                    693, 1);
                                                        } else {
                                                            result = new InvItem(
                                                                    def.getItemID(),
                                                                    1);
                                                        }
                                                        owner.message(
                                                                "You make a "
                                                                + result.getDef()
                                                                        .getName());
                                                        owner.getInventory().add(
                                                                result);
                                                        owner.incExp(12,
                                                                def.getExp(), true);
                                                    } else {
                                                        owner.message("You don't have a "
                                                                + reply
                                                                + ".");
                                                        owner.checkAndInterruptBatchEvent();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                    owner.getSender().sendMenu(options);
                                }
                            });
                            owner.getSender().sendMenu(options);
                        }
                    });
                    return;
                }
                if(item.getID() == 384) { // Silver Bar (Crafting)
                    World.getWorld().getDelayedEventHandler().add(new MiniEvent(owner) {
                        public void action() {
                            owner.message(
                                    "What would you like to make?");
                            String[] options = new String[]{"Holy Symbol of Saradomin", "Unholy symbol of Zamorak"};
                            owner.setMenuHandler(new MenuHandler(options) {
                                public void handleReply(final int option,
                                        String reply) {
                                    if(owner.isBusy() || option < 0 || option > 1) {
                                        return;
                                    }
                                    int[] moulds = {386, 1026};
                                    final int[] results = {44, 1027};
                                    if(owner.getInventory()
                                            .countId(moulds[option]) < 1) {
                                        owner.message(
                                                "You need a " + EntityManager.getItem(moulds[option]).getName() + " to make a " + reply + "!");
                                        return;
                                    }

                                    owner.setBatchEvent(new BatchEvent(owner, 1400,
                                            Formulae.getRepeatTimes(owner, 12)) {

                                        @Override
                                        public void action() {
                                            if(owner.getCurStat(12) < 16) {
                                                owner.message("You need a crafting skill of level 16 to make this");
                                                interrupt();
                                                return;
                                            }
                                            if(owner.getInventory().remove(item) > -1) {
                                                showBubble(owner, item);
                                                InvItem result = new InvItem(
                                                        results[option]);
                                                owner.message("You make a "
                                                        + result.getDef()
                                                                .getName());
                                                owner.getInventory().add(result);
                                                owner.incExp(12, 50, true);
                                            } else {
                                                interrupt();
                                            }
                                        }

                                    });

                                }
                            });
                            owner.getSender().sendMenu(options);
                        }
                    });
                    return;
                } else if(item.getID() == 625) { // Sand (Glass)
                    if(owner.getInventory().countId(624) < 1) {
                        owner.message(
                                "You need some soda ash to make glass");
                        return;
                    }
                    owner.setBusy(true);
                    showBubble(owner, item);
                    owner.message("you heat the sand and soda ash in the furnace to make glass");
                    World.getWorld().getDelayedEventHandler().add(new ShortEvent(owner) {
                        @Override
                        public void action() {
                            if(owner.getInventory().remove(624, 1) > -1 && owner.getInventory().remove(item) > -1) {
                                owner.getInventory().add(new InvItem(623, 1));
                                owner.getInventory().add(new InvItem(21, 1));
                                owner.incExp(12, 20, true);
                            }
                            owner.setBusy(false);
                        }
                    });
                    return;
                }
                break;
        }
    }

    @Override
    public void onInvUseOnItem(Player player, InvItem item1, InvItem item2) {
        if(item1.getID() == 167 && doCutGem(player, item1, item2)) {
            return;
        } else if(item2.getID() == 167 && doCutGem(player, item2, item1)) {
            return;
        } else if(item1.getID() == 621 && doGlassBlowing(player, item1, item2)) {
            return;
        } else if(item2.getID() == 621 && doGlassBlowing(player, item2, item1)) {
            return;
        }
        if(item1.getID() == 39 && makeLeather(player, item1, item2)) {
            return;
        } else if(item2.getID() == 39 && makeLeather(player, item2, item1)) {
            return;
        } else if(item1.getID() == 207 && useWool(player, item1, item2)) {
            return;
        } else if(item2.getID() == 207 && useWool(player, item2, item1)) {
            return;
        } else if((item1.getID() == 50 || item1.getID() == 141 || item1
                .getID() == 342) && useWater(player, item1, item2)) {
            return;
        } else if((item2.getID() == 50 || item2.getID() == 141 || item2
                .getID() == 342) && useWater(player, item2, item1)) {
            return;
        } else if(item1.getID() == 623 && item2.getID() == 1017
                || item1.getID() == 1017 && item2.getID() == 623) {
            if(player.getCurStat(12) < 10) {
                player.message(
                        "You need a crafting level of 10 to make the lens");
                return;
            }
            if(player.getInventory().remove(new InvItem(623)) > -1) {
                player.message("You pour the molten glass into the mould");
                player.message("And clasp it together");
                player.message("It produces a small convex glass disc");
                player.getInventory().add(new InvItem(1018));
            }
            return;
        }
        return;
    }

    private boolean doCutGem(Player player, final InvItem chisel, final InvItem gem) {
        final ItemGemDef gemDef = EntityManager.getItemGemDef(gem.getID());
        if(gemDef == null) {
            return false;
        }
        player.setBatchEvent(new BatchEvent(player, 650, Formulae.getRepeatTimes(player, 12)) {
            @Override
            public void action() {
                if(owner.getCurStat(12) < gemDef.getReqLevel()) {
                    owner.message("You need a crafting level of " + gemDef.getReqLevel()
                            + " to cut " + gem.getDef().getName().toLowerCase().replace("uncut ", "") + "s");
                    interrupt();
                    return;
                }
                if(owner.getInventory().remove(gem) > -1) {
                    InvItem cutGem = new InvItem(gemDef.getGemID(), 1);
                    /**
                     * Jade, Opal and red topaz fail handler - 25% chance to fail *
                     */
                    if((gem.getID() == 889 || gem.getID() == 890 || gem.getID() == 891) && Util.random(0, 3) == 2) {
                        owner.message("You miss hit the chisel and smash the " + cutGem.getDef().getName() + " to pieces!");
                        owner.getInventory().add(new InvItem(915));
                        owner.incExp(12, (gem.getID() == 889 ? 6.25 : gem.getID() == 890 ? 5 : 3.75), true);
                    } else {
                        owner.message("You cut the " + cutGem.getDef().getName().toLowerCase());
                        owner.getSender().sendSound(SoundEffect.CHISEL);
                        owner.getInventory().add(cutGem);
                        owner.incExp(12, gemDef.getExp(), true);
                    }
                } else {
                    interrupt();
                }
            }
        });
        return true;
    }

    private boolean doGlassBlowing(Player player, final InvItem pipe,
            final InvItem glass) {
        if(glass.getID() != 623) {
            return false;
        }
        player.message("what would you like to make?");
        World.getWorld().getDelayedEventHandler().add(new MiniEvent(player) {
            @Override
            public void action() {
                String[] options = new String[]{"Vial", "orb", "Beer glass"};
                owner.setMenuHandler(new MenuHandler(options) {
                    @Override
                    public void handleReply(final int option, final String reply) {
                        InvItem result;
                        int reqLvl, exp;
                        switch(option) {
                            case 0:
                                result = new InvItem(465, 1);
                                reqLvl = 33;
                                exp = 35;
                                break;
                            case 1:
                                result = new InvItem(611, 1);
                                reqLvl = 46;
                                exp = 53;
                                break;
                            case 2:
                                result = new InvItem(620, 1);
                                reqLvl = 3;
                                exp = 18;
                                break;
                            default:
                                return;
                        }
                        if(owner.getCurStat(12) < reqLvl) {
                            owner.message(
                                    "You need a crafting level of " + reqLvl
                                    + " to make a "
                                    + result.getDef().getName() + ".");
                            return;
                        }
                        if(owner.getInventory().remove(glass) > -1) {
                            owner.message(
                                    "You make a " + result.getDef().getName());
                            owner.getInventory().add(result);
                            owner.incExp(12, exp, true);
                        }
                    }
                });
                owner.getSender().sendMenu(options);
            }
        });
        return true;
    }

    private boolean makeLeather(Player player, final InvItem needle, final InvItem leather) {
        if(leather.getID() != 148) {
            return false;
        }
        if(player.getInventory().countId(43) < 1) {
            player.message(
                    "You need some thread to make anything out of leather");
            return true;
        }
        if(Util.random(0, 5) == 0) {
            player.getInventory().remove(43, 1);
        }
        World.getWorld().getDelayedEventHandler().add(new MiniEvent(player) {
            @Override
            public void action() {
                String[] options = new String[]{"Armour", "Gloves", "Boots",
                    "Cancel"};
                owner.setMenuHandler(new MenuHandler(options) {
                    public void handleReply(final int option, final String reply) {
                        InvItem result;
                        int reqLvl, exp;
                        switch(option) {
                            case 0:
                                result = new InvItem(15, 1);
                                reqLvl = 14;
                                exp = 25;
                                break;
                            case 1:
                                result = new InvItem(16, 1);
                                reqLvl = 1;
                                exp = 14;
                                break;
                            case 2:
                                result = new InvItem(17, 1);
                                reqLvl = 7;
                                exp = 17;
                                break;
                            default:
                                return;
                        }
                        if(owner.getCurStat(12) < reqLvl) {
                            owner.message("You need a crafting level of " + reqLvl + " to make " + result.getDef().getName() + ".");
                            return;
                        }
                        if(owner.getInventory().remove(leather) > -1) {
                            owner.message("You make some " + result.getDef().getName());
                            owner.getInventory().add(result);
                            owner.incExp(12, exp, true);
                        }
                    }
                });
                owner.getSender().sendMenu(options);
            }
        });
        return true;
    }

    private boolean useWool(Player player, final InvItem woolBall, final InvItem item) {
        int newID;
        switch(item.getID()) {
            case 44: // Holy Symbol of saradomin
                newID = 45;
                break;
            case 1027: // Unholy Symbol of Zamorak
                newID = 1028;
                break;
            case 296: // Gold Amulet
                newID = 301;
                break;
            case 297: // Sapphire Amulet
                newID = 302;
                break;
            case 298: // Emerald Amulet
                newID = 303;
                break;
            case 299: // Ruby Amulet
                newID = 304;
                break;
            case 300: // Diamond Amulet
                newID = 305;
                break;
            case 524: // Dragonstone Amulet
                newID = 610;
                break;
            default:
                return false;
        }
        final int newId = newID;
        player.setBatchEvent(new BatchEvent(player, 650, Formulae.getRepeatTimes(player, 12)) {
            @Override
            public void action() {
                if(owner.getInventory().countId(item.getID()) <= 0 || owner.getInventory().countId(207) <= 0) {
                    interrupt();
                    return;
                }
                if(owner.getInventory().remove(woolBall) > -1 && owner.getInventory().remove(item) > -1) {
                    owner.message("You put some string on your " + item.getDef().getName().toLowerCase());
                    owner.getInventory().add(new InvItem(newId, 1));
                } else {
                    interrupt();
                }
            }
        });
        return true;
    }

    private boolean useWater(Player player, final InvItem water, final InvItem item) {
        int jugID = Formulae.getEmptyJug(water.getID());
        if(jugID == -1) { // This shouldn't happen
            return false;
        }
        switch(item.getID()) {
            case 149: // Clay
                if(player.getInventory().remove(water) > -1 && player.getInventory().remove(item) > -1) {
                    message(player, 1200, "You mix the clay and water");
                    player.message("You now have some soft workable clay");
                    player.getInventory().add(new InvItem(jugID, 1));
                    player.getInventory().add(new InvItem(243, 1));
                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean blockInvUseOnItem(Player player, InvItem item1, InvItem item2) {
        if(item1.getID() == 167) {
            return true;
        } else if(item2.getID() == 167) {
            return true;
        } else if(item1.getID() == 621) {
            return true;
        } else if(item2.getID() == 621) {
            return true;
        } else if(item1.getID() == 39) {
            return true;
        } else if(item2.getID() == 39) {
            return true;
        } else if(item1.getID() == 207) {
            return true;
        } else if(item2.getID() == 207) {
            return true;
        } else if((item1.getID() == 50 || item1.getID() == 141 || item1
                .getID() == 342) && item2.getID() == 149) {
            return true;
        } else if((item2.getID() == 50 || item2.getID() == 141 || item2
                .getID() == 342) && item1.getID() == 149) {
            return true;
        } else if(item1.getID() == 623 && item2.getID() == 1017
                || item1.getID() == 1017 && item2.getID() == 623) {
            return true;
        }

        return false;
    }

    @Override
    public boolean blockInvUseOnObject(GameObject obj, InvItem item, Player player) {
        if((obj.getID() == 118 || obj.getID() == 813)
                && (item.getID() == 384 || item.getID() == 172 || item.getID() == 625)
                || item.getID() == 691) {
            return true;
        }
        return false;
    }

}
