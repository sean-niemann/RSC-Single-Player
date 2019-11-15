package org.nemotech.rsc.plugins.npcs.varrock;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import org.nemotech.rsc.plugins.Plugin;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Guildmaster extends Plugin implements TalkToNpcListener, TalkToNpcExecutiveListener {

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getDef().getName().equals("Guildmaster");
    }

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        /*npcTalk(p, n, "Would you like to buy or sell some staffs?");
        int option = showMenu(p,n,"Yes please", "No, thank you");
        if (option == 0) {
            p.setAccessingShop(shop);
            ActionSender.showShop(p, shop);
        }*/
        int option = showMenu(p, n, "What is this place?", "Do you know where I could get a rune plate mail body?");
        if(option == 0) {
            npcTalk(p,
                n,
                " This is the champions' guild",
                " Only Adventurers who have proved themselves worthy",
                " by gaining influence from quests are allowed in here",
                " As the number of quests in the world rises",
                " So will the requirements to get in here",
                " But so will the rewards");
        } else if(option == 1) {
            npcTalk(p,
                    n,
                    "I have a friend called Oziach who lives by the cliffs",
                    "He has a supply of rune plate mail",
                    "He may sell you some if you're lucky, he can be little strange sometimes though");
            if (p.getQuestStage(Plugin.DRAGON_SLAYER) == 0) {
                p.updateQuestStage(DRAGON_SLAYER, 1);
            }
        }
    }

}