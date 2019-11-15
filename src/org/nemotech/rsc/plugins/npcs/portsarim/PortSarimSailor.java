package org.nemotech.rsc.plugins.npcs.portsarim;

import static org.nemotech.rsc.plugins.Plugin.getNearestNpc;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;

import org.nemotech.rsc.model.Point;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import static org.nemotech.rsc.plugins.Plugin.DRAGON_SLAYER;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;
import org.nemotech.rsc.plugins.menu.Menu;
import org.nemotech.rsc.plugins.menu.Option;

public final class PortSarimSailor implements ObjectActionExecutiveListener, ObjectActionListener, TalkToNpcExecutiveListener,
        TalkToNpcListener {

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        npcTalk(p, n, "Do you want to go on a trip to Karamja?");
        Menu defaultMenu = new Menu();
        if (p.getQuestStage(DRAGON_SLAYER) == 2) {
            defaultMenu.addOption(new Option("I'd rather go to Crandor Isle") {
                @Override
                public void action() {
                    npcTalk(p, n, "No I need to stay alive");
                    npcTalk(p, n, "I have a wife and family to support");
                }
            });
        }
        defaultMenu.addOption(new Option("Yes please") {
            @Override
            public void action() {
                if (p.getInventory().remove(10, 30) > -1) {
                    message(p, "You pay 30 gold", "You board the ship");
                    p.teleport(324, 713, false);
                    message(p, "The ship arrives at Karamja");
                } else {
                    playerTalk(p, n,
                            "Oh dear I don't seem to have enough money");
                }
            }
        });
        defaultMenu.addOption(new Option("No thankyou") {
            @Override
            public void action() {
                npcTalk(p, n, "No I need to stay alive");
                npcTalk(p, n, "I have a wife and family to support");
            }
        });
        defaultMenu.showMenu(p, n);
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 166 || n.getID() == 171 || n.getID() == 170;
    }

    @Override
    public void onObjectAction(GameObject arg0, String arg1, Player p) {
        NPC sailor = getNearestNpc(p, 166, 10);
        if(sailor != null) {
            sailor.initializeTalkScript(p);
        }
    }
 
    @Override
    public boolean blockObjectAction(GameObject arg0, String arg1, Player arg2) {
        return (arg0.getID() == 155 && arg0.getLocation().equals(Point.getLocation(265, 645)))
                || (arg0.getID() == 156 && arg0.getLocation().equals(Point.getLocation(265, 650)))
                || (arg0.getID() == 157 && arg0.getLocation().equals(Point.getLocation(265, 652)));
    }
}
