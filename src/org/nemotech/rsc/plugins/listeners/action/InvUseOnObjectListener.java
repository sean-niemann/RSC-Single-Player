package org.nemotech.rsc.plugins.listeners.action;

import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;

public interface InvUseOnObjectListener {

    public void onInvUseOnObject(GameObject obj, InvItem item, Player player);
    
}