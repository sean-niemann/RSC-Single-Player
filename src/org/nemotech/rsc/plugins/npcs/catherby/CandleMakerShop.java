package org.nemotech.rsc.plugins.npcs.catherby;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.npcTalk;
import static org.nemotech.rsc.plugins.Plugin.playerTalk;

import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;

import static org.nemotech.rsc.plugins.Plugin.MERLINS_CRYSTAL;
import org.nemotech.rsc.plugins.ShopInterface;
import org.nemotech.rsc.plugins.listeners.action.TalkToNpcListener;
import org.nemotech.rsc.plugins.listeners.executive.TalkToNpcExecutiveListener;
import org.nemotech.rsc.plugins.menu.Menu;
import org.nemotech.rsc.plugins.menu.Option;

public class CandleMakerShop  implements ShopInterface,
TalkToNpcListener, TalkToNpcExecutiveListener {

    private static final int CANDLEMAKER = 282;
    private final Shop shop = new Shop("Candle Shop", false, 1000, 100, 80,2, new InvItem(599, 10));

    @Override
    public boolean blockTalkToNpc(final Player p, final NPC n) {
        return n.getID() == CANDLEMAKER;
    }

    @Override
    public Shop[] getShops() {
        return new Shop[] { shop };
    }

    @Override
    public boolean isMembers() {
        return true;
    }

    @Override
    public void onTalkToNpc(final Player p, final NPC n) {
        if(p.getCache().hasKey("candlemaker")) {
            npcTalk(p,n, "Have you got any wax yet?");
            if(p.getInventory().hasItemId(605)) {
                playerTalk(p,n, "Yes I have some now");
                p.message("You exchange the wax with the candle maker for a black candle");
                addItem(p, 600, 1);
                p.getCache().remove("candlemaker");
            } else {
                //NOTHING HAPPENS
            }
            return;
        }
        Menu defaultMenu = new Menu();
        npcTalk(p,n, "Hi would you be interested in some of my fine candles");
        if(p.getQuestStage(MERLINS_CRYSTAL) == 3) {
            defaultMenu.addOption(new Option("Have you got any black candles?") {
                @Override
                public void action() {
                    npcTalk(p,n, "Black candles hmm?",
                            "It's very bad luck to make black candles");
                    playerTalk(p,n, "I can pay well for one");
                    npcTalk(p,n, "I still dunno",
                            "Tell you what, I'll supply with you with a black candle",
                            "If you can bring me a bucket full of wax");
                    p.getCache().store("candlemaker", true);
                }
            });

        }
        defaultMenu.addOption(new Option("Yes please") {
            @Override
            public void action() {
                org.nemotech.rsc.client.action.ActionManager.get(org.nemotech.rsc.client.action.impl.ShopHandler.class).handleShopOpen(shop);
            }
        });
        defaultMenu.addOption(new Option("No thankyou") {
            @Override
            public void action() {

            }
        });
        defaultMenu.showMenu(p, n);
    }

}
