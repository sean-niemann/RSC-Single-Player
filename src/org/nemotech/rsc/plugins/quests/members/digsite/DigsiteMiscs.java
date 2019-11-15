package org.nemotech.rsc.plugins.quests.members.digsite;

import static org.nemotech.rsc.plugins.Plugin.*;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.DropListener;
import org.nemotech.rsc.plugins.listeners.executive.DropExecutiveListener;

public class DigsiteMiscs implements DropListener, DropExecutiveListener {
    
    public static int UNINDENTIFIED_LIQUID = 1232;
    public static int MIXED_CHEMICALS = 1178;
    public static int MIXED_CHEMICALS2 = 1180;
    public static int NITROGLYCERIN = 1161;
    public static int EXPLOSIVE_COMPOUND = 1176;

    @Override
    public boolean blockDrop(Player p, InvItem i) {
        if(i.getID() == UNINDENTIFIED_LIQUID || i.getID() == NITROGLYCERIN || i.getID() == MIXED_CHEMICALS || i.getID() == MIXED_CHEMICALS2 || i.getID() == EXPLOSIVE_COMPOUND) {
            return true;
        }
        return false;
    }

    @Override
    public void onDrop(Player p, InvItem i) {
        if(i.getID() == UNINDENTIFIED_LIQUID) {
            p.message("bang!");
            removeItem(p, UNINDENTIFIED_LIQUID, 1);
            p.damage((int) (getCurrentLevel(p, HITS) * 0.3D + 5));
            playerTalk(p, null, "Ow!");
            p.message("The liquid exploded!");
            p.message("You were injured by the burning liquid");
        }
        if(i.getID() == MIXED_CHEMICALS || i.getID() == MIXED_CHEMICALS2) {
            p.message("bang!");
            removeItem(p, i.getID(), 1);
            p.damage((int) (getCurrentLevel(p, HITS) / 2 + 6));
            playerTalk(p, null, "Ow!");
            p.message("The chemicals exploded!");
            p.message("You were injured by the exploding liquid");
        }
        if(i.getID() == NITROGLYCERIN) {
            p.message("bang!");
            removeItem(p, NITROGLYCERIN, 1);
            p.damage((int) (getCurrentLevel(p, HITS) / 2 - 3));
            playerTalk(p, null, "Ow!");
            p.message("The nitroglycerin exploded!");
            p.message("You were injured by the exploding liquid");
        }
        if(i.getID() == EXPLOSIVE_COMPOUND) {
            message(p, "bang!");
            removeItem(p, EXPLOSIVE_COMPOUND, 1);
            p.damage(61);
            playerTalk(p, null, "Ow!");
            p.message("The compound exploded!");
            p.message("You were badly injured by the exploding liquid");
        }
    }
}
