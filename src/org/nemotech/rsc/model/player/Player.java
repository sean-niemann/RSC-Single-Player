package org.nemotech.rsc.model.player;

import java.awt.Dimension;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.external.definition.PrayerDef;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.util.Formulae;
import org.nemotech.rsc.util.StatefulEntityCollection;
import org.nemotech.rsc.event.DelayedEvent;
import org.nemotech.rsc.event.MiniEvent;
import org.nemotech.rsc.event.impl.RangeEvent;
import org.nemotech.rsc.event.ShortEvent;
import org.nemotech.rsc.model.ChatMessage;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.MenuHandler;
import org.nemotech.rsc.model.Mob;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.landscape.Path;
import org.nemotech.rsc.model.Point;
import org.nemotech.rsc.model.Projectile;
import org.nemotech.rsc.model.player.states.Action;
import org.nemotech.rsc.model.player.states.CombatState;

import org.nemotech.rsc.client.update.impl.MiscUpdater;

import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import org.nemotech.rsc.client.mudclient;
import org.nemotech.rsc.event.impl.BatchEvent;
import org.nemotech.rsc.client.sound.SoundEffect;
import org.nemotech.rsc.model.Shop;
import org.nemotech.rsc.plugins.QuestInterface;
import org.nemotech.rsc.plugins.menu.Menu;

public final class Player extends Mob {
    
    private DelayedEvent healUpdate = null;
    
    public void activateQuickHealUpdater(boolean quick) {   
        if(World.getWorld().getDelayedEventHandler().contains(healUpdate)) {
            World.getWorld().getDelayedEventHandler().remove(healUpdate);
        }
        
        healUpdate = new DelayedEvent(this, quick ? 20000 : 60000) {

            private void checkStat(int statIndex) {
                if (statIndex != 3 && owner.getCurStat(statIndex) == owner.getMaxStat(statIndex)) {
                    owner.getSender().sendMessage("Your " + Formulae.STAT_NAMES[statIndex] + " ability has returned to normal.");
                }
            }

            @Override
            public void run() {
                for (int statIndex = 0; statIndex < 7; statIndex++) {
                    if (statIndex == 5) {
                        continue;
                    }
                    int curStat = getCurStat(statIndex);
                    int maxStat = getMaxStat(statIndex);

                    if (curStat > maxStat) {
                        setCurStat(statIndex, curStat - 1);
                        getSender().sendStat(statIndex);
                        checkStat(statIndex);
                    } else if (curStat < maxStat) {
                        setCurStat(statIndex, curStat + 1);
                        getSender().sendStat(statIndex);
                        checkStat(statIndex);
                    }
                }
            }
        };
        World.getWorld().getDelayedEventHandler().add(healUpdate);
    }
    
    private BatchEvent batchEvent = null;
    
    public void checkAndInterruptBatchEvent() {
        if (batchEvent != null) {
            batchEvent.interrupt();
            batchEvent = null;
        }
    }
    
    /*private FireCannonEvent cannonEvent = null;

    public void resetCannonEvent() {
        if (cannonEvent != null) {
            cannonEvent.stop();
        }
        cannonEvent = null;
    }

    public boolean isCannonEventActive() {
        return cannonEvent != null;
    }

    public void setCannonEvent(FireCannonEvent event) {
        cannonEvent = event;
    }*/
    
    /**
     * The current active menu
     */
    private Menu menu;
    
    public Menu getMenu() {
        return menu;
    }
    
    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    
    public void setAccessingShop(Shop shop) {
        // todo
    }
    
    private long consumeTimer = 0;

    public boolean cantConsume() {
        return consumeTimer - System.currentTimeMillis() > 0;
    }

    public void setConsumeTimer(long l) {
        consumeTimer = System.currentTimeMillis() + l;
    }
    
    public void damage(int amount) {
        setLastDamage(amount);
        setCurStat(3, getCurStat(3) - amount);
        getSender().sendStat(3);
        informOfModifiedHits(this);
        if(getCurStat(3) <= 0) {
            killedBy(null);
        }
    }
    
    // START LEGACY
    
    private Cache cache = new Cache();
    
    public Cache getCache() {
        return cache;
    }
    
    public void message(String message) {
        getSender().sendMessage("@whi@" + message + "@que@");
    }
    
    public void setBatchEvent(BatchEvent batchEvent) {
        if (batchEvent != null) {
            this.batchEvent = batchEvent;
            World.getWorld().getDelayedEventHandler().add(batchEvent);
        }
    }
    
    // END LEGACY
    
    public int planksNailed = 0;
    public int bonesGiven = 0;
    
    private int guthixCasts = 0;
    private int saradominCasts = 0;
    private int zamorakCasts = 0;
    
    public int getGuthixCasts() {
        return guthixCasts;
    }
    
    public int getSaradominCasts() {
        return saradominCasts;
    }
    
    public int getZamorakCasts() {
        return zamorakCasts;
    }
    
    public void incGuthixCasts() {
        guthixCasts++;
    }
    
    public void incSaradominCasts() {
        saradominCasts++;
    }
    
    public void incZamorakCasts() {
        zamorakCasts++;
    }
    
    public int click = -1;
    
    public long lastCast = 0L;
    
    private MiscUpdater miscUpdater;
    
    public MiscUpdater getSender() {
        if(miscUpdater == null) {
            miscUpdater = new MiscUpdater();
        }
        return miscUpdater;
    }
    
    public NPC lastNpcChasingYou = null;
    
    public NPC getLastNpcChasingYou() {
        return lastNpcChasingYou;
    }

    public void setLastNpcChasingYou(NPC lastNpcChasingYou) {
        this.lastNpcChasingYou = lastNpcChasingYou;
    }
    
    private long lastRun = System.currentTimeMillis();
    
    public long getLastRun() {
        return lastRun;
    }

    public void setLastRun(long lastRun) {
        this.lastRun = lastRun;
    }
    
    public int dropTickCount = -1;

    private final Map<Integer, Integer> questStages = new ConcurrentHashMap<>();

    public int getQuestStage(QuestInterface q) {
        return getQuestStage(q.getQuestID());
    }
    
    public int getQuestStage(int id) {
        if(questStages.containsKey(id)) {
            return questStages.get(id);
        }
        return 0;
    }

    public void updateQuestStage(QuestInterface q, int stage) {
        questStages.put(q.getQuestID(), stage);
        getSender().sendQuestInfo(q.getQuestID(), stage);
    }

    public void updateQuestStage(int q, int stage) {
        questStages.put(q, stage);
        getSender().sendQuestInfo(q, stage);
    }

    public void sendQuestComplete(int questId) {
        world.getQuest(questId).handleReward(this);
        updateQuestStage(questId, -1);
        getSender().sendQuestPoints();
    }

    public void sendQuestComplete(QuestInterface quest) {
        Player.this.sendQuestComplete(quest.getQuestID());
    }

    private int questPoints = 0;

    public void incQuestPoints(int amount) {
        questPoints += amount;
    }
    
    public void setQuestPoints(int amount) {
        questPoints = amount;
    }

    public int getQuestPoints() {
        return questPoints;
    }

    public long lastNPCChat = System.currentTimeMillis();

    private int loops = 0;

    public void setSkillLoops(int i) {
        loops = i;

    }

    public int getSkillLoops() {
        return loops;
    }

    public int lastOption = -2;

    private DelayedEvent sleepEvent;

    public void startSleepEvent(final boolean bed) {
        sleepEvent = new DelayedEvent(this, 600) {
            @Override
            public void run() {
                if (tempFatigue == 0 || !sleeping) {
                    running = false;
                    return;
                }

                if (bed) {
                    owner.tempFatigue -= 2100; // todo
                } else {
                    owner.tempFatigue -= 431; // todo
                }

                if (owner.tempFatigue < 0) {
                    owner.tempFatigue = 0;
                }

                owner.getSender().sendTempFatigue(owner.tempFatigue / 10); // todo
            }
        };

        tempFatigue = fatigue;
        getSender().sendFatigue(0);// todo: (tempFatigue / 10);
        World.getWorld().getDelayedEventHandler().add(sleepEvent);
    }
    
    public void handleWakeup() {
        fatigue = tempFatigue;
    }
    
    private boolean sleeping;

    public boolean isSleeping() {
        return sleeping;
    }

    public void setSleeping(boolean isSleeping) {
        this.sleeping = isSleeping;
    }

    private String sleepword;

    public String getSleepword() {
        return sleepword;
    }

    public void setSleepword(String sleepword) {
        this.sleepword = sleepword;
    }
    
    private int pvpSettingChangesLeft = 3;
    
    public int getPvpSettingChangesLeft() {
        return pvpSettingChangesLeft;
    }

    public void setPvpSettingChangesLeft(int pvpSettingChangesLeft) {
        this.pvpSettingChangesLeft = pvpSettingChangesLeft;
    }

    private ShortEvent sEvent = null;
    public void setSEvent(ShortEvent sEvent) {
        this.sEvent = sEvent;
        world.getDelayedEventHandler().add(sEvent);
    }

    /**
     * The player's username
     */
    private String username;
    /**
     * The player's username hash
     */
    private long usernameHash;
    /**
     * The player's password
     */
    private String password;
    /**
     * The main accounts group is
     */
    private int groupID = 4;
    /**
     * Whether the player is currently logged in
     */
    private boolean loggedIn = false;
    /**
     * Last time a 'ping' was received
     */
    private long lastPing = System.currentTimeMillis();
    public boolean admin;
    /**
     * The Players appearance
     */
    private Appearance appearance;
    /**
     * The items being worn by the player
     */
    private int[] wornItems = new int[12];
    /**
     * The current stat array
     */
    private int[] curStat = new int[18];
    /**
     * The max stat array
     */
    private int[] maxStat = new int[18];
    /**
     * The exp level array
     */
    private int[] exp = new int[18];
    /**
     * If the player has been sending suscicious packets
     */
    private boolean suspicious = false;
    /**
     * List of players this player 'knows' (recieved from the client) about
     */
    private HashMap<Integer, Integer> knownPlayersAppearanceIDs = new HashMap<>();
    /**
     * Nearby players that we should be aware of
     */
    private StatefulEntityCollection<Player> watchedPlayers = new StatefulEntityCollection<>();
    /**
     * Nearby game objects that we should be aware of
     */
    private StatefulEntityCollection<GameObject> watchedObjects = new StatefulEntityCollection<>();
    /**
     * Nearby items that we should be aware of
     */
    private StatefulEntityCollection<Item> watchedItems = new StatefulEntityCollection<>();
    /**
     * Nearby npcs that we should be aware of
     */
    private StatefulEntityCollection<NPC> watchedNpcs = new StatefulEntityCollection<>();
    /**
     * Inventory to hold items
     */
    private Inventory inventory;
    /**
     * Bank for banked items
     */
    private Bank bank;
    /**
     * Users game settings, camera rotation preference etc
     */
    private boolean[] gameSettings = new boolean[4];
    /**
     * Unix time when the player last logged in
     */
    private long lastLogin = 0;
    /**
     * Unix time when the player logged in
     */
    private long currentLogin = 0;
    /**
     * Stores the last IP address used
     */
    private String lastIP = "0.0.0.0";
    /**
     * Stores the current IP address used
     */
    private String currentIP = "0.0.0.0";
    /**
     * If the player is reconnecting after connection loss
     */
    private boolean reconnecting = false;
    /**
     * Controls if were allowed to accept appearance updates
     */
    private boolean changingAppearance = false;
    /**
     * Is the character male?
     */
    private boolean maleGender;
    /**
     * List of all projectiles needing displayed
     */
    private ArrayList<Projectile> projectilesNeedingDisplayed = new ArrayList<>();
    /**
     * List of players who have been hit
     */
    private ArrayList<Player> playersNeedingHitsUpdate = new ArrayList<>();
    /**
     * List of players who have been hit
     */
    private ArrayList<NPC> npcsNeedingHitsUpdate = new ArrayList<>();
    /**
     * Chat messages needing displayed
     */
    private ArrayList<ChatMessage> chatMessagesNeedingDisplayed = new ArrayList<>();
    /**
     * NPC messages needing displayed
     */
    private ArrayList<ChatMessage> npcMessagesNeedingDisplayed = new ArrayList<>();
    /**
     * The time of the last spell cast, used as a throttle
     */
    private long lastSpellCast = 0;
    /**
     * Players we have been attacked by signed login, used to check if we should get a skull for attacking back
     */
    private HashMap<Long, Long> attackedBy = new HashMap<>();
    /**
     * Time last report was sent, used to throttle reports
     */
    private long lastReport = 0;
    /**
     * Time of last charge spell
     */
    private long lastCharge = 0;
    /**
     * Combat style: 0 - all, 1 - str, 2 - att, 3 - def
     */
    private int combatStyle = 0;
    /**
     * Should we destroy this player?
     */
    private boolean destroy = false;
    /**
     * Session keys for the players session
     */
    private int[] sessionKeys = new int[4];
    /**
     * Is the player accessing their bank?
     */
    private boolean inBank = false;
    /**
     * A handler for any menu we are currently in
     */
    private MenuHandler menuHandler = null;
    /**
     * DelayedEvent responsible for handling prayer drains
     */
    private DelayedEvent drainer;
    /**
     * The drain rate of the prayers currently enabled
     */
    private int drainRate = 0;
    /**
     * DelayedEvent used for removing players skull after 20mins
     */
    private DelayedEvent skullEvent = null;
    /**
     * Amount of fatigue - 0 to 100
     */
    private int fatigue = 7500, tempFatigue = 7500;
    /**
     * Has the player been registered into the world?
     */
    private boolean initialized = false;
    /**
     * The npc we are currently interacting with
     */
    private NPC interactingNpc = null;
    /**
     * The ID of the owning account
     */
    private int owner = 1;
    /**
     * When the users subscription expires (or 0 if they don't have one)
     */
    private long subscriptionExpires = 0;
    /**
     * Who we are currently following (if anyone)
     */
    private Mob following;
    /**
     * Event to handle following
     */
    private DelayedEvent followEvent;
    /**
     * Ranging event
     */
    private RangeEvent rangeEvent;
    /**
     * Last arrow fired
     */
    private long lastArrow = 0;
    /**
     * List of chat messages to send
     */
    private LinkedList<ChatMessage> chatQueue = new LinkedList<ChatMessage>();
    /**
     * The current status of the player
     */
    private Action status = Action.IDLE;

    public void setStatus(Action a) {
        status = a;
    }

    public Action getStatus() {
        return status;
    }

    public ChatMessage getNextChatMessage() {
        return chatQueue.poll();
    }

    public void setArrowFired() {
        lastArrow = System.currentTimeMillis();
    }

    public void setRangeEvent(RangeEvent event) {
        if(isRanging()) {
            resetRange();
        }
        rangeEvent = event;
        rangeEvent.setLastRun(lastArrow);
        world.getDelayedEventHandler().add(rangeEvent);
    }

    public boolean isRanging() {
        return rangeEvent != null;
    }

    public void resetRange() {
        if(rangeEvent != null) {
            rangeEvent.interrupt();
            rangeEvent = null;
        }
        setStatus(Action.IDLE);
    }

    public boolean canLogout() {
        return !isBusy() && System.currentTimeMillis() - getCombatTimer() > 10000;
    }

    public boolean isFollowing() {
        return followEvent != null && following != null;
    }

    public boolean isFollowing(Mob mob) {
        return isFollowing() && mob.equals(following);
    }

    public void setFollowing(Mob mob) {
        setFollowing(mob, 0);
    }

    public void setFollowing(final Mob mob, final int radius) {
        if(isFollowing()) {
            resetFollowing();
        }
        following = mob;
        followEvent = new DelayedEvent(this, 500) {
            public void run() {
                if(!owner.withinRange(mob) || mob.isRemoved() || owner.isBusy()) {
                    resetFollowing();
                }
                else if(!owner.finishedPath() && owner.withinRange(mob, radius)) {
                    owner.resetPath();
                }
                else if(owner.finishedPath() && !owner.withinRange(mob, radius + 1)) {
                    owner.setPath(new Path(owner.getX(), owner.getY(), mob.getX(), mob.getY()));
                }
            }
        };
        world.getDelayedEventHandler().add(followEvent);
    }

    public void resetFollowing() {
        following = null;
        if(followEvent != null) {
            followEvent.interrupt();
            followEvent = null;
        }
        resetPath();
    }

    public void setSkulledOn(Player player) {
        player.addAttackedBy(this);
        if(System.currentTimeMillis() - lastAttackedBy(player) > 1200000) {
            addSkull(1200000);
        }
    }

    public void setSubscriptionExpires(long expires) {
        subscriptionExpires = expires;
    }

    public int getDaysSubscriptionLeft() {
        long now = (System.currentTimeMillis() / 1000);
        if(subscriptionExpires == 0 || now >= subscriptionExpires) {
            return 0;
        }
        return (int)((subscriptionExpires - now) / 86400);
    }

    public boolean isSuspicious() {
        return suspicious;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getOwner() {
        return owner;
    }

    public NPC getInteractingNpc() {
        return interactingNpc;
    }

    public void setInteractingNpc(NPC npc) {
        interactingNpc = npc;
    }

    @Override
    public void remove() {
        removed = true;
    }

    public boolean initialized() {
        return initialized;
    }

    public void setInitialized() {
        initialized = true;
    }

    public int getDrainRate() {
        return drainRate;
    }

    public void setDrainRate(int rate) {
        drainRate = rate;
    }

    public int getRangeEquip() {
        for(InvItem item : inventory.getItems()) {
            if(item.isWielded() && (Util.inArray(Formulae.BOWS, item.getID()) || Util.inArray(Formulae.CROSSBOWS, item.getID()))) {
                return item.getID();
            }
        }
        return -1;
    }

    public void reset() {
        //resetCannonEvent();
        if(getMenu() != null) {
            menu = null;
        }
        if(getMenuHandler() != null) {
            resetMenuHandler();
        }
        if(interactingNpc != null) {
            interactingNpc.unblock();
        }
        if(isFollowing()) {
            resetFollowing();
        }
        if(isRanging()) {
            resetRange();
        }
        setStatus(Action.IDLE);
    }

    public void setMenuHandler(MenuHandler menuHandler) {
        menuHandler.setOwner(this);
        this.menuHandler = menuHandler;
    }

    public void setQuestMenuHandler(MenuHandler menuHandler)  {
        this.menuHandler = menuHandler;
        menuHandler.setOwner(this);
        getSender().sendMenu(menuHandler.getOptions());
    }

    public void resetMenuHandler() {
        menu = null;
        menuHandler = null;
        getSender().hideMenu();
    }

    public MenuHandler getMenuHandler() {
        return menuHandler;
    }

    public Player() {
        currentIP = "localhost";
        currentLogin = System.currentTimeMillis();
        setBusy(true);
    }

    public void setServerKey(long key) {
        sessionKeys[2] = (int)(key >> 32);
        sessionKeys[3] = (int)key;
    }

    public boolean setSessionKeys(int[] keys) {
        boolean valid = (sessionKeys[2] == keys[2] && sessionKeys[3] == keys[3]);
        sessionKeys = keys;
        return valid;
    }
    //  save
    public boolean destroyed() {
        return destroy;
    }

    public void destroy(boolean force) {
        if(destroy) {
            return;
        }
        if(force || canLogout()) {
            destroy = true;
            getSender().sendLogout();
        } else {
            final long startDestroy = System.currentTimeMillis();
            world.getDelayedEventHandler().add(new DelayedEvent(this, 3000) {
                @Override
                public void run() {
                    if(owner.canLogout() || (!(owner.inCombat()) && System.currentTimeMillis() - startDestroy > 60000)) {
                        owner.destroy(true);
                        running = false;
                    }
                }
            });
        }
    }

    @Override
    public int getCombatStyle() {
        return combatStyle;
    }

    public void setCombatStyle(int style) {
        combatStyle = style;
    }
    
    public boolean muted;
    
    private SaveFile playerData = new SaveFile(false);
    
    public Dimension preferredDimension;

    public void load(String username) {
        usernameHash = Util.usernameToHash(username);
        this.username = Util.hashToUsername(usernameHash);
        
        healUpdate = new DelayedEvent(this, 60000) {
            @Override
            public void run() {
                for(int statIndex = 0;statIndex < 18;statIndex++) {
                    if(statIndex == 5) {
                        continue;
                    }
                    int curStat = getCurStat(statIndex);
                    int maxStat = getMaxStat(statIndex);
                    if(curStat > maxStat) {
                        setCurStat(statIndex, curStat - 1);
                        getSender().sendStat(statIndex);
                        checkStat(statIndex);
                    }
                    else if(curStat < maxStat) {
                        setCurStat(statIndex, curStat + 1);
                        getSender().sendStat(statIndex);
                        checkStat(statIndex);
                    }
                }
            }


            private void checkStat(int statIndex) {
                if(statIndex != 3 && owner.getCurStat(statIndex) == owner.getMaxStat(statIndex)) {
                    owner.getSender().sendMessage("Your " + Formulae.STAT_NAMES[statIndex] + " ability has returned to normal");
                }
            }           
        };
        
        World.getWorld().getDelayedEventHandler().add(healUpdate);
        
        drainer = new DelayedEvent(this, Integer.MAX_VALUE) {
            @Override
            public void run() {
                int curPrayer = getCurStat(5);
                if(getDrainRate() > 0 && curPrayer > 0) {
                    incCurStat(5, -1);
                    getSender().sendStat(5);
                    if(curPrayer <= 1) {
                        for(int prayerID = 0; prayerID < 14; prayerID++) { //Prayer was < 14
                            setPrayer(prayerID, false);
                        }
                        setDrainRate(0);
                        setDelay(Integer.MAX_VALUE);
                        getSender().sendMessage("You have run out of prayer points. Return to a church to recharge");
                        getSender().sendPrayers();
                    }
                }
            }
        };
        world.getDelayedEventHandler().add(drainer);
        
        try {
            // load player cache file
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Constants.CACHE_DIRECTORY + "players/" + username + "_cache.dat"));
            Cache playerCache = (Cache) ois.readObject();
            cache = playerCache;
            ois.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        try {
            // load player data file
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Constants.CACHE_DIRECTORY + "players/" + username + "_data.dat"));
            SaveFile data = (SaveFile) ois.readObject();
            playerData = data;
            ois.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
            
        // load player data
        playerData.load(this);
        
        preferredDimension = new Dimension(playerData.applicationWidth, playerData.applicationHeight);
            
        for(InvItem item : getInventory().getItems()) {
            if(item.isWielded() && item.getDef().isWieldable()) {
                item.setWield(true);
                updateWornItems(item.getWieldableDef().getWieldPos(), item.getWieldableDef().getSprite());
            }
        }

        /* End of loading methods */

        world.registerPlayer(this);

        updateViewedPlayers();
        updateViewedObjects();

        getSender().sendWorldInfo();
        getSender().sendInventory();
        getSender().sendEquipmentStats();
        getSender().sendStats();
        getSender().sendGameSettings();
        getSender().sendCombatStyle();
        getSender().sendFatigue(fatigue);
        
        getSender().sendMessage("Welcome to RuneScape");
        getSender().sendQuestInfo();
        getSender().sendQuestPoints();
        
        if(getLastLogin() == 0) {
            setChangingAppearance(true);
            getSender().sendAppearanceScreen();
        }
        setLastLogin(System.currentTimeMillis());
        getSender().sendLoginBox();

        setLoggedIn(true);
        setBusy(false);
    }

    public void save() {
        // save player data
        playerData.save(this);
        try {
            // save serialized player cache file
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Constants.CACHE_DIRECTORY + "players/" + username + "_cache.dat"));
            oos.writeObject(cache);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // save serialized player data file
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Constants.CACHE_DIRECTORY + "players/" + username + "_data.dat"));
            oos.writeObject(playerData);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCharged() {
        lastCharge = System.currentTimeMillis();
    }

    public boolean isCharged() {
        return System.currentTimeMillis() - lastCharge < 60000;
    }

    public boolean canReport() {
        return System.currentTimeMillis() - lastReport > 60000;
    }

    public void setLastReport() {
        lastReport = System.currentTimeMillis();
    }

    @Override
    public void killedBy(Mob mob) {
        Mob opponent = super.getOpponent();
        if(opponent != null) {
            opponent.resetCombat(CombatState.WON);
        }
        getSender().sendSound(SoundEffect.DEATH);
        //getSender().sendDied();
        for(int i = 0;i < 18;i++) {
            curStat[i] = maxStat[i];
            getSender().sendStat(i);
        }

        inventory.sort();
        ListIterator<InvItem> iterator = inventory.iterator();
        if(!isSkulled()) {
            for(int i = 0;i < 3 && iterator.hasNext();i++) {
                if((iterator.next()).getDef().isStackable()) {
                    iterator.previous();
                    break;
                }
            }
        }
        if(activatedPrayers[8] && iterator.hasNext()) {
            if(((InvItem)iterator.next()).getDef().isStackable()) {
                iterator.previous();
            }
        }
        for(int slot = 0;iterator.hasNext();slot++) {
            InvItem item = (InvItem)iterator.next();
            if(item.isWielded()) {
                item.setWield(false);
                updateWornItems(item.getWieldableDef().getWieldPos(), appearance.getSprite(item.getWieldableDef().getWieldPos()));
            }
            iterator.remove();
            world.registerItem(new Item(item.getID(), getX(), getY(), item.getAmount(), null));
        }
        removeSkull();
        world.registerItem(new Item(20, getX(), getY(), 1, null));

        for(int x = 0;x < activatedPrayers.length;x++) {
            if(activatedPrayers[x]) {
                removePrayerDrain(x);
                activatedPrayers[x] = false;
            }
        }
        getSender().sendPrayers();
        
        boolean inTutorial = getLocation().inTutorialLanding();
        Point spawn = inTutorial ? new Point(224, 732) : new Point(122, 647);
        setLocation(spawn, true);
        
        Collection<Player> allWatched = watchedPlayers.getAllEntities();
        for (Player p : allWatched) {
            p.removeWatchedPlayer(this);
        }

        resetPath();
        resetCombat(CombatState.LOST);
        getSender().sendWorldInfo();
        getSender().sendEquipmentStats();
        getSender().sendInventory();
    }

    public void addAttackedBy(Player p) {
        attackedBy.put(p.getUsernameHash(), System.currentTimeMillis());
    }

    public long lastAttackedBy(Player p) {
        Long time = attackedBy.get(p.getUsernameHash());
        if(time != null) {
            return time;
        }
        return 0;
    }

    public void setCastTimer() {
        lastSpellCast = System.currentTimeMillis();
    }

    public void setSpellFail() {
        lastSpellCast = System.currentTimeMillis() + 20000;
    }

    public int getSpellWait() {
        return Util.roundUp((double)(1200 - (System.currentTimeMillis() - lastSpellCast)) / 1000D);
    }

    public long getCastTimer() {
        return lastSpellCast;
    }

    public boolean castTimer() {
        return System.currentTimeMillis() - lastSpellCast > 1200;
    }

    public void informOfChatMessage(ChatMessage cm) {
        chatMessagesNeedingDisplayed.add(cm);
    }

    public void informOfNPCMessage(ChatMessage cm) {
        npcMessagesNeedingDisplayed.add(cm);
    }

    public List<ChatMessage> getNpcMessagesNeedingDisplayed() {
        return npcMessagesNeedingDisplayed;
    }

    public List<ChatMessage> getChatMessagesNeedingDisplayed() {
        return chatMessagesNeedingDisplayed;
    }

    public void clearNpcMessagesNeedingDisplayed() {
        npcMessagesNeedingDisplayed.clear();
    }

    public void clearChatMessagesNeedingDisplayed() {
        chatMessagesNeedingDisplayed.clear();
    }

    public void informOfModifiedHits(Mob mob) {
        if(mob instanceof Player) {
            playersNeedingHitsUpdate.add((Player)mob);
        }
        else if(mob instanceof NPC) {
            npcsNeedingHitsUpdate.add((NPC)mob);
        }
    }

    public List<Player> getPlayersRequiringHitsUpdate() {
        return playersNeedingHitsUpdate;
    }

    public List<NPC> getNpcsRequiringHitsUpdate() {
        return npcsNeedingHitsUpdate;
    }

    public void clearPlayersNeedingHitsUpdate() {
        playersNeedingHitsUpdate.clear();
    }

    public void clearNpcsNeedingHitsUpdate() {
        npcsNeedingHitsUpdate.clear();
    }

    public void informOfProjectile(Projectile p) {
        projectilesNeedingDisplayed.add(p);
    }

    public List<Projectile> getProjectilesNeedingDisplayed() {
        return projectilesNeedingDisplayed;
    }

    public void clearProjectilesNeedingDisplayed() {
        projectilesNeedingDisplayed.clear();
    }

    public void addPrayerDrain(int prayerID) {
        PrayerDef prayer = EntityManager.getPrayer(prayerID);
        drainRate += prayer.getDrainRate();
        drainer.setDelay((int)(240000 / drainRate));
    }

    public void removePrayerDrain(int prayerID) {
        PrayerDef prayer = EntityManager.getPrayer(prayerID);
        drainRate -= prayer.getDrainRate();
        if(drainRate <= 0) {
            drainRate = 0;
            drainer.setDelay(Integer.MAX_VALUE);
        } else {
            drainer.setDelay((int)(240000 / drainRate));
        }
    }

    public void setMale(boolean male) {
        maleGender = male;
    }

    public boolean isMale() {
        return maleGender;
    }

    public void setChangingAppearance(boolean b) {
        changingAppearance = b;
    }

    public boolean isChangingAppearance() {
        return changingAppearance;
    }

    public boolean isReconnecting() {
        return reconnecting;
    }

    public void setLastLogin(long l) {
        lastLogin = l;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public int getDaysSinceLastLogin() {
        return (int) ((lastLogin - System.currentTimeMillis()) / (1000 * 60 * 60 * 24));
    }

    public long getCurrentLogin() {
        return currentLogin;
    }

    public void setLastIP(String ip) {
        lastIP = ip;
    }

    public String getCurrentIP() {
        return currentIP;
    }

    public String getLastIP() {
        return lastIP;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    @Override
    public int getArmourPoints() {
        int points = 1;
        for(InvItem item : inventory.getItems()) {
            if(item.isWielded()) {
                points += item.getWieldableDef().getArmourPoints();
            }
        }
        return points < 1 ? 1 : points;
    }

    @Override
    public int getWeaponAimPoints() {
        int points = 1;
        for(InvItem item : inventory.getItems()) {
            if(item.isWielded()) {
                points += item.getWieldableDef().getWeaponAimPoints();
            }
        }
        return points < 1 ? 1 : points;
    }

    @Override
    public int getWeaponPowerPoints() {
        int points = 1;
        for(InvItem item : inventory.getItems()) {
            if(item.isWielded()) {
                points += item.getWieldableDef().getWeaponPowerPoints();
            }
        }
        return points < 1 ? 1 : points;
    }

    public int getMagicPoints() {
        int points = 1;
        for(InvItem item : inventory.getItems()) {
            if(item.isWielded()) {
                points += item.getWieldableDef().getMagicPoints();
            }
        }
        return points < 1 ? 1 : points;
    }

    public int getPrayerPoints() {
        int points = 1;
        for(InvItem item : inventory.getItems()) {
            if(item.isWielded()) {
                points += item.getWieldableDef().getPrayerPoints();
            }
        }
        return points < 1 ? 1 : points;
    }

    public int getRangePoints() {
        int points = 1;
        for(InvItem item : inventory.getItems()) {
            if(item.isWielded()) {
                points += item.getWieldableDef().getRangePoints();
            }
        }
        return points < 1 ? 1 : points;
    }

    public int[] getWornItems() {
        return wornItems;
    }

    public void updateWornItems(int index, int id) {
        wornItems[index] = id;
        System.arraycopy(wornItems, 0, mudclient.getInstance().localPlayer.equippedItem, 0, wornItems.length);
    }

    public void setWornItems(int[] worn) {
        wornItems = worn;
        super.ourAppearanceChanged = true;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory i) {
        inventory = i;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank b) {
        bank = b;
    }
    
    public void setGameSettings(boolean[] settings) {
        System.arraycopy(settings, 0, gameSettings, 0, settings.length);
    }

    public void setGameSetting(int i, boolean b) {
        gameSettings[i] = b;
    }

    public boolean getGameSetting(int i) {
        return gameSettings[i];
    }
    
    public boolean[] getGameSettings() {
        return new boolean[] { gameSettings[0], gameSettings[1], gameSettings[2], gameSettings[3] };
    }

    public long getLastPing() {
        return lastPing;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        if(loggedIn) {
            currentLogin = System.currentTimeMillis();
        }
        this.loggedIn = loggedIn;
    }

    public String getUsername() {
        return username;
    }

    public long getUsernameHash() {
        return usernameHash;
    }

    public String getPassword() {
        return password;
    }

    public void ping() {
        lastPing = System.currentTimeMillis();
    }

    public boolean isSkulled() {
        return skullEvent != null;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public void setAppearance(Appearance appearance) {
        this.appearance = appearance;
    }
    
    public void setAppearanceData(int[] data) {
        this.appearance = new Appearance(data[0], data[1], data[2], data[3], data[4], data[5]);
    }
    
    public int[] getAppearanceData() {
        return new int[] { appearance.getHairColor(), appearance.getShirtColor(), appearance.getPantsColor(), appearance.getSkinColor(), appearance.getHeadType(), appearance.getBodyType() };
    }

    public int getSkullTime() {
        if(isSkulled()) {
            return skullEvent.timeTillNextRun();
        }
        return 0;
    }
    //  destroy
    public void addSkull(long timeLeft) {
        if(!isSkulled()) {
            skullEvent = new DelayedEvent(this, 1200000) {
                public void run() {
                    removeSkull();
                }
            };
            world.getDelayedEventHandler().add(skullEvent);
            super.setAppearnceChanged(true);
        }
        skullEvent.setLastRun(System.currentTimeMillis() - (1200000 - timeLeft));
    }

    public void removeSkull() {
        if(!isSkulled()) {
            return;
        }
        super.setAppearnceChanged(true);
        skullEvent.interrupt();
        skullEvent = null;
    }

    public void setSuspiciousPlayer(boolean suspicious) {
        this.suspicious = suspicious;
    }

    public void addPlayersAppearanceIDs(int[] indicies, int[] appearanceIDs) {
        for (int x = 0; x < indicies.length; x++) {
            knownPlayersAppearanceIDs.put(indicies[x], appearanceIDs[x]);
        }
    }

    public List<Player> getPlayersRequiringAppearanceUpdate() {
        List<Player> needingUpdates = new ArrayList<Player>();
        needingUpdates.addAll(watchedPlayers.getNewEntities());
        if (super.ourAppearanceChanged) {
            needingUpdates.add(this);
        }
        for (Player p : watchedPlayers.getKnownEntities()) {
            if (needsAppearanceUpdateFor(p)) {
                needingUpdates.add(p);
            }
        }
        return needingUpdates;
    }

    private boolean needsAppearanceUpdateFor(Player p) {
        int playerServerIndex = p.getIndex();
        if (knownPlayersAppearanceIDs.containsKey(playerServerIndex)) {
            int knownPlayerAppearanceID = knownPlayersAppearanceIDs.get(playerServerIndex);
            if(knownPlayerAppearanceID != p.getAppearanceID()) {
                return true;
            }
        }
        else {
            return true;
        }
        return false;
    }

    public void updateViewedPlayers() {
        List<Player> playersInView = viewArea.getPlayersInView();
        for (Player p : playersInView) {
            if (p.getIndex() != getIndex() && p.isLoggedIn()) {
                this.informOfPlayer(p);
                p.informOfPlayer(this);             
            }
        }
    }

    public void updateViewedObjects() {
        List<GameObject> objectsInView = viewArea.getGameObjectsInView();
        for (GameObject o : objectsInView) {
            if (!watchedObjects.contains(o) && !o.isRemoved() && withinRange(o)) {
                watchedObjects.add(o);
            }
        }
    }

    public void updateViewedItems() {
        List<Item> itemsInView = viewArea.getItemsInView();
        for (Item i : itemsInView) {
            if (!watchedItems.contains(i) && !i.isRemoved() && withinRange(i) && i.visibleTo(this)) {
                watchedItems.add(i);
            }
        }
    }

    public void updateViewedNpcs() {
        List<NPC> npcsInView = viewArea.getNpcsInView();
        for (NPC n : npcsInView) {
            if ((!watchedNpcs.contains(n) || watchedNpcs.isRemoving(n)) && withinRange(n)) {
                watchedNpcs.add(n);
            }
        }
    }

    public void teleport(int x, int y) {
        teleport(x, y, false);
    }

    public void teleport(int x, int y, boolean bubble) {
        Mob opponent = super.getOpponent();
        if(inCombat()) {
            resetCombat(CombatState.ERROR);
        }
        if(opponent != null) {
            opponent.resetCombat(CombatState.ERROR);
        }
        if(bubble) {
            getSender().sendTeleBubble(getX(), getY(), false);
        }
        setLocation(new Point(x, y), true);
        resetPath();
        getSender().sendWorldInfo();
    }
    
    /**
     * This is a 'another player has tapped us on the shoulder' method.
     *
     * If we are in another players viewArea, they should in theory be in ours.
     * So they will call this method to let the player know they should probably
     * be informed of them.
     */
    public void informOfPlayer(Player p) {
        if ((!watchedPlayers.contains(p) || watchedPlayers.isRemoving(p)) && withinRange(p)) {
            watchedPlayers.add(p);
        }
    }

    public StatefulEntityCollection<Player> getWatchedPlayers() {
        return watchedPlayers;      
    }

    public StatefulEntityCollection<GameObject> getWatchedObjects() {
        return watchedObjects;      
    }

    public StatefulEntityCollection<Item> getWatchedItems() {
        return watchedItems;        
    }

    public StatefulEntityCollection<NPC> getWatchedNpcs() {
        return watchedNpcs;     
    }

    public void removeWatchedNpc(NPC n) {
        watchedNpcs.remove(n);
    }

    public void removeWatchedPlayer(Player p) {
        watchedPlayers.remove(p);
    }

    public void revalidateWatchedPlayers() {
        for (Player p : watchedPlayers.getKnownEntities()) {
            if (!withinRange(p) || !p.isLoggedIn()) {
                watchedPlayers.remove(p);
                knownPlayersAppearanceIDs.remove(p.getIndex());
            }
        }
    }

    public void revalidateWatchedObjects() {
        for (GameObject o : watchedObjects.getKnownEntities()) {
            if (!withinRange(o) || o.isRemoved()) {
                watchedObjects.remove(o);
            }
        }
    }

    public void revalidateWatchedItems() {
        for (Item i : watchedItems.getKnownEntities()) {
            if (!withinRange(i) || i.isRemoved() || !i.visibleTo(this)) {
                watchedItems.remove(i);
            }
        }
    }

    public void revalidateWatchedNpcs() {
        for (NPC n : watchedNpcs.getKnownEntities()) {
            if (!withinRange(n) || n.isRemoved()) {
                watchedNpcs.remove(n);
            }
        }
    }

    public int[] getCurStats() {
        return curStat;
    }

    public int getCurStat(int id) {
        return curStat[id];
    }

    @Override
    public int getHits() {
        return getCurStat(3);
    }

    @Override
    public int getAttack() {
        return getCurStat(0);
    }

    @Override
    public int getDefense() {
        return getCurStat(1);
    }

    @Override
    public int getStrength() {
        return getCurStat(2);
    }

    @Override
    public void setHits(int lvl) {
        setCurStat(3, lvl);
    }

    public void setCurStat(int id, int lvl) {
        if(lvl <= 0) {
            lvl = 0;
        }
        curStat[id] = lvl;
        if(this != null) {
            if(getSender() != null) {
                getSender().sendStat(id);
            }
        }
    }

    public int getMaxStat(int id) {
        return maxStat[id];
    }

    public void setMaxStat(int id, int lvl) {
        if(lvl < 0) {
            lvl = 0;
        }
        maxStat[id] = lvl;
    }

    public int[] getMaxStats() {
        return maxStat;
    }

    public int getSkillTotal() {
        int total = 0;
        for(int stat : maxStat) {
            total += stat;
        }
        return total;
    }

    public void incCurStat(int i, int amount) {
        curStat[i] += amount;
        if(curStat[i] < 0) {
            curStat[i] = 0;
        }
    }

    public void incMaxStat(int i, int amount) {
        maxStat[i] += amount;
        if(maxStat[i] < 0) {
            maxStat[i] = 0;
        }
    }

    public void setFatigue(int fatigue) {
        this.fatigue = fatigue;
    }

    public int getFatigue() {
        return fatigue;
    }
    
    public void incQuestExp(int i, double amount) {
        incExp(i, amount, false, false);
    }

    public void incExp(int i, double amount, boolean useFatigue) {
        incExp(i, amount, useFatigue, true);
    }

    public void incExp(int i, double amount, boolean useFatigue, boolean multiplied) {
        if(useFatigue) {
            if(fatigue >= 100) {
                getSender().sendMessage("@gre@You are too tired to gain experience, get some rest!");
                return;
            }
            if(fatigue >= 96) {
                getSender().sendMessage("@gre@You start to feel tired, maybe you should rest soon");
            }
            fatigue++;
            getSender().sendFatigue(fatigue);
        }
        if(multiplied)
            amount *= Constants.EXPERIENCE_MULTIPLIER;
        exp[i] += amount;
        if(exp[i] < 0) {
            exp[i] = 0;
        }
        int level = Formulae.experienceToLevel(exp[i]);
        if(level != maxStat[i]) {
            int advanced = level - maxStat[i];
            incCurStat(i, advanced);
            incMaxStat(i, advanced);
            getSender().sendStat(i);
            getSender().sendMessage("@gre@You just advanced " + advanced + " " + Formulae.STAT_NAMES[i] + " level" + (advanced == 1 ? "" : "s") + "!");
            getSender().sendSound(SoundEffect.ADVANCE);
            world.getDelayedEventHandler().add(new MiniEvent(this) {
                @Override
                public void action() {
                    owner.getSender().sendScreenshot();
                }
            });
            int comb = Formulae.getCombatlevel(maxStat);
            if(comb != getCombatLevel()) {
                setCombatLevel(comb);
            }
        }
        getSender().sendStat(i);
    }
    
    public int[] getExps() {
        return exp;
    }

    public int getExp(int id) {
        return exp[id];
    }

    public void setExp(int id, int lvl) {
        if(lvl < 0) {
            lvl = 0;
        }
        exp[id] = lvl;
    }

    public void setExp(int[] lvls) {
        exp = lvls;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Player) {
            Player p = (Player)o;
            return usernameHash == p.getUsernameHash();
        }
        return false;
    }

}