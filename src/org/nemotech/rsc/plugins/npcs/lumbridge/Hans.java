package org.nemotech.rsc.plugins.npcs.lumbridge;

import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Hans implements TalkToNpcListener,TalkToNpcExecutiveListener{

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == 5;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p, n, "Hello what are you doing here?");
        int option = showMenu(p,n, "I'm looking for whoever is in charge of this place", 
                                "I have come to kill everyone in this castle", "I don't know. I am lost. Where am I?");
        if(option ==0)
            npcTalk(p,n,"Sorry I don't know where he is right now");
        else if (option ==1)
            npcTalk(p,n,"HELP HELP!");
        else if (option ==2)
            npcTalk(p,n,"You are in Lumbridge Castle");
        
    }

}
