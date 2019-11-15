package org.nemotech.rsc.plugins.items;

import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.event.impl.BatchEvent;
import org.nemotech.rsc.client.sound.SoundEffect;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.showBubble;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnObjectListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnObjectExecutiveListener;
import org.nemotech.rsc.util.Formulae;

public class SpinningWheel extends Plugin implements InvUseOnObjectListener,
        InvUseOnObjectExecutiveListener {

    @Override
    public boolean blockInvUseOnObject(GameObject obj, InvItem item, Player player) {
        return obj.getID() == 121;
    }

    @Override
    public void onInvUseOnObject(GameObject obj, final InvItem item, Player player) {

        int produceID = -1;
        int requiredLevel = -1;
        int experience = -1;
        switch (item.getID()) {
        case 145: // Wool
            produceID = 207;
            requiredLevel = 1;
            experience = 3;
            break;
        case 675: // Flax
            produceID = 676;
            requiredLevel = 10;
            experience = 15;
            break;
        default:
            player.message("Nothing interesting happens");
            return;
        }
        final int produce = produceID;
        final int requirement = requiredLevel;
        final int exp = experience;
        if (produce == -1 || requirement == -1 || exp == -1) {
            return;
        }
        player.setBatchEvent(new BatchEvent(player, 650, Formulae.getRepeatTimes(player, CRAFTING)) {
            @Override
            public void action() {
                if (owner.getCurStat(CRAFTING) < requirement) {
                    message(owner, "You need a crafting level of "
                            + requirement + " to spin "
                            + item.getDef().getName().toLowerCase() + "!");
                    interrupt();
                    return;
                }
                if (owner.getInventory().remove(item.getID(), 1) > -1) {
                    showBubble(owner, item);
                    owner.getSender().sendSound(SoundEffect.MECHANICAL);
                    owner.message("You make the "
                            + item.getDef().getName()
                            + " into a "
                            + EntityManager.getItem(produce).getName()
                                    .toLowerCase() + "");
                    owner.getInventory().add(new InvItem(produce, 1));
                    owner.incExp(12, exp, true);
                } else {
                    interrupt();
                }
            }
        });

    }
}
