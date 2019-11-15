package org.nemotech.rsc.plugins.quests.free;

import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import static org.nemotech.rsc.plugins.Plugin.spawnNpc;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.QuestInterface;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.action.PlayerKilledNpcListener;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerAttackNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerKilledNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class VampireSlayer implements QuestInterface,TalkToNpcListener,
        TalkToNpcExecutiveListener, ObjectActionListener,
        ObjectActionExecutiveListener, PlayerKilledNpcExecutiveListener,
        PlayerKilledNpcListener, PlayerAttackNpcExecutiveListener {
    @Override
    public int getQuestID() {
        return Plugin.VAMPIRE_SLAYER;
    }

    @Override
    public String getQuestName() {
        return "Vampire slayer";
    }

    private void morganDialogue(Player p, NPC n) {
        switch (p.getQuestStage(this)) {
        case 0:
            npcTalk(p, n, "Please please help us, bold hero");
            playerTalk(p, n, "What's the problem?");
            npcTalk(p,
                    n,
                    "Our little village has been dreadfully ravaged by an evil vampire",
                    "There's hardly any of us left",
                    "We need someone to get rid of him once and for good");
            int choice = showMenu(p, n, new String[] {
                    "No. vampires are scary", "Ok I'm up for an adventure",
                    "I tried fighting him. He wouldn't die" });
            if (choice == 0) {
                npcTalk(p, n, "I don't blame you");
            } else if (choice == 1) {
                npcTalk(p,
                        n,
                        "I think first you should seek help",
                        "I have a friend who is a retired vampire hunter",
                        "Called Dr Hallow",
                        "He may be able to give you some tips",
                        "He can normally be found in the Jolly boar inn these days",
                        "He's a bit of an old soak",
                        "Mention his old friend Morgan",
                        "I'm sure he wouldn't want me to be killed by a vampire");
                playerTalk(p, n, "I'll look him up then");
                p.updateQuestStage(getQuestID(), 1);
            } else if (choice == 2) {
                npcTalk(p,
                        n,
                        "Maybe you're not going about it right",
                        "I think first you should seek help",
                        "I have a friend who is a retired vampire hunter",
                        "Called Dr Hallow",
                        "He may be able to give you some tips",
                        "He can normally be found in the Jolly boar inn these days",
                        "He's a bit of an old soak",
                        "Mention his old friend Morgan",
                        "I'm sure he wouldn't want me to be killed by a vampire");
                playerTalk(p, n, "I'll look him up then");
                p.updateQuestStage(getQuestID(), 1);
            }
            break;
        case 1:
        case 2:
            npcTalk(p, n, "How are you doing with your quest?");
            playerTalk(p, n, "I'm working on it still");
            npcTalk(p, n, "Please hurry", "Every day we live in fear of lives",
                    "That we will be the vampires next victim");
            break;
        case -1:
            npcTalk(p, n, "How are you doing with your quest?");
            playerTalk(p, n, "I have slain the foul creature");
            npcTalk(p, n, "Thank you, thank you",
                    "You will always be a hero in our village");
            break;
        }
    }

    private void harlowDialogue(Player p, NPC n) {
        switch (p.getQuestStage(this)) {
        case -1:
        case 1:
        case 2:
            String[] options;
            npcTalk(p, n, "Buy me a drink pleassh");
            if (!hasItem(p, 217)
                    && p.getQuestStage(Plugin.VAMPIRE_SLAYER) != -1) {
                options = new String[] { "No you've had enough", "Ok mate",
                        "Morgan needs your help" };
            } else {
                options = new String[] { "No you've had enough", "Ok mate" };
            }
            int choice = showMenu(p, n, options);
            if (choice == 0) {
            } else if (choice == 1) {
                if (p.getInventory().hasItemId(193)) {
                    p.message("You give a beer to Dr Harlow");
                    p.getInventory().remove(
                            p.getInventory().getLastIndexById(193));
                    npcTalk(p, n, "Cheersh matey");
                } else {
                    playerTalk(p, n, "I'll just go and buy one");
                }
            } else if (choice == 2) {
                npcTalk(p, n, "Morgan you shhay?");
                playerTalk(p, n,
                        "His village is being terrorised by a vampire",
                        "He wanted me to ask you how I should go about stoping it");
                npcTalk(p, n,
                        "Buy me a beer then I will teash you what you need to know");
                int choice2 = showMenu(p, n, new String[] { "Ok mate",
                        "But this is your friend Morgan we're talking about" });
                if (choice2 == 0) {
                    if (p.getInventory().hasItemId(193)) {
                        p.message("You give a beer to Dr Harlow");
                        npcTalk(p, n, "Cheersh matey");
                        p.getInventory().remove(p.getInventory().getLastIndexById(193));
                        playerTalk(p, n, "So tell me how to kill vampires then");
                        npcTalk(p, n,
                                "Yesh yesh vampires I was very good at killing em once");
                        p.message("Dr Harlow appears to sober up slightly");
                        npcTalk(p,
                                n,
                                "Well you're gonna to kill it with a stake",
                                "Otherwishe he'll just regenerate",
                                "Yes your killing blow must be done with a stake",
                                "I jusht happen to have one on me");
                        p.message("Dr Harlow hands you a stake");
                        p.getInventory().add(new InvItem(217));
                        npcTalk(p,
                                n,
                                "You'll need a hammer to hand to drive it in properly as well",
                                "One last thing",
                                "It's wise to carry garlic with you",
                                "Vampires are weakened somewhat if they can smell garlic",
                                "Dunno where you'd find that though",
                                "Remember even then a vampire is a dangeroush foe");
                        playerTalk(p, n, "Thank you very much");
                        p.updateQuestStage(getQuestID(), 2);
                    } else {
                        playerTalk(p, n, "I'll just go and buy one");

                    }
                } else if (choice2 == 1) {
                    npcTalk(p, n, "Buy ush a drink anyway");
                }
            }
            break;
        }
    }

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        if (n.getID() == 97) {
            morganDialogue(p, n);
        }
        if (n.getID() == 98) {
            harlowDialogue(p, n);
        }

    }

    @Override
    public void onObjectAction(GameObject obj, String command, Player player) {
        if (player.getQuestStage(this) == -1 && command.equals("search")
                && obj.getID() == 136 && obj.getY() == 3380) {
            player.message("There's a pillow in here");
            return;
        } else if (obj.getID() == 141 && obj.getY() == 1562) {
            if (!player.getInventory().hasItemId(218)) {
                player.message("You search the cupboard");
                player.message("You find a clove of garlic that you take");
                player.getInventory().add(new InvItem(218));
            } else {
                player.message("You search the cupboard");
                player.message("The cupboard is empty");
            }
            return;
        } else if (obj.getID() == 136 && obj.getY() == 3380) {
            for(NPC npc : player.getViewArea().getNpcsInView()) {
                if(npc.getID() == 96) {
                    player.message("There's nothing there."); // TODO - Does this work?
                    return;
                }
            }
            
            final NPC n = spawnNpc(96, 206, 3381, 1000 * 60 * 5);
            n.setShouldRespawn(false);
            player.message("A vampire jumps out off the coffin");
            return;
        }
    }
    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if (n.getID() == 97 || n.getID() == 98) {
            return true;
        }
        return false;
    }

    @Override
    public boolean blockObjectAction(GameObject obj, String command,
            Player player) {
        if (obj.getID() == 136 && obj.getY() == 3380) {
            return true;
        }
        if (obj.getID() == 141 && obj.getY() == 1562) {
            return true;
        }
        return false;
    }

    @Override
    public void handleReward(Player player) {
        player.message("Well done you have completed the vampire slayer quest");
        player.incQuestExp(0, 4825);
        player.incQuestPoints(3);
        player.message("@gre@You have gained 3 quest points!");

    }

    @Override
    public boolean blockPlayerKilledNpc(Player p, NPC n) {
        if (n.getID() == 96) {
            return true;
        }
        return false;
    }

    @Override
    public void onPlayerKilledNpc(Player p, NPC n) {
        if (n.getID() == 96) {
            if (p.getInventory().wielding(217) == true
                    && p.getInventory().hasItemId(168)) {
                p.getInventory().remove(p.getInventory().getLastIndexById(217));
                p.message("You hammer the stake in to the vampires chest!");
                n.killedBy(p);
                n.remove();
                // Completed Vampire Slayer Quest.
                if (p.getQuestStage(this) == 2) {
                    p.sendQuestComplete(Plugin.VAMPIRE_SLAYER);
                }
            } else {
                n.setHits(35);
                p.message("The vampire seems to regenerate");
            }
        }
    }

    @Override
    public boolean isMembers() {
        return false;
    }

    @Override
    public boolean blockPlayerAttackNpc(Player p, NPC n) {
        if (n.getID() == 96) {
            if (p.getInventory().hasItemId(218)) {
                p.message("The vampire appears to weaken");
            }
        }
        return false;
    }
}