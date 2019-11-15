package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.event.WalkToObjectEvent;
import org.nemotech.rsc.model.landscape.ActiveTile;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.player.states.Action;
import org.nemotech.rsc.client.action.ActionHandler;

public class InventoryUseOnObjectHandler implements ActionHandler {

    public void handleInventoryUseOnObject(int item, int x, int y) {
        Player player = World.getWorld().getPlayer();

        if (player.isBusy()) {
            player.resetPath();
            return;
        }
        player.reset();
        ActiveTile tile = World.getWorld().getTile(x, y);
        if (tile == null) {
            player.setSuspiciousPlayer(true);
            player.resetPath();
            return;
        }
        GameObject object = tile.getGameObject();
        InvItem invItem = player.getInventory().get(item);
        if (object == null || object.getType() == 1 || invItem == null) {
            player.setSuspiciousPlayer(true);
            return;
        }

        handleObject(player, object, invItem);
        //tile.cleanItself();
    }

    private void handleObject(final Player player, final GameObject object, final InvItem item) {
        player.setStatus(Action.USING_INVITEM_ON_OBJECT);
        World.getWorld().getDelayedEventHandler().add(new WalkToObjectEvent(player, object, false) {
            public void arrived() {
                owner.resetPath(); //|| !tile.hasGameObject() || !tile.getGameObject().equals(object) //!owner.nextTo(player, object)
                if (owner.isBusy() || owner.isRanging() || !owner.getInventory().contains(item) || !owner.withinRange(object, 3) || owner.getStatus() != Action.USING_INVITEM_ON_OBJECT) {
                    return;
                }
                owner.reset();

                /*if (item.getDef().isMembers() && !Config.MEMBERS_CONTENT) {
                    owner.getSender().sendMessage("Nothing interesting happens");
                    return;
                }*/

                if (PluginManager.getInstance().blockDefaultAction("InvUseOnObject", new Object[]{ object, item, owner })) {
                    return;
                }
            }
        });
    }
    
}