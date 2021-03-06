package org.nemotech.rsc.plugins.npcs.falador;

import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class Barmaid implements TalkToNpcExecutiveListener, TalkToNpcListener {
    
    private final String notEnoughMoney = "Oh dear. I don't seem to have enough money";

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        if (!p.getCache().hasKey("barcrawl")
                && !p.getCache().hasKey("barthree")) {
            playerTalk(p, n, "Hi, what ales are you serving?");
            npcTalk(p,
                    n,
                    "Well you can either have a nice Asagarnian Ale, or a Wizards Mind Bomb",
                    "Or a Dwarven Stout");

            String[] options = new String[] { "One Asgarnian Ale please",
                    "I'll try the mind bomb", "Can I have a Dwarven Stout?",
                    "I don't feel like any of those" };
            int option = showMenu(p, n, options);
            switch (option) {
            case 0:
                npcTalk(p, n, "That'll be two gold");

                if (p.getInventory().remove(10, 2) > -1) {
                    p.message("You buy a pint of Asgarnian Ale");
                    p.getInventory().add(new InvItem(267, 1));

                } else {
                    playerTalk(p, n, notEnoughMoney);
                }
                break;
            case 1:
                npcTalk(p, n, "That'll be two gold");

                if (p.getInventory().remove(10, 2) > -1) {
                    p.message("You buy a pint of Wizard's Mind Bomb");
                    p.getInventory().add(new InvItem(268, 1));

                } else {
                    playerTalk(p, n, notEnoughMoney);
                }

                break;
            case 2:
                npcTalk(p, n, "That'll be three gold");

                if (p.getInventory().remove(10, 3) > -1) {
                    p.message("You buy a pint of Dwarven Stout");
                    p.getInventory().add(new InvItem(269, 1));

                } else {
                    playerTalk(p, n, notEnoughMoney);
                }
                break;
            }
        } else {
            int barCrawlOpt = showMenu(p, n, new String[] {
                    "Hi what ales are you serving",
                    "I'm doing Alfred Grimhand's barcrawl" });
            if (barCrawlOpt == 0) {
                npcTalk(p,
                        n,
                        "Well you can either have a nice Asagarnian Ale, or a Wizards Mind Bomb",
                        "Or a Dwarven Stout");

                String[] options = new String[] { "One Asgarnian Ale please",
                        "I'll try the mind bomb",
                        "Can I have a Dwarven Stout?",
                        "I don't feel like any of those" };

                int option = showMenu(p, n, options);
                switch (option) {
                case 0:
                    npcTalk(p, n, "That'll be two gold");

                    if (p.getInventory().remove(10, 2) > -1) {
                        p.message("You buy a pint of Asgarnian Ale");
                        p.getInventory().add(new InvItem(267, 1));

                    } else {
                        playerTalk(p, n, notEnoughMoney);
                    }
                    break;
                case 1:
                    npcTalk(p, n, "That'll be two gold");

                    if (p.getInventory().remove(10, 2) > -1) {
                        p.message("You buy a pint of Wizard's Mind Bomb");
                        p.getInventory().add(new InvItem(268, 1));

                    } else {
                        playerTalk(p, n, notEnoughMoney);
                    }
                    break;
                case 2:
                    npcTalk(p, n, "That'll be three gold");

                    if (p.getInventory().remove(10, 3) > -1) {
                        p.message("You buy a pint of Dwarven Stout");
                        p.getInventory().add(new InvItem(269, 1));

                    } else {
                        playerTalk(p, n, notEnoughMoney);
                    }
                    break;
                case 3:
                    break;
                }
            } else if (barCrawlOpt == 1) {
                npcTalk(p,
                        n,
                        "Hehe this'll be fun",
                        "You'll be after our off the menu hand of death cocktail then",
                        "Lots of expensive parts to the cocktail though",
                        "So it will cost you 70 coins");
                if (hasItem(p, 10, 70)) {
                    message(p, "You buy a hand of death cocktail");
                    p.getInventory().remove(10, 70);
                    message(p, "You drink the cocktail",
                            "You stumble around the room",
                            "The barmaid giggles",
                            "The barmaid signs your card");
                    p.getCache().store("barthree", true);
                } else {
                    playerTalk(p, n, "I don't have 70 coins right now");
                }
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 142;
    }

}
