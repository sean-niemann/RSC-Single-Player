package org.nemotech.rsc.plugins.minigames.fishingtrawler;

import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.util.Util;

import static org.nemotech.rsc.plugins.Plugin.addItem;
import static org.nemotech.rsc.plugins.Plugin.message;
import static org.nemotech.rsc.plugins.Plugin.showBubble;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.listeners.action.ObjectActionListener;
import org.nemotech.rsc.plugins.listeners.executive.ObjectActionExecutiveListener;

public class TrawlerCatch extends Plugin implements ObjectActionListener, ObjectActionExecutiveListener {

    public static final int TRAWLER_CATCH = 1106;
    public static final int[] JUNK_ITEMS = new int[] { 1875, 778, 1155, 1157, 1158, 1167, 1169, 1165, 1159, 1166,
            2078 };

    @Override
    public boolean blockObjectAction(GameObject obj, String command, Player p) {
        return obj.getID() == TRAWLER_CATCH;
    }

    @Override
    public void onObjectAction(GameObject obj, String command, Player p) {
        if (obj.getID() == TRAWLER_CATCH) {
            message(p, 1900, "you search the smelly net");
            showBubble(p, new InvItem(376));
            if (p.getCache().hasKey("fishing_trawler_reward")) {
                p.message("you find...");
                int fishCaught = p.getCache().getInt("fishing_trawler_reward");
                for (int fishGiven = 0; fishGiven < fishCaught; fishGiven++) {
                    if (catchFish(81, p.getCurStat(FISHING))) {
                        message(p, 1200, "..some manta ray");
                        addItem(p, 2068, 1); // RAW MANTA RAY NOTES
                        p.incExp(FISHING, 115, false);
                    } else if (catchFish(75, p.getCurStat(FISHING))) {
                        message(p, 1200, "..some sea turtle");
                        addItem(p, 2069, 1); // RAW SEA TURTLE NOTES
                        p.incExp(FISHING, 105, false);
                    } else if (catchFish(70, p.getCurStat(FISHING))) {
                        message(p, 1200, "..some shark");
                        addItem(p, 1752, 1); // RAW SHARK NOTES
                        p.incExp(FISHING, 110, false);
                    } else if (catchFish(50, p.getCurStat(FISHING))) {
                        message(p, 1200, "..some swordfish");
                        addItem(p, 1613, 1); // RAW SWORDFISH NOTES
                        p.incExp(FISHING, 100, false);
                    } else if (catchFish(40, p.getCurStat(FISHING))) {
                        message(p, 1200, "..some lobster...");
                        addItem(p, 1616, 1); // RAW LOBSTER NOTES
                        p.incExp(FISHING, 90, false);
                    } else if (catchFish(30, p.getCurStat(FISHING))) {
                        message(p, 1200, "..some tuna...");
                        addItem(p, 1610, 1); // RAW TUNA NOTES
                        p.incExp(FISHING, 80, false);
                    } else if (catchFish(15, p.getCurStat(FISHING))) {
                        message(p, 1200, "..some anchovies...");
                        addItem(p, 1595, 1); // RAW ANCHOVIES NOTES
                        p.incExp(FISHING, 40, false);
                    } else if (catchFish(5, p.getCurStat(FISHING))) {
                        message(p, 1200, "..some sardine...");
                        addItem(p, 1598, 1); // RAW SARDINE NOTES
                        p.incExp(FISHING, 20, false);
                    } else if (catchFish(1, p.getCurStat(FISHING))) {
                        message(p, 1200, "..some shrimp...");
                        addItem(p, 1594, 1); // RAW SHRIMP NOTES
                        p.incExp(FISHING, 10, false);
                    } else {
                        int randomJunkItem = JUNK_ITEMS[Util.random(0, JUNK_ITEMS.length - 1)];
                        message(p, 1200, "..some " + EntityManager.getItem(randomJunkItem).getName().toLowerCase());
                        addItem(p, randomJunkItem, 1);
                    }
                }
                p.getCache().remove("fishing_trawler_reward");
                p.message("that's the lot");
            } else {
                p.message("the smelly net is empty");
            }
        }
    }

    public boolean catchFish(int levelReq, int level) {
        int levelDiff = level - levelReq;
        if (levelDiff <= 0) {
            return false;
        }
        return Util.percentChance(offsetToPercent(levelDiff));
    }

    private static int offsetToPercent(int levelDiff) {
        return levelDiff > 40 ? 60 : 20 + levelDiff;
    }

}
