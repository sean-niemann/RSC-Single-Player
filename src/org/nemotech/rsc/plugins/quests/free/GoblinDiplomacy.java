package org.nemotech.rsc.plugins.quests.free;

import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.plugins.QuestInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class GoblinDiplomacy implements QuestInterface,TalkToNpcListener,
        TalkToNpcExecutiveListener {

    @Override
    public int getQuestID() {
        return Plugin.GOBLIN_DIPLOMACY;
    }

    @Override
    public String getQuestName() {
        return "Goblin diplomacy";
    }

    public void onTalkToNpc(Player p, final NPC n) {
        final NPC otherGoblin = n.getID() == 151 ? World.getWorld().getNpc(152,
                314, 330, 441, 457) : World.getWorld().getNpc(151, 321, 445,
                326, 449);
        ;
        if (n.getID() == 151 || n.getID() == 152) {
            if (p.getQuestStage(this) == 0) {
                if (n.getID() == 151) {
                    npcTalk(p, n, "Green armour best");
                    npcTalk(p, otherGoblin, "No, no red every time");
                    npcTalk(p, n, "Go away human, we busy");
                } else {
                    npcTalk(p, n, "Red armour best");
                    npcTalk(p, otherGoblin, "No, no green every time");
                    npcTalk(p, n, "Go away human, we busy");
                }
            } else if (p.getQuestStage(this) == 1) {
                if (n.getID() == 151) {
                    npcTalk(p, n, "Green armour best");
                    npcTalk(p, otherGoblin, "No, no red every time");
                    npcTalk(p, n, "Go away human, we busy");
                } else {
                    npcTalk(p, n, "Red armour best");
                    npcTalk(p, otherGoblin, "No, no green every time");
                    npcTalk(p, n, "Go away human, we busy");
                }

                int option = showMenu(p, n, new String[] {
                        "Why are you arguing about the colour of your armour?",
                        "Wouldn't you prefer peace?",
                        "Do you want me to pick an armour colour for you" });
                switch (option) {
                case 0: // yes
                    npcTalk(p, n, "We decide to celevrate goblin new century",
                            "By changing the colour of our armour",
                            "Light blue get boring after a bit",
                            "And we cant change",
                            "Problem is we want different change to us");
                    break;
                case 1: // No
                    npcTalk(p, n,
                            "Yeah peace is good as long as it is peace wearing green armour");
                    npcTalk(p, otherGoblin, "But green to much like skin!",
                            "Nearly make you look naked!");
                    break;
                case 2:
                    playerTalk(p, n, "Different to either green or red");
                    npcTalk(p, n, "Hmm me dunno what that'd look like",
                            "You'd have to bring me some, so us could decide");
                    npcTalk(p, otherGoblin, "Yep bring us orange armour");
                    npcTalk(p, n, "Yep orange might be good");
                    p.updateQuestStage(getQuestID(), 2);
                }
            } else if (p.getQuestStage(this) == 2) {
                npcTalk(p, n, "Oh it you",
                        "Have you got some orange goblin armour yet?");
                if (p.getInventory().hasItemId(274)) {
                    playerTalk(p, n, "Yeah I have it right here");
                    message(p, "You hand Wartface the armour");
                    p.getInventory().remove(274, 1);
                    npcTalk(p, n, "No I don't like that much");
                    npcTalk(p, otherGoblin, "It clashes with my skin colour");
                    npcTalk(p, n, "Try bringing us dark blue armour");
                    p.updateQuestStage(getQuestID(), 3);
                } else {
                    playerTalk(p, n, "Err no");
                    npcTalk(p, n, "Come back when you have some");
                }
            } else if (p.getQuestStage(this) == 3) {
                npcTalk(p, n, "Oh it you",
                        "Have you got us some dark blue goblin armour?");
                if (p.getInventory().hasItemId(275)) {
                    playerTalk(p, n, "Yes, here you go");
                    p.message(
                            "You hand Wartface the armour");
                    p.getInventory().remove(275, 1);
                    npcTalk(p, n, "Doesn't seem quite right");
                    npcTalk(p, otherGoblin, "Maybe if it was a bit lighter");
                    npcTalk(p, n, "Yeah try light blue");
                    playerTalk(
                            p,
                            n,
                            "I thought that was the armour you were changing from",
                            "But never mind, anything is worth a try to avoid a war");
                    p.updateQuestStage(getQuestID(), 4);
                } else {
                    npcTalk(p, n, "Not yet");
                    npcTalk(p, n, "Come back when you have some");
                }
            } else if (p.getQuestStage(this) == 4) {
                npcTalk(p, n, "Oh it you",
                        "Have you got some light blue goblin armour yet?");
                if (p.getInventory().hasItemId(273)) {
                    playerTalk(p, n, "Sigh...", "Yes, here it is");

                    p.message(
                            "You hand Wartface the armour");
                    p.getInventory().remove(273, 1);

                    npcTalk(p, n, "That is rather nice");
                    npcTalk(p, otherGoblin,
                            "Yes i could see myself wearing somethin' like that");
                    npcTalk(p, n, "It a deal then", "Light blue it is",
                            "Thank you for sorting our argument");

                    p.sendQuestComplete(Plugin.GOBLIN_DIPLOMACY);
                } else {
                    npcTalk(p, n, "Not yet");
                    npcTalk(p, n, "Come back when you have some");
                }
            } else if (p.getQuestStage(this) == -1) { // COMPLETED
                npcTalk(p, n,
                        "Now you've solved our argument we gotta think of something else to do");
                npcTalk(p, otherGoblin, "Yep, we bored now");
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        if (n.getID() == 151 || n.getID() == 152) {
            return true;
        }
        return false;
    }

    @Override
    public void handleReward(Player player) {
        player.incQuestExp(12, player.getMaxStat(12) * 30 + 20);
        player.incQuestPoints(5);
        player.getInventory().add(new InvItem(172, 1));
        player.message(
                "@gre@You have gained 5 quest points!");
        player.message(
                "General Wartface gives you a gold bar as thanks");
    }

    @Override
    public boolean isMembers() {
        return false;
    }

}
