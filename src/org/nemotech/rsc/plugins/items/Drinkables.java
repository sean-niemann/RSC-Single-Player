package org.nemotech.rsc.plugins.items;

import static org.nemotech.rsc.plugins.Plugin.checkAndRemoveBlurberry;
import static org.nemotech.rsc.plugins.Plugin.showBubble;
import static org.nemotech.rsc.plugins.Plugin.sleep;

import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.listeners.action.InvActionListener;
import org.nemotech.rsc.plugins.listeners.executive.InvActionExecutiveListener;

public class Drinkables implements InvActionListener, InvActionExecutiveListener {

    @Override
    public boolean blockInvAction(InvItem item, Player player) {
        return item.getDef().getCommand().equalsIgnoreCase("drink");
    }

    @Override
    public void onInvAction(InvItem item, Player player) {
        if (player.cantConsume()) {
            return;
        }
        player.setConsumeTimer(1200);
        player.getSender().sendItemBubble(item.getID());
        switch (item.getID()) {
        case 876: // alcohol - brandy
        case 869: // alcohol - vodka
        case 870: // alcohol - gin
        case 868: // alcohol - whisky
            player.message("You drink the " + item.getDef().getName().toLowerCase());
            player.message("You feel slightly reinvigorated");
            player.message("And slightly dizzy too");
            if(item.getID() == 868) 
                player.setCurStat(0, player.getCurStat(0) - 6);
            else 
                player.setCurStat(0, player.getCurStat(0) - 3);
            if (player.getCurStat(2) <= player.getMaxStat(2)) {
                player.setCurStat(2, player.getCurStat(2) + 5);
            }
            final boolean heals = player.getCurStat(3) < player.getMaxStat(3);
            if (heals) {
                int newHp = player.getCurStat(3) + 4;
                if (newHp > player.getMaxStat(3)) {
                    newHp = player.getMaxStat(3);
                }
                player.setCurStat(3, newHp);
            }
            player.getInventory().remove(item);
            break;
        case 853:// half cocktail glass
        case 854:// full cocktail glass
        case 867:// odd looking cocktail
            player.setCurStat(0, player.getCurStat(0) - 3);
            player.setCurStat(1, player.getCurStat(1) - 1);
            player.setCurStat(2, player.getCurStat(2) - 4);
            player.message("You drink the cocktail");
            player.message("It tastes awful..yuck");
            player.getInventory().remove(item);
            player.getInventory().add(new InvItem(833));
            checkAndRemoveBlurberry(player, true);
            break;
        case 937: // fruit blast
        case 866: // fruit blast
        case 940: // pineapple punch
        case 879: // pineapple punch
            if (player.getCurStat(3) < player.getMaxStat(3)) {
                int newHp = player.getCurStat(3) + 8;
                if (newHp > player.getMaxStat(3)) {
                    newHp = player.getMaxStat(3);
                }
                player.setCurStat(3, newHp);
            }
            player.message("You drink the cocktail");
            player.message("yum ..it tastes great");
            player.message("You feel reinvigorated");
            player.getInventory().remove(item);
            player.getInventory().add(new InvItem(833));
            break;
        case 938: // blurberry special
        case 877: // blurberry special
        case 939: // wizard blizzard
        case 878: // wizard blizzard
        case 941: // SGG
        case 874: // SGG
        case 942: // chocolate saturday
        case 875: // chocolate saturday
        case 943: // drunk dragon
        case 872: // drunk dragon
            if (player.getCurStat(3) < player.getMaxStat(3)) {
                int newHp = player.getCurStat(3) + 5;
                if (newHp > player.getMaxStat(3)) {
                    newHp = player.getMaxStat(3);
                }
                player.setCurStat(3, newHp);
            }
            player.setCurStat(0, player.getCurStat(0) - 4);
            if (player.getCurStat(2) <= player.getMaxStat(2)) {
                player.setCurStat(2, player.getCurStat(2) + 6);
            }
            player.message("You drink the cocktail");
            player.message("yum ..it tastes great");
            player.message("although you feel slightly dizzy");
            player.getInventory().remove(item);
            player.getInventory().add(new InvItem(833));
            break;
        case 180: // bad wine
            player.message("You drink the bad wine");
            showBubble(player, item);

            player.getInventory().remove(item);
            player.getInventory().add(new InvItem(140));

            player.setCurStat(0, player.getCurStat(0) - 3);
            sleep(1200);
            player.message("You start to feel sick");
            break;
        case 142:
            showBubble(player, item);
            player.message("You drink the wine");
            player.message("It makes you feel a bit dizzy");
            player.getInventory().remove(item);
            player.getInventory().add(new InvItem(140));
            if (player.getCurStat(3) < player.getMaxStat(3)) {
                int newStat = player.getCurStat(3) + 11;
                if (newStat > player.getMaxStat(3)) {
                    player.setCurStat(3, player.getMaxStat(3));
                } else {
                    player.setCurStat(3, newStat);
                }
            }
            player.setCurStat(0, player.getCurStat(0) - 3);
            break;
        case 739: // Tea
            showBubble(player, item);
            player.message("You drink the " + item.getDef().getName().toLowerCase());
            player.getInventory().remove(item);
            break;
        case 193: // Beer
            showBubble(player, item);
            player.message("You drink the beer");
            player.message("You feel slightly reinvigorated");
            player.message("And slightly dizzy too");
            player.getInventory().remove(item);
            player.getInventory().add(new InvItem(620));
            player.setCurStat(0, player.getCurStat(0) - 4);
            if (player.getCurStat(2) <= player.getMaxStat(2)) {
                player.setCurStat(2, player.getCurStat(2) + 2);
            }
            break;
        case 830: // Greenmans Ale
            showBubble(player, item);
            player.message("You drink the " + item.getDef().getName() + ".");
            player.getInventory().remove(item);
            player.getInventory().add(new InvItem(620));
            sleep(1200);
            player.message("It has a strange taste.");
            for (int stat = 0; stat < 3; stat++) {
                player.setCurStat(stat, player.getCurStat(stat) - 4);
            }
            if (player.getCurStat(15) <= player.getMaxStat(15)) {
                player.setCurStat(15, player.getCurStat(15) + 1);
            }
            break;
        case 268: // Mind Bomb
            showBubble(player, item);
            player.message("You drink the " + item.getDef().getName() + ".");
            player.getInventory().remove(item);
            player.getInventory().add(new InvItem(620));
            sleep(1200);
            player.message("You feel very strange.");
            for (int stat = 0; stat < 3; stat++) {
                player.setCurStat(stat, player.getCurStat(stat) - 4);
            }
            int change = (player.getMaxStat(6) > 55 ? 3 : 2);
            int maxWithBomb = (player.getMaxStat(6) + change);
            if (maxWithBomb - player.getCurStat(6) < change) {
                change = maxWithBomb - player.getCurStat(6);
            }
            if (player.getCurStat(
                    6) <= (player.getMaxStat(6) + (player.getMaxStat(6) > 55 ? 3 : 2))) {
                player.setCurStat(6, player.getCurStat(6) + change);
            }
            break;
        case 269: // Dwarven Stout
            showBubble(player, item);
            player.message("You drink the " + item.getDef().getName() + ".");
            player.getInventory().remove(item);
            player.getInventory().add(new InvItem(620));
            sleep(1200);
            player.message("It tastes foul.");
            for (int stat = 0; stat < 3; stat++) {
                player.setCurStat(stat, player.getCurStat(stat) - 4);
            }
            if (player.getCurStat(13) <= player.getMaxStat(13)) {
                player.setCurStat(13, player.getCurStat(13) + 1);
            }
            if (player.getCurStat(14) <= player.getMaxStat(14)) {
                player.setCurStat(14, player.getCurStat(14) + 1);
            }
            break;
        case 267: // Asgarnian Ale
            player.message("You drink the " + item.getDef().getName() + ".");
            showBubble(player, item);
            player.getInventory().remove(item);
            player.getInventory().add(new InvItem(620));
            sleep(1200);
            player.message("You feel slightly reinvigorated");
            player.message("And slightly dizzy too.");
            player.setCurStat(0, player.getCurStat(0) - 4);
            if (player.getCurStat(2) <= player.getMaxStat(2)) {
                player.setCurStat(2, player.getCurStat(2) + 2);
            }
            break;
        case 829: // Dragon Bitter
            player.message("You drink the " + item.getDef().getName() + ".");
            player.getInventory().remove(item);
            player.getInventory().add(new InvItem(620));
            showBubble(player, item);
            sleep(1200);
            player.message("You feel slightly dizzy.");
            player.setCurStat(0, player.getCurStat(0) - 4);
            if (player.getCurStat(2) <= player.getMaxStat(2)) {
                player.setCurStat(2, player.getCurStat(2) + 2);
            }
            break;

        case 221: // Strength Potion - 4 dose
            useNormalPotion(player, item, 2, 10, 2, 222, 3);
            break;
        case 222: // Strength Potion - 3 dose
            useNormalPotion(player, item, 2, 10, 2, 223, 2);
            break;
        case 223: // Strength Potion - 2 dose
            useNormalPotion(player, item, 2, 10, 2, 224, 1);
            break;
        case 224: // Strength Potion - 1 dose
            useNormalPotion(player, item, 2, 10, 2, 465, 0);
            break;
        case 474: // attack Potion - 3 dose
            useNormalPotion(player, item, 0, 10, 2, 475, 2);
            break;
        case 475: // attack Potion - 2 dose
            useNormalPotion(player, item, 0, 10, 2, 476, 1);
            break;
        case 476: // attack Potion - 1 dose
            useNormalPotion(player, item, 0, 10, 2, 465, 0);
            break;
        case 477: // stat restoration Potion - 3 dose
            useStatRestorePotion(player, item, 478, 2);
            break;
        case 478: // stat restoration Potion - 2 dose
            useStatRestorePotion(player, item, 479, 1);
            break;
        case 479: // stat restoration Potion - 1 dose
            useStatRestorePotion(player, item, 465, 0);
            break;
        case 480: // defense Potion - 3 dose
            useNormalPotion(player, item, 1, 10, 2, 481, 2);
            break;
        case 481: // defense Potion - 2 dose
            useNormalPotion(player, item, 1, 10, 2, 482, 1);
            break;
        case 482: // defense Potion - 1 dose
            useNormalPotion(player, item, 1, 10, 2, 465, 0);
            break;
        case 483: // restore prayer Potion - 3 dose
            usePrayerPotion(player, item, 484, 2);
            break;
        case 484: // restore prayer Potion - 2 dose
            usePrayerPotion(player, item, 485, 1);
            break;
        case 485: // restore prayer Potion - 1 dose
            usePrayerPotion(player, item, 465, 0);
            break;
        case 486: // Super attack Potion - 3 dose
            useNormalPotion(player, item, 0, 15, 4, 487, 2);
            break;
        case 487: // Super attack Potion - 2 dose
            useNormalPotion(player, item, 0, 15, 4, 488, 1);
            break;
        case 488: // Super attack Potion - 1 dose
            useNormalPotion(player, item, 0, 15, 4, 465, 0);
            break;
        case 489: // fishing Potion - 3 dose
            useFishingPotion(player, item, 490, 2);
            break;
        case 490: // fishing Potion - 2 dose
            useFishingPotion(player, item, 491, 1);
            break;
        case 491: // fishing Potion - 1 dose
            useFishingPotion(player, item, 465, 0);
            break;
        case 492: // Super strength Potion - 3 dose
            useNormalPotion(player, item, 2, 15, 4, 493, 2);
            break;
        case 493: // Super strength Potion - 2 dose
            useNormalPotion(player, item, 2, 15, 4, 494, 1);
            break;
        case 494: // Super strength Potion - 1 dose
            useNormalPotion(player, item, 2, 15, 4, 465, 0);
            break;
        case 495: // Super defense Potion - 3 dose
            useNormalPotion(player, item, 1, 15, 4, 496, 2);
            break;
        case 496: // Super defense Potion - 2 dose
            useNormalPotion(player, item, 1, 15, 4, 497, 1);
            break;
        case 497: // Super defense Potion - 1 dose
            useNormalPotion(player, item, 1, 15, 4, 465, 0);
            break;
        case 498: // ranging Potion - 3 dose
            useNormalPotion(player, item, 4, 10, 2, 499, 2);
            break;
        case 499: // ranging Potion - 2 dose
            useNormalPotion(player, item, 4, 10, 2, 500, 1);
            break;
        case 500: // ranging Potion - 1 dose
            useNormalPotion(player, item, 4, 10, 2, 465, 0);
            break;
        case 566: // cure poison potion - 3 dose
            useCurePotion(player, item, 567, 2);
            break;
        case 567: // rcure poison potion - 2 dose
            useCurePotion(player, item, 568, 1);
            break;
        case 568: // cure poison potion - 1 dose
            useCurePotion(player, item, 465, 0);
            break;
        case 569: // poison antidote potion - 3 dose
            usePoisonAntidotePotion(player, item, 570, 2);
            break;
        case 570: // poison antidote potion - 2 dose
            usePoisonAntidotePotion(player, item, 571, 1);
            break;
        case 571: // poison antidote potion - 1 dose
            usePoisonAntidotePotion(player, item, 465, 0);
            break;
        case 963: // Zamorak potion - 3 dose
            useZamorakPotion(player, item, 964, 2);
            break;
        case 964: // Zamorak potion - 2 dose
            useZamorakPotion(player, item, 965, 1);
            break;
        case 965: // Zamorak potion - 1 dose
            useZamorakPotion(player, item, 465, 0);
            break;
        default:
            player.message("Nothing interesting happens");
            return;
        }
    }

    private void useFishingPotion(Player player, final InvItem item, final int newItem, final int left) {
        player.message("You drink some of your " + item.getDef().getName().toLowerCase());
        player.getInventory().remove(item);
        player.getInventory().add(new InvItem(newItem));
        player.setCurStat(10, player.getMaxStat(10) + 3);
        sleep(1200);
        if(left <= 0) {
            player.message("You have finished your potion");
        } else {
            player.message("You have " + left + " doses of potion left");
        }
    }

    private void useCurePotion(Player player, final InvItem item, final int newItem, final int dosesLeft) {
        player.message("You drink some of your " + item.getDef().getName().toLowerCase());
        player.getInventory().remove(item);
        player.getInventory().add(new InvItem(newItem));
        //player.cure();
        sleep(1200);
        if(dosesLeft <= 0) {
            player.message("You have finished your potion");
        } else {
            player.message("You have " + dosesLeft + " doses of potion left");
        }
    }

    private void usePoisonAntidotePotion(Player player, final InvItem item, final int newItem, final int dosesLeft) {
        player.message("You drink some of your " + item.getDef().getName().toLowerCase() + " potion");
        player.getInventory().remove(item);
        player.getInventory().add(new InvItem(newItem));
        //player.cure();
        //player.setAntidoteProtection(); // 90 seconds.
        sleep(1200);
        if(dosesLeft <= 0) {
            player.message("You have finished your potion");
        } else {
            player.message("You have " + dosesLeft + " doses of potion left");
        }
    }

    private void useNormalPotion(Player player, final InvItem item, final int affectedStat, final int percentageIncrease, final int modifier, final int newItem, final int left) {
        player.message("You drink some of your " + item.getDef().getName().toLowerCase());
        int baseStat = player.getCurStat(affectedStat) > player.getMaxStat(affectedStat) ? player.getMaxStat(affectedStat) : player.getCurStat(affectedStat);
        int newStat = baseStat
                + Util.roundUp((player.getMaxStat(affectedStat) / 100D) * percentageIncrease)
                + modifier;
        if (newStat > player.getCurStat(affectedStat)) {
            player.setCurStat(affectedStat, newStat);
        }
        player.getInventory().remove(item);
        player.getInventory().add(new InvItem(newItem));
        sleep(1200);
        if(left <= 0) {
            player.message("You have finished your potion");
        } else {
            player.message("You have " + left + " dose" + (left == 1 ? "" : "s") + " of potion left");
        }
    }

    private void useZamorakPotion(Player player, final InvItem item, final int newItem, final int left) {
        player.message("You drink some of your zamorak potion");
        player.getInventory().remove(item);
        player.getInventory().add(new InvItem(newItem));
        int attackBoost = (int) 22.5;
        int strengthBoost = (int) 15;
        int defenceDecrease = (int) 12.5;
        int hitsDecrease = (int) 10;
        // ugly but right formula.
        if (player.getCurStat(0) > player.getMaxStat(0)) {
            int baseStat = player.getMaxStat(0);
            int newStat = baseStat + Util.roundUp(player.getMaxStat(0) / 100D * attackBoost);
            if (newStat > player.getCurStat(0)) {
                player.setCurStat(0, newStat);
            }
        } else {
            int baseStat = player.getCurStat(0);
            int newStat = baseStat + Util.roundUp(player.getMaxStat(0) / 100D * attackBoost);
            if (newStat > player.getCurStat(0)) {
                player.setCurStat(0, newStat);
            }
        }
        if (player.getCurStat(2) > player.getMaxStat(2)) {
            int baseStat = player.getMaxStat(2);
            int newStat = baseStat + Util.roundUp(player.getMaxStat(2) / 100D * strengthBoost);
            if (newStat > player.getCurStat(2)) {
                player.setCurStat(2, newStat);
            }
        } else {
            int baseStat = player.getCurStat(2);
            int newStat = baseStat + Util.roundUp(player.getMaxStat(2) / 100D * strengthBoost);
            if (newStat > player.getCurStat(2)) {
                player.setCurStat(2, newStat);
            }
        }
        if (player.getCurStat(1) < player.getMaxStat(1)) {
            int baseStat = player.getMaxStat(1);
            int newStat = baseStat - Util.roundUp(player.getMaxStat(1) / 100D * defenceDecrease);
            if (newStat < player.getCurStat(1)) {
                player.setCurStat(1, newStat);
            }
        } else {
            int baseStat = player.getCurStat(1);
            int newStat = baseStat - Util.roundUp(player.getMaxStat(1) / 100D * defenceDecrease);
            if (newStat < player.getCurStat(1)) {
                player.setCurStat(1, newStat);
            }
        }
        if (player.getCurStat(3) < player.getMaxStat(3)) {
            int baseStat = player.getMaxStat(3);
            int newStat = baseStat - Util.roundUp(player.getMaxStat(3) / 100D * hitsDecrease);
            if (newStat < player.getCurStat(3)) {
                player.setCurStat(3, newStat);
            }
        } else {
            int baseStat = player.getCurStat(3);
            int newStat = baseStat - Util.roundUp(player.getMaxStat(3) / 100D * hitsDecrease);
            if (newStat < player.getCurStat(3)) {
                player.setCurStat(3, newStat);
            }
        }
        sleep(1200);
        if(left <= 0) {
            player.message("You have finished your potion");
        } else {
            player.message("You have " + left + " dose" + (left == 1 ? "" : "s") + " of potion left");
        }
    }

    private void usePrayerPotion(Player player, final InvItem item, final int newItem, final int left) {
        player.message("You drink some of your " + item.getDef().getName().toLowerCase());
        player.getInventory().remove(item);
        player.getInventory().add(new InvItem(newItem));
        int newPrayer = player.getCurStat(5) + (int) ((player.getMaxStat(5) * 0.25) + 7);
        if (newPrayer > player.getMaxStat(5)) {
            newPrayer = player.getMaxStat(5);
        }
        player.setCurStat(5, newPrayer);
        sleep(1200);
        if(left <= 0) {
            player.message("You have finished your potion");
        } else {
            player.message("You have " + left + " dose" + (left == 1 ? "" : "s") + " of potion left");
        }
    }

    private void useStatRestorePotion(Player player, final InvItem item, final int newItem, final int left) {
        player.message("You drink some of your " + item.getDef().getName().toLowerCase());
        player.getInventory().remove(item);
        player.getInventory().add(new InvItem(newItem));
        for (int i = 0; i < 7; i++) {
            if (i == 3 || i == 5) {
                continue;
            }
            if(player.getCurStat(i) > player.getMaxStat(i)) {
                continue;
            }
            int newStat = player.getCurStat(i) + (int) ((player.getMaxStat(i) * 0.3) + 10);
            if (newStat > player.getMaxStat(i)) {
                newStat = player.getMaxStat(i);
            }
            if(newStat < 14) {
                player.setCurStat(i, player.getMaxStat(i));
            } else {
                player.setCurStat(i, newStat);
            }
        }
        sleep(1200);
        if(left <= 0) {
            player.message("You have finished your potion");
        } else {
            player.message("You have " + left + " dose" + (left == 1 ? "" : "s") + " of potion left");
        }
    }
}
