package org.nemotech.rsc.plugins.npcs.alkharid;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class SilkTrader implements TalkToNpcListener,
        TalkToNpcExecutiveListener {

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 71;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p, n, "Do you want to buy any fine silks?");
        int option = showMenu(p, n, "How much are they?",
                "No. Silk doesn't suit me");
        if (option == 0) {
            npcTalk(p, n, "3 Coins");
            int sub_opt = showMenu(p, n, "No. That's too much for me",
                    "OK, that sounds good");
            if (sub_opt == 0) {
            } else if (sub_opt == 1) {
                if (p.getInventory().remove(10, 3) > -1) {
                    addItem(p, 200, 1);
                    p.message("You buy some silk for 3 coins");
                } else {
                    playerTalk(p, n, "Oh dear. I don't have enough money");
                }
            }
        }
    }
}
