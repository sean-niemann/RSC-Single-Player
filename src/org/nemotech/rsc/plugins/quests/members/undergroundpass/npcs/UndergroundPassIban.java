package org.nemotech.rsc.plugins.quests.members.undergroundpass.npcs;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.getNearestNpc;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnObjectListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnObjectExecutiveListener;

public class UndergroundPassIban implements InvUseOnObjectListener, InvUseOnObjectExecutiveListener {

    public static int PIT_OF_THE_DAMNED = 913;
    public static int IBAN = 649;

    @Override
    public boolean blockInvUseOnObject(GameObject obj, InvItem item, Player p) {
        if(obj.getID() == PIT_OF_THE_DAMNED) {
            return true;
        }
        return false;
    }

    @Override
    public void onInvUseOnObject(GameObject obj, InvItem item, Player p) {
        if(obj.getID() == PIT_OF_THE_DAMNED) {
            if(p.getCache().hasKey("poison_on_doll") 
                    && p.getCache().hasKey("cons_on_doll") 
                    && p.getCache().hasKey("ash_on_doll") 
                    && p.getCache().hasKey("shadow_on_doll")) {
                NPC iban = getNearestNpc(p, IBAN, 10);
                message(p, "you throw the doll of iban into the pit");
                if(iban != null) {
                    p.setAttribute("iban_bubble_show", true);
                    npcTalk(p,iban, "what's happening?, it's dark here...so dark",
                            "im falling into the dark, what have you done?");
                    message(p, "iban falls to his knees clutching his throat");
                    npcTalk(p,iban, " noooooooo!");
                    message(p, "iban slumps motionless to the floor",
                            "a roar comes from the pit of the damned",
                            "the infamous iban has finally gone to rest");
                    p.message("amongst ibans remains you find his staff..");
                    message(p, "...and some runes");
                    p.message("suddenly around you rocks crash to the floor..");
                    message(p, "...as the ground begins to shake",
                            "the temple walls begin to collapse in",
                            "and you're thrown from the temple platform");
                    addItem(p, 1000, 1);
                    addItem(p, 38, 15);
                    addItem(p, 31, 30);
                    p.teleport(687, 3485);
                    /* end the show! */
                }
            } else {
                p.message("the doll is still incomplete");
            }
        }
    }
}
