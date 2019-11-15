package org.nemotech.rsc.plugins.skills;

import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.event.SingleEvent;
import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.external.definition.extra.ItemHerbDef;
import org.nemotech.rsc.external.definition.extra.ItemHerbSecond;
import org.nemotech.rsc.external.definition.extra.ItemUnIdentHerbDef;
import org.nemotech.rsc.event.impl.BatchEvent;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import static org.nemotech.rsc.plugins.Plugin.displayTeleportBubble;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.showBubble;
import org.nemotech.rsc.plugins.listeners.action.InvActionListener;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnItemListener;
import org.nemotech.rsc.plugins.listeners.executive.InvActionExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnItemExecutiveListener;
import org.nemotech.rsc.util.Formulae;

public class Herblaw implements InvActionListener, InvUseOnItemListener,
InvActionExecutiveListener, InvUseOnItemExecutiveListener {

    @Override
    public void onInvAction(final InvItem item, Player player) {
        if (item.getDef().getCommand().equalsIgnoreCase("Identify")) {
            handleHerbCleanup(item, player);
        }
    }

    @Override
    public boolean blockInvAction(final InvItem i, Player p) {
        if (i.getDef().getCommand().equalsIgnoreCase("Identify")) {
            return true;
        }
        return false;
    }

    private boolean handleHerbCleanup(final InvItem item, Player player) {
        if (!Constants.MEMBER_WORLD) {
            player.message(Constants.MEMBERS_ONLY_MESSAGE);
            return false;
        }
        if (player.getQuestStage(Plugin.DRUIDIC_RITUAL) != -1) {
            player.message("You need to complete Druidic ritual quest first");
            return false;
        }
        ItemUnIdentHerbDef herb = item.getUnIdentHerbDef();
        if (herb == null) {
            return false;
        }
        if (player.getCurStat(15) < herb.getLevelRequired()) {
            player.message("You cannot identify this herb");
            player.message("you need a higher herblaw level");
            return false;
        }
        player.setBatchEvent(new BatchEvent(player, 500, Formulae.getRepeatTimes(player, 15)) {
            @Override
            public void action() {
                if (!owner.getInventory().hasItemId(item.getID())) {
                    interrupt();
                    return;
                }
                ItemUnIdentHerbDef herb = item.getUnIdentHerbDef();
                InvItem newItem = new InvItem(herb.getNewId());
                owner.getInventory().remove(item);
                owner.getInventory().add(newItem);
                owner.message("This herb is a " + newItem.getDef().getName());
                owner.incExp(15, herb.getExp(), true);
                owner.setBusy(false);
            }
        });
        return true;
    }

    @Override
    public void onInvUseOnItem(Player player, InvItem item, InvItem usedWith) {
        ItemHerbSecond secondDef = null;
        if ((secondDef = EntityManager.getItemHerbSecond(item.getID(), usedWith
                .getID())) != null) {
            doHerbSecond(player, item, usedWith, secondDef);
        } else if ((secondDef = EntityManager.getItemHerbSecond(usedWith
                .getID(), item.getID())) != null) {
            doHerbSecond(player, usedWith, item, secondDef);
        } else if (item.getID() == 468) {
            doGrind(player, item, usedWith);
        } else if (usedWith.getID() == 468) {
            doGrind(player, usedWith, item);
        } else if (item.getID() == 464) {
            doHerblaw(player, item, usedWith);
        } else if (usedWith.getID() == 464) {
            doHerblaw(player, usedWith, item);
        } else if(item.getID() == 1052 && usedWith.getID() == 1051) {
            makeLiquid(player, item, usedWith);
        } else if(item.getID() == 1051 && usedWith.getID() == 1052) {
            makeLiquid(player, usedWith, item);
        } else if(item.getID() == 1074 && (usedWith.getID() == 1051 || usedWith.getID() == 444)) {
            makeLiquid(player, item, usedWith);
        } else if(usedWith.getID() == 1074 && (item.getID() == 1051 || item.getID() == 444)) {
            makeLiquid(player, usedWith, item);
        } else if(usedWith.getID() == 1161 && item.getID() == 1160 || usedWith.getID() == 1160 && item.getID() == 1161) {
            if (player.getCurStat(15) < 6) {
                player.message("You need to have a herblaw level of 6 or over to mix this liquid");
                return;
            }
            player.incExp(15, 5.0, true);
            player.message("You mix the nitrate powder into the liquid");
            player.message("It has produced a foul mixture");
            showBubble(player, new InvItem(1160));
            player.getInventory().remove(1160, 1);
            player.getInventory().replace(1161, 1178);
        } else if(usedWith.getID() == 1179 && item.getID() == 1178 || usedWith.getID() == 1178 && item.getID() == 1179) {
            if (player.getCurStat(15) < 10) {
                player.message("You need to have a herblaw level of 10 or over to mix this liquid");
                return;
            }
            player.incExp(15, 6.0, true);
            player.message("You mix the charcoal into the liquid");
            player.message("It has produced an even fouler mixture");
            showBubble(player, new InvItem(1179));
            player.getInventory().remove(1179, 1);
            player.getInventory().replace(1178, 1180);
        } else if(usedWith.getID() == 1284 && item.getID() == 1180 || usedWith.getID() == 1180 && item.getID() == 1284) {
            if (player.getCurStat(15) < 10) {
                player.message("You need to have a herblaw level of 10 or over to mix this liquid");
                return;
            }
            player.incExp(15, 7.0, true);
            player.message("You mix the root into the mixture");
            player.message("You produce a potentially explosive compound...");
            showBubble(player, new InvItem(1284));
            player.getInventory().remove(1284, 1);
            player.getInventory().replace(1180, 1176);
            playerTalk(player, null, "Excellent this looks just right");
        } else if(usedWith.getID() == 1251 && item.getID() == 818 || usedWith.getID() == 818 && item.getID() == 1251) {
            if (player.getCurStat(15) < 45) {
                player.message("You need to have a herblaw level of 45 or over to mix this potion");
                return;
            }
            player.message("You add the Ardrigal to the Snakesweed Solution.");
            player.message("The mixture seems to bubble slightly with a strange effervescence...");
            player.getInventory().remove(818, 1);
            player.getInventory().replace(1251, 1253);
        } else if(usedWith.getID() == 1252 && item.getID() == 816 || usedWith.getID() == 816 && item.getID() == 1252) {
            if (player.getCurStat(15) < 45) {
                player.message("You need to have a herblaw level of 45 or over to mix this potion");
                return;
            }
            player.message("You add the Snake Weed to the Ardrigal solution.");
            player.message("The mixture seems to bubble slightly with a strange effervescence...");
            player.getInventory().remove(816, 1);
            player.getInventory().replace(1252, 1253);
        }
    }
    // ARCANIRA ROOT = 1284
    // mixed chemicals = 1178

    @Override
    public boolean blockInvUseOnItem(Player p, InvItem item, InvItem usedWith) {
        if ((EntityManager.getItemHerbSecond(item.getID(), usedWith.getID())) != null
                || (EntityManager.getItemHerbSecond(usedWith.getID(), item
                        .getID())) != null) {
            return true;
        } else if (item.getID() == 468 || usedWith.getID() == 468) {
            return true;
        } else if (item.getID() == 464 || usedWith.getID() == 464) {
            return true;
        } else if(item.getID() == 1052 && usedWith.getID() == 1051
                || item.getID() == 1051 && usedWith.getID() == 1052) {
            return true;
        } else if(item.getID() == 1074 && (usedWith.getID() == 1051 || usedWith.getID() == 444)
                || usedWith.getID() == 1074 && (item.getID() == 1051 || item.getID() == 444)) {
            return true;
        } else if(usedWith.getID() == 1161 && item.getID() == 1160 || usedWith.getID() == 1160 && item.getID() == 1161) {
            return true;
        } else if(usedWith.getID() == 1179 && item.getID() == 1178 || usedWith.getID() == 1178 && item.getID() == 1179) {
            return true;
        } else if(usedWith.getID() == 1284 && item.getID() == 1180 || usedWith.getID() == 1180 && item.getID() == 1284) {
            return true;
        } else if(usedWith.getID() == 1251 && item.getID() == 818 || usedWith.getID() == 818 && item.getID() == 1251) {
            return true;
        } else if(usedWith.getID() == 1252 && item.getID() == 816 || usedWith.getID() == 816 && item.getID() == 1252) {
            return true;
        }
        return false;
    }

    private boolean doHerblaw(Player player, final InvItem vial,
            final InvItem herb) {
        if (!Constants.MEMBER_WORLD) {
            player.message(Constants.MEMBERS_ONLY_MESSAGE);
            return false;
        }
        if(vial.getID() == 464 && herb.getID() == 1051) {
            player.message("You mix the ground bones into the water");
            player.message("Fizz!!!");
            playerTalk(player, null, "Oh dear, the mixture has evaporated!",
                    "It's useless...");
            player.getInventory().remove(vial.getID(), 1);
            player.getInventory().remove(herb.getID(), 1);
            player.getInventory().add(new InvItem(465, 1));
            return false;
        }
        if(vial.getID() == 464 && (herb.getID() == 936)) {
            player.message("You mix the berries into the water");
            player.getInventory().remove(vial.getID(), 1);
            player.getInventory().remove(herb.getID(), 1);
            player.getInventory().add(new InvItem(1074, 1));
            return false;
        }
        if(vial.getID() == 464 && (herb.getID() == 818)) {
            player.message("You put the ardrigal herb into the watervial.");
            player.message("You make a solution of Ardrigal.");
            player.getInventory().remove(vial.getID(), 1);
            player.getInventory().remove(herb.getID(), 1);
            player.getInventory().add(new InvItem(1252, 1));
            return false;
        }
        if(vial.getID() == 464 && (herb.getID() == 816)) {
            player.message("You put the Snake Weed herb into the watervial.");
            player.message("You make a solution of Snake Weed.");
            player.getInventory().remove(vial.getID(), 1);
            player.getInventory().remove(herb.getID(), 1);
            player.getInventory().add(new InvItem(1251, 1));
            return false;
        }
        final ItemHerbDef herbDef = EntityManager.getItemHerbDef(herb.getID());
        if (herbDef == null) {
            return false;
        }
        World.getWorld().getDelayedEventHandler().add(new SingleEvent(player, 1200) {
            @Override
            public void action() {
                if (owner.getCurStat(15) < herbDef.getReqLevel()) {
                    owner.message("you need level " + herbDef.getReqLevel()
                    + " herblaw to make this potion");
                    interrupt();
                    return;
                }
                if (owner.getInventory().hasItemId(vial.getID())
                        && owner.getInventory().hasItemId(herb.getID())) {
                    owner.getInventory().remove(vial.getID(), 1);
                    owner.getInventory().remove(herb.getID(), 1);
                    owner.message("You put the " + herb.getDef().getName()
                            + " into the vial of water");
                    owner.getInventory().add(new InvItem(herbDef.getPotionID(), 1));
                } else {
                    interrupt();
                }
            }
        });
        return true;
    }

    private boolean doHerbSecond(Player player, final InvItem second,
            final InvItem unfinished, final ItemHerbSecond def) {
        if (!Constants.MEMBER_WORLD) {
            player.message(Constants.MEMBERS_ONLY_MESSAGE);
            return false;
        }
        if (unfinished.getID() != def.getUnfinishedID()) {
            return false;
        }
        World.getWorld().getDelayedEventHandler().add(new SingleEvent(player, 1000) {
            public void action() {
                if (owner.getCurStat(15) < def.getReqLevel()) {
                    owner.message("You need a herblaw level of "
                            + def.getReqLevel() + " to make this potion");
                    interrupt();
                    return;
                }
                if (owner.getInventory().hasItemId(second.getID())
                        && owner.getInventory().hasItemId(unfinished.getID())) {
                    owner.message("You mix the " + second.getDef().getName()
                            + " into your potion");
                    owner.getInventory().remove(second.getID(), 1);
                    owner.getInventory().remove(unfinished.getID(), 1);
                    owner.getInventory().add(new InvItem(def.getPotionID(), 1));
                    owner.incExp(15, def.getExp(), true);
                } else
                    interrupt();
            }
        });
        return false;
    }
    // 1052.
    private boolean makeLiquid(Player p, final InvItem ingredient,
            final InvItem unfinishedPot) {
        if (!Constants.MEMBER_WORLD) {
            p.message(Constants.MEMBERS_ONLY_MESSAGE);
            return false;
        }
        if(unfinishedPot.getID() == 1074 && (ingredient.getID() == 1051 || ingredient.getID() == 444)
                || ingredient.getID() == 1074 && (unfinishedPot.getID() == 1051 || unfinishedPot.getID() == 444)) {
            p.message("You mix the liquid with the " + ingredient.getDef().getName().toLowerCase());
            p.message("Bang!!!");
            displayTeleportBubble(p, p.getX(), p.getY(), true);
            p.damage(8);
            playerTalk(p, null, "Ow!");
            p.message("You mixed this ingredients incorrectly and the mixture exploded!");
            p.getInventory().remove(unfinishedPot.getID(), 1);
            p.getInventory().remove(ingredient.getID(), 1);
            p.getInventory().add(new InvItem(465, 1));
            return false;
        }
        if(unfinishedPot.getID() == 1052 && ingredient.getID() == 1051 
                || unfinishedPot.getID() == 1051 && ingredient.getID() == 1052) {
            if (p.getCurStat(15) < 14) {
                p.message("You need to have a herblaw level of 14 or over to mix this liquid");
                return false;
            }
            if (p.getInventory().hasItemId(ingredient.getID())
                    && p.getInventory().hasItemId(unfinishedPot.getID())) {
                p.message("You mix the " + ingredient.getDef().getName().toLowerCase() + " into the liquid");
                p.message("You produce a strong potion");
                p.getInventory().remove(ingredient.getID(), 1);
                p.getInventory().remove(unfinishedPot.getID(), 1);
                p.getInventory().add(new InvItem(1053, 1));
                p.incExp(15, 25.0, true);
            }
        }
        return false;
    }

    private boolean doGrind(Player player, final InvItem mortar,
            final InvItem item) {
        if (!Constants.MEMBER_WORLD) {
            player.message(Constants.MEMBERS_ONLY_MESSAGE);
            return false;
        }
        int newID;
        switch (item.getID()) {
        case 466: // Unicorn Horn
            newID = 473;
            break;
        case 467: // Blue dragon scale
            newID = 472;
            break;
            /**
             * Quest items.
             */
        case 604: // Bat bone
            newID = 1051; // Ground bat
            break;
        case 983: // Charcoal GEM
            newID = 1179; // Ground charcoal
            player.message("You grind the charcoal to a powder");
            break;
        case 337:
            newID = 772; // Chocolate dust
            break;
            /**
             * End of Herblaw Quest Items.
             */
        default:
            return false;
        }
        if (player.getInventory().remove(item) > -1) {
            if(item.getID() != 983) {
                player.message("You grind the " + item.getDef().getName()
                        + " to dust");
            }
            showBubble(player, new InvItem(468));
            player.getInventory().add(new InvItem(newID, 1));
        }
        return true;
    }
    
}