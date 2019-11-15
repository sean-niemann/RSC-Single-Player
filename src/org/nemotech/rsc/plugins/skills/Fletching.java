package org.nemotech.rsc.plugins.skills;

import org.nemotech.rsc.Constants;
import org.nemotech.rsc.event.MiniEvent;
import org.nemotech.rsc.event.impl.BatchEvent;
import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.external.definition.extra.ItemArrowHeadDef;
import org.nemotech.rsc.external.definition.extra.ItemBowStringDef;
import org.nemotech.rsc.external.definition.extra.ItemDartTipDef;
import org.nemotech.rsc.external.definition.extra.ItemLogCutDef;
import org.nemotech.rsc.model.MenuHandler;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;
import static org.nemotech.rsc.plugins.Plugin.FLETCHING;
import static org.nemotech.rsc.plugins.Plugin.addItem;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnItemExecutiveListener;
import org.nemotech.rsc.util.Formulae;

// TODO!!!! BOWSTRING JSON MISFORMATTED FOR WINDOWS.. SEE BELOW
public class Fletching implements InvUseOnItemExecutiveListener {

    @Override
    public boolean blockInvUseOnItem(Player player, InvItem item1, InvItem item2) {
        if (item1.getID() == 381 && attachFeathers(player, item1, item2)) {
            return true;
        } else if (item2.getID() == 381 && attachFeathers(player, item2, item1)) {
            return true;
        } else if (item1.getID() == 676 && doBowString(player, item1, item2)) {
            return true;
        } else if (item2.getID() == 676 && doBowString(player, item2, item1)) {
            return true;
        } else if (item1.getID() == 637 && doArrowHeads(player, item1, item2)) {
            return true;
        } else if (item2.getID() == 637 && doArrowHeads(player, item2, item1)) {
            return true;
        } else if (item1.getID() == 13 && doLogCut(player, item1, item2)) {
            return true;
        } else if (item2.getID() == 13 && doLogCut(player, item2, item1)) {
            return true;
        }
        return false;
    }

    private boolean attachFeathers(Player player, final InvItem feathers, final InvItem item) {
        if (!Constants.MEMBER_WORLD) {
            player.message(Constants.MEMBERS_ONLY_MESSAGE);
            return true;
        }
        int amount = 10;
        if (feathers.getAmount() < amount) {
            amount = feathers.getAmount();
        }
        if (item.getAmount() < amount) {
            amount = item.getAmount();
        }
        final int itemID;
        int exp;
        ItemDartTipDef tipDef = null;
        if (item.getID() == 280) {
            itemID = 637;
            exp = amount;
        } else if ((tipDef = EntityManager.getItemDartTipDef(item.getID())) != null) {
            itemID = tipDef.getDartID();
            exp = (int) (tipDef.getExp() * (double) amount);
        } else {
            return false;
        }
        final int amt = amount;
        final int xp = exp;
        player.setBatchEvent(new BatchEvent(player, 650, Formulae.getRepeatTimes(player, FLETCHING)) {
            @Override
            public void action() {
                if(owner.getInventory().countId(feathers.getID()) < 1) {
                    interrupt();
                    return;
                }
                if(owner.getInventory().countId(item.getID()) < 1) {
                    interrupt();
                    return;
                }
                if (owner.getInventory().remove(feathers.getID(), amt) > -1
                        && owner.getInventory().remove(item.getID(), amt) > -1) {
                    owner.message("You attach feathers to some of your "
                            + item.getDef().getName());
                    addItem(owner, itemID, amt);
                    owner.incExp(9, xp, true);
                } else {
                    interrupt();
                }
            }
        });
        return true;
    }

    private boolean doArrowHeads(Player player, final InvItem headlessArrows, final InvItem arrowHeads) {
        if (!Constants.MEMBER_WORLD) {
            player.message(Constants.MEMBERS_ONLY_MESSAGE);
            return true;
        }
        final ItemArrowHeadDef headDef = EntityManager.getItemArrowHeadDef(arrowHeads.getID());
        if (headDef == null) {
            return false;
        }

        player.setBatchEvent(new BatchEvent(player, 650, Formulae.getRepeatTimes(player, FLETCHING)) {
            @Override
            public void action() {
                if (owner.getCurStat(9) < headDef.getReqLevel()) {
                    owner.message("You need a fletching skill of "
                            + headDef.getReqLevel() + " or above to do that");
                    interrupt();
                    return;
                }
                if(owner.getInventory().countId(arrowHeads.getID()) < 1) {
                    interrupt();
                    return;
                }
                if(owner.getInventory().countId(headlessArrows.getID()) < 1) {
                    interrupt();
                    return;
                }
                int amount = 10;
                if (headlessArrows.getAmount() < amount) {
                    amount = headlessArrows.getAmount();
                }
                if (arrowHeads.getAmount() < amount) {
                    amount = arrowHeads.getAmount();
                }
                final int amt = amount;
                if (owner.getInventory().remove(headlessArrows.getID(), amt) > -1
                        && owner.getInventory().remove(arrowHeads.getID(), amt) > -1) {
                    owner.message("You attach "
                            + arrowHeads.getDef().getName().toLowerCase()
                            + " to some of your arrows");
                    owner.getInventory().add(new InvItem(headDef.getArrowID(), amt));
                    owner.incExp(9, (int) (headDef.getExp() * (double) amt),
                            true);
                } else {
                    interrupt();
                }
            }
        });
        return true;
    }

    private boolean doBowString(Player player, final InvItem bowString, final InvItem bow) {
        if (!Constants.MEMBER_WORLD) {
            player.message(Constants.MEMBERS_ONLY_MESSAGE);
            return true;
        }
        final ItemBowStringDef stringDef = null;//TODO: EntityManager.getItemBowStringDef(bow.getID());
        if (stringDef == null) {
            return false;
        }

        player.setBatchEvent(new BatchEvent(player, 650, Formulae
                .getRepeatTimes(player, FLETCHING)) {
            @Override
            public void action() {
                if (owner.getCurStat(9) < stringDef.getReqLevel()) {
                    owner.message("You need a fletching skill of "
                            + stringDef.getReqLevel() + " or above to do that");
                    interrupt();
                    return;
                }
                if(owner.getInventory().countId(bow.getID()) < 1) {
                    interrupt();
                    return;
                }
                if(owner.getInventory().countId(bowString.getID()) < 1) {
                    interrupt();
                    return;
                }
                if (owner.getInventory().remove(bowString) > -1
                        && owner.getInventory().remove(bow) > -1) {
                    owner.message("You add a string to the bow");
                    owner.getInventory().add(new InvItem(stringDef.getBowID(), 1));
                    owner.incExp(9, stringDef.getExp(), true);
                } else 
                    interrupt();
            }
        });
        return true;
    }

    private boolean doLogCut(final Player player, final InvItem knife, final InvItem log) {
        if (!Constants.MEMBER_WORLD) {
            player.message(Constants.MEMBERS_ONLY_MESSAGE);
            return true;
        }
        final ItemLogCutDef cutDef = EntityManager.getItemLogCutDef(log.getID());
        if (cutDef == null) {
            return false;
        }
        player.message("What would you like to make?");
        World.getWorld().getDelayedEventHandler().add(new MiniEvent(player) {
            @Override
            public void action() {
                String[] option = new String[] { "Make arrow shafts",
                        "Make shortbow", "Make longbow" };
                String[] options = new String[] { "Make shortbow",
                "Make longbow" };
                if (log.getID() == 14) {
                    owner.setMenuHandler(new MenuHandler(option) {
                        @Override
                        public void handleReply(final int option, final String reply) {
                            final int reqLvl, exp, itemID, amount;
                            String cutMessage = null;
                            switch (option) {
                            case 0:
                                itemID = 280;
                                amount = 10;
                                reqLvl = cutDef.getShaftLvl();
                                exp = cutDef.getShaftExp();
                                cutMessage = "You carefully cut the wood into 10 arrow shafts";
                                break;
                            case 1:
                                itemID = cutDef.getShortbowID();
                                amount = 1;
                                reqLvl = cutDef.getShortbowLvl();
                                exp = cutDef.getShortbowExp();
                                cutMessage = "You carefully cut the wood into a shortbow";
                                break;
                            case 2:
                                itemID = cutDef.getLongbowID();
                                amount = 1;
                                reqLvl = cutDef.getLongbowLvl();
                                exp = cutDef.getLongbowExp();
                                cutMessage = "You carefully cut the wood into a longbow";
                                break;
                            default:
                                return;
                            }
                            final int requiredLvl = reqLvl;
                            final int experience = exp;
                            final String cutMessages = cutMessage;
                            player.setBatchEvent(new BatchEvent(player, 650, Formulae
                                    .getRepeatTimes(player, FLETCHING)) {

                                @Override
                                public void action() {
                                    if (owner.getCurStat(9) < requiredLvl) {
                                        owner.message("You need a skill level of " + requiredLvl + " or above to do that");
                                        interrupt();
                                        return;
                                    }
                                    if (owner.getInventory().remove(log) > -1) {
                                        owner.message(cutMessages);
                                        addItem(owner, itemID, amount);
                                        owner.incExp(9, experience, true);
                                    } else
                                        interrupt();
                                }
                            });
                        }
                    });
                    owner.getSender().sendMenu(option);
                    return;
                }
                owner.setMenuHandler(new MenuHandler(options) {
                    @Override
                    public void handleReply(final int option, final String reply) {
                        final int reqLvl, exp, itemID, amount;
                        String cutMessage = null;
                        switch (option) {
                        case 0:
                            itemID = cutDef.getShortbowID();
                            amount = 1;
                            reqLvl = cutDef.getShortbowLvl();
                            exp = cutDef.getShortbowExp();
                            cutMessage = "You carefully cut the wood into a shortbow";
                            break;
                        case 1:
                            itemID = cutDef.getLongbowID();
                            amount = 1;
                            reqLvl = cutDef.getLongbowLvl();
                            exp = cutDef.getLongbowExp();
                            cutMessage = "You carefully cut the wood into a longbow";
                            break;
                        default:
                            return;
                        }

                        final int requiredLvl = reqLvl;
                        final int experience = exp;
                        final String cutMessages = cutMessage;
                        player.setBatchEvent(new BatchEvent(player, 650, Formulae.getRepeatTimes(player, FLETCHING)) {
                            @Override
                            public void action() {
                                if (owner.getCurStat(9) < requiredLvl) {
                                    owner.message("You need a skill level of " + requiredLvl + " or above to do that");
                                    interrupt();
                                    return;
                                }
                                if (owner.getInventory().remove(log) > -1) {
                                    owner.message(cutMessages);
                                    addItem(owner, itemID, amount);
                                    owner.incExp(9, experience, true);
                                } else
                                    interrupt();
                            }
                        });
                    }
                });
                owner.getSender().sendMenu(options);
            }
        });
        return true;
    }
}