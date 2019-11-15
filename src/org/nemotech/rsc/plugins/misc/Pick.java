package org.nemotech.rsc.plugins.misc;

import org.nemotech.rsc.event.SingleEvent;
import org.nemotech.rsc.client.sound.SoundEffect;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;
import static org.nemotech.rsc.plugins.Plugin.addItem;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;

public class Pick implements ObjectActionExecutiveListener, ObjectActionListener {

    @Override
    public boolean blockObjectAction(final GameObject obj,
            final String command, final Player player) {
        return command.equals("pick")
                || /* Flax */obj.getID() == 313;
    }

    private void handleFlaxPickup(final Player owner, GameObject obj) {
        owner.setBusyTimer(250);
        if (!owner.getInventory().full()) {
            owner.message("You uproot a flax plant");
            addItem(owner, 675, 1);
        } else {
            owner.message("Your inventory is full!");
        }
    }

    @Override
    public void onObjectAction(final GameObject object, final String command,
            final Player owner) {
        switch (object.getID()) {
        case 72: // Wheat
            owner.message("You get some grain");
            owner.getInventory().add(new InvItem(29, 1));
            break;
        case 191: // Potatos
            owner.message("You pick a potato");
            owner.getInventory().add(new InvItem(348, 1));
            break;
        case 313: // Flax
            handleFlaxPickup(owner, object);
            return;
        default:
            owner.message("Nothing interesting happens");
            return;
        }
        owner.getSender().sendSound(SoundEffect.POTATO);
        owner.setBusy(true);
        World.getWorld().getDelayedEventHandler().add(
                new SingleEvent(owner, 200) {
                    @Override
                    public void action() {
                        owner.setBusy(false);
                    }
                });
        return;
    }
}