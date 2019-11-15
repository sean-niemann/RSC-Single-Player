package org.nemotech.rsc.event.impl.extra;

import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.model.player.Player;

/**
 * Random appearing messages from the underground cave appearing at random time stamps triggers 
 * when you cross an obstacle (Rock) & (Agility obstacles in the black area(Map3)).
 */
public class UndergroundPassMessageEvent extends DelayedEvent {

    private Player p;

    public UndergroundPassMessageEvent(Player p, int delay) {
        super(null, delay);
        this.p = p;
    }

    @Override
    public void run() {
        int random = Util.getRandom().nextInt(6);
        if(random == 0) {
            p.message("@dre@iban will save you....he'll save us all");
        } else if(random == 1) {
            p.message("@dre@join us...join us...embrace the mysery");
        } else if(random == 2 && p.getQuestStage(Plugin.UNDERGROUND_PASS) >= 4) {
            p.message("@dre@I see you adventurer...you can't hide");
        } else if(random == 3 && p.getQuestStage(Plugin.UNDERGROUND_PASS) >= 4) {
            p.message("@dre@Come taste the pleasure of evil");
        } else if(random == 4 && p.getQuestStage(Plugin.UNDERGROUND_PASS) >= 4) {
            p.message("@dre@Death is only the beginning");
        } else if(random == 5) {
            interrupt();
        }
        interrupt();
    }
    
}