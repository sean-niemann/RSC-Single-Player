package org.nemotech.rsc.plugins.npcs.varrock;


import org.nemotech.rsc.event.SingleEvent;
import org.nemotech.rsc.event.impl.BatchEvent;
import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.player.InvItem;
import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import static org.nemotech.rsc.plugins.Plugin.ROMEO_N_JULIET;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class Apothecary implements TalkToNpcExecutiveListener,
        TalkToNpcListener {

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        if (p.getQuestStage(ROMEO_N_JULIET) == 4) {
            playerTalk(p, n, "Apothecary. Father Lawrence sent me",
                    "I need some Cadava potion to help Romeo and Juliet");
            npcTalk(p, n, "Cadava potion. Its pretty nasty. And hard to make",
                    "Wing of Rat, Tail of frog. Ear of snake and horn of dog",
                    "I have all that, but i need some cadavaberries",
                    "You will have to find them while i get the rest ready",
                    "Bring them here when you have them. But be careful. They are nasty");
            p.updateQuestStage(ROMEO_N_JULIET, 5);
            return;
        } else if (p.getQuestStage(ROMEO_N_JULIET) == 5) {
            if (!p.getInventory().hasItemId(55)) {
                npcTalk(p, n, "Keep searching for the berries",
                        "they are needed for the potion");
            } else {
                npcTalk(p, n, "Well done. You have the berries");
                message(p, "You hand over the berries");
                p.getInventory().remove(p.getInventory().getLastIndexById(55));
                p.message("Which the apothecary shakes up in vial of strange liquid");
                npcTalk(p, n, "Here is what you need");
                p.message("The apothecary gives you a Cadava potion");
                p.getInventory().add(new InvItem(57));
                p.message("I'm meant to give this to Juliet");
                p.updateQuestStage(ROMEO_N_JULIET, 6);
            }
            return;
        }
        npcTalk(p,n, "I am the apothecary", "I have potions to brew. Do you need anything specific?");
        int option = showMenu(p,n, "Can you make a strength potion?",
                "Do you know a potion to make hair fall out?",
                "Have you got any good potions to give away?");
        switch (option) {
            case 0:
                if (hasItem(p, 10, 5)
                        && hasItem(p, 220, 1)
                        && hasItem(p, 219, 1)) {
                    playerTalk(p,n,  "I have the root and spider eggs needed to make it",
                            "Well give me them and 5 gold and I'll make you your potion");
                    
                    message(p, "The apothecary starts brewing some strength potion");
                    p.setBatchEvent(new BatchEvent(p, 650, 14) {
                        @Override
                        public void action() {
                            if (owner.getInventory().countId(10) < 5) {
                                owner.message("You don't have all the ingredients");
                                interrupt();
                                return;
                            }
                            if (owner.getInventory().countId(220) < 1 || owner.getInventory().countId(219) < 1) {
                                owner.message("You don't have all the ingredients");
                                interrupt();
                                return;
                            }
                            owner.message("Apothecary gives you a strength potion.");
                            removeItem(owner, 10, 5);
                            removeItem(owner, 220, 1);
                            removeItem(owner, 219, 1);
                            addItem(owner, 221, 1);
                        }
                    });
                } else {
                    npcTalk(p, n,
                            "Yes. But the ingredients are a little hard to find",
                            "If you ever get them I will make it for you... for a cost",
                            "You'll need to find the eggs of the deadly red spider",
                            "And a limpwurt root",
                            "Oh and you'll have to pay me 5 coins");
                    playerTalk(p, n, "Ok, I'll look out for them");
                    
                }   break;
            case 1:
                npcTalk(p,n, "I do indeed. I gave it to my mother. That's why I now live alone");
                break;
            case 2:
                npcTalk(p,n, "Sorry, charity is not my strongest point");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 33;
    }
}
