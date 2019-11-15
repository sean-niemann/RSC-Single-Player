package org.nemotech.rsc.plugins.npcs.varrock;

import static org.nemotech.rsc.plugins.Plugin.*;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.InvUseOnNpcListener;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.InvUseOnNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class Thrander implements TalkToNpcListener, TalkToNpcExecutiveListener, InvUseOnNpcListener, InvUseOnNpcExecutiveListener {

    public static int THRANDER = 160;

    @Override
    public boolean blockTalkToNpc(Player p, NPC n) {
        return n.getID() == THRANDER;
    }

    @Override
    public void onTalkToNpc(Player p, NPC n) {
        npcTalk(p, n, "Hello I'm Thrander the smith",
                "I'm an expert in armour modification",
                "Give me your armour designed for men",
                "And I can convert it into something more comfortable for a women",
                "And visa versa");
    }

    @Override
    public boolean blockInvUseOnNpc(Player player, NPC npc, InvItem item) {
        return npc.getID() == THRANDER && (getNewID(item) != -1);
    }

    @Override
    public void onInvUseOnNpc(Player player, NPC npc, InvItem item) {
        if(inArray(item.getID(), 308, 312, 309, 313, 310, 311, 407, 117, 
            8, 118, 196, 119, 120, 401, 214, 215, 225, 434, 226, 227, 406, 206, 9, 121, 248, 122, 123, 402)) {
            int newID = -1;
            switch (item.getID()) {
            case 308: // Bronze top
                newID = 117;
                break;
            case 312: // Iron top
                newID = 8;
                break;
            case 309: // Steel top
                newID = 118;
                break;
            case 313: // Black top
                newID = 196;
                break;
            case 310: // Mithril top
                newID = 119;
                break;
            case 311: // Adamantite top
                newID = 120;
                break;
            case 407: // Rune top
                newID = 401;
                break;
            case 117: // Bronze body
                newID = 308;
                break;
            case 8: // Iron body
                newID = 312;
                break;
            case 118: // Steel body
                newID = 309;
                break;
            case 196: // Black body
                newID = 313;
                break;
            case 119: // Mithril body
                newID = 310;
                break;
            case 120: // Adamantite body
                newID = 311;
                break;
            case 401: // Rune body
                newID = 407;
                break;
            case 214: // Bronze skirt
                newID = 206;
                break;
            case 215: // Iron skirt
                newID = 9;
                break;
            case 225: // Steel skirt
                newID = 121;
                break;
            case 434: // Black skirt
                newID = 248;
                break;
            case 226: // Mithril skirt
                newID = 122;
                break;
            case 227: // Adamantite skirt
                newID = 123;
                break;
            case 406: // Rune skirt
                newID = 402;
                break;
            case 206: // Bronze legs
                newID = 214;
                break;
            case 9: // Iron legs
                newID = 215;
                break;
            case 121: // Steel legs
                newID = 225;
                break;
            case 248: // Black legs
                newID = 434;
                break;
            case 122: // Mithril legs
                newID = 226;
                break;
            case 123: // Adamantite legs
                newID = 227;
                break;
            case 402: // Rune legs
                newID = 406;
                break;
            }
            InvItem changedItem = getItem(newID);
            if(removeItem(player, item.getID(), 1)) {
                message(player, npc, 1300, "You give Thrander a " + item.getDef().getName().toLowerCase() + "",
                        "Thrander hammers it for a bit");
                player.message("Thrander gives you a " + changedItem.getDef().getName().toLowerCase() + "");
                addItem(player, newID, 1);
            }
        }
    }
    public int getNewID(InvItem item) {
        int newID = -1;
        switch (item.getID()) {
        case 308: // Bronze top
            newID = 117;
            break;
        case 312: // Iron top
            newID = 8;
            break;
        case 309: // Steel top
            newID = 118;
            break;
        case 313: // Black top
            newID = 196;
            break;
        case 310: // Mithril top
            newID = 119;
            break;
        case 311: // Adamantite top
            newID = 120;
            break;
        case 407: // Rune top
            newID = 401;
            break;
        case 117: // Bronze body
            newID = 308;
            break;
        case 8: // Iron body
            newID = 312;
            break;
        case 118: // Steel body
            newID = 309;
            break;
        case 196: // Black body
            newID = 313;
            break;
        case 119: // Mithril body
            newID = 310;
            break;
        case 120: // Adamantite body
            newID = 311;
            break;
        case 401: // Rune body
            newID = 407;
            break;
        case 214: // Bronze skirt
            newID = 206;
            break;
        case 215: // Iron skirt
            newID = 9;
            break;
        case 225: // Steel skirt
            newID = 121;
            break;
        case 434: // Black skirt
            newID = 248;
            break;
        case 226: // Mithril skirt
            newID = 122;
            break;
        case 227: // Adamantite skirt
            newID = 123;
            break;
        case 406: // Rune skirt
            newID = 402;
            break;
        case 206: // Bronze legs
            newID = 214;
            break;
        case 9: // Iron legs
            newID = 215;
            break;
        case 121: // Steel legs
            newID = 225;
            break;
        case 248: // Black legs
            newID = 434;
            break;
        case 122: // Mithril legs
            newID = 226;
            break;
        case 123: // Adamantite legs
            newID = 227;
            break;
        case 402: // Rune legs
            newID = 406;
            break;
        }
        return newID;
    }

}
