package org.nemotech.rsc.plugins.npcs.portsarim;

import static org.nemotech.rsc.plugins.Plugin.doDoor;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;
import static org.nemotech.rsc.plugins.Plugin.showMenu;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;

import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.action.WallObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;
import org.nemotech.rsc.plugins.listeners.executive.WallObjectActionExecutiveListener;

public final class WydinsGrocery implements ShopInterface,
        TalkToNpcExecutiveListener, TalkToNpcListener,
        WallObjectActionExecutiveListener, WallObjectActionListener {

    private final Shop shop = new Shop("Wydin's Food Store", false, 12500, 100, 70, 1, new InvItem(136,
            3), new InvItem(133, 1), new InvItem(18, 3), new InvItem(249, 3),
            new InvItem(236, 1), new InvItem(138, 0), new InvItem(337, 1),
            new InvItem(319, 3), new InvItem(320, 3), new InvItem(348, 1));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == 129;
    }

    @Override
    public boolean blockWallObjectAction(final GameObject obj,
            final Integer click, final Player player) {
        return obj.getID() == 47 && obj.getX() == 277 && obj.getY() == 658;
    }

    @Override
    public Shop[] getShops() {
        return new Shop[] { shop };
    }

    @Override
    public boolean isMembers() {
        return false;
    }

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        npcTalk(p, n, "welcome to my foodstore",
                "would you like to buy anything");

        final String[] options = new String[] { "yes please", "No thankyou",
                "what can you recommend?" };
        int option = showMenu(p, n, options);
        switch (option) {
        case 0:
            org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            break;
        case 2:
            npcTalk(p, n, "we have this really exotic fruit",
                    "all the way from Karamja", "it's called a banana");
            break;
        }

    }

    @Override
    public void onWallObjectAction(final GameObject obj, final Integer click,
            final Player p) {
        if (obj.getID() == 47 && obj.getX() == 277 && obj.getY() == 658) {
            final NPC n = World.getWorld().getNpcById(129);

            if (n != null && !p.getCache().hasKey("job_wydin")) {
                n.face(p);
                p.face(n);
                npcTalk(p, n, "heh you can't go in there",
                        "only employees of the grocery store can go in");

                final String[] options = new String[] {
                        "Well can I get a job here?", "Sorry I didn't realise" };
                int option = showMenu(p, n, options);
                if (option == 0) {
                    npcTalk(p, n, "Well you're keen I'll give you that",
                            "Ok I'll give you a go",
                            "Have you got your own apron?");
                    if (p.getInventory().wielding(182)) {
                        playerTalk(p, n, "Yes I have one right here");
                        npcTalk(p, n,
                                "Wow you are well prepared, you're hired",
                                "Go through to the back and tidy up for me please");
                        p.getCache().store("job_wydin", true);
                    } else {
                        npcTalk(p, n,
                                "well you can't work here unless you have an apron",
                                "health and safety regulations, you understand");
                    }
                }
            } else {
                if (!p.getInventory().wielding(182)) {
                    npcTalk(p, n, "Can you put your apron on before going in there please");
                } else {
                    if (p.getX() < 277) {
                        doDoor(obj, p);
                        p.teleport(277, 658, false);
                    } else {
                        doDoor(obj, p);
                        p.teleport(276, 658, false);
                    }
                }
            }
        }
    }

}
