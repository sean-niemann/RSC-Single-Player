package org.nemotech.rsc.plugins.npcs.tutorial;

import org.nemotech.rsc.client.mudclient;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Boatman implements TalkToNpcExecutiveListener, TalkToNpcListener {
    /**
     * @author Davve
     * Tutorial island boat man - last npc before main land (Lumbridge)
     */
    @Override
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p, n, "Hello my job is to take you to the main game area",
                "It's only a short row",
                "I shall take you to the small town of Lumbridge",
                "In the kingdom of Misthalin");
        int menu = showMenu(p, n, "Ok I'm ready to go", "I'm not done here yet");
        if(menu == 0) {
                        if (!p.getInventory().hasItemId(70)) { // bronze long sword
                p.getInventory().add(new InvItem(70, 1));
            }
            if (!p.getInventory().hasItemId(4)) { // wooden shield
                p.getInventory().add(new InvItem(4, 1));
            }
            if (!p.getInventory().hasItemId(376)) { // net
                p.getInventory().add(new InvItem(376, 1));
            }
            if (!p.getInventory().hasItemId(156)) { // bronze pickaxe
                p.getInventory().add(new InvItem(156, 1));
            }
            if (!p.getInventory().hasItemId(33)) { // air runes
                p.getInventory().add(new InvItem(33, 12));
            }
            if (!p.getInventory().hasItemId(35)) { // mind runes
                p.getInventory().add(new InvItem(35, 8));
            }
            if (!p.getInventory().hasItemId(32)) { // water runes
                p.getInventory().add(new InvItem(32, 3));
            }
            if (!p.getInventory().hasItemId(34)) { // earth runes
                p.getInventory().add(new InvItem(34, 2));
            }
            if (!p.getInventory().hasItemId(36)) { // body runes
                p.getInventory().add(new InvItem(36, 1));
            }
            if (!p.getInventory().hasItemId(1263)) { // sleeping bag
                p.getInventory().add(new InvItem(1263, 1));
            }
            npcTalk(p, n, "Lets go then");
            p.message("You have completed the tutorial");
            p.teleport(120, 648, false);
            if(p.getCache().hasKey("tutorial")) {
                p.getCache().remove("tutorial");
            }
            sleep(2000);
            p.message("The boat arrives in Lumbridge");
            //mudclient.getInstance().getMusicPlayer().startNewPlayerSong();
            //mudclient.getInstance().musicStopped = false;
            //p.getSender().sendAlert("Music % % You can toggle music in the wrench menu", false);
        } else if(menu == 1) {
            // MISSING DIALOGUE.
            npcTalk(p, n, "Take your time"); // this is a guess
        }
        
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 497;
    }

}
