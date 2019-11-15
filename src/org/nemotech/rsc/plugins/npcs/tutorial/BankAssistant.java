package org.nemotech.rsc.plugins.npcs.tutorial;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class BankAssistant implements TalkToNpcExecutiveListener,
        TalkToNpcListener {
    /**
     * @author Davve Tutorial island bank assistant
     */

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p, n, "Hello welcome to the bank of runescape",
                "You can deposit your items in banks",
                "This allows you to own much more equipment",
                "Than can be fitted in your inventory",
                "It will also keep your items safe",
                "So you won't lose them when you die",
                "You can withdraw deposited items from any bank in the world");
        if (p.getCache().hasKey("tutorial")
                && p.getCache().getInt("tutorial") == 55) {
            playerTalk(p, n, "Can I access my bank account please?");
            npcTalk(p, n, "Certainly " + (p.isMale() ? "Sir" : "Miss"));
            p.getSender().showBank();
            p.getCache().set("tutorial", 60);
        } else {
            npcTalk(p, n, "Now proceed through the next door");
            int menu = showMenu(p, n, "Can I access my bank account please?",
                    "Okay thank you for your help");
            if (menu == 0) {
                npcTalk(p, n, "Certainly " + (p.isMale() ? "Sir" : "Miss"));
                p.getSender().showBank();
            } else if (menu == 1) {
                npcTalk(p, n, "Not a problem");
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 485;
    }

}
