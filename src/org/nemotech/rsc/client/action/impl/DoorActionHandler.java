package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.model.landscape.ActiveTile;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.external.definition.DoorDef;
import org.nemotech.rsc.event.WalkToPointEvent;
import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.model.player.states.Action;
import org.nemotech.rsc.client.action.ActionHandler;

public class DoorActionHandler implements ActionHandler {

    public void handleDoor(int x, int y, final boolean first) {
        Player player = World.getWorld().getPlayer();
        if (player.isBusy()) {
            player.resetPath();
            return;
        }
        ActiveTile t = World.getWorld().getTile(x, y);
        final GameObject door = t.getGameObject();
        if(door == null) {
            return;
        }
        player.setStatus(Action.USING_DOOR);
        World.getWorld().getDelayedEventHandler().add(new WalkToPointEvent(player, door.getLocation(), 1, false) {
            @Override
            public void arrived() {
                owner.resetPath();

                DoorDef def = door.getDoorDef();
                if (owner.isBusy()|| !owner.atObject(door)/*!p.withinRange(door, 3)*/ || owner.isRanging() || def == null || owner.getStatus() != Action.USING_DOOR) {
                    return;
                }

                owner.reset();
                String command = (first ? def.getCommandFirst() : def.getCommandSecond()).toLowerCase();
                int click = 0;
                if(!first) click = 1;

                if (PluginManager.getInstance().blockDefaultAction("WallObjectAction", new Object[] { door, click, owner })) {
                    return;
                }
            }
        });
    }

}