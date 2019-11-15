package org.nemotech.rsc.plugins.npcs.falador;

import org.nemotech.rsc.model.World;
import static org.nemotech.rsc.plugins.Plugin.hasItem;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.removeItem;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class MakeOverMage implements TalkToNpcListener,
        TalkToNpcExecutiveListener {
    /**
     * World instance
     */
    public World world = World.getWorld();

    @Override
    public void onTalkToNpc(Player p, final NPC n) {
        npcTalk(p, n, "Are you happy with your looks?",
                "If not I can change them for the cheap cheap price",
                "Of 3000 coins");
        int opt = showMenu(p, n, "I'm happy with how I look thank you",
                "Yes change my looks please");
        if (opt == 1) {
            if (!hasItem(p, 10, 3000)) {
                playerTalk(p, n,"I'll just go get the cash");
            } else {
                removeItem(p, 10, 3000);
                p.getSender().sendAppearanceScreen();
            }
        }
    }

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 339;
    }

}
