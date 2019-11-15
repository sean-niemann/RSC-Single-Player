package org.nemotech.rsc.plugins.npcs;

import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.external.definition.extra.CerterDef;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.event.ShortEvent;
import org.nemotech.rsc.model.MenuHandler;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.ChatMessage;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Certer extends Plugin implements TalkToNpcListener, TalkToNpcExecutiveListener {

    static int[] CERTERS = {
        225, 226, 227, 466, 467, 299, 778, 341, 369,
        370, 794, 348, 267, 795, 543, 231, 333
    };
    
    @Override
    public boolean blockTalkToNpc(Player player, NPC npc) {
        return inArray(CERTERS, npc.getID());
    }
    
    @Override
    public void onTalkToNpc(Player player, final NPC npc) {
        final CerterDef certerDef = EntityManager.getCerterDef(npc.getID());
        if (certerDef == null) {
            return;
        }
        final String[] names = certerDef.getCertNames();
        player.informOfNPCMessage(new ChatMessage(npc, "Welcome to my " + certerDef.getType() + " exchange stall", player));
        player.setBusy(true);
        World.getWorld().getDelayedEventHandler().add(new ShortEvent(player) {
            public void action() {
            owner.setBusy(false);
            String[] options = new String[] { "I have some certificates to trade in", "I have some " + certerDef.getType() + " to trade in" };
            owner.setMenuHandler(new MenuHandler(options) {
                public void handleReply(final int option, final String reply) {
                if (owner.isBusy() || option < 0) {
                    return;
                }
                owner.informOfChatMessage(new ChatMessage(owner, reply, npc));
                owner.setBusy(true);
                world.getDelayedEventHandler().add(new ShortEvent(owner) {
                    public void action() {
                    owner.setBusy(false);
                    switch (option) {
                    case 0:
                        owner.getSender().sendMessage("What sort of certificate do you wish to trade in?");
                        owner.setMenuHandler(new MenuHandler(names) {
                        public void handleReply(final int index, String reply) {
                            owner.getSender().sendMessage("How many certificates do you wish to trade in?");
                            String[] options = new String[] { "One", "Two", "Three", "Four", "Five", "All to bank" };
                            owner.setMenuHandler(new MenuHandler(options) {
                            public void handleReply(int certAmount, String reply) {
                                owner.resetPath();
                                int certID = certerDef.getCertID(index);
                                if (certID < 0) {
                                    return;
                                }
                                int itemID = certerDef.getItemID(index);
                                if (certAmount == 5) {
                                certAmount = owner.getInventory().countId(certID);
                                if (certAmount <= 0) {
                                    owner.getSender().sendMessage("You don't have any " + names[index] + " certificates");
                                    return;
                                }
                                InvItem bankItem = new InvItem(itemID, certAmount * 5);
                                if (owner.getInventory().remove(new InvItem(certID, certAmount)) > -1) {
                                    owner.getSender().sendMessage("You exchange the certificates, " + bankItem.getAmount() + " " + bankItem.getDef().getName() + " is added to your bank");
                                    owner.getBank().add(bankItem);
                                }
                                } else {
                                certAmount += 1;
                                int itemAmount = certAmount * 5;
                                if (owner.getInventory().countId(certID) < certAmount) {
                                    owner.getSender().sendMessage("You don't have that many certificates");
                                    return;
                                }
                                if (owner.getInventory().remove(certID, certAmount) > -1) {
                                    owner.getSender().sendMessage("You exchange the certificates for " + certerDef.getType());
                                    for (int x = 0; x < itemAmount; x++) {
                                    owner.getInventory().add(new InvItem(itemID, 1));
                                    }
                                }
                                }
                                owner.getSender().sendInventory();
                            }
                            });
                            owner.getSender().sendMenu(options);
                        }
                        });
                        owner.getSender().sendMenu(names);
                        break;
                    case 1:
                        owner.getSender().sendMessage("What sort of " + certerDef.getType() + " do you wish to trade in?");
                        owner.setMenuHandler(new MenuHandler(names) {
                        public void handleReply(final int index, String reply) {
                            owner.getSender().sendMessage("How many " + certerDef.getType() + " do you wish to trade in?");
                            String[] options = new String[] { "Five", "Ten", "Fifteen", "Twenty", "Twentyfive", "All from bank" };
                            owner.setMenuHandler(new MenuHandler(options) {
                            public void handleReply(int certAmount, String reply) {
                                owner.resetPath();
                                int certID = certerDef.getCertID(index);
                                if (certID < 0) {
                                return;
                                }
                                int itemID = certerDef.getItemID(index);
                                if (certAmount == 5) {
                                certAmount = (int) (owner.getBank().countId(itemID) / 5);
                                int itemAmount = certAmount * 5;
                                if (itemAmount <= 0) {
                                    owner.getSender().sendMessage("You don't have any " + names[index] + " to cert");
                                    return;
                                }
                                if (owner.getBank().remove(itemID, itemAmount) > -1) {
                                    owner.getSender().sendMessage("You exchange the " + certerDef.getType() + ", " + itemAmount + " " + EntityManager.getItem(itemID).getName() + " is taken from your bank");
                                    owner.getInventory().add(new InvItem(certID, certAmount));
                                }
                                } else {
                                certAmount += 1;
                                int itemAmount = certAmount * 5;
                                if (owner.getInventory().countId(itemID) < itemAmount) {
                                    owner.getSender().sendMessage("You don't have that many " + certerDef.getType());
                                    return;
                                }
                                owner.getSender().sendMessage("You exchange the " + certerDef.getType() + " for certificates");
                                for (int x = 0; x < itemAmount; x++) {
                                    owner.getInventory().remove(itemID, 1);
                                }
                                owner.getInventory().add(new InvItem(certID, certAmount));
                                }
                                owner.getSender().sendInventory();
                            }
                            });
                            owner.getSender().sendMenu(options);
                        }
                        });
                        owner.getSender().sendMenu(names);
                        break;
                    }
                    npc.unblock();
                    }
                });
                }
            });
            owner.getSender().sendMenu(options);
            }
        });
        npc.blockedBy(player);
    }
    
    /*@Override
    public void onTalkToNpc(Player player, Npc npc) {
        if(Util.inArray(CERTERS, npc.getID())) {
            occupy(player, npc);
            CerterDef certerDef = EntityHandler.getCerterDef(npc.getID());
            if (certerDef == null) {
                release(player, npc);
                return;
            }
            String[] names = certerDef.getCertNames();
            sendChat(npc, player, "Welcome to my " + certerDef.getType().toLowerCase() + " exchange stall");
            int option = pickOption(player, npc, "I have some certificates to trade in", "I have some " + certerDef.getType().toLowerCase() + " to trade in");
            if(option == 0) {
                player.getSender().sendMessage("What sort of certificate do you wish to trade in?");
                int certNameOption = pickOption(player, npc, false, names);
                player.getSender().sendMessage("How many certificates do you wish to trade in?");
                int certAmountOption = pickOption(player, npc, false, "One", "Two", "Three", "Four", "Five", "All to bank");
                int certID = certerDef.getCertID(certNameOption);
                if(certID < 0) {
                    release(player, npc);
                    return;
                }
                int itemID = certerDef.getItemID(certNameOption);
                if(certAmountOption == 5) {
                    int certAmount = player.getInventory().countId(certID);
                    if(certAmount <= 0) {
                        player.getSender().sendMessage("You don't have any " + names[certNameOption] + " certificates");
                        release(player, npc);
                        return;
                    }
                    InvItem bankItem = new InvItem(itemID, certAmount * 5);
                    if (player.getInventory().remove(new InvItem(certID, certAmount)) > -1) {
                        player.getSender().sendMessage("You exchange the certificates, " + bankItem.getAmount() + " " + bankItem.getDef().getName() + " is added to your bank");
                        player.getBank().add(bankItem);
                    }
                } else {
                    int certAmount = certAmountOption;
                    certAmount += 1;
                    int itemAmount = certAmount * 5;
                    if (player.getInventory().countId(certID) < certAmount) {
                        player.getSender().sendMessage("You don't have that many certificates");
                        release(player, npc);
                        return;
                    }
                    if(hasItem(player, certID, certAmount)) {
                        removeItem(player, certID, certAmount);
                        addItem(player, itemID, itemAmount);
                        player.getSender().sendMessage("You exchange the certificates for " + certerDef.getType());
                    }
                }
            } else if(option == 1) {
                player.getSender().sendMessage("What sort of " + certerDef.getType().toLowerCase() + " do you wish to trade in?");
                int certNameOption = pickOption(player, npc, false, names);
                player.getSender().sendMessage("How many " + certerDef.getType().toLowerCase() + " do you wish to trade in?");
                int certAmountOption = pickOption(player, npc, false, "One", "Two", "Three", "Four", "Five", "All to bank");
                int certID = certerDef.getCertID(certNameOption);
                if(certID < 0) {
                    release(player, npc);
                    return;
                }
                int itemID = certerDef.getItemID(certNameOption);
                if(certAmountOption == 5) {
                    int certAmount = (int) (player.getBank().countId(itemID) / 5);
                    int itemAmount = certAmount * 5;
                    if(itemAmount <= 0) {
                        player.getSender().sendMessage("You don't have any " + names[certNameOption] + " to cert");
                        release(player, npc);
                        return;
                    }
                    if (player.getBank().remove(itemID, itemAmount) > -1) {
                        player.getSender().sendMessage("You exchange the " + certerDef.getType() + ", " + itemAmount + " " + EntityHandler.getItemDef(itemID).getName() + " is taken from your bank");
                        addItem(player, certID, certAmount);
                    }
                } else {
                    int certAmount = certAmountOption;
                    certAmount += 1;
                    int itemAmount = certAmount * 5;
                    if(player.getInventory().countId(itemID) < itemAmount) {
                        player.getSender().sendMessage("You don't have that many " + certerDef.getType());
                        release(player, npc);
                        return;
                    }
                    player.getSender().sendMessage("You exchange the " + certerDef.getType() + " for certificates");
                    player.getInventory().remove(itemID, itemAmount);
                    addItem(player, certID, certAmount);
                }
            }  
            release(player, npc);
        }
    }*/
    
}