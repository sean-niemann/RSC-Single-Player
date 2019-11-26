package org.nemotech.rsc.model.player;

import java.io.Serializable;

import org.nemotech.rsc.client.mudclient;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.model.Point;
import org.nemotech.rsc.util.Formulae;

public class SaveFile implements Serializable {

    private static final long serialVersionUID = -6935599993027150465L;
    
    public SaveFile(boolean register) {
        if(register) setDefaultValues();
    }
    
    private void setDefaultValues() {
        /* default values called upon registration */
        expStats = new int[18];
        curStats = new int[18];
        expStats[3] = 1154; // set the hitpoints experience to that of level 10
        for(int i = 0; i < 18; i++) {
            if(i == 3) {
                curStats[i] = 10; // set the current hitpoints level to 10
                continue;
            }
            curStats[i] = 1;
        }
        x = 217; y = 744; // starting player coordinates
        quests = new int[50];
        appearance = new int[6];
        gameSettings = new boolean[4];
        male = true;
        applicationWidth = Constants.APPLICATION_WIDTH;
        applicationHeight = Constants.APPLICATION_HEIGHT;
    }
    
    public String password = "deprecated";
    public boolean admin;
    public int x, y;
    public int fatigue;
    public long lastLogin;
    public int combatStyle;
    public boolean male;
    public int questPoints;
    public int applicationWidth;
    public int applicationHeight;
    
    public int[] expStats;
    public int[] curStats;
    public int[] quests;
    public int[] appearance;
    public boolean[] gameSettings;
    
    public int inventoryCount;
    public int[] inventoryItems;
    public int[] inventoryAmounts;
    public boolean[] inventoryWielded;
    
    public int bankCount;
    public int[] bankItems;
    public int[] bankAmounts;
    
    public void load(Player player) {
        player.setAdmin(player.getUsername().equalsIgnoreCase("root") || player.getUsername().equalsIgnoreCase("zoso"));
        player.setLocation(new Point(x, y), true);
        player.setFatigue(fatigue);
        player.setLastLogin(lastLogin);
        player.setCombatStyle(combatStyle);
        player.setGameSettings(gameSettings);
        player.setAppearanceData(appearance);
        player.setMale(male);
        player.setWornItems(player.getAppearance().getSprites());
        for(int i = 0; i < 18; i++) {
            player.setExp(i, expStats[i]);
            player.setMaxStat(i, Formulae.experienceToLevel(expStats[i]));
            player.setCurStat(i, curStats[i]);
        }
        player.setCombatLevel(Formulae.getCombatlevel(player.getMaxStats()));
        for(int i = 0; i < 50; i++) {
            player.updateQuestStage(i, quests[i]);
        }
        player.setQuestPoints(questPoints);
        Inventory inventory = new Inventory(player);
        for(int i = 0; i < inventoryCount; i++) {
            int id = inventoryItems[i];
            int amount = inventoryAmounts[i];
            boolean wear = inventoryWielded[i];
            InvItem item = new InvItem(id, amount);
            if(wear && item.getDef().isWieldable()) {
                item.setWield(true);
                player.updateWornItems(item.getWieldableDef().getWieldPos(), item.getWieldableDef().getSprite());
            }
            inventory.add(item);
        }
        player.setInventory(inventory);
        Bank bank = new Bank();
        for(int i = 0; i < bankCount; i++) {
            int id = bankItems[i];
            int amount = bankAmounts[i];
            InvItem item = new InvItem(id, amount);
            bank.add(item);
        }
        player.setBank(bank);
    }
    
    public void save(Player player) {
        admin = player.isAdmin();
        x = player.getLocation().getX();
        y = player.getLocation().getY();
        fatigue = player.getFatigue();
        lastLogin = player.getLastLogin();
        combatStyle = player.getCombatStyle();
        gameSettings = player.getGameSettings();
        appearance = player.getAppearanceData();
        male = player.isMale();
        for(int i = 0; i < 18; i++) {
            expStats[i] = player.getExp(i);
            curStats[i] = player.getCurStat(i);
        }
        for(int i = 0; i < 50; i++) {
            quests[i] = player.getQuestStage(i);
        }
        questPoints = player.getQuestPoints();
        Inventory inventory = player.getInventory();
        inventoryCount = inventory.size();
        inventoryItems = new int[inventoryCount];
        inventoryAmounts = new int[inventoryCount];
        inventoryWielded = new boolean[inventoryCount];
        for(int i = 0; i < inventoryCount; i++) {
            inventoryItems[i] = inventory.get(i).getID();
            inventoryAmounts[i] = inventory.get(i).getAmount();
            inventoryWielded[i] = inventory.get(i).isWielded();
        }
        Bank bank = player.getBank();
        bankCount = bank.size();
        bankItems = new int[bankCount];
        bankAmounts = new int[bankCount];
        for(int i = 0; i < bankCount; i++) {
            bankItems[i] = bank.get(i).getID();
            bankAmounts[i] = bank.get(i).getAmount();
        }
        applicationWidth = mudclient.getInstance().getGameWidth();
        applicationHeight = mudclient.getInstance().getGameHeight();
    }
    
}