package org.nemotech.rsc.plugins.quests.free;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.QuestInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class SheepShearer implements QuestInterface,TalkToNpcListener,
        TalkToNpcExecutiveListener {

    /**
     * Quest: Sheep Shearer - fully working made by Fate 2013-09-10. GIVE BALLS
     * OF WOOL ALL AT ONCE INSTEAD OF ONE BY ONE(THIS PLAYER PREFER THIS),
     * DIALOGUES, AFTER DIALOGUES - 100% Replicated.
     */

    @Override
    public int getQuestID() {
        return Plugin.SHEEP_SHEARER;
    }

    @Override
    public String getQuestName() {
        return "Sheep shearer";
    }

    class Fred {
        public static final int KILL = 0;
        public static final int LOST = 1;
    }

    private void fredDialogue(Player p, NPC n, int cID) {
        if (cID == -1) {
            switch (p.getQuestStage(this)) {
            case 0:
                npcTalk(p,
                        n,
                        "What are you doing on my land?",
                        "You're not the one who keeps leaving all my gates open?",
                        "And letting out all my sheep?");
                int choice = showMenu(p, n, new String[] {
                        "I'm looking for a quest",
                        "I'm looking for something to kill", "I'm lost" });
                if (choice == 0) {
                    npcTalk(p,
                            n,
                            "You're after a quest, you say?",
                            "Actually I could do with a bit of help",
                            "My sheep are getting mighty woolly",
                            "If you could shear them",
                            "And while your at it spin the wool for me too",
                            "Yes, that's it. Bring me 20 balls of wool",
                            "And I'm sure I could sort out some sort of payment",
                            "Of course, there's the small matter of the thing");
                    int choice1 = showMenu(p, n, new String[] {
                            "Yes okay. I can do that",
                            "That doesn't sound a very exciting quest",
                            "What do you mean, the thing?" });
                    if (choice1 == 0) {
                        npcTalk(p, n, "Ok I'll see you when you have some wool");
                        p.updateQuestStage(getQuestID(), 1);
                    } else if (choice1 == 1) {
                        npcTalk(p,
                                n,
                                "Well what do you expect if you ask a farmer for a quest?",
                                "Now are you going to help me or not?");
                        int choice2 = showMenu(p, n, new String[] {
                                "Yes okay. I can do that",
                                "No I'll give it a miss" });
                        if (choice2 == 0) {
                            npcTalk(p, n,
                                    "Ok I'll see you when you have some wool");
                            p.updateQuestStage(getQuestID(), 1);
                        }
                    } else if (choice1 == 2) {
                        npcTalk(p, n, "I wouldn't worry about it",
                                "Something ate all the previous shearers",
                                "They probably got unlucky",
                                "So are you going to help me?");
                        int choice2 = showMenu(p, n, new String[] {
                                "Yes okay. I can do that",
                                "Erm I'm a bit worried about this thing" });
                        if (choice2 == 0) {
                            npcTalk(p, n,
                                    "Ok I'll see you when you have some wool");
                            p.updateQuestStage(getQuestID(), 1);
                        } else if (choice2 == 1) {
                            npcTalk(p,
                                    n,
                                    "I'm sure it's nothing to worry about",
                                    "It's possible the other shearers aren't dead at all",
                                    "And are just hiding in the woods or something");

                            playerTalk(p, n, "I'm not convinced");
                        }
                    }
                } else if (choice == 1) {
                    fredDialogue(p, n, Fred.KILL);
                } else if (choice == 2) {
                    fredDialogue(p, n, Fred.LOST);
                }
                break;
            case 1:
                npcTalk(p, n, "How are you doing getting those balls of wool?");
                if (p.getInventory().hasItemId(207)
                        && p.getInventory().countId(207) >= 20) {
                    playerTalk(p, n, "I have some");
                    npcTalk(p, n, "Give em here then");
                    p.message("You give Fred 20 balls of wool");
                    for (int i = 0; i < 20; i++)
                        p.getInventory().remove(207, 1);
                    playerTalk(p, n, "Thats all of them");
                    npcTalk(p, n, "I guess I'd better pay you then");
                    p.message("The farmer hands you some coins");
                    p.sendQuestComplete(Plugin.SHEEP_SHEARER);
                    p.updateQuestStage(getQuestID(), -1);
                } else {
                    playerTalk(p, n, "I haven't got any at the moment");
                    npcTalk(p, n, "Ah well at least you haven't been eaten");
                }
                break;
            case -1:
                npcTalk(p, n, "What are you doing on my land?");
                int choice3 = showMenu(p, n, new String[] {
                        "I'm looking for something to kill", "I'm lost" });
                if (choice3 == 0) {
                    fredDialogue(p, n, Fred.KILL);
                } else if (choice3 == 1) {
                    fredDialogue(p, n, Fred.LOST);
                }
                break;
            }
        }
        switch (cID) {
        case Fred.KILL:
            npcTalk(p, n, "What on my land?",
                    "Leave my livestock alone you scoundrel");
            break;
        case Fred.LOST:
            npcTalk(p, n, "How can you be lost?",
                    "Just follow the road east and south",
                    "You'll end up in Lumbridge fairly quickly");
            break;
        }

    }

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        if (n.getID() == 77) {
            fredDialogue(p, n, -1);
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if (n.getID() == 77) {
            return true;
        }
        return false;
    }

    @Override
    public void handleReward(Player player) {
        player.incQuestExp(12, 180);
        player.incQuestPoints(1);
        player.message("Well done you have completed the sheep shearer quest");
        player.message("@gre@You have gained 1 quest point!");
        player.getInventory().add(new InvItem(10, 60));
    }

    @Override
    public boolean isMembers() {
        return false;
    }

}
