package org.nemotech.rsc.plugins.quests.members.legendsquest.npcs;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.PlayerAttackNpcListener;
import org.nemotech.rsc.plugins.listeners.action.PlayerKilledNpcListener;
import org.nemotech.rsc.plugins.listeners.action.PlayerMageNpcListener;
//import org.nemotech.rsc.plugins.listeners.action.PlayerNpcRunListener;
import org.nemotech.rsc.plugins.listeners.action.PlayerRangeNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerAttackNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerKilledNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerMageNpcExecutiveListener;
//import org.nemotech.rsc.plugins.listeners.executive.PlayerNpcRunExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.PlayerRangeNpcExecutiveListener;
import org.nemotech.rsc.plugins.quests.members.legendsquest.mechanism.LegendsQuestInvAction;

// TODO NO RUN LISTENER!!!!
public class LegendsQuestIrvigSenay implements PlayerAttackNpcListener, PlayerAttackNpcExecutiveListener, PlayerKilledNpcListener, PlayerKilledNpcExecutiveListener, PlayerMageNpcListener, PlayerMageNpcExecutiveListener, PlayerRangeNpcListener, PlayerRangeNpcExecutiveListener
    {///,PlayerNpcRunListener, PlayerNpcRunExecutiveListener {

    public static final int IRVIG_SENAY = 761;
    public final static int A_LUMP_OF_CRYSTAL = 1220;

    @Override
    public boolean blockPlayerAttackNpc(Player p, NPC n) {
        if(n.getID() == IRVIG_SENAY && !hasItem(p, A_LUMP_OF_CRYSTAL) && !p.getCache().hasKey("cavernous_opening")) {
            return true;
        }
        return false;
    }

    @Override
    public void onPlayerAttackNpc(Player p, NPC n) {
        if(n.getID() == IRVIG_SENAY && !hasItem(p, A_LUMP_OF_CRYSTAL) && !p.getCache().hasKey("cavernous_opening")) {
            attackMessage(p, n);
        }
    }

    private void attackMessage(Player p, NPC n) {
        if(n.getID() == IRVIG_SENAY && !hasItem(p, A_LUMP_OF_CRYSTAL) && !p.getCache().hasKey("cavernous_opening")) {
            npcTalk(p, n, "Greetings Brave warrior, destiny is upon you...");
            n.setChasing(p);
            npcTalk(p, n, "Ready your weapon and defend yourself.");
        }
    }

    @Override
    public boolean blockPlayerKilledNpc(Player p, NPC n) {
        if(n.getID() == IRVIG_SENAY && !p.getCache().hasKey("cavernous_opening")) {
            return true;
        }
        if(n.getID() == IRVIG_SENAY && p.getQuestStage(Plugin.LEGENDS_QUEST) == 8 && p.getCache().hasKey("viyeldi_companions")) {
            return true;
        }
        return false;
    }

    @Override
    public void onPlayerKilledNpc(Player p, NPC n) {
        if(n.getID() == IRVIG_SENAY && p.getQuestStage(Plugin.LEGENDS_QUEST) == 8 && p.getCache().hasKey("viyeldi_companions")) {
            n.remove();
            if(p.getCache().hasKey("viyeldi_companions") && p.getCache().getInt("viyeldi_companions") == 2) {
                p.getCache().set("viyeldi_companions", 3);
            }
            message(p, 1300, "A nerve tingling scream echoes around you as you slay the dead Hero.",
                    "@yel@Irvig Senay: Ahhhggggh",
                    "@yel@Irvig Senay: Forever must I live in this torment till this beast is slain...");
            sleep(650);
            LegendsQuestNezikchened.demonFight(p);
        }
        if(n.getID() == IRVIG_SENAY && !p.getCache().hasKey("cavernous_opening")) {
            if(hasItem(p, A_LUMP_OF_CRYSTAL) || hasItem(p, LegendsQuestInvAction.A_RED_CRYSTAL) || hasItem(p, LegendsQuestInvAction.A_RED_CRYSTAL + 9)) {
                npcTalk(p, n, "A fearsome foe you are, and bettered me once have you done already.");
                p.message("Your opponent is retreating");
                n.remove();
            } else {
                npcTalk(p, n, "You have proved yourself of the honour..");
                //p.resetCombatEvent();
                //n.resetCombatEvent();
                p.message("Your opponent is retreating");
                npcTalk(p, n, "");
                n.remove();
                message(p, 1300, "A piece of crystal forms in midair and falls to the floor.",
                        "You place the crystal in your inventory.");
                addItem(p, A_LUMP_OF_CRYSTAL, 1);
            }
        }
    }

    @Override
    public boolean blockPlayerMageNpc(Player p, NPC n, Integer spell) {
        if(n.getID() == IRVIG_SENAY && !hasItem(p, A_LUMP_OF_CRYSTAL) && !p.getCache().hasKey("cavernous_opening")) {
            return true;
        }
        return false;
    }

    @Override
    public void onPlayerMageNpc(Player p, NPC n, Integer spell) {
        if(n.getID() == IRVIG_SENAY && !hasItem(p, A_LUMP_OF_CRYSTAL) && !p.getCache().hasKey("cavernous_opening")) {
            attackMessage(p, n);
        }
    }

    @Override
    public boolean blockPlayerRangeNpc(Player p, NPC n) {
        if(n.getID() == IRVIG_SENAY && !hasItem(p, A_LUMP_OF_CRYSTAL) && !p.getCache().hasKey("cavernous_opening")) {
            return true;
        }
        return false;
    }

    @Override
    public void onPlayerRangeNpc(Player p, NPC n) {
        if(n.getID() == IRVIG_SENAY && !hasItem(p, A_LUMP_OF_CRYSTAL) && !p.getCache().hasKey("cavernous_opening")) {
            attackMessage(p, n);
        }
    }

    /*@Override
    public boolean blockPlayerNpcRun(Player p, Npc n) {
        if(n.getID() == IRVIG_SENAY && p.getQuestStage(Constants.Quests.LEGENDS_QUEST) == 8 && p.getCache().hasKey("viyeldi_companions")) {
            return true;
        }
        return false;
    }

    @Override
    public void onPlayerNpcRun(Player p, Npc n) {
        if(n.getID() == IRVIG_SENAY && p.getQuestStage(Constants.Quests.LEGENDS_QUEST) == 8 && p.getCache().hasKey("viyeldi_companions")) {
            n.remove();
            message(p, 1300, "As you try to make your escape,",
                    "the Viyeldi fighter is recalled by the demon...",
                    "@yel@Nezikchened : Ha, ha ha!",
                    "@yel@Nezikchened : Run then fetid worm...and never touch my totem again...");
        }
        
    }*/
}

