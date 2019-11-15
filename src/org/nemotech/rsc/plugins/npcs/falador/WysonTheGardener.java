package org.nemotech.rsc.plugins.npcs.falador;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class WysonTheGardener implements TalkToNpcListener, TalkToNpcExecutiveListener {

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 116;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p,n, "i am the gardener round here", "do you have any gardening that needs doing?");
        int option = showMenu(p,n, "I'm looking for woad leaves", "Not right now thanks");
        if(option == 0) {
            npcTalk(p,n, "well luckily for you i may have some around here somewhere");
            playerTalk(p,n, "can i buy one please?");
            npcTalk(p,n, "how much are you willing to pay?");
            int sub_option = showMenu(p,n, "How about 5 coins?", "How about 10 coins?", "How about 15 coins?", "How about 20 coins?");
            switch(sub_option) {
                case 0:
                case 1:
                    npcTalk(p, n, "no no, that's far too little. woad leaves are hard to get you know",
                            "i used to have plenty, but someone kept stealing them off me");
                    break;
                case 2:
                    npcTalk(p,n, "mmmm ok that sounds fair");
                    if(removeItem(p, 10, 15)) {
                        addItem(p, 281, 1);
                        p.message("you give wyson 15 coins");
                        p.message("wyson the gardener gives you some woad leaves");
                    } else
                        playerTalk(p,n, "i dont have enough coins to buy the leaves. i'll come back later");
                    break;
                case 3:
                    npcTalk(p,n, "that sounds more than fair");
                    if(removeItem(p, 10, 20)) {
                        p.setBusy(true);
                        addItem(p, 281, 1);
                        p.message("you give wyson 20 coins");
                        p.message("wyson the gardener gives you some woad leaves");
                        sleep(1500);
                        npcTalk(p, n, "Here, have some more, you're a generous person");
                        addItem(p, 281, 1);
                        p.message("wyson the gardener gives you some more woad leaves");
                        p.setBusy(false);
                    } else
                        playerTalk(p,n, "i dont have enough coins to buy the leaves. i'll come back later");
                    break;
                default:
                    break;
            }
        }
    }

}
