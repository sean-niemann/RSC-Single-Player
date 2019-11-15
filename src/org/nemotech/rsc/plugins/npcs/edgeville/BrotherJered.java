package org.nemotech.rsc.plugins.npcs.edgeville;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class BrotherJered implements TalkToNpcExecutiveListener,
TalkToNpcListener {

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p, n,
                "Hello friend, would you like me to bless your amulet of saradomin?");
        int option = showMenu(p, n, "Yes please", "No thanks");
        if (option == 0) {
            if (removeItem(p, 45, 1)) {
                message(p, "He quickly takes your amulet",
                        "He hands you back a blessed amulet of saradomin");
                addItem(p, 385, 1);
            } else {
                npcTalk(p, n, "Oh dear looks like you don't have any amulet to bless");
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 176;
    }

}
