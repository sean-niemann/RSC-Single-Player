package org.nemotech.rsc.plugins.npcs.alkharid;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public final class KebabSeller implements TalkToNpcListener,
        TalkToNpcExecutiveListener {

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        npcTalk(p, n, "Would you like to buy a nice kebab? Only 1 gold");
        int o = showMenu(p, n, "I think I'll give it a miss", "Yes please");
        if (o == 1) {
            if (removeItem(p, 10, 1)) {
                p.message("You buy a kebab");
                addItem(p, 210, 1);
            } else {
                playerTalk(p, n, "Oops I forgot to bring any money with me");
                npcTalk(p, n, "Come back when you have some");
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 90;
    }

}
