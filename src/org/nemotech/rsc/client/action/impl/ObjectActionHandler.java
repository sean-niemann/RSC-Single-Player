package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.model.landscape.ActiveTile;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.external.definition.GameObjectDef;
import org.nemotech.rsc.event.WalkToObjectEvent;
import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.model.player.states.Action;
import org.nemotech.rsc.client.action.ActionHandler;
import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.external.location.TelePoint;

public class ObjectActionHandler implements ActionHandler {

    public void handleObjectAction(int x, int y, final boolean first) {
        Player player = World.getWorld().getPlayer();
        if (player.isBusy()) {
            player.resetPath();
            return;
        }
        ActiveTile t = World.getWorld().getTile(x, y);
        final GameObject object = t.getGameObject();
        player.click = first ? 0 : 1;
        if(object == null) {
            return;
        }
        player.setStatus(Action.USING_OBJECT);
        World.getWorld().getDelayedEventHandler().add(new WalkToObjectEvent(player, object, false) {
            @Override
            public void arrived() {
                owner.resetPath();

                GameObjectDef def = object.getGameObjectDef();
                if (owner.isBusy() || !owner.atObject(object)/*!p.withinRange(object, 3)*/ || owner.isRanging() || def == null || owner.getStatus() != Action.USING_OBJECT) {
                    return;
                }

                owner.reset();
                String command = (first ? def.getFirstCommand() : def.getSecondCommand()).toLowerCase();
                
                TelePoint telePoint = EntityManager.getTelePoint(object.getLocation());
                if(telePoint != null) {
                    owner.teleport(telePoint.x2, telePoint.y2, false);
                    if(!telePoint.message.isEmpty()) {
                        owner.message(telePoint.message);
                    }
                    return;
                }

                if (PluginManager.getInstance().blockDefaultAction("ObjectAction", new Object[] { object, command, owner })) {
                    return;
                }
            }
        });
    }

}