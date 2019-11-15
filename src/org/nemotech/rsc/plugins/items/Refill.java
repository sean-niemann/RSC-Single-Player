package org.nemotech.rsc.plugins.items;

import org.nemotech.rsc.event.impl.BatchEvent;
import org.nemotech.rsc.client.sound.SoundEffect;
import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.inArray;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.showBubble;
import static org.nemotech.rsc.plugins.Plugin.sleep;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnObjectListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnObjectExecutiveListener;

public class Refill implements InvUseOnObjectListener,
        InvUseOnObjectExecutiveListener {

    final int[] VALID_OBJECTS = { 2, 466, 814, 48, 26, 86, 1130 };
    final int[] REFILLABLE = { 21, 140, 341, 465 };
    final int[] REFILLED = { 50, 141, 342, 464 };

    @Override
    public boolean blockInvUseOnObject(GameObject obj, InvItem item, Player player) {
        return inArray(obj.getID(), 2, 466, 814, 48, 26, 86, 1130) && inArray(item.getID(), 21, 140, 341, 465);
    }

    @Override
    public void onInvUseOnObject(final GameObject obj, final InvItem item, Player player) {
        for (int i = 0; i < REFILLABLE.length; i++) {
            if (REFILLABLE[i] == item.getID()) {
                final int itemID = item.getID();
                final int refilledID = REFILLED[i];
                player.setBatchEvent(new BatchEvent(player, 300, player.getInventory().countId(itemID)) {
                    @Override
                    public void action() {
                        if (removeItem(owner, itemID, 1)) {
                            showBubble(owner, item);
                            owner.getSender().sendSound(SoundEffect.FILL_JUG);
                            sleep(300);
                            owner.message("You fill the " + item.getDef().getName().toLowerCase() + " from the " + obj.getGameObjectDef().getName().toLowerCase());
                            addItem(owner, refilledID, 1);
                        } else {
                            interrupt();
                        }
                    }
                });
                break;
            }
        }
    }

}
