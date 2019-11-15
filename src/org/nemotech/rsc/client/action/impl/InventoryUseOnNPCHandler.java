package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.Constants;
import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.event.WalkToMobEvent;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.player.states.Action;
import org.nemotech.rsc.client.action.ActionHandler;

public class InventoryUseOnNPCHandler implements ActionHandler {
    
    public void handleInventoryUseOnNPC(int npcIndex, int itemIndex) {
        Player player = World.getWorld().getPlayer();
        
        if (player.isBusy()) {
            player.resetPath();
            return;
        }
        player.reset();
        
        final NPC affectedNpc = World.getWorld().getNpc(npcIndex);
        final InvItem item = player.getInventory().get(itemIndex);
        
        if (affectedNpc == null || item == null) { // This shouldn't happen
            return;
        }

        player.setFollowing(affectedNpc);
        player.setStatus(Action.USING_INVITEM_ON_NPC);
        World.getWorld().getDelayedEventHandler().add(new WalkToMobEvent(player, affectedNpc, 1) {
            public void arrived() {
                owner.resetPath();
                owner.resetFollowing(); //|| !owner.nextTo(affectedNpc)
                if (!owner.getInventory().contains(item) || owner.isBusy() || owner.isRanging() || !owner.withinRange(affectedNpc, 1) || affectedNpc.isBusy() || owner.getStatus() != Action.USING_INVITEM_ON_NPC) {
                    return;
                }
                owner.reset();

                if (item.getDef().isMembers() && !Constants.MEMBER_WORLD) {
                    owner.getSender().sendMessage("Nothing interesting happens");
                    return;
                }
                
                if(PluginManager.getInstance().blockDefaultAction("InvUseOnNpc", new Object[]{ owner, affectedNpc, item })) {
                    return;
                }
            }
        });
    }
    
}