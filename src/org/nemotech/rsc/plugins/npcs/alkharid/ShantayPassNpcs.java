package org.nemotech.rsc.plugins.npcs.alkharid;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.getNearestNpc;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.event.SingleEvent;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.action.PickupListener;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.PickupExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class ShantayPassNpcs implements ShopInterface,
TalkToNpcExecutiveListener, TalkToNpcListener, ObjectActionListener,
ObjectActionExecutiveListener, PickupListener, PickupExecutiveListener {

    private final Shop shop = new Shop("Shantay Pass Shop", false, 10000, 120, 70, 3,
            new InvItem(141, 15), new InvItem(342, 15), new InvItem(50, 15),
            new InvItem(1085, 15), new InvItem(1016, 15),
            new InvItem(1019, 15), new InvItem(1020, 15), new InvItem(990, 15),
            new InvItem(166, 5), new InvItem(167, 5), new InvItem(168, 5),
            new InvItem(169, 5), new InvItem(381, 200), new InvItem(1030, 20),
            new InvItem(13, 20));

    private boolean inJail = false;

    public static int ASSISTANT = 720;
    public static int SHANTAY_DISCLAIMER = 1099;
    public static int SHANTAY_STANDING_GUARD = 719;
    public static int SHANTAY_MOVING_GUARD = 717;
    public static int SHANTAY = 549;
    public static int BANK_CHEST = 942;
    public static int STONE_GATE = 916;
    public static int SHANTAY_PASS = 1030;

    @Override
    public void onTalkToNpc(final Player p, NPC n) {
        if (n.getID() == SHANTAY_STANDING_GUARD) {
            npcTalk(p, n, "Hello there!", "What can I do for you?");
            int menu = showMenu(p, n, "I'd like to go into the desert please.",
                    "Nothing thanks.");
            if (menu == 0) {
                npcTalk(p,n,"Of course!");
                if(!hasItem(p, SHANTAY_PASS)) {
                    npcTalk(p, n, "You'll need a Shantay pass to go through the gate into the desert.",
                            "See Shantay, he'll sell you one for a very reasonable price.");
                } else {
                    int menus = 0;
                    if(!hasItem(p, SHANTAY_DISCLAIMER)) {
                        message(p, "There is a large poster on the wall near the gateway. It reads..",
                                "@gre@The Desert is a VERY Dangerous place...do not enter if you are scared of dying.",
                                "@gre@Beware of high temperatures, sand storms, robbers, and slavers...",
                                "@gre@No responsibility is taken by Shantay ",
                                "@gre@If anything bad should happen to you in any circumstances whatsoever.",
                                "That seems pretty scary! Are you sure you want to go through?");
                        menus = showMenu(p,
                                "Yeah, that poster doesn't scare me!",
                                "No, I'm having serious second thoughts now.");
                    } else {
                        message(p, "A poster on the wall says exactly the same as the disclaimer.",
                                "Are you sure you want to go through?");
                        menus = showMenu(p,
                                "Yeah, I'm not scared!",
                                "No, I'm having serious second thoughts now.");
                    }
                    if(menus == 0) {
                        final NPC npc = new NPC(719, 63, 731);
                        npc.setShouldRespawn(false);
                        World.getWorld().registerNpc(npc);
                        World.getWorld().getDelayedEventHandler().add(
                                new SingleEvent(null, 60000) {
                                    public void action() {
                                        npcTalk(owner, npc, "Right, time for dinner!");
                                        npc.remove();
                                    }
                                });
                        sleep(1000);
                        npcTalk(p,npc, "Can I see your Shantay Desert Pass please.");
                        p.message("You hand over a Shantay Pass.");
                        removeItem(p, SHANTAY_PASS, 1);
                        playerTalk(p,npc, "Sure, here you go!");
                        if(!hasItem(p, SHANTAY_DISCLAIMER)) {
                            npcTalk(p, npc, "Here, have a disclaimer...",
                                    "It means that Shantay isn't responsible if you die in the desert.");
                            p.message("The guard gives you a disclaimer.");
                            addItem(p, SHANTAY_DISCLAIMER, 1);
                        }
                        p.message("you go through the gate");
                        p.teleport(62, 735);
                    } else if(menus == 1) {
                        message(p, "You decide that your visit to the desert can be postponed..");
                        p.message("Perhaps indefinitely!");
                    }

                }
            } else if (menu == 1) {
                npcTalk(p, n, "Ok then, have a nice day.");
            }
            return;
        }
        if (n.getID() == SHANTAY_MOVING_GUARD) {
            npcTalk(p, n, "Go talk to Shantay or one of his assistants.",
                    "I'm on duty and I don't have time to talk to the likes of you!");
            message(p, "The guard seems quite bad tempered,",
                    "probably from having to wear heavy armour in this intense heat.");
            return;
        }
        if (n.getID() == SHANTAY) {
            npcTalk(p,n,"Hello Effendi, I am Shantay.");
            if(!hasItem(p, SHANTAY_DISCLAIMER)) {
                npcTalk(p,n, "I see you're new!",
                        "Make sure you read the poster before going into the desert.");
            } 
            if(p.getQuestStage(Plugin.TOURIST_TRAP) == 0) {
                npcTalk(p, n, "There is a heartbroken Mother just past the gates and in the Desert.",
                        "Her name is Irena and she mourns her lost Daughter. Such a shame.");
            }
        } else if (n.getID() == ASSISTANT) {
            npcTalk(p, n, "Hello Effendi, I am a Shantay Pass Assistant.");
            if(!hasItem(p, SHANTAY_DISCLAIMER)) {
                npcTalk(p,n, "I see you're new!",
                        "Make sure you read the poster before going into the desert.");
            }
        }
        int menu = showMenu(p, n, "What is this place?",
                "Can I see what you have to sell please?", "I must be going.");
        if (menu == 0) {
            if (inJail) {
                npcTalk(p,
                        n,
                        "You should be in jail!",
                        "Well, no doubt the authorities in Port Sarim know what they're doing.",
                        "But if you get into any more trouble, you'll be stuck back in jail.");
                inJail = false;
                return;
            }
            npcTalk(p,
                    n,
                    "This is the pass of Shantay.",
                    "Mr Shantay guards this area with his men.",
                    "He is responsible for keeping this pass open and repaired.",
                    "He and his men prevent outlaws from getting out of the desert.",
                    "And he stops the inexperienced from a dry death in the sands.",
                    "Which would you say you were?");
            int menu2 = showMenu(p, n,
                    "I am definitely an outlaw, prepare to die!",
                    "I am a little inexperienced.",
                    "Er, neither, I'm an adventurer.");
            if (menu2 == 0) {
                npcTalk(p, n, "Ha, very funny.....", "Guards arrest him!");
                message(p, "The guards arrest you and place you in the jail.");
                npcTalk(p,
                        n,
                        "You'll have to stay in there until you pay the fine of five gold pieces.",
                        "Do you want to pay now?");
                inJail = true;
                int menu6 = showMenu(p, n, "Yes, Ok.",
                        "No thanks, you're not having my money.");
                if (menu6 == 0) {
                    npcTalk(p, n,
                            "Good, I see that you have come to your senses.");
                    if (p.getInventory().countId(10) >= 5) {
                        message(p, "You hand over five gold pieces to Shantay.");
                        npcTalk(p, n,
                                "Great Effendi, now please try to keep the peace.");
                        message(p,
                                "The assistant unlocks the door to the cell.");
                        removeItem(p, 10, 5);
                        inJail = false;
                    } else {
                        npcTalk(p,
                                n,
                                "You don't have that kind of cash on you I see.",
                                "But perhaps you have some in your bank?",
                                "You can transfer some money from your bank and pay the fine.",
                                "or you will be sent to a maximum security prison in Port Sarim.",
                                "Which is it going to be?");
                        int menu8 = showMenu(p, n, "I'll pay the fine.",
                                "I'm not paying the fine!");
                        if (menu8 == 0) {
                            npcTalk(p, n,
                                    "Ok then..., you'll need access to your bank.");
                            p.getSender().showBank();
                            inJail = false;
                        } else if (menu8 == 1) {
                            npcTalk(p,
                                    n,
                                    "You are to be transported to a maximum security prison in Port Sarim.",
                                    "I hope you've learnt an important lesson from this.");
                            p.teleport(281, 665, false);
                        }
                    }
                } else if (menu6 == 1) {
                    npcTalk(p,
                            n,
                            "You have a choice.",
                            "You can either pay five gold pieces or...",
                            "You can be transported to a maximum security prison in Port Sarim.",
                            "Will you pay the five gold pieces?");
                    int menu7 = showMenu(p, n, "Yes, Ok.", "No, do your worst!");
                    if (menu7 == 0) {
                        npcTalk(p, n,
                                "Good, I see that you have come to your senses.");
                        if (p.getInventory().countId(10) >= 5) {
                            message(p,
                                    "You hand over five gold pieces to Shantay.");
                            npcTalk(p, n,
                                    "Great Effendi, now please try to keep the peace.");
                            message(p,
                                    "The assistant unlocks the door to the cell.");
                            removeItem(p, 10, 5);
                            inJail = false;
                        } else {
                            npcTalk(p,
                                    n,
                                    "You don't have that kind of cash on you I see.",
                                    "But perhaps you have some in your bank?",
                                    "You can transfer some money from your bank and pay the fine.",
                                    "or you will be sent to a maximum security prison in Port Sarim.",
                                    "Which is it going to be?");
                            int menu8 = showMenu(p, n, "I'll pay the fine.",
                                    "I'm not paying the fine!");
                            if (menu8 == 0) {
                                npcTalk(p, n,
                                        "Ok then..., you'll need access to your bank.");
                                p.getSender().showBank();
                                inJail = false;
                            } else if (menu8 == 1) {
                                npcTalk(p,
                                        n,
                                        "You are to be transported to a maximum security prison in Port Sarim.",
                                        "I hope you've learnt an important lesson from this.");
                                p.teleport(281, 665, false);
                            }
                        }
                    } else if (menu7 == 1) {
                        npcTalk(p,
                                n,
                                "You are to be transported to a maximum security prison in Port Sarim.",
                                "I hope you've learnt an important lesson from this.");
                        p.teleport(281, 665, false);
                    }
                }
            } else if (menu2 == 1) {
                npcTalk(p,
                        n,
                        "Can I recommend that you purchase a full waterskin and a knife!",
                        "These items will no doubt save your life...",
                        "A waterskin will keep water from evaporating in the desert.",
                        "And a keen woodsman with a knife can extract the juice from a cactus.",
                        "Before you go into the desert, it's advisable to wear desert clothes.",
                        "It's very hot in the desert and you'll surely cook if you wear armour.",
                        "To  keep the pass open and bandit free, we charge a small toll of five gold pieces.",
                        "You can buy a desert pass from me, just ask me to open the shop.",
                        "You can also use our free banking services by clicking on the chest.");
                int menu5 = showMenu(p, n,
                        "Can I see what you have to sell please?",
                        "I must be going.");
                if (menu5 == 0) {
                    npcTalk(p, n, "Absolutely Effendi!");
                    org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
                } else if (menu5 == 1) {
                    npcTalk(p, n, " So long...");
                }
            } else if (menu2 == 2) {
                npcTalk(p,
                        n,
                        "Great, I have just the thing for the desert adventurer.",
                        "I sell desert clothes which will keep you cool in the heat of the desert.",
                        "I also sell waterskins so that you won't die in the desert.",
                        "A waterskin and a knife help you survive from the juice of a cactus.",
                        "Use the chest to store your items, we'll take them to the bank.",
                        "It's hot in the desert, you'll bake in all that armour.",
                        "To keep the pass open we ask for 5 gold pieces.",
                        "and we give you a Shantay Pass, just ask to see what I sell to buy one.");
                int menu3 = showMenu(p, n,
                        "Can I see what you have to sell please?",
                        "I must be going.",
                        "Why do I have to pay to go into the desert?");
                if (menu3 == 0) {
                    npcTalk(p, n, "Absolutely Effendi!");
                    org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
                } else if (menu3 == 1) {
                    npcTalk(p, n, "So long...");
                } else if (menu3 == 2) {
                    message(p,
                            "The Assistant opens his arms wide as if too embrace you.");
                    npcTalk(p,
                            n,
                            "Effendi, you insult me!",
                            "We are not interested in making a profit from you!",
                            "I merely seek to cover my expenses in keeping this pass open.",
                            "There is repair work to carry out and also the mens wages to consider.",
                            "For the paltry sum of 5 Gold pieces, I think we offer a great service.");
                    int menu4 = showMenu(p, n,
                            "Can I see what you have to sell please?",
                            "I must be going.");
                    if (menu4 == 0) {

                    } else if (menu4 == 1) {
                        npcTalk(p, n, " Absolutely Effendi!");
                        org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
                    }
                }
            }
        } else if (menu == 1) {
            npcTalk(p, n, "Absolutely Effendi!");
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
        } else if (menu == 2) {
            npcTalk(p, n, "So long...");
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == ASSISTANT || n.getID() == SHANTAY || n.getID() == SHANTAY_MOVING_GUARD
                || n.getID() == SHANTAY_STANDING_GUARD;
    }

    @Override
    public Shop[] getShops() {
        return new Shop[] { shop };
    }

    @Override
    public boolean isMembers() {
        return true;
    }

    @Override
    public boolean blockObjectAction(GameObject obj, String command,
            Player player) {
        if (obj.getID() == BANK_CHEST) {
            return true;
        }
        if(obj.getID() == STONE_GATE) {
            return true;
        }
        return false;
    }
    
    @Override
    public void onObjectAction(GameObject obj, String command, Player p) {
        if (obj.getID() == BANK_CHEST) {
            if(!p.getCache().hasKey("shantay-chest")) {
                message(p, "This chest is used by Shantay and his men.",
                    "They can put things in and out of storage for you.",
                    "You open the bank.");
                p.getCache().store("shantay-chest", true);
            }
            p.getSender().showBank();
        }
        if(obj.getID() == STONE_GATE) {
            if(command.equals("go through")) {
                if(p.getY() >= 735) {
                    p.message("you go through the gate");
                    p.teleport(62, 732);
                } else {
                    int menu = 0;
                    if(!hasItem(p, SHANTAY_DISCLAIMER)) {
                        message(p, "There is a large poster on the wall near the gateway. It reads..",
                                "@gre@The Desert is a VERY Dangerous place...do not enter if you are scared of dying.",
                                "@gre@Beware of high temperatures, sand storms, robbers, and slavers...",
                                "@gre@No responsibility is taken by Shantay ",
                                "@gre@If anything bad should happen to you in any circumstances whatsoever.",
                                "That seems pretty scary! Are you sure you want to go through?");
                        menu = showMenu(p,
                                "Yeah, that poster doesn't scare me!",
                                "No, I'm having serious second thoughts now.");
                    } else {
                        message(p, "A poster on the wall says exactly the same as the disclaimer.",
                                "Are you sure you want to go through?");
                        menu = showMenu(p,
                                "Yeah, I'm not scared!",
                                "No, I'm having serious second thoughts now.");
                    }
                    NPC shantayGuard = getNearestNpc(p, SHANTAY_STANDING_GUARD, 5);
                    if(menu == 0) {
                        if(!hasItem(p, SHANTAY_PASS)) {
                            message(p, "A guard stops you on your way out of the gate...");
                            if(shantayGuard != null) {
                                npcTalk(p,shantayGuard, "You need a Shantay pass to get through this gate.",
                                        "See Shantay, he will sell you one for a very reasonable price.");
                            } else {
                                p.message("Shantay guard seem to be busy at the moment.");
                            }
                        } else {
                            if(shantayGuard != null) {
                                npcTalk(p,shantayGuard, "Can I see your Shantay Desert Pass please.");
                                p.message("You hand over a Shantay Pass.");
                                removeItem(p, SHANTAY_PASS, 1);
                                playerTalk(p,shantayGuard, "Sure, here you go!");
                                if(!hasItem(p, SHANTAY_DISCLAIMER)) {
                                    npcTalk(p, shantayGuard, "Here, have a disclaimer...",
                                            "It means that Shantay isn't responsible if you die in the desert.");
                                    p.message("The guard gives you a disclaimer.");
                                    addItem(p, SHANTAY_DISCLAIMER, 1);
                                }
                                p.message("you go through the gate");
                                p.teleport(62, 735);
                            } else {
                                p.message("Shantay guard seem to be busy at the moment.");
                            }
                        }
                    } else if(menu == 1) {
                        message(p, "You decide that your visit to the desert can be postponed..");
                        p.message("Perhaps indefinitely!");
                    }
                }
            } else if(command.equals("look")) {
                message(p, "You look at the huge Stone Gate.",
                        "On the gate is a large poster, it reads.",
                        "@gre@The Desert is a VERY Dangerous place...do not enter if you are scared of dying.",
                        "@gre@Beware of high temperatures, sand storms, robbers, and slavers...",
                        "@gre@No responsibility is taken by Shantay ",
                        "@gre@If anything bad should happen to you in any circumstances whatsoever.",
                        "Despite this warning lots of people seem to pass through the gate.");
            }
        }
    }

    @Override
    public boolean blockPickup(Player p, Item i) {
        if(i.getID() == 1099) {
            return true;
        }
        return false;
    }

    @Override
    public void onPickup(Player p, Item i) {
        if(i.getID() == 1099) {
            p.message("This looks very important indeed, would you like to read it now?");
            addItem(p, 1099, 1);
            i.remove();
            int menu = showMenu(p, "Yes, I'll read it now!", "No thanks, it'll keep!");
            if(menu == 0) {
                p.getSender().sendAlert("@red@*** Shantay Disclaimer***% %@gre@The Desert is a VERY Dangerous place.% %@red@Do not enter if you're scared of dying.% %@gre@Beware of high temperatures, sand storms, and slavers% %@red@No responsibility is taken by Shantay% %@gre@If anything bad happens to you under any circumstances.", true);
            } else if(menu == 1) {
                p.message("You decide not to read the disclaimer.");
            }
        }
    }
}
