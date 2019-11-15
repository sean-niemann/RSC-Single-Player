package org.nemotech.rsc.plugins.npcs;

import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;

public class GeneralStore extends Plugin implements TalkToNpcListener, TalkToNpcExecutiveListener {
    
    int[] SHOP_KEEPERS = { 105, 106, 55, 83, 186, 185, 169, 168, 82, 143, 88, 87 };
    
    public static final Shop GENERAL_STORE = new Shop("General Store", true, 1, 12400, 100, 40, new InvItem(135, 3), new InvItem(140, 2), new InvItem(144, 2), new InvItem(21, 2), new InvItem(166, 2), new InvItem(167, 2), new InvItem(168, 5), new InvItem(1263, 10));
    public static final Shop DWARVEN_MINE_GENERAL = new Shop(GENERAL_STORE);
    public static final Shop VARROCK_GENERAL = new Shop(GENERAL_STORE);
    public static final Shop FALADOR_GENERAL = new Shop(GENERAL_STORE);
    public static final Shop LUMBRIDGE_GENERAL = new Shop(GENERAL_STORE);
    public static final Shop RIMMINGTON_GENERAL  = new Shop(GENERAL_STORE);
    public static final Shop KARAMJA_GENERAL = new Shop(GENERAL_STORE);
    public static final Shop AL_KHARID_GENERAL = new Shop(GENERAL_STORE);
    public static final Shop EDGEVILLE_GENERAL = new Shop(GENERAL_STORE);
    
    @Override
    public boolean blockTalkToNpc(Player player, NPC npc) {
        if (Util.inArray(SHOP_KEEPERS, npc.getID())) {
            return true;
        }
        return false;
    }

    @Override
    public void onTalkToNpc(Player player, NPC npc) {
        npcTalk(player, npc, "Can I help you at all?");
        int option = showMenu(player, npc, "Yes please. What are you selling?", "No thanks");
        if(option == 0) {
            npcTalk(player, npc, "Take a look");
            int x = player.getX();
            int y = player.getY();
            Shop shop = null;
            if (x >= 132 && x <= 137 && y >= 639 && y <= 644) {
                shop = LUMBRIDGE_GENERAL;
            } else if (x >= 317 && x <= 322 && y >= 530 && y <= 536) {
                shop = FALADOR_GENERAL;
            } else if (x >= 124 && x <= 129 && y >= 513 && y <= 518) {
                shop = VARROCK_GENERAL;
            } else if (x >= 222 && x <= 227 && y >= 439 && y <= 443) {
                shop = EDGEVILLE_GENERAL;
            } else if (x >= 289 && x <= 295 && y >= 3337 && y <= 3342) {
                shop = DWARVEN_MINE_GENERAL;
            } else if (x >= 358 && x <= 364 && y >= 712 && y <= 716) {
                shop = KARAMJA_GENERAL;
            } else if (x >= 54 && x <= 58 && y >= 679 && y <= 684) {
                shop = AL_KHARID_GENERAL;
            } else if (x >= 329 && x <= 332 && y >= 658 && y <= 664) {
                shop = RIMMINGTON_GENERAL;
            }
            if (shop != null) {
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            }
        }
    }

}