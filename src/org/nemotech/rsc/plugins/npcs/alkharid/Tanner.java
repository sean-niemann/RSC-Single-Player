package org.nemotech.rsc.plugins.npcs.alkharid;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Tanner implements TalkToNpcListener, TalkToNpcExecutiveListener {
    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        npcTalk(p, n, "Greeting friend i'm a manufacturer of leather");
        int option = showMenu(p, n, "Can I buy some leather then?",
                "Here's some cow hides, can I buy some leather now?",
                "Leather is rather weak stuff");

        switch (option) {
        case 0:
            npcTalk(p,n, "I make leather from cow hides", "Bring me some of them and a gold coin per hide");
            break;
        case 1:
            npcTalk(p,n, "Ok");
            while(true) {
                sleep(500);
                if(p.getInventory().countId(147) < 1) {
                    p.message("You have run out of cow hides");
                    break;
                } else if (p.getInventory().countId(10) < 1) {
                    p.message("You have run out of coins");
                    break;
                } else if (p.getInventory().remove(new InvItem(147)) > -1 && p.getInventory().remove(10, 1) > -1) {
                    addItem(p, 148, 1);
                } else {
                    break;
                }
            }
            break;
        case 2:
            npcTalk(p,n, "Well yes if all you're concerned with is how much it will protect you in a fight");
            break;
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 172;
    }

}
