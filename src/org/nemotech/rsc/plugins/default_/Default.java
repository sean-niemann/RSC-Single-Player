package org.nemotech.rsc.plugins.default_;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.DefaultHandler;
import org.nemotech.rsc.plugins.listeners.action.InvActionListener;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnItemListener;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnObjectListener;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnNpcListener;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnWallObjectListener;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.action.WallObjectActionListener;

/**
 * 
 * Handles any interaction that is not handled in other scripts (Default message).
 * 
 */
public class Default implements DefaultHandler, TalkToNpcListener, ObjectActionListener,
    InvUseOnObjectListener, InvUseOnWallObjectListener, InvUseOnNpcListener, WallObjectActionListener,
    InvUseOnItemListener, InvActionListener {
    
    private DoorAction doors = new DoorAction();
    private Ladders ladders = new Ladders();

    @Override
    public void onInvUseOnNpc(Player player, NPC npc, InvItem item) {
        player.getSender().sendMessage("Nothing interesting happens");
    }

    @Override
    public void onInvUseOnObject(GameObject object, InvItem item, Player player) {
        if(doors.blockInvUseOnWallObject(object, item, player)) {
            doors.onInvUseOnWallObject(object, item, player);
            return;
        }
        player.getSender().sendMessage("Nothing interesting happens");
    }

    @Override
    public void onObjectAction(GameObject object, String command, Player player) {
        if (doors.blockObjectAction(object, command, player)) {
            doors.onObjectAction(object, command, player);
        } else if (ladders.blockObjectAction(object, command, player)) {
            ladders.onObjectAction(object, command, player);
        } else {
            player.message("Nothing interesting happens");
        }
    }

    @Override
    public void onTalkToNpc(Player player, NPC npc) {
        player.getSender().sendMessage("The " + npc.getDef().getName() + " does not appear interested in talking");
    }

    @Override
    public void onInvUseOnWallObject(GameObject object, InvItem item, Player player) {
        if(doors.blockInvUseOnWallObject(object, item, player)) {
            doors.onInvUseOnWallObject(object, item, player);
            return;
        }
        player.getSender().sendMessage("Nothing interesting happens");
    }

    @Override
    public void onWallObjectAction(GameObject object, Integer click, Player player) {
        if(doors.blockWallObjectAction(object, click, player)) {
            doors.onWallObjectAction(object, click, player);
            return;
        }
        player.getSender().sendMessage("Nothing interesting happens");
    }

    @Override
    public void onInvUseOnItem(Player player, InvItem item1, InvItem item2) {
        player.getSender().sendMessage("Nothing interesting happens");
    }

    @Override
    public void onInvAction(InvItem item, Player player) {
        player.getSender().sendMessage("Nothing interesting happens");
    }
    
}