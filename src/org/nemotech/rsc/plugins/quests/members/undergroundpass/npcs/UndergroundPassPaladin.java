package org.nemotech.rsc.plugins.quests.members.undergroundpass.npcs;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.PlayerKilledNpcListener;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerKilledNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class UndergroundPassPaladin implements TalkToNpcListener,
TalkToNpcExecutiveListener, PlayerKilledNpcListener, PlayerKilledNpcExecutiveListener {

    public static int COAT_OF_ARMS_RED = 998;
    public static int COAT_OF_ARMS_BLUE = 999;

    public static int PALADIN_BEARD = 632;
    public static int PALADIN = 633;

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == PALADIN_BEARD;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        switch(p.getQuestStage(Plugin.UNDERGROUND_PASS)) {
        case 4:
            playerTalk(p,n, "hello paladin");
            if(!p.getCache().hasKey("paladin_food")) {
                npcTalk(p,n, "you've done well to get this far traveller, here eat");
                p.message("the paladin gives you some food");
                addItem(p, 259, 2);
                addItem(p, 138, 1);
                addItem(p, 346, 1);
                addItem(p, 475, 1);
                addItem(p, 484, 1);
                p.getCache().store("paladin_food", true);
                playerTalk(p,n, "thanks");
            }
            npcTalk(p,n, "you should leave this place now traveller",
                    "i heard the crashing of rocks further down the cavern",
                    "iban must be restless",
                    "i have no doubt that zamorak still controls these caverns",
                    "a little further on lies the great door of iban",
                    "we've tried everything, but it will not let us enter",
                    "leave now before iban awakes and it's too late");
            break;
        case 5:
        case 6:
        case 7:
        case -1:
            playerTalk(p, n, "hello");
            npcTalk(p, n, "you again, die zamorakian scum");
            n.startCombat(p);
            break;
        }
    }

    @Override
    public boolean blockPlayerKilledNpc(Player p, NPC n) {
        return n.getID() == PALADIN_BEARD || n.getID() == PALADIN;
    }

    @Override
    public void onPlayerKilledNpc(Player p, NPC n) {
        if(n.getID() == PALADIN_BEARD) {
            n.killedBy(p);
            message(p, "the paladin slumps to the floor",
                    "you search his body");
            if(!hasItem(p, COAT_OF_ARMS_RED)) {
                addItem(p, COAT_OF_ARMS_RED, 1);
                p.message("and find a paladin coat of arms");
            } else {
                p.message("but find nothing");
            }
        }
        if(n.getID() == PALADIN) {
            n.killedBy(p);
            message(p, "the paladin slumps to the floor",
                    "you search his body");
            if(!hasItem(p, COAT_OF_ARMS_BLUE, 2)) {
                addItem(p, COAT_OF_ARMS_BLUE, 1);
                p.message("and find a paladin coat of arms");
            } else {
                p.message("but find nothing");
            }
        }
    }
}
