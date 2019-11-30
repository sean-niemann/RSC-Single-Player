package org.nemotech.rsc.client;

import org.nemotech.rsc.client.action.impl.*;
import org.nemotech.rsc.client.action.*;
import org.nemotech.rsc.core.ClientUpdater;
import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.util.Colors;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.client.sound.SoundEffect;
import org.nemotech.rsc.client.sound.MusicPlayer;
import org.nemotech.rsc.model.player.Player;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class mudclient extends Shell {
    
    public mudclient() {
        initVars();
    }
    
    public int getGameWidth() {
        return gameWidth;
    }
    
    public int getGameHeight() {
        return gameHeight;
    }
    
    public int getMouseX() {
        return mouseX;
    }
    
    public int getMouseY() {
        return mouseY;
    }
    
    public int getMouseClick() {
        return mouseButtonClick;
    }
    
    public void setMouseClick(int i) {
        mouseButtonClick = i;
    }
    
    public int getMouseButtonDown() {
        return mouseButtonDown;
    }
    
    public int getLastMouseDown() {
        return lastMouseButtonDown;
    }
    
    public void resizeGame(int width, int height) {
        gameWidth = width;
        gameHeight = height - 12;

        // set raster
        surface.pixels = new int[width * height];
        surface.boundsBottomX = surface.width2 = width;
        surface.boundsBottomY = surface.height2 = height;
        surface.imageConsumer.setDimensions(width, height);

        // set scene
        scene.raster = surface.pixels;
        scene.clipX = width;
        scene.clipY = height;
        scene.setBounds(gameWidth / 2, gameHeight / 2, gameWidth / 2, gameHeight / 2, gameWidth, const_9);
        
        createMessageTabPanel();
        createAppearancePanel();
        
        panelMagic = new Menu(surface, 5);
        int x = surface.width2 - 199;
        byte y = 36;
        controlListMagic = panelMagic.addTextListInteractive(x, y + 24, 196, 90, 1, 500, true);
        panelMusic = new Menu(surface, 5);
        controlListMusic = panelMusic.addTextListInteractive(x, y + 24, 196, 166 - 24, 1, 2000, true);
        panelQuestList = new Menu(surface, 5);
        controlListQuest = panelQuestList.addTextListInteractive(x, y + 24, 196, 251, 1, 500, true);

        graphics = getGraphics();
        //repaint();
    }
    
    // END RESIZE

    public static mudclient INSTANCE;

    public static mudclient getInstance() {
        return INSTANCE;
    }

    public String username;

    public void showAlert(String message, boolean big) {
        serverMessage = message;
        showDialogServerMessage = true;
        serverMessageBoxBig = big;
    }
    
    private int cameraPitch = 912;
    private boolean serverMessageBoxBig;
    private boolean errorLoadingData;
    public String shopName = "";
    public TreeMap<Integer, String> newQuestNames = new TreeMap<>();
    public Map<Integer, Integer> questStage = new HashMap<>();
    private final int menuMaxSize = 250;
    private final int pathStepsMax = 8000;
    private final int playersMax = 500;
    private final int npcsMax = 1000;
    private final int wallObjectsMax = 500;
    private final int playersServerMax = 4000;
    private final int groundItemsMax = 5000;
    private final int npcsServerMax = 5000;
    private final int objectsMax = 1500;
    private final int playerStatCount = 18;
    private final int playerStatEquipmentCount = 5;
    public int localRegionX;
    public int localRegionY;
    private int controlTextListChat;
    private int controlTextListAll;
    private int controlTextListQuest;
    private int controlTextListPrivate;
    private int messageTabSelected;
    private String receivedMessages[];
    private int mouseClickXX;
    private int mouseClickXY;
    private int controlListMusic;
    private String selectedItemName;
    private int controlListQuest;
    private int uiTabPlayerInfoSubTab;
    
    @Override
    public void handleMouseScroll(int scroll) {
        if (scroll > 1) {
            scroll += scroll;
        } else if (scroll < -1) {
            scroll -= -scroll;
        }
        if (loginScreen == 1) {
            panelRegisterUser.handleScroll(controlRegisterTerms, scroll);
        } else if (showUiTab == 5) {
            panelMusic.handleScroll(controlListMusic, scroll);
        } else if (showUiTab == 4) {
            panelMagic.handleScroll(controlListMagic, scroll);
        } else if (showUiTab == 3) {
            panelQuestList.handleScroll(controlListQuest, scroll);
        } else if (messageTabSelected == 1) {
            panelMessageTabs.handleScroll(controlTextListChat, scroll);
        } else if (messageTabSelected == 2) {
            panelMessageTabs.handleScroll(controlTextListQuest, scroll);
        } else if (messageTabSelected == 3) {
            panelMessageTabs.handleScroll(controlTextListPrivate, scroll);
        }
    }
    
    public Surface getSurface() {
        return surface;
    }

    int controlListMagic;
    int tabMagicPrayer;
    private int menuIndices[];
    private boolean cameraAutoAngleDebug;
    public int wallObjectDirection[];
    public int wallObjectId[];
    private int anInt659;
    private int animIndex;
    private int cameraRotationX;
    private int cameraRotationXIncrement;
    public Scene scene;
    private int inventoryMaxItemCount;
    public int bankItemsMax;
    public String optionMenuEntry[];
    public int newBankItems[];
    public int newBankItemsCount[];
    private int loginScreen;
    public int teleportBubbleTime[];
    private int receivedMessageX[];
    private int receivedMessageY[];
    private int receivedMessageMidPoint[];
    private int receivedMessageHeight[];
    public Mob localPlayer;
    public Screen surface;
    private Menu panelMessageTabs;
    private int menuItemX[];
    private int menuItemY[];
    private int bankItems[];
    private int bankItemsCount[];
    private int appearanceHeadType;
    private int appearanceBodyGender;
    private int appearance2Colour;
    private int appearanceHairColour;
    private int appearanceTopColour;
    private int appearanceBottomColour;
    private int appearanceSkinColour;
    private int appearanceHeadGender;
    private String loginUser;
    private int cameraAngle;
    private int anInt707;
    public int deathScreenTimeout;
    public boolean optionSoundDisabled;
    public boolean optionMusicLoop;
    public boolean optionMusicAuto;
    private boolean showRightClickMenu;
    private int cameraRotationY;
    private int cameraRotationYIncrement;
    private boolean objectAlreadyInMenu[];
    public int combatStyle;
    private String menuItemText1[];
    private int welcomeUnreadMessages;
    private int controlButtonAppearanceHead1;
    private int controlButtonAppearanceHead2;
    private int controlButtonAppearanceHair1;
    private int controlButtonAppearanceHair2;
    private int controlButtonAppearanceGender1;
    private int controlButtonAppearanceGender2;
    private int controlButtonAppearanceTop1;
    private int controlButtonAppearanceTop2;
    private int controlButtonAppearanceSkin1;
    private int controlButtonAppearanceSkin2;
    private int controlButtonAppearanceBottom1;
    private int controlButtonAppearanceBottom2;
    private int controlButtonAppearanceAccept;
    public int logoutTimeout;
    private int loginTimer;
    private int npcCombatModelArray2[] = {
        0, 0, 0, 0, 0, 1, 2, 1
    };
    private int lastObjectAnimationNumberFireLightningSpell;
    private int lastObjectAnimationNumberTorch;
    private int lastOjectAnimationNumberClaw;
    private Graphics graphics;
    public int regionX;
    public int regionY;
    public int planeIndex;
    public boolean welcomScreenAlreadyShown;
    private int mouseButtonClick;
    public boolean isSleeping;
    private int cameraRotation;
    public int teleportBubbleX[];
    public int playerExperience[];
    private int healthBarCount;
    public int spriteMedia;
    private int spriteUtil;
    public int spriteItem;
    private int spriteProjectile;
    public int spriteTexture;
    private int spriteTextureWorld;
    private int spriteLogo;
    private int controlLoginStatus1, controlLoginStatus2;
    private int controlLoginUser;
    private int controlLoginOk;
    private int controlLoginCancel;
    public int teleportBubbleCount;
    public boolean showDialogWelcome;
    public int groundItemCount;
    public int teleportBubbleY[];
    private int receivedMessagesCount;
    private int messageTabFlashAll;
    private int messageTabFlashHistory;
    private int messageTabFlashQuest;
    private int messageTabFlashPrivate;
    public Mob players[];
    private int bankItemCount;
    public boolean prayerOn[];
    private int menuSourceType[];
    private int menuSourceIndex[];
    private int menuTargetIndex[];
    private boolean wallObjectAlreadyInMenu[];
    private int objectAnimationNumberFireLightningSpell;
    private int objectAnimationNumberTorch;
    private int objectAnimationNumberClaw;
    public int magicLoc;
    public int loggedIn;
    public int npcCount;
    public int npcCacheCount;
    private int objectAnimationCount;
    private boolean fogOfWar;
    private int gameWidth;
    private int gameHeight;
    private int const_9;
    private int selectedSpell;
    public boolean showOptionMenu;
    private int mouseClickXStep;
    public int newBankItemCount;
    private int npcAnimationArray[][] = {
        {
            11, 2, 9, 7, 1, 6, 10, 0, 5, 8,
            3, 4
        }, {
            11, 2, 9, 7, 1, 6, 10, 0, 5, 8,
            3, 4
        }, {
            11, 3, 2, 9, 7, 1, 6, 10, 0, 5,
            8, 4
        }, {
            3, 4, 2, 9, 7, 1, 6, 10, 8, 11,
            0, 5
        }, {
            3, 4, 2, 9, 7, 1, 6, 10, 8, 11,
            0, 5
        }, {
            4, 3, 2, 9, 7, 1, 6, 10, 8, 11,
            0, 5
        }, {
            11, 4, 2, 9, 7, 1, 6, 10, 0, 5,
            8, 3
        }, {
            11, 2, 9, 7, 1, 6, 10, 0, 5, 8,
            4, 3
        }
    };
    public int playerStatCurrent[];
    private int controlWelcomeNewUser;
    private int controlWelcomeExistingUser;
    private int npcWalkModel[] = {
        0, 1, 2, 1
    };
    private int controlRegisterStatus;
    private int controlRegisterUser;
    private int controlRegisterSubmit, controlRegisterCancel;
    public int teleportBubbleType[];
    private Menu panelLoginWelcome;
    private int combatTimeout;
    private Menu panelRegisterUser;
    public int optionMenuCount;
    public boolean showDialogShop;
    public int shopItem[];
    public int shopItemCount[];
    public int[] shopItemBuyPrice, shopItemSellPrice;
    public Model gameModels[];
    private String serverMessage;
    private int cameraRotationTime;
    private Menu panelMusic;
    public int playerStatBase[];
    public Mob npcsCache[];
    private int characterSkinColours[] = {
        0xecded0, 0xccb366, 0xb38c40, 0x997326, 0x906020
    };
    public int groundItemX[];
    public int groundItemY[];
    public int[] groundItemID;
    public int groundItemZ[];
    private int bankSelectedItemSlot;
    private int bankSelectedItem;
    private int messageHistoryTimeout[];
    public boolean optionCameraModeAuto;
    public int objectX[];
    public int objectY[];
    public int objectID[];
    public int objectDirection[];
    private int characterTopBottomColours[] = {
        0xff0000, 0xff8000, 0xffe000, 0xa0e000, 57344, 32768, 41088, 45311, 33023, 12528,
        0xe000e0, 0x303030, 0x604000, 0x805000, 0xffffff
    };
    private int itemsAboveHeadCount;
    private int showUiWildWarn;
    private int selectedItemInventoryIndex;
    public int statFatigue;
    public int fatigueSleeping;
    public boolean loadingArea;
    public boolean showDialogServerMessage;
    private int menuItemID[];
    public Model wallObjectModel[];
    private int menuX;
    private int menuY;
    private int menuWidth;
    private int menuHeight;
    private int menuItemsCount;
    private int actionBubbleX[];
    private int actionBubbleY[];
    private Menu panelQuestList;
    private int cameraZoom;
    private Menu panelMagic;
    private int showUiTab;
    public int planeWidth;
    public int planeHeight;
    public int planeMultiplier;
    public int lastHeightOffset;
    public boolean showDialogBank;
    public int playerQuestPoints;
    private int characterHairColours[] = {
        0xffc030, 0xffa040, 0x805030, 0x604020, 0x303030, 0xff6020, 0xff4000, 0xffffff, 65280, 65535
    };
    private int bankActivePage;
    public int welcomeLastLoggedInDays;
    private String equipmentStatNames[] = {
        "Armour", "WeaponAim", "WeaponPower", "Magic", "Prayer"
    };
    public boolean optionMouseButtonOne;
    public int inventoryItemsCount;
    public int inventoryItemId[];
    public int inventoryItemStackCount[];
    public int inventoryEquipped[];
    public String skillNameShort[] = {
        "Attack", "Defense", "Strength", "Hits", "Ranged", "Prayer", "Magic", "Cooking", "Woodcut", "Fletching",
        "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblaw", "Agility", "Thieving"
    };
    public Mob knownPlayers[];
    private String messageHistory[];
    private Menu panelAppearance;
    private int minimapRandom_1;
    private int minimapRandom_2;
    private Menu panelLoginExistingUser;
    public int objectCount;
    private int cameraAutoRotatePlayerX;
    private int cameraAutoRotatePlayerY;
    private int actionBubbleScale[];
    private int actionBubbleItem[];
    public boolean showAppearanceChange;
    public int shopSelectedItemIndex;
    public int shopSelectedItemType;
    public int projectileMaxRange;
    public String sleepingStatusText;
    private int npcCombatModelArray1[] = {
        0, 1, 2, 1, 0, 0, 0, 0
    };
    public Mob npcs[];
    private int experienceArray[];
    private int healthBarX[];
    private int healthBarY[];
    private int healthBarMissing[];
    private String skillNameLong[] = {
        "Attack", "Defense", "Strength", "Hits", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching",
        "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblaw", "Agility", "Thieving"
    };
    private Mob playerServer[];
    public int playerCount;
    public int knownPlayerCount;
    private int spriteCount;
    private int walkPathX[];
    private int walkPathY[];
    public int wallObjectCount;
    public int wallObjectX[];
    public int wallObjectY[];
    private int localLowerX;
    private int localLowerY;
    private int localUpperX;
    private int localUpperY;
    private String menuItemText2[];
    public Mob npcsServer[];
    private int sleepWordDelayTimer;
    public int playerStatEquipment[];
    public Terrain world;
    public Model objectModel[];
    
    private void initVars() {
        username = "";
        menuIndices = new int[menuMaxSize];
        cameraAutoAngleDebug = false;
        wallObjectDirection = new int[wallObjectsMax];
        wallObjectId = new int[wallObjectsMax];
        cameraRotationXIncrement = 2;
        inventoryMaxItemCount = 30;
        bankItemsMax = 48;
        optionMenuEntry = new String[12];
        newBankItems = new int[256];
        newBankItemsCount = new int[256];
        teleportBubbleTime = new int[50];
        receivedMessageX = new int[50];
        receivedMessageY = new int[50];
        receivedMessageMidPoint = new int[50];
        receivedMessageHeight = new int[50];
        localPlayer = new Mob();
        menuItemX = new int[menuMaxSize];
        menuItemY = new int[menuMaxSize];
        bankItems = new int[256];
        bankItemsCount = new int[256];
        appearanceBodyGender = 1;
        appearance2Colour = 2;
        appearanceHairColour = 2;
        appearanceTopColour = 8;
        appearanceBottomColour = 14;
        appearanceHeadGender = 1;
        loginUser = "";
        cameraAngle = 1;
        optionSoundDisabled = false;
        optionMusicLoop = true;
        optionMusicAuto = true;
        showRightClickMenu = false;
        cameraRotationYIncrement = 2;
        objectAlreadyInMenu = new boolean[objectsMax];
        menuItemText1 = new String[menuMaxSize];
        lastObjectAnimationNumberFireLightningSpell = -1;
        lastObjectAnimationNumberTorch = -1;
        lastOjectAnimationNumberClaw = -1;
        planeIndex = -1;
        welcomScreenAlreadyShown = false;
        isSleeping = false;
        cameraRotation = 128;
        teleportBubbleX = new int[50];
        playerExperience = new int[playerStatCount];
        showDialogWelcome = false;
        teleportBubbleY = new int[50];
        receivedMessages = new String[50];
        players = new Mob[playersMax];
        prayerOn = new boolean[50];
        menuSourceType = new int[menuMaxSize];
        menuSourceIndex = new int[menuMaxSize];
        menuTargetIndex = new int[menuMaxSize];
        wallObjectAlreadyInMenu = new boolean[wallObjectsMax];
        magicLoc = 128;
        fogOfWar = false;
        gameWidth = Constants.APPLICATION_WIDTH;
        gameHeight = Constants.APPLICATION_HEIGHT;
        const_9 = 9;
        selectedSpell = -1;
        showOptionMenu = false;
        playerStatCurrent = new int[playerStatCount];
        teleportBubbleType = new int[50];
        showDialogShop = false;
        shopItem = new int[256];
        shopItemCount = new int[256];
        shopItemBuyPrice = new int[256];
        shopItemSellPrice = new int[256];
        gameModels = new Model[1000];
        serverMessage = "";
        playerStatBase = new int[playerStatCount];
        npcsCache = new Mob[npcsMax];
        groundItemX = new int[groundItemsMax];
        groundItemY = new int[groundItemsMax];
        groundItemID = new int[groundItemsMax];
        groundItemZ = new int[groundItemsMax];
        bankSelectedItemSlot = -1;
        bankSelectedItem = -2;
        messageHistoryTimeout = new int[5];
        optionCameraModeAuto = true;
        objectX = new int[objectsMax];
        objectY = new int[objectsMax];
        objectID = new int[objectsMax];
        objectDirection = new int[objectsMax];
        selectedItemInventoryIndex = -1;
        selectedItemName = "";
        loadingArea = false;
        showDialogServerMessage = false;
        menuItemID = new int[menuMaxSize];
        wallObjectModel = new Model[wallObjectsMax];
        actionBubbleX = new int[50];
        actionBubbleY = new int[50];
        cameraZoom = 550 - 180;
        lastHeightOffset = -1;
        showDialogBank = false;
        optionMouseButtonOne = false;
        inventoryItemId = new int[35];
        inventoryItemStackCount = new int[35];
        inventoryEquipped = new int[35];
        knownPlayers = new Mob[playersMax];
        messageHistory = new String[5];
        actionBubbleScale = new int[50];
        actionBubbleItem = new int[50];
        showAppearanceChange = false;
        shopSelectedItemIndex = -1;
        shopSelectedItemType = -2;
        projectileMaxRange = 40;
        npcs = new Mob[npcsMax];
        experienceArray = new int[99];
        healthBarX = new int[50];
        healthBarY = new int[50];
        healthBarMissing = new int[50];
        playerServer = new Mob[playersServerMax];
        walkPathX = new int[pathStepsMax];
        walkPathY = new int[pathStepsMax];
        wallObjectX = new int[wallObjectsMax];
        wallObjectY = new int[wallObjectsMax];
        menuItemText2 = new String[menuMaxSize];
        npcsServer = new Mob[npcsServerMax];
        playerStatEquipment = new int[playerStatEquipmentCount];
        objectModel = new Model[objectsMax];
    }

    public void playSoundEffect(SoundEffect sound) {
        if (!optionSoundDisabled) {
            sound.play();
        }
    }

    private boolean walkToActionSource(int startX, int startY, int x1, int y1, int x2, int y2, boolean checkObjects, boolean walkToAction) {
        int steps = world.getStepCount(startX, startY, x1, y1, x2, y2, walkPathX, walkPathY, checkObjects);
        if (steps == -1) {
            return false;
            /*if (walkToAction) {
                steps = 1;
                walkPathX[0] = x1;
                walkPathY[0] = y1;
            } else {
                return false;
            }*/
        }
        steps--;
        startX = walkPathX[steps];
        startY = walkPathY[steps];
        steps--;
        if (walkToAction && steps == -1 && (startX + regionX) % 5 == 0) {
            steps = 0;
        }

        byte[] xWaypoints = new byte[steps + 1];
        byte[] yWaypoints = new byte[steps + 1];
        for (int i = steps; i >= 0 && i > steps - 25; i--) {
            xWaypoints[i] = (byte) (walkPathX[i] - startX);
            yWaypoints[i] = (byte) (walkPathY[i] - startY);
        }

        for (int i = 0; i < xWaypoints.length / 2; i++) { // reverse x waypoints
            byte temp = xWaypoints[i];
            xWaypoints[i] = xWaypoints[xWaypoints.length - i - 1];
            xWaypoints[xWaypoints.length - i - 1] = temp;
        }

        for (int i = 0; i < yWaypoints.length / 2; i++) { // reverse y waypoints
            byte temp = yWaypoints[i];
            yWaypoints[i] = yWaypoints[yWaypoints.length - i - 1];
            yWaypoints[yWaypoints.length - i - 1] = temp;
        }
        
        ActionManager.get(WalkHandler.class).handleWalk(startX + regionX, startY + regionY, xWaypoints, yWaypoints, walkToAction);

        mouseClickXStep = -24;
        mouseClickXX = super.mouseX;
        mouseClickXY = super.mouseY;
        return true;
    }

    private boolean walkTo(int startX, int startY, int x1, int y1, int x2, int y2, boolean checkObjects, boolean walkToAction) {
        int steps = world.getStepCount(startX, startY, x1, y1, x2, y2, walkPathX, walkPathY, checkObjects);
        if (steps == -1) {
            return false;
        }
        steps--;
        startX = walkPathX[steps];
        startY = walkPathY[steps];
        steps--;
        if (walkToAction && steps == -1 && (startX + regionX) % 5 == 0) {
            steps = 0;
        }

        byte[] xWaypoints = new byte[steps + 1];
        byte[] yWaypoints = new byte[steps + 1];
        for (int i = steps; i >= 0 && i > steps - 25; i--) {
            xWaypoints[i] = (byte) (walkPathX[i] - startX);
            yWaypoints[i] = (byte) (walkPathY[i] - startY);
        }

        for (int i = 0; i < xWaypoints.length / 2; i++) { // reverse x waypoints
            byte temp = xWaypoints[i];
            xWaypoints[i] = xWaypoints[xWaypoints.length - i - 1];
            xWaypoints[xWaypoints.length - i - 1] = temp;
        }

        for (int i = 0; i < yWaypoints.length / 2; i++) { // reverse y waypoints
            byte temp = yWaypoints[i];
            yWaypoints[i] = yWaypoints[yWaypoints.length - i - 1];
            yWaypoints[yWaypoints.length - i - 1] = temp;
        }

        ActionManager.get(WalkHandler.class).handleWalk(startX + regionX, startY + regionY, xWaypoints, yWaypoints, walkToAction);

        mouseClickXStep = -24;
        mouseClickXX = super.mouseX;
        mouseClickXY = super.mouseY;
        return true;
    }

    private void drawMinimapEntity(int x, int y, int c) {
        surface.setPixel(x, y, c);
        surface.setPixel(x - 1, y, c);
        surface.setPixel(x + 1, y, c);
        surface.setPixel(x, y - 1, c);
        surface.setPixel(x, y + 1, c);
    }

    public void updateBankItems() {
        bankItemCount = newBankItemCount;
        for (int i = 0; i < newBankItemCount; i++) {
            bankItems[i] = newBankItems[i];
            bankItemsCount[i] = newBankItemsCount[i];
        }

        for (int invidx = 0; invidx < inventoryItemsCount; invidx++) {
            if (bankItemCount >= bankItemsMax) {
                break;
            }
            int invid = inventoryItemId[invidx];
            boolean hasitemininv = false;
            for (int bankidx = 0; bankidx < bankItemCount; bankidx++) {
                if (bankItems[bankidx] != invid) {
                    continue;
                }
                hasitemininv = true;
                break;
            }

            if (!hasitemininv) {
                bankItems[bankItemCount] = invid;
                bankItemsCount[bankItemCount] = 0;
                bankItemCount++;
            }
        }

    }

    private void drawDialogWildWarn() {
        int xOff = (gameWidth / 2) - 256;
        int yOff = (gameHeight / 2) - 172;
        int y = 97 + yOff;
        surface.drawBox(86 + xOff, 77 + yOff, 340, 180, 0);
        surface.drawBoxEdge(86 + xOff, 77 + yOff, 340, 180, 0xffffff);
        surface.drawStringCenter("Warning! Proceed with caution", 256 + xOff, y, 4, 0xff0000);
        y += 26;
        surface.drawStringCenter("If you go much further north you will enter the", 256 + xOff, y, 1, 0xffffff);
        y += 13;
        surface.drawStringCenter("wilderness. This a very dangerous area where", 256 + xOff, y, 1, 0xffffff);
        y += 13;
        surface.drawStringCenter("other players can attack you!", 256 + xOff, y, 1, 0xffffff);
        y += 22;
        surface.drawStringCenter("The further north you go the more dangerous it", 256 + xOff, y, 1, 0xffffff);
        y += 13;
        surface.drawStringCenter("becomes, but the more treasure you will find.", 256 + xOff, y, 1, 0xffffff);
        y += 22;
        surface.drawStringCenter("In the wilderness an indicator at the bottom-right", 256 + xOff, y, 1, 0xffffff);
        y += 13;
        surface.drawStringCenter("of the screen will show the current level of danger", 256 + xOff, y, 1, 0xffffff);
        y += 22;
        int j = 0xffffff;
        if (super.mouseY > y - 12 && super.mouseY <= y && super.mouseX > 178 + xOff && super.mouseX < 334 + xOff) {
            j = 0xff0000;
        }
        surface.drawStringCenter("Click here to close window", 256 + xOff, y, 1, j);
        if (mouseButtonClick == 1) {
            if (j == 0xff0000) {
                showUiWildWarn = 2;
            }
        }
        mouseButtonClick = 0;
    }

    private void drawAboveHeadStuff() {
        for (int msgidx = 0; msgidx < receivedMessagesCount; msgidx++) {
            int txtheight = surface.textHeight(1);
            int x = receivedMessageX[msgidx];
            int y = receivedMessageY[msgidx];
            int mid = receivedMessageMidPoint[msgidx];
            int msgheight = receivedMessageHeight[msgidx];
            boolean flag = true;
            while (flag) {
                flag = false;
                for (int i4 = 0; i4 < msgidx; i4++) {
                    if (y + msgheight > receivedMessageY[i4] - txtheight && y - txtheight < receivedMessageY[i4] + receivedMessageHeight[i4] && x - mid < receivedMessageX[i4] + receivedMessageMidPoint[i4] && x + mid > receivedMessageX[i4] - receivedMessageMidPoint[i4] && receivedMessageY[i4] - txtheight - msgheight < y) {
                        y = receivedMessageY[i4] - txtheight - msgheight;
                        flag = true;
                    }
                }

            }
            receivedMessageY[msgidx] = y;
            surface.drawStringCentered(receivedMessages[msgidx], x, y, 1, 0xffff00, 300);
        }

        for (int itemidx = 0; itemidx < itemsAboveHeadCount; itemidx++) {
            int x = actionBubbleX[itemidx];
            int y = actionBubbleY[itemidx];
            int scale = actionBubbleScale[itemidx];
            int id = actionBubbleItem[itemidx];
            int scaleX = (39 * scale) / 100;
            int scaleY = (27 * scale) / 100;
            surface.drawActionBubble(x - scaleX / 2, y - scaleY, scaleX, scaleY, spriteMedia + 9, 85);
            int scaleXClip = (36 * scale) / 100;
            int scaleYClip = (24 * scale) / 100;
            surface.spriteClipping(x - scaleXClip / 2, (y - scaleY + scaleY / 2) - scaleYClip / 2, scaleXClip, scaleYClip, EntityManager.getItem(id).getSprite() + spriteItem, EntityManager.getItem(id).getMask(), 0, 0, false);
        }

        for (int j1 = 0; j1 < healthBarCount; j1++) {
            int i2 = healthBarX[j1];
            int l2 = healthBarY[j1];
            int k3 = healthBarMissing[j1];
            surface.drawBoxAlpha(i2 - 15, l2 - 3, k3, 5, 65280, 192);
            surface.drawBoxAlpha((i2 - 15) + k3, l2 - 3, 30 - k3, 5, 0xff0000, 192);
        }

    }

    private boolean walkToActionSource(int sx, int sy, int dx, int dy, boolean action) {
        return walkToActionSource(sx, sy, dx, dy, dx, dy, false, action);
    }

    private void createMessageTabPanel() {
        /* changes to chat cursors and messages height goes here (and scrollbars for them) */
        panelMessageTabs = new Menu(surface, 10);
        int OFFSET = 20;
        controlTextListChat = panelMessageTabs.addTextListScrollable(5, gameHeight - 55 - 12-OFFSET/*269*/, gameWidth - 10, 56, 1, 20, true);
        controlTextListAll = panelMessageTabs.addTextListInput(7, gameHeight - 10-OFFSET/*324*/, gameWidth - 10 - 2, 14, 1, 80, false, true);
        controlTextListQuest = panelMessageTabs.addTextListScrollable(5, gameHeight - 55 - 12-OFFSET/*269*/, gameWidth - 10, 56, 1, 20, true);
        controlTextListPrivate = panelMessageTabs.addTextListScrollable(5, gameHeight - 55 - 12-OFFSET/*269*/, gameWidth - 10, 56, 1, 20, true);
        panelMessageTabs.setFocus(controlTextListAll);
    }

    private void disposeAndCollect() {
        if (surface != null) {
            surface.clear();
            surface.pixels = null;
            surface = null;
        }
        if (scene != null) {
            scene.dispose();
            scene = null;
        }
        gameModels = null;
        objectModel = null;
        wallObjectModel = null;
        playerServer = null;
        players = null;
        npcsServer = null;
        npcs = null;
        localPlayer = null;
        if (world != null) {
            world.terrainModels = null;
            world.wallModels = null;
            world.roofModels = null;
            world.parentModel = null;
            world = null;
        }
        System.gc();
    }

    private void drawUi() {
        if (logoutTimeout != 0) {
            drawDialogLogout();
        } else if (showDialogWelcome) {
            drawDialogWelcome();
        } else if (showDialogServerMessage) {
            drawDialogServerMessage();
        } else if (showUiWildWarn == 1) {
            drawDialogWildWarn();
        } else if (showDialogBank && combatTimeout == 0) {
            drawDialogBank();
        } else if (showDialogShop && combatTimeout == 0) {
            drawDialogShop();
        } else {
            if (showOptionMenu) {
                drawOptionMenu();
            }
            if (localPlayer.animationCurrent == 8 || localPlayer.animationCurrent == 9) {
                drawDialogCombatStyle();
            }
            setActiveUiTab();
            boolean nomenus = !showOptionMenu && !showRightClickMenu;
            if (nomenus) {
                menuItemsCount = 0;
            }
            if (showUiTab == 0 && nomenus) {
                createRightClickMenu();
            }
            if (showUiTab == 1) {
                drawUiTabInventory(nomenus);
            }
            if (showUiTab == 2) {
                drawUiTabMinimap(nomenus);
            }
            if (showUiTab == 3) {
                drawUiTabPlayerInfo(nomenus);
            }
            if (showUiTab == 4) {
                drawUiTabMagic(nomenus);
            }
            if (showUiTab == 5) {
                drawUiTabMusic(nomenus);
            }
            if (showUiTab == 6) {
                drawUiTabOptions(nomenus);
            }
            if (!showRightClickMenu && !showOptionMenu) {
                createTopMouseMenu();
            }
            if (showRightClickMenu && !showOptionMenu) {
                drawRightClickMenu();
            }
        }
        mouseButtonClick = 0;
    }

    protected void resetGame() {
        musicPlayer = new MusicPlayer();
        logoutTimeout = 0;
        loginScreen = 0;
        loggedIn = 1;
        surface.blackScreen();
        surface.draw(graphics, 0, 0);
        for (int i = 0; i < objectCount; i++) {
            scene.removeModel(objectModel[i]);
            world.updateObject(objectX[i], objectY[i], objectID[i], objectDirection[i]);
        }

        for (int j = 0; j < wallObjectCount; j++) {
            scene.removeModel(wallObjectModel[j]);
            world.updateDoor(wallObjectX[j], wallObjectY[j], wallObjectDirection[j], wallObjectId[j]);
        }

        objectCount = 0;
        wallObjectCount = 0;
        groundItemCount = 0;
        playerCount = 0;
        for (int k = 0; k < playersServerMax; k++) {
            playerServer[k] = null;
        }

        for (int l = 0; l < playersMax; l++) {
            players[l] = null;
        }

        npcCount = 0;
        for (int i1 = 0; i1 < npcsServerMax; i1++) {
            npcsServer[i1] = null;
        }

        for (int j1 = 0; j1 < npcsMax; j1++) {
            npcs[j1] = null;
        }

        for (int k1 = 0; k1 < 50; k1++) {
            prayerOn[k1] = false;
        }

        mouseButtonClick = 0;
        super.lastMouseButtonDown = 0;
        super.mouseButtonDown = 0;
        showDialogShop = false;
        showDialogBank = false;
        isSleeping = false;
    }

    private void drawUiTabMusic(boolean nomenus) {
        int uiX = surface.width2 - 199;
        int uiY = 36;
        surface.drawSprite(uiX - 49, 3, spriteMedia + 5);
        int uiWidth = 196;
        int uiHeight = 182;
        int l = Surface.rgb2long(220, 220, 220);
        surface.drawBoxAlpha(uiX, uiY, uiWidth / 2, 24, l, 128);
        surface.drawBoxAlpha(uiX + uiWidth / 2, uiY, uiWidth / 2, 24, l, 128);
        surface.drawBoxAlpha(uiX, uiY + 24, uiWidth, uiHeight - 24, Surface.rgb2long(220, 220, 220), 128);
        surface.drawLineHoriz(uiX, (uiY + uiHeight) - 16, uiWidth, 0);
        surface.drawLineHoriz(uiX, uiY + 24, uiWidth, 0);
        
        surface.drawSprite(uiX + 30, uiY, spriteMedia + 30); // back
        surface.drawSprite(uiX + 60, uiY, spriteMedia + 28); // pause
        surface.drawSprite(uiX + 90, uiY, spriteMedia + 27); // play
        surface.drawSprite(uiX + 120, uiY, spriteMedia + 29); // stop
        surface.drawSprite(uiX + 150, uiY, spriteMedia + 31); // forward
        
        if(mouseButtonClick == 1) {
            // back button
            if(super.mouseX > uiX + 30 && super.mouseX < uiX + 50 && super.mouseY > uiY + 2 && super.mouseY < uiY + 20) {
                if(musicPlayer.isRunning()) {
                    if(selectedSong != null) {
                        musicPlayer.start(selectedSong);
                    } else {
                        musicPlayer.start(musicPlayer.getCurrentSong());
                    }
                }
            }
            // pause button
            if(super.mouseX > uiX + 60 && super.mouseX < uiX + 80 && super.mouseY > uiY + 2 && super.mouseY < uiY + 20) {
                if(!musicPlayer.isPaused()) {
                    musicPlayer.pause();
                }
            }
            // play button
            if(super.mouseX > uiX + 90 && super.mouseX < uiX + 110 && super.mouseY > uiY + 2 && super.mouseY < uiY + 20) {
                if(musicPlayer.isPaused()) {
                    musicPlayer.resume();
                } else if(!musicPlayer.isRunning()) {
                    musicPlayer.startRandom();
                }
            }
            // stop button
            if(super.mouseX > uiX + 120 && super.mouseX < uiX + 140 && super.mouseY > uiY + 2 && super.mouseY < uiY + 20) {
                if(musicPlayer.isRunning()) {
                    musicPlayer.stop();
                }
            }
            // forward button
            if(super.mouseX > uiX + 150 && super.mouseX < uiX + 170 && super.mouseY > uiY + 2 && super.mouseY < uiY + 20) {
                selectedSong = null;
                if(musicPlayer.isRunning()) {
                    musicPlayer.startRandom();
                }
            }
        }
        
        panelMusic.clearList(controlListMusic);
        File musicFolder = new File(Constants.CACHE_DIRECTORY + "audio/music");
        FilenameFilter filter = (File dir, String name) -> {
            return name.endsWith(".mid");
        };
        File[] songFiles = musicFolder.listFiles(filter);
        Arrays.sort(songFiles);
        for(int i = 0; i < songFiles.length; i++) {
            panelMusic.addListEntry(controlListMusic, i, Util.capitalizeWord(songFiles[i].getName().replace(".mid", "").replace("_", " ")));
        }
        panelMusic.drawPanel();
        String s = (selectedSong != null) ? selectedSong : musicPlayer.getCurrentSong();
        if(musicPlayer.isRunning()) {
            String song = Util.capitalizeWord(s.replace(".mid", "").replace("_", " "));
            surface.drawStringCenter("Playing: " + song, uiX + uiWidth / 2, (uiY + uiHeight) - 3, 1, Colors.GREEN2);
        } else if(musicPlayer.isPaused()) {
            String song = Util.capitalizeWord(s.replace(".mid", "").replace("_", " "));
            surface.drawStringCenter("Paused: " + song, uiX + uiWidth / 2, (uiY + uiHeight) - 3, 1, Colors.YELLOW);
        } else {
            surface.drawStringCenter("No song currently selected", uiX + uiWidth / 2, (uiY + uiHeight) - 3, 1, Colors.RED);
        }
        if (!nomenus) {
            return;
        }
        uiX = super.mouseX - (surface.width2 - 199);
        uiY = super.mouseY - 36;
        if (uiX >= 0 && uiY >= 0 && uiX < 196 && uiY < 182) {
            panelMusic.handleMouse(uiX + (surface.width2 - 199), uiY + 36, super.lastMouseButtonDown, super.mouseButtonDown);
            if (mouseButtonClick == 1) {
                int index = panelMusic.getListEntryIndex(controlListMusic);
                if(index >= 0 && index <= songFiles.length) {
                    selectedSong = songFiles[index].getName();
                    musicPlayer.stop();
                    musicPlayer.start(selectedSong);
                }
            }
            mouseButtonClick = 0;
        }
    }
    
    public String selectedSong;

    @Override
    protected void handleKeyPress(int i) {
        if (loggedIn == 0) {
            if (loginScreen == 0 && panelLoginWelcome != null) {
                panelLoginWelcome.keyPress(i);
            }
            if (loginScreen == 1 && panelRegisterUser != null) {
                panelRegisterUser.keyPress(i);
            }
            if (loginScreen == 2 && panelLoginExistingUser != null) {
                panelLoginExistingUser.keyPress(i);
            }
        }
        if (loggedIn == 1) {
            if (showAppearanceChange && panelAppearance != null) {
                panelAppearance.keyPress(i);
                return;
            }
            if (!isSleeping && panelMessageTabs != null) {
                panelMessageTabs.keyPress(i);
            }
        }
    }

    private void sendLogout() {
        if (loggedIn == 0) {
            return;
        }
        if (combatTimeout > 450) {
            showMessage("@cya@You can't logout during combat!", 3);
            return;
        }
        if (combatTimeout > 0) {
            showMessage("@cya@You can't logout for 10 seconds after combat", 3);
        } else {
            ActionManager.get(LogoutHandler.class).handleLogout();
            logoutTimeout = 1000;
        }
    }

    public Mob createPlayer(int serverIndex, int x, int y, int anim) {
        if (playerServer[serverIndex] == null) {
            playerServer[serverIndex] = new Mob();
            playerServer[serverIndex].serverIndex = serverIndex;
            playerServer[serverIndex].serverId = 0;
        }
        Mob character = playerServer[serverIndex];
        boolean flag = false;
        for (int i1 = 0; i1 < knownPlayerCount; i1++) {
            if (knownPlayers[i1].serverIndex != serverIndex) {
                continue;
            }
            flag = true;
            break;
        }

        if (flag) {
            character.animationNext = anim;
            int j1 = character.waypointCurrent;
            if (x != character.waypointsX[j1] || y != character.waypointsY[j1]) {
                character.waypointCurrent = j1 = (j1 + 1) % 10;
                character.waypointsX[j1] = x;
                character.waypointsY[j1] = y;
            }
        } else {
            character.serverIndex = serverIndex;
            character.movingStep = 0;
            character.waypointCurrent = 0;
            character.waypointsX[0] = character.currentX = x;
            character.waypointsY[0] = character.currentY = y;
            character.animationNext = character.animationCurrent = anim;
            character.stepCount = 0;
        }
        players[playerCount++] = character;
        return character;
    }
    
    private void drawAppearancePanelCharacterSprites() {
        surface.interlace = false;
        surface.blackScreen();
        panelAppearance.drawPanel();
        int x = (gameWidth / 2);
        int y = 25;
        surface.spriteClipping(x - 32 - 55, y, 64, 102, EntityManager.getAnimation(appearance2Colour).getNumber(), characterTopBottomColours[appearanceBottomColour]);
        surface.spriteClipping(x - 32 - 55, y, 64, 102, EntityManager.getAnimation(appearanceBodyGender).getNumber(), characterTopBottomColours[appearanceTopColour], characterSkinColours[appearanceSkinColour], 0, false);
        surface.spriteClipping(x - 32 - 55, y, 64, 102, EntityManager.getAnimation(appearanceHeadType).getNumber(), characterHairColours[appearanceHairColour], characterSkinColours[appearanceSkinColour], 0, false);
        surface.spriteClipping(x - 32, y, 64, 102, EntityManager.getAnimation(appearance2Colour).getNumber() + 6, characterTopBottomColours[appearanceBottomColour]);
        surface.spriteClipping(x - 32, y, 64, 102, EntityManager.getAnimation(appearanceBodyGender).getNumber() + 6, characterTopBottomColours[appearanceTopColour], characterSkinColours[appearanceSkinColour], 0, false);
        surface.spriteClipping(x - 32, y, 64, 102, EntityManager.getAnimation(appearanceHeadType).getNumber() + 6, characterHairColours[appearanceHairColour], characterSkinColours[appearanceSkinColour], 0, false);
        surface.spriteClipping((x - 32) + 55, y, 64, 102, EntityManager.getAnimation(appearance2Colour).getNumber() + 12, characterTopBottomColours[appearanceBottomColour]);
        surface.spriteClipping((x - 32) + 55, y, 64, 102, EntityManager.getAnimation(appearanceBodyGender).getNumber() + 12, characterTopBottomColours[appearanceTopColour], characterSkinColours[appearanceSkinColour], 0, false);
        surface.spriteClipping((x - 32) + 55, y, 64, 102, EntityManager.getAnimation(appearanceHeadType).getNumber() + 12, characterHairColours[appearanceHairColour], characterSkinColours[appearanceSkinColour], 0, false);
        surface.drawSprite(0, gameHeight, spriteMedia + 22);
        surface.draw(graphics, 0, 0);
    }

    private void createAppearancePanel() {
        panelAppearance = new Menu(surface, 100);
        int x = (gameWidth / 2);
        int y = 24;
        panelAppearance.addText(x, y, "Please design Your Character", 4, true);
        panelAppearance.addText(x - 55, y + 110, "Front", 3, true);
        panelAppearance.addText(x, y + 110, "Side", 3, true);
        panelAppearance.addText(x + 55, y + 110, "Back", 3, true);
        byte xoff = 54;
        y += 145;
        panelAppearance.addBoxRounded(x - xoff, y, 53, 41);
        panelAppearance.addText(x - xoff, y - 8, "Head", 1, true);
        panelAppearance.addText(x - xoff, y + 8, "Type", 1, true);
        panelAppearance.addSprite(x - xoff - 40, y, Menu.baseSpriteStart + 7);
        controlButtonAppearanceHead1 = panelAppearance.addButton(x - xoff - 40, y, 20, 20);
        panelAppearance.addSprite((x - xoff) + 40, y, Menu.baseSpriteStart + 6);
        controlButtonAppearanceHead2 = panelAppearance.addButton((x - xoff) + 40, y, 20, 20);
        panelAppearance.addBoxRounded(x + xoff, y, 53, 41);
        panelAppearance.addText(x + xoff, y - 8, "Hair", 1, true);
        panelAppearance.addText(x + xoff, y + 8, "Color", 1, true);
        panelAppearance.addSprite((x + xoff) - 40, y, Menu.baseSpriteStart + 7);
        controlButtonAppearanceHair1 = panelAppearance.addButton((x + xoff) - 40, y, 20, 20);
        panelAppearance.addSprite(x + xoff + 40, y, Menu.baseSpriteStart + 6);
        controlButtonAppearanceHair2 = panelAppearance.addButton(x + xoff + 40, y, 20, 20);
        y += 50;
        panelAppearance.addBoxRounded(x - xoff, y, 53, 41);
        panelAppearance.addText(x - xoff, y, "Gender", 1, true);
        panelAppearance.addSprite(x - xoff - 40, y, Menu.baseSpriteStart + 7);
        controlButtonAppearanceGender1 = panelAppearance.addButton(x - xoff - 40, y, 20, 20);
        panelAppearance.addSprite((x - xoff) + 40, y, Menu.baseSpriteStart + 6);
        controlButtonAppearanceGender2 = panelAppearance.addButton((x - xoff) + 40, y, 20, 20);
        panelAppearance.addBoxRounded(x + xoff, y, 53, 41);
        panelAppearance.addText(x + xoff, y - 8, "Top", 1, true);
        panelAppearance.addText(x + xoff, y + 8, "Color", 1, true);
        panelAppearance.addSprite((x + xoff) - 40, y, Menu.baseSpriteStart + 7);
        controlButtonAppearanceTop1 = panelAppearance.addButton((x + xoff) - 40, y, 20, 20);
        panelAppearance.addSprite(x + xoff + 40, y, Menu.baseSpriteStart + 6);
        controlButtonAppearanceTop2 = panelAppearance.addButton(x + xoff + 40, y, 20, 20);
        y += 50;
        panelAppearance.addBoxRounded(x - xoff, y, 53, 41);
        panelAppearance.addText(x - xoff, y - 8, "Skin", 1, true);
        panelAppearance.addText(x - xoff, y + 8, "Color", 1, true);
        panelAppearance.addSprite(x - xoff - 40, y, Menu.baseSpriteStart + 7);
        controlButtonAppearanceSkin1 = panelAppearance.addButton(x - xoff - 40, y, 20, 20);
        panelAppearance.addSprite((x - xoff) + 40, y, Menu.baseSpriteStart + 6);
        controlButtonAppearanceSkin2 = panelAppearance.addButton((x - xoff) + 40, y, 20, 20);
        panelAppearance.addBoxRounded(x + xoff, y, 53, 41);
        panelAppearance.addText(x + xoff, y - 8, "Bottom", 1, true);
        panelAppearance.addText(x + xoff, y + 8, "Color", 1, true);
        panelAppearance.addSprite((x + xoff) - 40, y, Menu.baseSpriteStart + 7);
        controlButtonAppearanceBottom1 = panelAppearance.addButton((x + xoff) - 40, y, 20, 20);
        panelAppearance.addSprite(x + xoff + 40, y, Menu.baseSpriteStart + 6);
        controlButtonAppearanceBottom2 = panelAppearance.addButton(x + xoff + 40, y, 20, 20);
        y += 82;
        y -= 35;
        panelAppearance.addButtonBackground(x, y, 200, 30);
        panelAppearance.addText(x, y, "Accept", 4, false);
        controlButtonAppearanceAccept = panelAppearance.addButton(x, y, 200, 30);
    }

    private void drawDialogWelcome() {
        int xOff = (gameWidth / 2) - 256;
        int yOff = (gameHeight / 2) - 172;
        int i = 140;
        int y = 167 - i / 2 + yOff;
        surface.drawBox(56 + xOff, 167 - i / 2 + yOff, 400, i, 0);
        surface.drawBoxEdge(56 + xOff, 167 - i / 2 + yOff, 400, i, 0xffffff);
        y += 20;
        surface.drawStringCenter("Welcome to RuneScape " + Util.title(loginUser), 256 + xOff, y, 4, 0xffff00);
        y += 30;
        String s;
        switch (welcomeLastLoggedInDays) {
            case 0:
                s = "earlier today";
                break;
            case 1:
                s = "yesterday";
                break;
            default:
                s = welcomeLastLoggedInDays + " days ago";
                break;
        }
        surface.drawStringCenter("You last logged in " + s, 256 + xOff, y, 1, 0xffffff);
        y += 15;
        surface.drawStringCenter("from: localhost", 256 + xOff, y, 1, 0xffffff);
        y += 15;
        y += 15;
        if (welcomeUnreadMessages == 0) {
            surface.drawStringCenter("You have @yel@0@whi@ unread messages in your message-centre", 256 + xOff, y, 1, 0xffffff);
        }
        y += 15;
        y += 15;
        int l = 0xffffff;
        if (super.mouseY > y - 12 && super.mouseY <= y && super.mouseX > 177 + xOff && super.mouseX < 333 + xOff) {
            l = 0xff0000;
        }
        surface.drawStringCenter("Click here to close window", 256 + xOff, y, 1, l);
        if (mouseButtonClick == 1) {
            if (l == 0xff0000) {
                showDialogWelcome = false;
            }
        }
        mouseButtonClick = 0;
    }

    @Override
    public Graphics getGraphics() {
        return application.getGraphics();
    }

    public void drawItem(int x, int y, int w, int h, int id, int tx, int ty) {
        int picture = EntityManager.getItem(id).getSprite() + spriteItem;
        int mask = EntityManager.getItem(id).getMask();
        surface.spriteClipping(x, y, w, h, picture, mask, 0, 0, false);
    }

    public Player player;
    private ClientUpdater updater = null;
    private long lastSentClientUpdate = 0;

    private void updateClient() {
        if (updater == null) {
            updater = new ClientUpdater();
        }
        long now = System.currentTimeMillis();
        if (now - lastSentClientUpdate >= 600) {
            lastSentClientUpdate = now;
            updater.updateClients();
        }
    }

    private void handleGameInput() {
        updateClient(); // TODO
        if (logoutTimeout > 0) {
            logoutTimeout--;
        }
        if (localPlayer.animationCurrent == 8 || localPlayer.animationCurrent == 9) {
            combatTimeout = 500;
        }
        if (combatTimeout > 0) {
            combatTimeout--;
        }
        if (showAppearanceChange) {
            handleAppearancePanelControls();
            return;
        }
        for (int i = 0; i < playerCount; i++) {
            Mob character = players[i];
            int k = (character.waypointCurrent + 1) % 10;
            if (character.movingStep != k) {
                int i1 = -1;
                int l2 = character.movingStep;
                int j4;
                if (l2 < k) {
                    j4 = k - l2;
                } else {
                    j4 = (10 + k) - l2;
                }
                int j5 = 4;
                if (j4 > 2) {
                    j5 = (j4 - 1) * 4;
                }
                if (character.waypointsX[l2] - character.currentX > magicLoc * 3 || character.waypointsY[l2] - character.currentY > magicLoc * 3 || character.waypointsX[l2] - character.currentX < -magicLoc * 3 || character.waypointsY[l2] - character.currentY < -magicLoc * 3 || j4 > 8) {
                    character.currentX = character.waypointsX[l2];
                    character.currentY = character.waypointsY[l2];
                } else {
                    if (character.currentX < character.waypointsX[l2]) {
                        character.currentX += j5;
                        character.stepCount++;
                        i1 = 2;
                    } else if (character.currentX > character.waypointsX[l2]) {
                        character.currentX -= j5;
                        character.stepCount++;
                        i1 = 6;
                    }
                    if (character.currentX - character.waypointsX[l2] < j5 && character.currentX - character.waypointsX[l2] > -j5) {
                        character.currentX = character.waypointsX[l2];
                    }
                    if (character.currentY < character.waypointsY[l2]) {
                        character.currentY += j5;
                        character.stepCount++;
                        if (i1 == -1) {
                            i1 = 4;
                        } else if (i1 == 2) {
                            i1 = 3;
                        } else {
                            i1 = 5;
                        }
                    } else if (character.currentY > character.waypointsY[l2]) {
                        character.currentY -= j5;
                        character.stepCount++;
                        if (i1 == -1) {
                            i1 = 0;
                        } else if (i1 == 2) {
                            i1 = 1;
                        } else {
                            i1 = 7;
                        }
                    }
                    if (character.currentY - character.waypointsY[l2] < j5 && character.currentY - character.waypointsY[l2] > -j5) {
                        character.currentY = character.waypointsY[l2];
                    }
                }
                if (i1 != -1) {
                    character.animationCurrent = i1;
                }
                if (character.currentX == character.waypointsX[l2] && character.currentY == character.waypointsY[l2]) {
                    character.movingStep = (l2 + 1) % 10;
                }
            } else {
                character.animationCurrent = character.animationNext;
            }
            if (character.messageTimeout > 0) {
                character.messageTimeout--;
            }
            if (character.bubbleTimeout > 0) {
                character.bubbleTimeout--;
            }
            if (character.combatTimer > 0) {
                character.combatTimer--;
            }
            if (deathScreenTimeout > 0) {
                deathScreenTimeout--;
                if (deathScreenTimeout == 0) {
                    showMessage("You have been granted another life. Be more careful this time!", 3);
                }
                if (deathScreenTimeout == 0) {
                    showMessage("You retain your skills. Your objects land where you died", 3);
                }
            }
        }
        for (int j = 0; j < npcCount; j++) {
            Mob character_1 = npcs[j];
            int j1 = (character_1.waypointCurrent + 1) % 10;
            if (character_1.movingStep != j1) {
                int i3 = -1;
                int k4 = character_1.movingStep;
                int k5;
                if (k4 < j1) {
                    k5 = j1 - k4;
                } else {
                    k5 = (10 + j1) - k4;
                }
                int l5 = 4;
                if (k5 > 2) {
                    l5 = (k5 - 1) * 4;
                }
                if (character_1.waypointsX[k4] - character_1.currentX > magicLoc * 3 || character_1.waypointsY[k4] - character_1.currentY > magicLoc * 3 || character_1.waypointsX[k4] - character_1.currentX < -magicLoc * 3 || character_1.waypointsY[k4] - character_1.currentY < -magicLoc * 3 || k5 > 8) {
                    character_1.currentX = character_1.waypointsX[k4];
                    character_1.currentY = character_1.waypointsY[k4];
                } else {
                    if (character_1.currentX < character_1.waypointsX[k4]) {
                        character_1.currentX += l5;
                        character_1.stepCount++;
                        i3 = 2;
                    } else if (character_1.currentX > character_1.waypointsX[k4]) {
                        character_1.currentX -= l5;
                        character_1.stepCount++;
                        i3 = 6;
                    }
                    if (character_1.currentX - character_1.waypointsX[k4] < l5 && character_1.currentX - character_1.waypointsX[k4] > -l5) {
                        character_1.currentX = character_1.waypointsX[k4];
                    }
                    if (character_1.currentY < character_1.waypointsY[k4]) {
                        character_1.currentY += l5;
                        character_1.stepCount++;
                        if (i3 == -1) {
                            i3 = 4;
                        } else if (i3 == 2) {
                            i3 = 3;
                        } else {
                            i3 = 5;
                        }
                    } else if (character_1.currentY > character_1.waypointsY[k4]) {
                        character_1.currentY -= l5;
                        character_1.stepCount++;
                        if (i3 == -1) {
                            i3 = 0;
                        } else if (i3 == 2) {
                            i3 = 1;
                        } else {
                            i3 = 7;
                        }
                    }
                    if (character_1.currentY - character_1.waypointsY[k4] < l5 && character_1.currentY - character_1.waypointsY[k4] > -l5) {
                        character_1.currentY = character_1.waypointsY[k4];
                    }
                }
                if (i3 != -1) {
                    character_1.animationCurrent = i3;
                }
                if (character_1.currentX == character_1.waypointsX[k4] && character_1.currentY == character_1.waypointsY[k4]) {
                    character_1.movingStep = (k4 + 1) % 10;
                }
            } else {
                character_1.animationCurrent = character_1.animationNext;
                if (character_1.npcId == 43) {
                    character_1.stepCount++;
                }
            }
            if (character_1.messageTimeout > 0) {
                character_1.messageTimeout--;
            }
            if (character_1.bubbleTimeout > 0) {
                character_1.bubbleTimeout--;
            }
            if (character_1.combatTimer > 0) {
                character_1.combatTimer--;
            }
        }

        if (showUiTab != 2) {
            if (Surface.anInt346 > 0) {
                sleepWordDelayTimer++;
            }
            if (Surface.anInt347 > 0) {
                sleepWordDelayTimer = 0;
            }
            Surface.anInt346 = 0;
            Surface.anInt347 = 0;
        }
        for (int l = 0; l < playerCount; l++) {
            Mob character = players[l];
            if (character.projectileRange > 0) {
                character.projectileRange--;
            }
        }

        if (cameraAutoAngleDebug) {
            if (cameraAutoRotatePlayerX - localPlayer.currentX < -500 || cameraAutoRotatePlayerX - localPlayer.currentX > 500 || cameraAutoRotatePlayerY - localPlayer.currentY < -500 || cameraAutoRotatePlayerY - localPlayer.currentY > 500) {
                cameraAutoRotatePlayerX = localPlayer.currentX;
                cameraAutoRotatePlayerY = localPlayer.currentY;
            }
        } else {
            if (cameraAutoRotatePlayerX - localPlayer.currentX < -500 || cameraAutoRotatePlayerX - localPlayer.currentX > 500 || cameraAutoRotatePlayerY - localPlayer.currentY < -500 || cameraAutoRotatePlayerY - localPlayer.currentY > 500) {
                cameraAutoRotatePlayerX = localPlayer.currentX;
                cameraAutoRotatePlayerY = localPlayer.currentY;
            }
            if (cameraAutoRotatePlayerX != localPlayer.currentX) {
                cameraAutoRotatePlayerX += (localPlayer.currentX - cameraAutoRotatePlayerX) / (16 + (cameraZoom - 500) / 15);
            }
            if (cameraAutoRotatePlayerY != localPlayer.currentY) {
                cameraAutoRotatePlayerY += (localPlayer.currentY - cameraAutoRotatePlayerY) / (16 + (cameraZoom - 500) / 15);
            }
            if (optionCameraModeAuto) {
                int k1 = cameraAngle * 32;
                int j3 = k1 - cameraRotation;
                byte byte0 = 1;
                if (j3 != 0) {
                    anInt707++;
                    if (j3 > 128) {
                        byte0 = -1;
                        j3 = 256 - j3;
                    } else if (j3 > 0) {
                        byte0 = 1;
                    } else if (j3 < -128) {
                        byte0 = 1;
                        j3 = 256 + j3;
                    } else if (j3 < 0) {
                        byte0 = -1;
                        j3 = -j3;
                    }
                    cameraRotation += ((anInt707 * j3 + 255) / 256) * byte0;
                    cameraRotation &= 255;// 0xff;
                } else {
                    anInt707 = 0;
                }
            }
        }
        if (sleepWordDelayTimer > 20) {
            sleepWordDelayTimer = 0;
        }
        if (isSleeping) {
            if (super.inputTextFinal.length() > 0) {
                if (super.inputTextFinal.equalsIgnoreCase("escape")) {
                    isSleeping = false;
                    return;
                }
                boolean success = ActionManager.get(SleepHandler.class).handleGuess(super.inputTextFinal);
                super.inputTextCurrent = "";
                super.inputTextFinal = "";
                sleepingStatusText = "Please wait...";
                if(!success) {
                    player.getSender().sendEnterSleep();
                }
            }
            if (super.lastMouseButtonDown == 1 && super.mouseY > 275 && super.mouseY < 310 && super.mouseX > 56 && super.mouseX < 456) {
                // send new sleepword here
                super.inputTextCurrent = "";
                super.inputTextFinal = "";
                sleepingStatusText = "Please wait...";
                player.getSender().sendEnterSleep();
            }
            super.lastMouseButtonDown = 0;
            return;
        }
        int OFFSET = 20;
        if (super.mouseY > gameHeight - 4 - OFFSET) {
            if (super.mouseX > 15 + (gameWidth / 2 - 256) && super.mouseX < 96 + (gameWidth / 2 - 256) && super.lastMouseButtonDown == 1) {
                messageTabSelected = 0;
            }
            if (super.mouseX > 110 + (gameWidth / 2 - 256) && super.mouseX < 194 + (gameWidth / 2 - 256) && super.lastMouseButtonDown == 1) {
                messageTabSelected = 1;
                panelMessageTabs.controlScrollAmount[controlTextListChat] = 999999;
            }
            if (super.mouseX > 215 + (gameWidth / 2 - 256) && super.mouseX < 295 + (gameWidth / 2 - 256) && super.lastMouseButtonDown == 1) {
                messageTabSelected = 2;
                panelMessageTabs.controlScrollAmount[controlTextListQuest] = 999999;
            }
            if (super.mouseX > 315 + (gameWidth / 2 - 256) && super.mouseX < 395 + (gameWidth / 2 - 256) && super.lastMouseButtonDown == 1) {
                messageTabSelected = 3;
                panelMessageTabs.controlScrollAmount[controlTextListPrivate] = 999999;
            }
            if (super.mouseX > 417 + (gameWidth / 2 - 256) && super.mouseX < 497 + (gameWidth / 2 - 256) && super.lastMouseButtonDown == 1) {
                player.getSender().sendMessage("This feature has been disabled");
            }
            super.lastMouseButtonDown = 0;
            super.mouseButtonDown = 0;
        }
        panelMessageTabs.handleMouse(super.mouseX, super.mouseY, super.lastMouseButtonDown, super.mouseButtonDown);
        if (messageTabSelected > 0 && super.mouseX >= 494 && super.mouseY >= gameHeight - 66) {
            super.lastMouseButtonDown = 0;
        }
        if (panelMessageTabs.isClicked(controlTextListAll)) {
            String s = panelMessageTabs.getText(controlTextListAll);
            panelMessageTabs.updateText(controlTextListAll, "");
            if (s.startsWith("::")) {
                ActionManager.get(CommandHandler.class).handleCommand(s.substring(2));
            } else {
                localPlayer.messageTimeout = 150;
                localPlayer.message = s;
                showMessage(localPlayer.name + ": " + s, 2);
            }
        }
        if (messageTabSelected == 0) {
            for (int l1 = 0; l1 < 5; l1++) {
                if (messageHistoryTimeout[l1] > 0) {
                    messageHistoryTimeout[l1]--;
                }
            }
        }
        if (deathScreenTimeout != 0) {
            super.lastMouseButtonDown = 0;
        }
        if (super.lastMouseButtonDown == 1) {
            mouseButtonClick = 1;
        } else if (super.lastMouseButtonDown == 2) {
            mouseButtonClick = 2;
        }
        scene.setMouseLoc(super.mouseX, super.mouseY);
        super.lastMouseButtonDown = 0;
        if (optionCameraModeAuto) {
            if (anInt707 == 0 || cameraAutoAngleDebug) {
                if (super.keyLeft) {
                    cameraAngle = cameraAngle + 1 & 7;
                    super.keyLeft = false;
                    if (!fogOfWar) {
                        if ((cameraAngle & 1) == 0) {
                            cameraAngle = cameraAngle + 1 & 7;
                        }
                        for (int i2 = 0; i2 < 8; i2++) {
                            if (isValidCameraAngle(cameraAngle)) {
                                break;
                            }
                            cameraAngle = cameraAngle + 1 & 7;
                        }

                    }
                }
                if (super.keyRight) {
                    cameraAngle = cameraAngle + 7 & 7;
                    super.keyRight = false;
                    if (!fogOfWar) {
                        if ((cameraAngle & 1) == 0) {
                            cameraAngle = cameraAngle + 7 & 7;
                        }
                        for (int j2 = 0; j2 < 8; j2++) {
                            if (isValidCameraAngle(cameraAngle)) {
                                break;
                            }
                            cameraAngle = cameraAngle + 7 & 7;
                        }

                    }
                }
            }
        } else if (super.keyLeft) {
            cameraRotation = cameraRotation + 2 & 255;// 0xff;
        } else if (super.keyRight) {
            cameraRotation = cameraRotation - 2 & 255;// 0xff;
        } else if (super.keyDown) {
            //
        } else if (super.keyUp) {
            //
        }
        if (fogOfWar && cameraZoom > 550 - 180) {
            cameraZoom -= 4;
        } else if (!fogOfWar && cameraZoom < 750 - 300) {
            cameraZoom += 4;
        }
        if (mouseClickXStep > 0) {
            mouseClickXStep--;
        } else if (mouseClickXStep < 0) {
            mouseClickXStep++;
        }
        scene.animateTexture(17); /* animates water texture? (fountains, portals, fish, etc) */
        objectAnimationCount++;
        if (objectAnimationCount > 5) {
            objectAnimationCount = 0;
            objectAnimationNumberFireLightningSpell = (objectAnimationNumberFireLightningSpell + 1) % 3;
            objectAnimationNumberTorch = (objectAnimationNumberTorch + 1) % 4;
            objectAnimationNumberClaw = (objectAnimationNumberClaw + 1) % 5;
        }
        for (int k2 = 0; k2 < objectCount; k2++) {
            int l3 = objectX[k2];
            int l4 = objectY[k2];
            if (l3 >= 0 && l4 >= 0 && l3 < 96 && l4 < 96 && objectID[k2] == 74) { /* rotates the windmill sails */
                //objectModel[k2].rotate((int) (Math.random() * 10), (int) (Math.random() * 10), (int) (Math.random() * 10));
                objectModel[k2].rotate(1, 0, 0);
            }
        }

        for (int i4 = 0; i4 < teleportBubbleCount; i4++) {
            teleportBubbleTime[i4]++;
            if (teleportBubbleTime[i4] > 50) {
                teleportBubbleCount--;
                for (int i5 = i4; i5 < teleportBubbleCount; i5++) {
                    teleportBubbleX[i5] = teleportBubbleX[i5 + 1];
                    teleportBubbleY[i5] = teleportBubbleY[i5 + 1];
                    teleportBubbleTime[i5] = teleportBubbleTime[i5 + 1];
                    teleportBubbleType[i5] = teleportBubbleType[i5 + 1];
                }

            }
        }

    }

    private void renderLoginScreenViewports() {
        int rh = 0;
        byte rx = 50;
        byte ry = 50;
        world.loadSector(rx * 48 + 23, ry * 48 + 23, rh);
        //world.registerObjectDir(rh, rh, rh);
        world.loadSectorObjects(gameModels);
        int x = 9728;
        int y = 6400;
        int zoom = 1100;
        int rotation = 888;
        scene.clipFar3d = 4100;
        scene.clipFar2d = 4100;
        scene.fogZDensity = 1;
        scene.fogZDistance = 4000;
        surface.blackScreen();
        scene.setCamera(x, -world.getAveragedElevation(x, y), y, 912, rotation, 0, zoom * 2);
        scene.render();
        surface.fade2black();
        surface.fade2black();
        surface.drawBox(0, 0, gameWidth, 6, 0);
        for (int j = 6; j >= 1; j--) {
            surface.drawLineAlpha(0, j, 0, j, gameWidth, 8);
        }

        surface.drawBox(0, 194, gameWidth, 20, 0);
        for (int k = 6; k >= 1; k--) {
            surface.drawLineAlpha(0, k, 0, 194 - k, gameWidth, 8);
        }

        surface.drawSprite(gameWidth / 2 - surface.spriteWidth[spriteMedia + 10] / 2, 15, spriteMedia + 10); // runescape logo
        surface.drawSpriteClipping(spriteLogo, 0, 0, gameWidth, 200);
        surface.drawWorld(spriteLogo);
        x = 9216;
        y = 9216;
        zoom = 1100;
        rotation = 888;
        scene.clipFar3d = 4100;
        scene.clipFar2d = 4100;
        scene.fogZDensity = 1;
        scene.fogZDistance = 4000;
        surface.blackScreen();
        scene.setCamera(x, -world.getAveragedElevation(x, y), y, 912, rotation, 0, zoom * 2);
        scene.render();
        surface.fade2black();
        surface.fade2black();
        surface.drawBox(0, 0, gameWidth, 6, 0);
        for (int l = 6; l >= 1; l--) {
            surface.drawLineAlpha(0, l, 0, l, gameWidth, 8);
        }

        surface.drawBox(0, 194, gameWidth, 20, 0);
        for (int i1 = 6; i1 >= 1; i1--) {
            surface.drawLineAlpha(0, i1, 0, 194 - i1, gameWidth, 8);
        }

        surface.drawSprite(gameWidth / 2 - surface.spriteWidth[spriteMedia + 10] / 2, 15, spriteMedia + 10);
        surface.drawSpriteClipping(spriteLogo + 1, 0, 0, gameWidth, 200);
        surface.drawWorld(spriteLogo + 1);

        for (int j1 = 0; j1 < 64; j1++) {
            scene.removeModel(world.roofModels[0][j1]);
            scene.removeModel(world.wallModels[1][j1]);
            scene.removeModel(world.roofModels[1][j1]);
            scene.removeModel(world.wallModels[2][j1]);
            scene.removeModel(world.roofModels[2][j1]);
        }

        x = 11136;
        y = 10368;
        zoom = 500;
        rotation = 376;
        scene.clipFar3d = 4100;
        scene.clipFar2d = 4100;
        scene.fogZDensity = 1;
        scene.fogZDistance = 4000;
        surface.blackScreen();
        scene.setCamera(x, -world.getAveragedElevation(x, y), y, 912, rotation, 0, zoom * 2);
        scene.render();
        surface.fade2black();
        surface.fade2black();
        surface.drawBox(0, 0, gameWidth, 6, 0);
        for (int k1 = 6; k1 >= 1; k1--) {
            surface.drawLineAlpha(0, k1, 0, k1, gameWidth, 8);
        }

        surface.drawBox(0, 194, gameWidth, 20, 0);
        for (int l1 = 6; l1 >= 1; l1--) {
            surface.drawLineAlpha(0, l1, 0, 194, gameWidth, 8);
        }

        surface.drawSprite(gameWidth / 2 - surface.spriteWidth[spriteMedia + 10] / 2, 15, spriteMedia + 10);
        surface.drawSpriteClipping(spriteMedia + 10, 0, 0, gameWidth, 200);
        surface.drawWorld(spriteMedia + 10);
    }

    public void createLoginPanels() {
        panelLoginWelcome = new Menu(surface, 50);
        int y = 40;
        int x = gameWidth / 2;
        panelLoginWelcome.addText(x, 200 + y, "Welcome to RuneScape Classic", 4, true);
        panelLoginWelcome.addButtonBackground(x - 100, 240 + y, 120, 35);
        panelLoginWelcome.addButtonBackground(x + 100, 240 + y, 120, 35);
        panelLoginWelcome.addText(x - 100, 240 + y, "New User", 5, false);
        panelLoginWelcome.addText(x + 100, 240 + y, "Existing User", 5, false);
        controlWelcomeNewUser = panelLoginWelcome.addButton(x - 100, 240 + y, 120, 35);
        controlWelcomeExistingUser = panelLoginWelcome.addButton(x + 100, 240 + y, 120, 35);
        panelRegisterUser = new Menu(surface, 50);
        y = 10;
        controlRegisterStatus = panelRegisterUser.addText(x, y, "@yel@Fill out the details below to create an account", 4, true);
        controlRegisterTerms = panelRegisterUser.addTextListScrollable(gameWidth / 2 - (400 / 2), 20, 400, 190, 1, 1000, true);
        writeTerms(panelRegisterUser, controlRegisterTerms);
        y += 192;
        y += 36;
        panelRegisterUser.addButtonBackground(gameWidth / 2, y + 17, 240, 34);
        panelRegisterUser.addText(gameWidth / 2, y + 8, "Pick a username:", 4, false);
        controlRegisterUser = panelRegisterUser.addTextInput(gameWidth / 2, y + 24, 240, 34, 4, 12, false, false);
        y += 36;
        panelRegisterUser.addButtonBackground(gameWidth / 2 - 60, y + 17, 100, 25);
        panelRegisterUser.addText(gameWidth / 2 - 60, y + 16, "Create", 4, false);
        controlRegisterSubmit = panelRegisterUser.addButton(gameWidth / 2 - 60, y + 24, 100, 25);
        panelRegisterUser.addButtonBackground(gameWidth / 2 + 60, y + 17, 100, 25);
        panelRegisterUser.addText(gameWidth / 2 + 60, y + 16, "Cancel", 4, false);
        controlRegisterCancel = panelRegisterUser.addButton(gameWidth / 2 + 60, y + 24, 100, 25);
        panelRegisterUser.setFocus(controlRegisterUser);

        panelLoginExistingUser = new Menu(surface, 50);
        y = 230;
        controlLoginStatus1 = panelLoginExistingUser.addText(x, y - 30, "", 4, true);
        controlLoginStatus2 = panelLoginExistingUser.addText(x, y - 10, "Please enter your username", 4, true);
        y += 28;
        panelLoginExistingUser.addButtonBackground(gameWidth / 2, y + 8, 240, 36);
        panelLoginExistingUser.addText(gameWidth / 2, y - 2, "Username:", 4, false);
        controlLoginUser = panelLoginExistingUser.addTextInput(gameWidth / 2, y + 16, 240, 36, 4, 12, false, false);
        y += 28;
        panelLoginExistingUser.addButtonBackground(gameWidth / 2 - 60, y + 17, 100, 25);
        panelLoginExistingUser.addText(gameWidth / 2 - 60, y + 16, "Login", 4, false);
        controlLoginOk = panelLoginExistingUser.addButton(gameWidth / 2 - 60, y + 24, 100, 25);
        panelLoginExistingUser.addButtonBackground(gameWidth / 2 + 60, y + 17, 100, 25);
        panelLoginExistingUser.addText(gameWidth / 2 + 60, y + 16, "Cancel", 4, false);
        controlLoginCancel = panelLoginExistingUser.addButton(gameWidth / 2 + 60, y + 24, 100, 25);
        panelLoginExistingUser.setFocus(controlLoginUser);
    }

    private int controlRegisterTerms;

    public void writeTerms(Menu panel, int control) {
        panel.addListEntry("About this software", control);
        panel.addListEntry("", control);
        panel.addListEntry("RSC Single Player is a one player RuneScape Classic clone. No need", control);
        panel.addListEntry("for any sort of internet connection what-so-ever to play this game.", control);
        panel.addListEntry("This is essentially a heavily modified RSCL base, with stripped", control);
        panel.addListEntry("networking and packet handling. It had taken me nearly 6 months to", control);
        panel.addListEntry("complete this and create new loading routines, however it is finally", control);
        panel.addListEntry("fully functional. The client handles itself and uses no server.", control);
        panel.addListEntry("", control);
        panel.addListEntry("@yel@The game mode is currently set to P2P with ALL content included.", control);
        panel.addListEntry("@yel@This includes all members skills, NPC dialogues, and quests.", control);
        panel.addListEntry("", control);
        panel.addListEntry("", control);
        panel.addListEntry("REQUIREMENTS", control);
        panel.addListEntry("", control);
        panel.addListEntry("  * Java 8 or newer", control);
        panel.addListEntry("", control);
        panel.addListEntry("LICENSE", control);
        panel.addListEntry("", control);
        panel.addListEntry("Copyright " + Calendar.getInstance().get(Calendar.YEAR) + " Sean Niemann", control);
        panel.addListEntry("", control);
        panel.addListEntry("Permission is hereby granted, free of charge, to any person", control);
        panel.addListEntry("obtaining a copy of this software and associated documentation", control);
        panel.addListEntry("files (the \"Software\"), to deal in the Software without", control);
        panel.addListEntry("restriction, including without limitation the rights to use,", control);
        panel.addListEntry("copy, modify, merge, publish, distribute, sublicense, and/or", control);
        panel.addListEntry("sell copies of the Software, and to permit persons to whom the", control);
        panel.addListEntry("Software is furnished to do so, subject to the following conditions:", control);
        panel.addListEntry("", control);
        panel.addListEntry("The above copyright notice and this permission notice shall be", control);
        panel.addListEntry("included in all copies or substantial portions of the Software.", control);
        panel.addListEntry("", control);
        panel.addListEntry("THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY", control);
        panel.addListEntry("KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE", control);
        panel.addListEntry("WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR", control);
        panel.addListEntry("PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHOR", control);
        panel.addListEntry("OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR", control);
        panel.addListEntry("OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR", control);
        panel.addListEntry("OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE", control);
        panel.addListEntry("SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.", control);
    }

    private void drawUiTabInventory(boolean nomenus) {
        int uiX = surface.width2 - 248;
        surface.drawSprite(uiX, 3, spriteMedia + 1);
        for (int itemIndex = 0; itemIndex < inventoryMaxItemCount; itemIndex++) {
            int slotX = uiX + (itemIndex % 5) * 49;
            int slotY = 36 + (itemIndex / 5) * 34;
            if (itemIndex < inventoryItemsCount && inventoryEquipped[itemIndex] == 1) {
                surface.drawBoxAlpha(slotX, slotY, 49, 34, 0xff0000, 128);
            } else {
                surface.drawBoxAlpha(slotX, slotY, 49, 34, Surface.rgb2long(181, 181, 181), 128);
            }
            if (itemIndex < inventoryItemsCount) {
                surface.spriteClipping(slotX, slotY, 48, 32, spriteItem + EntityManager.getItem(inventoryItemId[itemIndex]).getSprite(), EntityManager.getItem(inventoryItemId[itemIndex]).getMask(), 0, 0, false);
                if (EntityManager.getItem(inventoryItemId[itemIndex]).isStackable()) {
                    surface.drawString(String.valueOf(inventoryItemStackCount[itemIndex]), slotX + 1, slotY + 10, 1, 0xffff00);
                }
            }
        }

        for (int rows = 1; rows <= 4; rows++) {
            surface.drawLineVert(uiX + rows * 49, 36, (inventoryMaxItemCount / 5) * 34, 0);
        }

        for (int cols = 1; cols <= inventoryMaxItemCount / 5 - 1; cols++) {
            surface.drawLineHoriz(uiX, 36 + cols * 34, 245, 0);
        }

        if (!nomenus) {
            return;
        }
        int mouseX = super.mouseX - (surface.width2 - 248);
        int mouseY = super.mouseY - 36;
        if (mouseX >= 0 && mouseY >= 0 && mouseX < 248 && mouseY < (inventoryMaxItemCount / 5) * 34) {
            int itemIndex = mouseX / 49 + (mouseY / 34) * 5;
            if (itemIndex < inventoryItemsCount) {
                int i2 = inventoryItemId[itemIndex];
                if (selectedSpell >= 0) {
                    if (EntityManager.getSpell(selectedSpell).getType() == 3) {
                        menuItemText1[menuItemsCount] = "Cast " + EntityManager.getSpell(selectedSpell).getName() + " on";
                        menuItemText2[menuItemsCount] = "@lre@" + EntityManager.getItem(i2).getName();
                        menuItemID[menuItemsCount] = 600;
                        menuSourceType[menuItemsCount] = itemIndex;
                        menuSourceIndex[menuItemsCount] = selectedSpell;
                        menuItemsCount++;
                        return;
                    }
                } else {
                    if (selectedItemInventoryIndex >= 0) {
                        menuItemText1[menuItemsCount] = "Use " + selectedItemName + " with";
                        menuItemText2[menuItemsCount] = "@lre@" + EntityManager.getItem(i2).getName();
                        menuItemID[menuItemsCount] = 610;
                        menuSourceType[menuItemsCount] = itemIndex;
                        menuSourceIndex[menuItemsCount] = selectedItemInventoryIndex;
                        menuItemsCount++;
                        return;
                    }
                    if (inventoryEquipped[itemIndex] == 1) {
                        menuItemText1[menuItemsCount] = "Remove";
                        menuItemText2[menuItemsCount] = "@lre@" + EntityManager.getItem(i2).getName();
                        menuItemID[menuItemsCount] = 620;
                        menuSourceType[menuItemsCount] = itemIndex;
                        menuItemsCount++;
                    } else if (EntityManager.getItem(i2).isWieldable()) {
                        menuItemText1[menuItemsCount] = EntityManager.getItem(i2).getWearCommand();
                        menuItemText2[menuItemsCount] = "@lre@" + EntityManager.getItem(i2).getName();
                        menuItemID[menuItemsCount] = 630;
                        menuSourceType[menuItemsCount] = itemIndex;
                        menuItemsCount++;
                    }
                    if (!EntityManager.getItem(i2).getCommand().equals("")) {
                        menuItemText1[menuItemsCount] = EntityManager.getItem(i2).getCommand();
                        menuItemText2[menuItemsCount] = "@lre@" + EntityManager.getItem(i2).getName();
                        menuItemID[menuItemsCount] = 640;
                        menuSourceType[menuItemsCount] = itemIndex;
                        menuItemsCount++;
                    }
                    menuItemText1[menuItemsCount] = "Use";
                    menuItemText2[menuItemsCount] = "@lre@" + EntityManager.getItem(i2).getName();
                    menuItemID[menuItemsCount] = 650;
                    menuSourceType[menuItemsCount] = itemIndex;
                    menuItemsCount++;
                    menuItemText1[menuItemsCount] = "Drop";
                    menuItemText2[menuItemsCount] = "@lre@" + EntityManager.getItem(i2).getName();
                    menuItemID[menuItemsCount] = 660;
                    menuSourceType[menuItemsCount] = itemIndex;
                    menuItemsCount++;
                    menuItemText1[menuItemsCount] = "Examine";
                    menuItemText2[menuItemsCount] = "@lre@" + EntityManager.getItem(i2).getName();
                    menuItemID[menuItemsCount] = 3600;
                    menuSourceType[menuItemsCount] = i2;
                    menuItemsCount++;
                }
            }
        }
    }

    private void autorotateCamera() {
        if ((cameraAngle & 1) == 1 && isValidCameraAngle(cameraAngle)) {
            return;
        }
        if ((cameraAngle & 1) == 0 && isValidCameraAngle(cameraAngle)) {
            if (isValidCameraAngle(cameraAngle + 1 & 7)) {
                cameraAngle = cameraAngle + 1 & 7;
                return;
            }
            if (isValidCameraAngle(cameraAngle + 7 & 7)) {
                cameraAngle = cameraAngle + 7 & 7;
            }
            return;
        }
        int ai[] = {
            1, -1, 2, -2, 3, -3, 4
        };
        for (int i = 0; i < 7; i++) {
            if (!isValidCameraAngle(cameraAngle + ai[i] + 8 & 7)) {
                continue;
            }
            cameraAngle = cameraAngle + ai[i] + 8 & 7;
            break;
        }

        if ((cameraAngle & 1) == 0 && isValidCameraAngle(cameraAngle)) {
            if (isValidCameraAngle(cameraAngle + 1 & 7)) {
                cameraAngle = cameraAngle + 1 & 7;
                return;
            }
            if (isValidCameraAngle(cameraAngle + 7 & 7)) {
                cameraAngle = cameraAngle + 7 & 7;
            }
        }
    }

    private void drawRightClickMenu() {
        if (mouseButtonClick != 0) {
            for (int i = 0; i < menuItemsCount; i++) {
                int k = menuX + 2;
                int i1 = menuY + 27 + i * 15;
                if (super.mouseX <= k - 2 || super.mouseY <= i1 - 12 || super.mouseY >= i1 + 4 || super.mouseX >= (k - 3) + menuWidth) {
                    continue;
                }
                menuItemClick(menuIndices[i]);
                break;
            }

            mouseButtonClick = 0;
            showRightClickMenu = false;
            return;
        }
        if (super.mouseX < menuX - 10 || super.mouseY < menuY - 10 || super.mouseX > menuX + menuWidth + 10 || super.mouseY > menuY + menuHeight + 10) {
            showRightClickMenu = false;
            return;
        }
        surface.drawBoxAlpha(menuX, menuY, menuWidth, menuHeight, 0xd0d0d0, 160);
        surface.drawString("Choose option", menuX + 2, menuY + 12, 1, 65535);
        for (int j = 0; j < menuItemsCount; j++) {
            int l = menuX + 2;
            int j1 = menuY + 27 + j * 15;
            int k1 = 0xffffff;
            if (super.mouseX > l - 2 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && super.mouseX < (l - 3) + menuWidth) {
                k1 = 0xffff00;
            }
            surface.drawString(menuItemText1[menuIndices[j]] + " " + menuItemText2[menuIndices[j]], l, j1, 1, k1);
        }

    }

    private void drawUiTabMinimap(boolean nomenus) {
        int uiX = surface.width2 - 199;
        int uiWidth = 156;
        int uiHeight = 152;
        surface.drawSprite(uiX - 49, 3, spriteMedia + 2);
        uiX += 40;
        surface.drawBox(uiX, 36, uiWidth, uiHeight, 0);
        surface.setBounds(uiX, 36, uiX + uiWidth, 36 + uiHeight);
        int k = 192 + minimapRandom_2;
        int i1 = cameraRotation + minimapRandom_1 & 255;//0xff;
        int k1 = ((localPlayer.currentX - 6040) * 3 * k) / 2048;
        int i3 = ((localPlayer.currentY - 6040) * 3 * k) / 2048;
        int k4 = Scene.sin2048Cache[1024 - i1 * 4 & 0x3ff];
        int i5 = Scene.sin2048Cache[(1024 - i1 * 4 & 0x3ff) + 1024];
        int k5 = i3 * k4 + k1 * i5 >> 18;
        i3 = i3 * i5 - k1 * k4 >> 18;
        k1 = k5;
        surface.drawMinimapSprite((uiX + uiWidth / 2) - k1, 36 + uiHeight / 2 + i3, spriteMedia - 1, i1 + 64 & 255, k);// landscape
        for (int i = 0; i < objectCount; i++) {
            int l1 = (((objectX[i] * magicLoc + 64) - localPlayer.currentX) * 3 * k) / 2048;
            int j3 = (((objectY[i] * magicLoc + 64) - localPlayer.currentY) * 3 * k) / 2048;
            int l5 = j3 * k4 + l1 * i5 >> 18;
            j3 = j3 * i5 - l1 * k4 >> 18;
            l1 = l5;
            drawMinimapEntity(uiX + uiWidth / 2 + l1, (36 + uiHeight / 2) - j3, 65535);
        }

        for (int j7 = 0; j7 < groundItemCount; j7++) {
            int i2 = (((groundItemX[j7] * magicLoc + 64) - localPlayer.currentX) * 3 * k) / 2048;
            int k3 = (((groundItemY[j7] * magicLoc + 64) - localPlayer.currentY) * 3 * k) / 2048;
            int i6 = k3 * k4 + i2 * i5 >> 18;
            k3 = k3 * i5 - i2 * k4 >> 18;
            i2 = i6;
            drawMinimapEntity(uiX + uiWidth / 2 + i2, (36 + uiHeight / 2) - k3, 0xff0000);
        }

        for (int k7 = 0; k7 < npcCount; k7++) {
            Mob character = npcs[k7];
            int j2 = ((character.currentX - localPlayer.currentX) * 3 * k) / 2048;
            int l3 = ((character.currentY - localPlayer.currentY) * 3 * k) / 2048;
            int j6 = l3 * k4 + j2 * i5 >> 18;
            l3 = l3 * i5 - j2 * k4 >> 18;
            j2 = j6;
            drawMinimapEntity(uiX + uiWidth / 2 + j2, (36 + uiHeight / 2) - l3, 0xffff00);
        }

        for (int l7 = 0; l7 < playerCount; l7++) {
            Mob character_1 = players[l7];
            int k2 = ((character_1.currentX - localPlayer.currentX) * 3 * k) / 2048;
            int i4 = ((character_1.currentY - localPlayer.currentY) * 3 * k) / 2048;
            int k6 = i4 * k4 + k2 * i5 >> 18;
            i4 = i4 * i5 - k2 * k4 >> 18;
            k2 = k6;
            int j8 = 0xffffff;
            drawMinimapEntity(uiX + uiWidth / 2 + k2, (36 + uiHeight / 2) - i4, j8);
        }

        surface.drawCircle(uiX + uiWidth / 2, 36 + uiHeight / 2, 2, 0xffffff, 255);
        surface.drawMinimapSprite(uiX + 19, 55, spriteMedia + 24, cameraRotation + 128 & 255, 128); // compass
        surface.setBounds(0, 0, gameWidth, gameHeight + 12);
        if (!nomenus) {
            return;
        }
        int mouseX = super.mouseX - (surface.width2 - 199);
        int mouseY = super.mouseY - 36;
        if (mouseX >= 40 && mouseY >= 0 && mouseX < 196 && mouseY < 152) {
            int c1 = 156;// '\234';
            int c3 = 152;// '\230';
            int l = 192 + minimapRandom_2;
            int j1 = cameraRotation + minimapRandom_1 & 255;
            int j = surface.width2 - 199;
            j += 40;
            int dx = ((super.mouseX - (j + c1 / 2)) * 16384) / (3 * l);
            int dy = ((super.mouseY - (36 + c3 / 2)) * 16384) / (3 * l);
            int l4 = Scene.sin2048Cache[1024 - j1 * 4 & 1023];
            int j5 = Scene.sin2048Cache[(1024 - j1 * 4 & 1023) + 1024];
            int l6 = dy * l4 + dx * j5 >> 15;
            dy = dy * j5 - dx * l4 >> 15;
            dx = l6;
            dx += localPlayer.currentX;
            dy = localPlayer.currentY - dy;
            if (mouseButtonClick == 1) {
                walkToActionSource(localRegionX, localRegionY, dx / 128, dy / 128, false);
            } else if (mouseButtonClick == 2) {
                ActionManager.get(CommandHandler.class).handleCommand("teleport " + (regionX + dx / 128) + " " + (regionY + dy / 128));
            }
            mouseButtonClick = 0;
        }
    }

    private void setActiveUiTab() {
        if (showUiTab == 0 && super.mouseX >= surface.width2 - 35 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 && super.mouseY < 35) {
            showUiTab = 1;
        }
        if (showUiTab == 0 && super.mouseX >= surface.width2 - 35 - 33 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 33 && super.mouseY < 35) {
            showUiTab = 2;
            minimapRandom_1 = (int) (Math.random() * 13D) - 6;
            minimapRandom_2 = (int) (Math.random() * 23D) - 11;
        }
        if (showUiTab == 0 && super.mouseX >= surface.width2 - 35 - 66 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 66 && super.mouseY < 35) {
            showUiTab = 3;
        }
        if (showUiTab == 0 && super.mouseX >= surface.width2 - 35 - 99 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 99 && super.mouseY < 35) {
            showUiTab = 4;
        }
        if (showUiTab == 0 && super.mouseX >= surface.width2 - 35 - 132 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 132 && super.mouseY < 35) {
            showUiTab = 5;
        }
        if (showUiTab == 0 && super.mouseX >= surface.width2 - 35 - 165 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 165 && super.mouseY < 35) {
            showUiTab = 6;
        }
        if (showUiTab != 0 && super.mouseX >= surface.width2 - 35 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 && super.mouseY < 26) {
            showUiTab = 1;
        }
        if (showUiTab != 0 && showUiTab != 2 && super.mouseX >= surface.width2 - 35 - 33 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 33 && super.mouseY < 26) {
            showUiTab = 2;
            minimapRandom_1 = (int) (Math.random() * 13D) - 6;
            minimapRandom_2 = (int) (Math.random() * 23D) - 11;
        }
        if (showUiTab != 0 && super.mouseX >= surface.width2 - 35 - 66 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 66 && super.mouseY < 26) {
            showUiTab = 3;
        }
        if (showUiTab != 0 && super.mouseX >= surface.width2 - 35 - 99 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 99 && super.mouseY < 26) {
            showUiTab = 4;
        }
        if (showUiTab != 0 && super.mouseX >= surface.width2 - 35 - 132 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 132 && super.mouseY < 26) {
            showUiTab = 5;
        }
        if (showUiTab != 0 && super.mouseX >= surface.width2 - 35 - 165 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 165 && super.mouseY < 26) {
            showUiTab = 6;
        }
        if (showUiTab == 1 && (super.mouseX < surface.width2 - 248 || super.mouseY > 36 + (inventoryMaxItemCount / 5) * 34)) {
            showUiTab = 0;
        }
        if (showUiTab == 3 && (super.mouseX < surface.width2 - 199 || super.mouseY > 316)) {
            showUiTab = 0;
        }
        if ((showUiTab == 2 || showUiTab == 4 || showUiTab == 5) && (super.mouseX < surface.width2 - 199 || super.mouseY > 240)) {
            showUiTab = 0;
        }
        if (showUiTab == 6 && (super.mouseX < surface.width2 - 199 || super.mouseY > 157 + 14)) {
            showUiTab = 0;
        }
    }

    private void drawOptionMenu() {
        if (mouseButtonClick != 0) {
            boolean found = false;
            for (int i = 0; i < optionMenuCount; i++) {
                if (super.mouseX >= surface.textWidth(optionMenuEntry[i], 1) || super.mouseY <= i * 12 || super.mouseY >= 12 + i * 12) {
                    continue;
                }
                found = true;
                ActionManager.get(OptionHandler.class).handleOption(i);
                break;
            }

            if (!found) {
                ActionManager.get(OptionHandler.class).handleOption(-1);
            }

            mouseButtonClick = 0;
            showOptionMenu = false;
            return;
        }
        for (int j = 0; j < optionMenuCount; j++) {
            int k = 65535;
            if (super.mouseX < surface.textWidth(optionMenuEntry[j], 1) && super.mouseY > j * 12 && super.mouseY < 12 + j * 12) {
                k = 0xff0000;
            }
            surface.drawString(optionMenuEntry[j], 6, 12 + j * 12, 1, k);
        }

    }

    void drawNpc(int x, int y, int w, int h, int id, int tx, int ty) {
        Mob character = npcs[id];
        int l1 = character.animationCurrent + (cameraRotation + 16) / 32 & 7;
        boolean flag = false;
        int i2 = l1;
        if (i2 == 5) {
            i2 = 3;
            flag = true;
        } else if (i2 == 6) {
            i2 = 2;
            flag = true;
        } else if (i2 == 7) {
            i2 = 1;
            flag = true;
        }
        int j2 = i2 * 3 + npcWalkModel[(character.stepCount / EntityManager.getNPC(character.npcId).getWalkModel()) % 4];
        if (character.animationCurrent == 8) {
            i2 = 5;
            l1 = 2;
            flag = false;
            x -= (EntityManager.getNPC(character.npcId).getCombatSprite() * ty) / 100;
            j2 = i2 * 3 + npcCombatModelArray1[(loginTimer / (EntityManager.getNPC(character.npcId).getCombatModel() - 1)) % 8];
        } else if (character.animationCurrent == 9) {
            i2 = 5;
            l1 = 2;
            flag = true;
            x += (EntityManager.getNPC(character.npcId).getCombatSprite() * ty) / 100;
            j2 = i2 * 3 + npcCombatModelArray2[(loginTimer / EntityManager.getNPC(character.npcId).getCombatModel()) % 8];
        }
        for (int k2 = 0; k2 < 12; k2++) {
            int l2 = npcAnimationArray[l1][k2];
            int k3 = EntityManager.getNPC(character.npcId).getSprite(l2);
            if (k3 >= 0) {
                int i4 = 0;
                int j4 = 0;
                int k4 = j2;
                if (flag && i2 >= 1 && i2 <= 3 && EntityManager.getAnimation(k3).hasF()) {
                    k4 += 15;
                }
                if (i2 != 5 || EntityManager.getAnimation(k3).hasA()) {
                    int l4 = k4 + EntityManager.getAnimation(k3).getNumber();
                    i4 = (i4 * w) / surface.spriteWidthFull[l4];
                    j4 = (j4 * h) / surface.spriteHeightFull[l4];
                    int i5 = (w * surface.spriteWidthFull[l4]) / surface.spriteWidthFull[EntityManager.getAnimation(k3).getNumber()];
                    i4 -= (i5 - w) / 2;
                    int col = EntityManager.getAnimation(k3).getCharColour();
                    int skincol = 0;
                    if (col == 1) {
                        col = EntityManager.getNPC(character.npcId).getHairColor();
                        skincol = EntityManager.getNPC(character.npcId).getSkinColor();
                    } else if (col == 2) {
                        col = EntityManager.getNPC(character.npcId).getShirtColour();
                        skincol = EntityManager.getNPC(character.npcId).getSkinColor();
                    } else if (col == 3) {
                        col = EntityManager.getNPC(character.npcId).getPantsColor();
                        skincol = EntityManager.getNPC(character.npcId).getSkinColor();
                    }
                    surface.spriteClipping(x + i4, y + j4, i5, h, l4, col, skincol, tx, flag);
                }
            }
        }

        if (character.messageTimeout > 0) {
            receivedMessageMidPoint[receivedMessagesCount] = surface.textWidth(character.message, 1) / 2;
            if (receivedMessageMidPoint[receivedMessagesCount] > 150) {
                receivedMessageMidPoint[receivedMessagesCount] = 150;
            }
            receivedMessageHeight[receivedMessagesCount] = (surface.textWidth(character.message, 1) / 300) * surface.textHeight(1);
            receivedMessageX[receivedMessagesCount] = x + w / 2;
            receivedMessageY[receivedMessagesCount] = y;
            receivedMessages[receivedMessagesCount++] = character.message;
        }
        if (character.animationCurrent == 8 || character.animationCurrent == 9 || character.combatTimer != 0) {
            if (character.combatTimer > 0) {
                int i3 = x;
                if (character.animationCurrent == 8) {
                    i3 -= (20 * ty) / 100;
                } else if (character.animationCurrent == 9) {
                    i3 += (20 * ty) / 100;
                }
                int l3 = (character.healthCurrent * 30) / character.healthMax;
                healthBarX[healthBarCount] = i3 + w / 2;
                healthBarY[healthBarCount] = y;
                healthBarMissing[healthBarCount++] = l3;
            }
            if (character.combatTimer > 150) {
                int j3 = x;
                if (character.animationCurrent == 8) {
                    j3 -= (10 * ty) / 100;
                } else if (character.animationCurrent == 9) {
                    j3 += (10 * ty) / 100;
                }
                surface.drawSprite((j3 + w / 2) - 12, (y + h / 2) - 12, spriteMedia + 12);
                surface.drawStringCenter(String.valueOf(character.damageTaken), (j3 + w / 2) - 1, y + h / 2 + 5, 3, 0xffffff);
            }
        }
    }

    @Override
    public Image createImage(int i, int j) {
        if (application != null) {
            return application.createImage(i, j);
        }
        return super.createImage(i, j);
    }

    private boolean walkToWallObject(int i, int j, int k) {
        if (k == 0) {
            return walkToActionSource(localRegionX, localRegionY, i, j - 1, i, j, false, true);
        }
        if (k == 1) {
            return walkToActionSource(localRegionX, localRegionY, i - 1, j, i, j, false, true);
        } else {
            return walkToActionSource(localRegionX, localRegionY, i, j, i, j, true, true);
        }
    }

    private void loadGameConfig() {
        // load cache here
    }

    public Mob addNpc(int serverIndex, int x, int y, int sprite, int type) {
        if (npcsServer[serverIndex] == null) {
            npcsServer[serverIndex] = new Mob();
            npcsServer[serverIndex].serverIndex = serverIndex;
        }
        Mob character = npcsServer[serverIndex];
        boolean foundNpc = false;
        for (int i = 0; i < npcCacheCount; i++) {
            if (npcsCache[i].serverIndex != serverIndex) {
                continue;
            }
            foundNpc = true;
            break;
        }

        if (foundNpc) {
            character.npcId = type;
            character.animationNext = sprite;
            int waypointIdx = character.waypointCurrent;
            if (x != character.waypointsX[waypointIdx] || y != character.waypointsY[waypointIdx]) {
                character.waypointCurrent = waypointIdx = (waypointIdx + 1) % 10;
                character.waypointsX[waypointIdx] = x;
                character.waypointsY[waypointIdx] = y;
            }
        } else {
            character.serverIndex = serverIndex;
            character.movingStep = 0;
            character.waypointCurrent = 0;
            character.waypointsX[0] = character.currentX = x;
            character.waypointsY[0] = character.currentY = y;
            character.npcId = type;
            character.animationNext = character.animationCurrent = sprite;
            character.stepCount = 0;
        }
        npcs[npcCount++] = character;
        return character;
    }
    
    private Dimension tempDimension;

    public void resetLoginVars() {
        loginScreen = 0;
        loggedIn = 0;
        logoutTimeout = 0;
        tempDimension = new Dimension(gameWidth, gameHeight);
        application.setResizable(false);
        application.setSize(Constants.APPLICATION_WIDTH, Constants.APPLICATION_HEIGHT);
        application.setLocationRelativeTo(null);
    }

    private void deposit(int amount) {
        if (bankSelectedItemSlot >= 0) {
            ActionManager.get(BankHandler.class).handleDeposit(bankItems[bankSelectedItemSlot], amount);
        }
    }

    private void withdraw(int amount) {
        if (bankSelectedItemSlot >= 0) {
            ActionManager.get(BankHandler.class).handleWithdrawl(bankItems[bankSelectedItemSlot], amount);
        }
    }

    boolean bankSwap = false;
    int bankSwapItem1 = -1;
    int bankSwapItem2 = -1;
    int bankSwapSlot1 = -1;
    int bankSwapSlot2 = -1;

    private long lastBankSwap = 0;

    public void setLastBankSwap() {
        lastBankSwap = System.currentTimeMillis();
    }

    public boolean canBankSwap() {
        return System.currentTimeMillis() - lastBankSwap > 1000;
    }

    private void drawDialogBank() {
        int dialogWidth = 408;
        int dialogHeight = 290;
        if (bankActivePage > 0 && bankItemCount <= 48) {
            bankActivePage = 0;
        }
        if (bankActivePage > 1 && bankItemCount <= 96) {
            bankActivePage = 1;
        }
        if (bankActivePage > 2 && bankItemCount <= 144) {
            bankActivePage = 2;
        }
        if (bankSelectedItemSlot >= bankItemCount || bankSelectedItemSlot < 0) {
            bankSelectedItemSlot = -1;
        }
        if (bankSelectedItemSlot != -1 && bankItems[bankSelectedItemSlot] != bankSelectedItem) {
            bankSelectedItemSlot = -1;
            bankSelectedItem = -2;
        }
        if (mouseButtonClick == 1) {
            mouseButtonClick = 0;
            int mouseX = super.mouseX - (gameWidth / 2 - dialogWidth / 2);
            int mouseY = super.mouseY - (gameHeight / 2 - dialogHeight / 2);
            //int mouseX = super.mouseX - (256 - dialogWidth / 2);
            //int mouseY = super.mouseY - (170 - dialogHeight / 2);
            if (mouseX >= 0 && mouseY >= 12 && mouseX < 408 && mouseY < 280) {
                int i1 = bankActivePage * 48;
                for (int l1 = 0; l1 < 6; l1++) {
                    for (int j2 = 0; j2 < 8; j2++) {
                        int l6 = 7 + j2 * 49;
                        int j7 = 28 + l1 * 34;
                        if (mouseX > l6 && mouseX < l6 + 49 && mouseY > j7 && mouseY < j7 + 34 && i1 < bankItemCount && bankItems[i1] != -1) {
                            bankSelectedItem = bankItems[i1];
                            bankSelectedItemSlot = i1;
                            if (bankSwap) {
                                if (bankItemsCount[bankSelectedItemSlot] > 0) {
                                    bankSwapItem2 = bankItems[bankSelectedItemSlot];
                                    bankSwapSlot2 = bankSelectedItemSlot;
                                    if ((bankSwapItem1 != -1 && bankSwapItem2 != -1 && bankSwapSlot1 != -1 && bankSwapSlot2 != -1) && (bankSwapSlot1 != bankSwapSlot2)) {
                                        ActionManager.get(BankHandler.class).handleSwap(bankSwapSlot1, bankSwapItem1, bankSwapSlot2, bankSwapItem2);
                                    }
                                }
                                bankSwap = false;
                                bankSwapItem1 = -1;
                                bankSwapItem2 = -1;
                                bankSwapSlot1 = -1;
                                bankSwapSlot2 = -1;
                                setLastBankSwap();
                            }
                        }
                        i1++;
                    }
                }

                mouseX = (gameWidth / 2) - dialogWidth / 2;
                mouseY = (gameHeight / 2) - dialogHeight / 2;
                int slot;
                if (bankSelectedItemSlot < 0) {
                    slot = -1;
                } else {
                    slot = bankItems[bankSelectedItemSlot];
                }
                if (slot != -1) {
                    int j1 = bankItemsCount[bankSelectedItemSlot];
                    if (!EntityManager.getItem(slot).isStackable() && j1 > 1) // check this
                    {
                        j1 = 1;
                    }
                    if (j1 >= 1 && super.mouseX >= mouseX + 220 && super.mouseY >= mouseY + 238 && super.mouseX < mouseX + 250 && super.mouseY <= mouseY + 249) {
                        withdraw(1);
                    }
                    if (j1 >= 5 && super.mouseX >= mouseX + 250 && super.mouseY >= mouseY + 238 && super.mouseX < mouseX + 280 && super.mouseY <= mouseY + 249) {
                        withdraw(5);
                    }
                    if (j1 >= 25 && super.mouseX >= mouseX + 280 && super.mouseY >= mouseY + 238 && super.mouseX < mouseX + 305 && super.mouseY <= mouseY + 249) {
                        withdraw(25);
                    }
                    if (j1 >= 100 && super.mouseX >= mouseX + 305 && super.mouseY >= mouseY + 238 && super.mouseX < mouseX + 335 && super.mouseY <= mouseY + 249) {
                        withdraw(100);
                    }
                    if (j1 >= 500 && super.mouseX >= mouseX + 335 && super.mouseY >= mouseY + 238 && super.mouseX < mouseX + 368 && super.mouseY <= mouseY + 249) {
                        withdraw(500);
                    }
                    if (j1 >= 2500 && super.mouseX >= mouseX + 370 && super.mouseY >= mouseY + 238 && super.mouseX < mouseX + 400 && super.mouseY <= mouseY + 249) {
                        withdraw(2500);
                    }
                    if (getInventoryCount(slot) >= 1 && super.mouseX >= mouseX + 220 && super.mouseY >= mouseY + 263 && super.mouseX < mouseX + 250 && super.mouseY <= mouseY + 274) {
                        deposit(1);
                    }
                    if (getInventoryCount(slot) >= 5 && super.mouseX >= mouseX + 250 && super.mouseY >= mouseY + 263 && super.mouseX < mouseX + 280 && super.mouseY <= mouseY + 274) {
                        deposit(5);
                    }
                    if (getInventoryCount(slot) >= 25 && super.mouseX >= mouseX + 280 && super.mouseY >= mouseY + 263 && super.mouseX < mouseX + 305 && super.mouseY <= mouseY + 274) {
                        deposit(25);
                    }
                    if (getInventoryCount(slot) >= 100 && super.mouseX >= mouseX + 305 && super.mouseY >= mouseY + 263 && super.mouseX < mouseX + 335 && super.mouseY <= mouseY + 274) {
                        deposit(100);
                    }
                    if (getInventoryCount(slot) >= 500 && super.mouseX >= mouseX + 335 && super.mouseY >= mouseY + 263 && super.mouseX < mouseX + 368 && super.mouseY <= mouseY + 274) {
                        deposit(500);
                    }
                    if (getInventoryCount(slot) >= 2500 && super.mouseX >= mouseX + 370 && super.mouseY >= mouseY + 263 && super.mouseX < mouseX + 400 && super.mouseY <= mouseY + 274) {
                        deposit(2500);
                    }
                }
            } else if (bankItemCount > 48 && mouseX >= 50 && mouseX <= 115 && mouseY <= 12) {
                bankActivePage = 0;
            } else if (bankItemCount > 48 && mouseX >= 115 && mouseX <= 180 && mouseY <= 12) {
                bankActivePage = 1;
            } else if (bankItemCount > 96 && mouseX >= 180 && mouseX <= 245 && mouseY <= 12) {
                bankActivePage = 2;
            } else if (bankItemCount > 144 && mouseX >= 245 && mouseX <= 310 && mouseY <= 12) {
                bankActivePage = 3;
            } else {
                //ActionManager.getBankHandler().handleClose();
                showDialogBank = false;
                return;
            }
        } else if (mouseButtonClick == 2) {
            mouseButtonClick = 0;
            int i = super.mouseX - ((gameWidth / 2) - dialogWidth / 2);
            int k = super.mouseY - ((gameHeight / 2) - dialogHeight / 2);
            if (i >= 0 && k >= 12 && i < 408 && k < 280) {
                int i1 = bankActivePage * 48;
                for (int l1 = 0; l1 < 6; l1++) {
                    for (int j2 = 0; j2 < 8; j2++) {
                        int l6 = 7 + j2 * 49;
                        int j7 = 28 + l1 * 34;
                        if (i > l6 && i < l6 + 49 && k > j7 && k < j7 + 34
                                && i1 < bankItemCount && bankItems[i1] != -1) {
                            bankSelectedItem = bankItems[i1];
                            bankSelectedItemSlot = i1;
                            if (bankItemsCount[bankSelectedItemSlot] > 0) {
                                if (canBankSwap()) {
                                    bankSwap = true;
                                    bankSwapItem1 = bankItems[bankSelectedItemSlot];
                                    bankSwapSlot1 = bankSelectedItemSlot;
                                }
                            }
                        }
                        i1++;
                    }
                }
            }
        }
        int x = gameWidth / 2 - dialogWidth / 2;
        int y = gameHeight / 2 - dialogHeight / 2;
        //int x = 256 - dialogWidth / 2;
        //int y = 170 - dialogHeight / 2;
        surface.drawBox(x, y, 408, 12, 192);
        surface.drawBoxAlpha(x, y + 12, 408, 17, 0x989898, 160);
        surface.drawBoxAlpha(x, y + 29, 8, 204, 0x989898, 160);
        surface.drawBoxAlpha(x + 399, y + 29, 9, 204, 0x989898, 160);
        surface.drawBoxAlpha(x, y + 233, 408, 47, 0x989898, 160);
        surface.drawString("Bank", x + 1, y + 10, 1, 0xffffff);
        int xOff = 50;
        if (bankItemCount > 48) {
            int l2 = 0xffffff;
            if (bankActivePage == 0) {
                l2 = 0xff0000;
            } else if (super.mouseX > x + xOff && super.mouseY >= y && super.mouseX < x + xOff + 65 && super.mouseY < y + 12) {
                l2 = 0xffff00;
            }
            surface.drawString("<page 1>", x + xOff, y + 10, 1, l2);
            xOff += 65;
            l2 = 0xffffff;
            if (bankActivePage == 1) {
                l2 = 0xff0000;
            } else if (super.mouseX > x + xOff && super.mouseY >= y && super.mouseX < x + xOff + 65 && super.mouseY < y + 12) {
                l2 = 0xffff00;
            }
            surface.drawString("<page 2>", x + xOff, y + 10, 1, l2);
            xOff += 65;
        }
        if (bankItemCount > 96) {
            int i3 = 0xffffff;
            if (bankActivePage == 2) {
                i3 = 0xff0000;
            } else if (super.mouseX > x + xOff && super.mouseY >= y && super.mouseX < x + xOff + 65 && super.mouseY < y + 12) {
                i3 = 0xffff00;
            }
            surface.drawString("<page 3>", x + xOff, y + 10, 1, i3);
            xOff += 65;
        }
        if (bankItemCount > 144) {
            int j3 = 0xffffff;
            if (bankActivePage == 3) {
                j3 = 0xff0000;
            } else if (super.mouseX > x + xOff && super.mouseY >= y && super.mouseX < x + xOff + 65 && super.mouseY < y + 12) {
                j3 = 0xffff00;
            }
            surface.drawString("<page 4>", x + xOff, y + 10, 1, j3);
            xOff += 65;
        }
        int colour = 0xffffff;
        if (super.mouseX > x + 320 && super.mouseY >= y && super.mouseX < x + 408 && super.mouseY < y + 12) {
            colour = 0xff0000;
        }
        surface.drawstringRight("Close window", x + 406, y + 10, 1, colour);
        surface.drawString("Number in bank in green", x + 7, y + 24, 1, 65280);
        surface.drawString("Number held in blue", x + 289, y + 24, 1, 65535);
        int k7 = bankActivePage * 48;
        for (int i8 = 0; i8 < 6; i8++) {
            for (int j8 = 0; j8 < 8; j8++) {
                int l8 = x + 7 + j8 * 49;
                int i9 = y + 28 + i8 * 34;
                if (bankSelectedItemSlot == k7) {
                    surface.drawBoxAlpha(l8, i9, 49, 34, (bankSwap ? Colors.CYAN : Colors.RED), 160);
                } else {
                    surface.drawBoxAlpha(l8, i9, 49, 34, 0xd0d0d0, 160);
                }
                surface.drawBoxEdge(l8, i9, 50, 35, 0);
                if (k7 < bankItemCount && bankItems[k7] != -1) {
                    surface.spriteClipping(l8, i9, 48, 32, spriteItem + EntityManager.getItem(bankItems[k7]).getSprite(), EntityManager.getItem(bankItems[k7]).getMask(), 0, 0, false);
                    surface.drawString(String.valueOf(bankItemsCount[k7]), l8 + 1, i9 + 10, 1, 65280);
                    surface.drawstringRight(String.valueOf(getInventoryCount(bankItems[k7])), l8 + 47, i9 + 29, 1, 65535);
                }
                k7++;
            }
        }

        surface.drawLineHoriz(x + 5, y + 256, 398, 0);
        if (bankSelectedItemSlot == -1) {
            surface.drawStringCenter("Select an object to withdraw or deposit", x + 204, y + 248, 3, 0xffff00);
            return;
        }
        int itemType;
        if (bankSelectedItemSlot < 0) {
            itemType = -1;
        } else {
            itemType = bankItems[bankSelectedItemSlot];
        }
        if (itemType != -1) {
            int itemCount = bankItemsCount[bankSelectedItemSlot];
            if (!EntityManager.getItem(itemType).isStackable() && itemCount > 1) {
                itemCount = 1;
            }
            if (itemCount > 0) {
                if (!bankSwap) {
                    surface.drawString("Withdraw " + EntityManager.getItem(itemType).getName(), x + 2, y + 248, 1, 0xffffff);
                    colour = 0xffffff;
                    if (super.mouseX >= x + 220 && super.mouseY >= y + 238 && super.mouseX < x + 250 && super.mouseY <= y + 249) {
                        colour = 0xff0000;
                    }
                    surface.drawString("One", x + 222, y + 248, 1, colour);
                    if (itemCount >= 5) {
                        colour = 0xffffff;
                        if (super.mouseX >= x + 250 && super.mouseY >= y + 238 && super.mouseX < x + 280 && super.mouseY <= y + 249) {
                            colour = 0xff0000;
                        }
                        surface.drawString("Five", x + 252, y + 248, 1, colour);
                    }
                    if (itemCount >= 25) {
                        colour = 0xffffff;
                        if (super.mouseX >= x + 280 && super.mouseY >= y + 238 && super.mouseX < x + 305 && super.mouseY <= y + 249) {
                            colour = 0xff0000;
                        }
                        surface.drawString("25", x + 282, y + 248, 1, colour);
                    }
                    if (itemCount >= 100) {
                        colour = 0xffffff;
                        if (super.mouseX >= x + 305 && super.mouseY >= y + 238 && super.mouseX < x + 335 && super.mouseY <= y + 249) {
                            colour = 0xff0000;
                        }
                        surface.drawString("100", x + 307, y + 248, 1, colour);
                    }
                    if (itemCount >= 500) {
                        colour = 0xffffff;
                        if (super.mouseX >= x + 335 && super.mouseY >= y + 238 && super.mouseX < x + 368 && super.mouseY <= y + 249) {
                            colour = 0xff0000;
                        }
                        surface.drawString("500", x + 337, y + 248, 1, colour);
                    }
                    if (itemCount >= 2500) {
                        colour = 0xffffff;
                        if (super.mouseX >= x + 370 && super.mouseY >= y + 238 && super.mouseX < x + 400 && super.mouseY <= y + 249) {
                            colour = 0xff0000;
                        }
                        surface.drawString("2500", x + 370, y + 248, 1, colour);
                    }
                } else {
                    surface.drawString("Swap " + EntityManager.getItem(itemType).getName() + " with...", x + 2, y + 248, 1, 0xffffff);
                }
            }
            if (getInventoryCount(itemType) > 0 && !bankSwap) {
                surface.drawString("Deposit " + EntityManager.getItem(itemType).getName(), x + 2, y + 273, 1, 0xffffff);
                colour = 0xffffff;
                if (super.mouseX >= x + 220 && super.mouseY >= y + 263 && super.mouseX < x + 250 && super.mouseY <= y + 274) {
                    colour = 0xff0000;
                }
                surface.drawString("One", x + 222, y + 273, 1, colour);
                if (getInventoryCount(itemType) >= 5) {
                    colour = 0xffffff;
                    if (super.mouseX >= x + 250 && super.mouseY >= y + 263 && super.mouseX < x + 280 && super.mouseY <= y + 274) {
                        colour = 0xff0000;
                    }
                    surface.drawString("Five", x + 252, y + 273, 1, colour);
                }
                if (getInventoryCount(itemType) >= 25) {
                    colour = 0xffffff;
                    if (super.mouseX >= x + 280 && super.mouseY >= y + 263 && super.mouseX < x + 305 && super.mouseY <= y + 274) {
                        colour = 0xff0000;
                    }
                    surface.drawString("25", x + 282, y + 273, 1, colour);
                }
                if (getInventoryCount(itemType) >= 100) {
                    colour = 0xffffff;
                    if (super.mouseX >= x + 305 && super.mouseY >= y + 263 && super.mouseX < x + 335 && super.mouseY <= y + 274) {
                        colour = 0xff0000;
                    }
                    surface.drawString("100", x + 307, y + 273, 1, colour);
                }
                if (getInventoryCount(itemType) >= 500) {
                    colour = 0xffffff;
                    if (super.mouseX >= x + 335 && super.mouseY >= y + 263 && super.mouseX < x + 368 && super.mouseY <= y + 274) {
                        colour = 0xff0000;
                    }
                    surface.drawString("500", x + 337, y + 273, 1, colour);
                }
                if (getInventoryCount(itemType) >= 2500) {
                    colour = 0xffffff;
                    if (super.mouseX >= x + 370 && super.mouseY >= y + 263 && super.mouseX < x + 400 && super.mouseY <= y + 274) {
                        colour = 0xff0000;
                    }
                    surface.drawString("2500", x + 370, y + 273, 1, colour);
                }
            }
        }
    }

    public boolean loadNextRegion(int lx, int ly) {
        if (deathScreenTimeout != 0) {
            world.playerIsAlive = false;
            return false;
        }
        loadingArea = false;
        lx += planeWidth;
        ly += planeHeight;
        if (lastHeightOffset == planeIndex && lx > localLowerX && lx < localUpperX && ly > localLowerY && ly < localUpperY) {
            world.playerIsAlive = true;
            return false;
        }
        surface.drawStringCenter("Loading... Please wait", 256, 192, 1, 0xffffff);
        drawChatMessageTabs();
        surface.draw(graphics, 0, 0);
        int ax = regionX;
        int ay = regionY;
        int sectionX = (lx + 24) / 48;
        int sectionY = (ly + 24) / 48;
        
        lastHeightOffset = planeIndex;
        regionX = sectionX * 48 - 48;
        regionY = sectionY * 48 - 48;
        localLowerX = sectionX * 48 - 32;
        localLowerY = sectionY * 48 - 32;
        localUpperX = sectionX * 48 + 32;
        localUpperY = sectionY * 48 + 32;
        world.loadSector(lx, ly, lastHeightOffset);
        regionX -= planeWidth;
        regionY -= planeHeight;
        int offsetx = regionX - ax;
        int offsety = regionY - ay;
        for (int objidx = 0; objidx < objectCount; objidx++) {
            objectX[objidx] -= offsetx;
            objectY[objidx] -= offsety;
            int x = objectX[objidx];
            int y = objectY[objidx];
            int id = objectID[objidx];
            int direction = objectDirection[objidx];
            Model gameModel = objectModel[objidx];
            try {
                int objw;
                int objh;
                if (direction == 0 || direction == 4) {
                    objw = EntityManager.getGameObjectDef(id).getWidth();
                    objh = EntityManager.getGameObjectDef(id).getHeight();
                } else {
                    objh = EntityManager.getGameObjectDef(id).getWidth();
                    objw = EntityManager.getGameObjectDef(id).getHeight();
                }
                int j6 = ((x + x + objw) * magicLoc) / 2;
                int k6 = ((y + y + objh) * magicLoc) / 2;
                if (x >= 0 && y >= 0 && x < 96 && y < 96) {
                    scene.addModel(gameModel);
                    gameModel.place(j6, -world.getAveragedElevation(j6, k6), k6);
                    world.method412(x, y, id, direction);
                    if (direction == 74) {
                        gameModel.translate(0, -480, 0);
                    }
                }
            } catch (RuntimeException e) {
                System.out.println("Loc Error: " + e.getMessage());
                System.out.println("i:" + objidx + " obj:" + gameModel);
                e.printStackTrace();
            }
        }

        for (int k2 = 0; k2 < wallObjectCount; k2++) {
            wallObjectX[k2] -= offsetx;
            wallObjectY[k2] -= offsety;
            int i3 = wallObjectX[k2];
            int l3 = wallObjectY[k2];
            int j4 = wallObjectId[k2];
            int i5 = wallObjectDirection[k2];
            try {
                world.method408(i3, l3, i5, j4);
                Model gameModel_1 = createWallModel(i3, l3, i5, j4, k2);
                wallObjectModel[k2] = gameModel_1;
            } catch (RuntimeException runtimeexception1) {
                System.out.println("Bound Error: " + runtimeexception1.getMessage());
                runtimeexception1.printStackTrace();
            }
        }

        for (int j3 = 0; j3 < groundItemCount; j3++) {
            groundItemX[j3] -= offsetx;
            groundItemY[j3] -= offsety;
        }

        for (int i4 = 0; i4 < playerCount; i4++) {
            Mob character = players[i4];
            character.currentX -= offsetx * magicLoc;
            character.currentY -= offsety * magicLoc;
            for (int j5 = 0; j5 <= character.waypointCurrent; j5++) {
                character.waypointsX[j5] -= offsetx * magicLoc;
                character.waypointsY[j5] -= offsety * magicLoc;
            }

        }

        for (int k4 = 0; k4 < npcCount; k4++) {
            Mob character_1 = npcs[k4];
            character_1.currentX -= offsetx * magicLoc;
            character_1.currentY -= offsety * magicLoc;
            for (int l5 = 0; l5 <= character_1.waypointCurrent; l5++) {
                character_1.waypointsX[l5] -= offsetx * magicLoc;
                character_1.waypointsY[l5] -= offsety * magicLoc;
            }

        }

        world.playerIsAlive = true;
        return true;
    }

    public void drawPlayer(int x, int y, int w, int h, int id, int tx, int ty) {
        Mob character = players[id];
        if (character.colourBottom == 255) // this means the character is invisible! MOD!!!
        {
            return;
        }
        int l1 = character.animationCurrent + (cameraRotation + 16) / 32 & 7;
        boolean flag = false;
        int i2 = l1;
        if (i2 == 5) {
            i2 = 3;
            flag = true;
        } else if (i2 == 6) {
            i2 = 2;
            flag = true;
        } else if (i2 == 7) {
            i2 = 1;
            flag = true;
        }
        int j2 = i2 * 3 + npcWalkModel[(character.stepCount / 6) % 4];
        if (character.animationCurrent == 8) {
            i2 = 5;
            l1 = 2;
            flag = false;
            x -= (5 * ty) / 100;
            j2 = i2 * 3 + npcCombatModelArray1[(loginTimer / 5) % 8];
        } else if (character.animationCurrent == 9) {
            i2 = 5;
            l1 = 2;
            flag = true;
            x += (5 * ty) / 100;
            j2 = i2 * 3 + npcCombatModelArray2[(loginTimer / 6) % 8];
        }
        for (int k2 = 0; k2 < 12; k2++) {
            int l2 = npcAnimationArray[l1][k2];
            int l3 = character.equippedItem[l2] - 1;
            if (l3 >= 0) {
                int k4 = 0;
                int i5 = 0;
                int j5 = j2;
                if (flag && i2 >= 1 && i2 <= 3) {
                    if (EntityManager.getAnimation(l3).hasF()) {
                        j5 += 15;
                    } else if (l2 == 4 && i2 == 1) {
                        k4 = -22;
                        i5 = -3;
                        j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
                    } else if (l2 == 4 && i2 == 2) {
                        k4 = 0;
                        i5 = -8;
                        j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
                    } else if (l2 == 4 && i2 == 3) {
                        k4 = 26;
                        i5 = -5;
                        j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
                    } else if (l2 == 3 && i2 == 1) {
                        k4 = 22;
                        i5 = 3;
                        j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
                    } else if (l2 == 3 && i2 == 2) {
                        k4 = 0;
                        i5 = 8;
                        j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
                    } else if (l2 == 3 && i2 == 3) {
                        k4 = -26;
                        i5 = 5;
                        j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
                    }
                }
                if (i2 != 5 || EntityManager.getAnimation(l3).hasA()) {
                    int k5 = j5 + EntityManager.getAnimation(l3).getNumber();
                    k4 = (k4 * w) / surface.spriteWidthFull[k5];
                    i5 = (i5 * h) / surface.spriteHeightFull[k5];
                    int l5 = (w * surface.spriteWidthFull[k5]) / surface.spriteWidthFull[EntityManager.getAnimation(l3).getNumber()];
                    k4 -= (l5 - w) / 2;
                    int i6 = EntityManager.getAnimation(l3).getCharColour();
                    int j6 = characterSkinColours[character.colourSkin];
                    if (i6 == 1) {
                        i6 = characterHairColours[character.colourHair];
                    } else if (i6 == 2) {
                        i6 = characterTopBottomColours[character.colourTop];
                    } else if (i6 == 3) {
                        i6 = characterTopBottomColours[character.colourBottom];
                    }
                    surface.spriteClipping(x + k4, y + i5, l5, h, k5, i6, j6, tx, flag);
                }
            }
        }

        if (character.messageTimeout > 0) {
            receivedMessageMidPoint[receivedMessagesCount] = surface.textWidth(character.message, 1) / 2;
            if (receivedMessageMidPoint[receivedMessagesCount] > 150) {
                receivedMessageMidPoint[receivedMessagesCount] = 150;
            }
            receivedMessageHeight[receivedMessagesCount] = (surface.textWidth(character.message, 1) / 300) * surface.textHeight(1);
            receivedMessageX[receivedMessagesCount] = x + w / 2;
            receivedMessageY[receivedMessagesCount] = y;
            receivedMessages[receivedMessagesCount++] = character.message;
        }
        if (character.bubbleTimeout > 0) {
            actionBubbleX[itemsAboveHeadCount] = x + w / 2;
            actionBubbleY[itemsAboveHeadCount] = y;
            actionBubbleScale[itemsAboveHeadCount] = ty;
            actionBubbleItem[itemsAboveHeadCount++] = character.bubbleItem;
        }
        if (character.animationCurrent == 8 || character.animationCurrent == 9 || character.combatTimer != 0) {
            if (character.combatTimer > 0) {
                int i3 = x;
                if (character.animationCurrent == 8) {
                    i3 -= (20 * ty) / 100;
                } else if (character.animationCurrent == 9) {
                    i3 += (20 * ty) / 100;
                }
                int i4 = (character.healthCurrent * 30) / character.healthMax;
                healthBarX[healthBarCount] = i3 + w / 2;
                healthBarY[healthBarCount] = y;
                healthBarMissing[healthBarCount++] = i4;
            }
            if (character.combatTimer > 150) {
                int j3 = x;
                if (character.animationCurrent == 8) {
                    j3 -= (10 * ty) / 100;
                } else if (character.animationCurrent == 9) {
                    j3 += (10 * ty) / 100;
                }
                surface.drawSprite((j3 + w / 2) - 12, (y + h / 2) - 12, spriteMedia + 11);
                surface.drawStringCenter(String.valueOf(character.damageTaken), (j3 + w / 2) - 1, y + h / 2 + 5, 3, 0xffffff);
            }
        }
        if (character.skullVisible == 1 && character.bubbleTimeout == 0) {
            int k3 = tx + x + w / 2;
            if (character.animationCurrent == 8) {
                k3 -= (20 * ty) / 100;
            } else if (character.animationCurrent == 9) {
                k3 += (20 * ty) / 100;
            }
            int j4 = (16 * ty) / 100;
            int l4 = (16 * ty) / 100;
            surface.spriteClipping(k3 - j4 / 2, y - l4 / 2 - (10 * ty) / 100, j4, l4, spriteMedia + 13);
        }
    }

    // MISSING: buttons.tga
    private void loadMedia() {
        try {
            byte media[] = readDataFile("media.jag", "2d graphics", 20);
            if (media == null) {
                errorLoadingData = true;
                return;
            }
            byte buff[] = Util.unpackData("index.dat", 0, media);
            byte[] tgaData = new byte[0xffff];
            Util.readFully(Constants.CACHE_DIRECTORY + "images/inv1.tga", tgaData, tgaData.length);
            surface.loadSpriteRaw(tgaData, 0, spriteMedia, true, false);
            Util.readFully(Constants.CACHE_DIRECTORY + "images/inv2.tga", tgaData, tgaData.length);
            //surface.parseSprite(spriteMedia, Util.unpackData("inv1.dat", 0, media), buff, 1);
            surface.loadSpriteRaw(tgaData, 0, spriteMedia + 1, true, 1, 6, false);
            //surface.parseSprite(spriteMedia + 1, Util.unpackData("inv2.dat", 0, media), buff, 6);
            surface.parseSprite(spriteMedia + 9, Util.unpackData("bubble.dat", 0, media), buff, 1);
            surface.parseSprite(spriteMedia + 10, Util.unpackData("runescape.dat", 0, media), buff, 1);

            surface.parseSprite(spriteMedia + 11, Util.unpackData("splat.dat", 0, media), buff, 3);
            surface.parseSprite(spriteMedia + 14, Util.unpackData("icon.dat", 0, media), buff, 8);
            surface.parseSprite(spriteMedia + 22, Util.unpackData("hbar.dat", 0, media), buff, 1);
            surface.parseSprite(spriteMedia + 23, Util.unpackData("hbar2.dat", 0, media), buff, 1);
            surface.parseSprite(spriteMedia + 24, Util.unpackData("compass.dat", 0, media), buff, 1); 
            surface.parseSprite(spriteMedia + 25, Util.unpackData("buttons.dat", 0, media), buff, 2);
            Util.readFully(Constants.CACHE_DIRECTORY + "images/play.tga", tgaData, tgaData.length);
            surface.loadSpriteRaw(tgaData, 0, spriteMedia + 27, true, false);
            Util.readFully(Constants.CACHE_DIRECTORY + "images/pause.tga", tgaData, tgaData.length);
            surface.loadSpriteRaw(tgaData, 0, spriteMedia + 28, true, false);
            Util.readFully(Constants.CACHE_DIRECTORY + "images/stop.tga", tgaData, tgaData.length);
            surface.loadSpriteRaw(tgaData, 0, spriteMedia + 29, true, false);
            Util.readFully(Constants.CACHE_DIRECTORY + "images/back.tga", tgaData, tgaData.length);
            surface.loadSpriteRaw(tgaData, 0, spriteMedia + 30, true, false);
            Util.readFully(Constants.CACHE_DIRECTORY + "images/forward.tga", tgaData, tgaData.length);
            surface.loadSpriteRaw(tgaData, 0, spriteMedia + 31, true, false);
            surface.parseSprite(spriteUtil, Util.unpackData("scrollbar.dat", 0, media), buff, 2);
            surface.parseSprite(spriteUtil + 2, Util.unpackData("corners.dat", 0, media), buff, 4);
            surface.parseSprite(spriteUtil + 6, Util.unpackData("arrows.dat", 0, media), buff, 2);
            int projectileCount = 7;
            surface.parseSprite(spriteProjectile, Util.unpackData("projectile.dat", 0, media), buff, projectileCount);

            int i = EntityManager.getItemSpriteCount();
            for (int j = 1; i > 0; j++) {
                int k = i;
                i -= 30;
                if (k > 30) {
                    k = 30;
                }
                surface.parseSprite(spriteItem + (j - 1) * 30, Util.unpackData("objects" + j + ".dat", 0, media), buff, k);
            }

            surface.loadSprite(spriteMedia);
            surface.loadSprite(spriteMedia + 9);
            for (int l = 11; l <= 26; l++) {
                surface.loadSprite(spriteMedia + l);
            }

            for (int i1 = 0; i1 < projectileCount; i1++) {
                surface.loadSprite(spriteProjectile + i1);
            }

            for (int j1 = 0; j1 < EntityManager.getItemSpriteCount(); j1++) {
                surface.loadSprite(spriteItem + j1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawChatMessageTabs() {
        /* bottom blue bar */
        int OFFSET = 20;
        surface.drawSprite(0, gameHeight-OFFSET, spriteMedia + 22);
        /* extended bottom blue bar */
        surface.drawSprite(gameWidth - 512, gameHeight-OFFSET, spriteMedia + 22);
        /* chat options bar */
        surface.drawSprite(gameWidth / 2 - 256, gameHeight-OFFSET - 4, spriteMedia + 23);
        int col = Surface.rgb2long(200, 200, 255);
        if (messageTabSelected == 0) {
            col = Surface.rgb2long(255, 200, 50);
        }
        if (messageTabFlashAll % 30 > 15) {
            col = Surface.rgb2long(255, 50, 50);
        }
        surface.drawStringCenter("All messages", gameWidth / 2 - 200, gameHeight-OFFSET + 6, 0, col);
        col = Surface.rgb2long(200, 200, 255);
        if (messageTabSelected == 1) {
            col = Surface.rgb2long(255, 200, 50);
        }
        if (messageTabFlashHistory % 30 > 15) {
            col = Surface.rgb2long(255, 50, 50);
        }
        surface.drawStringCenter("Chat history", gameWidth / 2 - 100, gameHeight-OFFSET + 6, 0, col);
        col = Surface.rgb2long(200, 200, 255);
        if (messageTabSelected == 2) {
            col = Surface.rgb2long(255, 200, 50);
        }
        if (messageTabFlashQuest % 30 > 15) {
            col = Surface.rgb2long(255, 50, 50);
        }
        surface.drawStringCenter("Quest history", gameWidth / 2, gameHeight-OFFSET + 6, 0, col);
        col = Surface.rgb2long(200, 200, 255);
        if (messageTabSelected == 3) {
            col = Surface.rgb2long(255, 200, 50);
        }
        if (messageTabFlashPrivate % 30 > 15) {
            col = Surface.rgb2long(255, 50, 50);
        }
        surface.drawStringCenter("Private history", gameWidth / 2 + 100, gameHeight-OFFSET + 6, 0, col);
        surface.drawStringCenter("Report abuse", gameWidth / 2 + 200, gameHeight-OFFSET + 6, 0, 0xffffff);
    }

    @Override
    protected void startGame() {
        int total_exp = 0;
        for (int level = 0; level < 99; level++) {
            int level_1 = level + 1;
            int exp = (int) ((double) level_1 + 300D * Math.pow(2D, (double) level_1 / 7D));
            total_exp += exp;
            experienceArray[level] = (total_exp & 0xffffffc) / 4;
        }
        loadGameConfig();
        spriteMedia = 2000;
        spriteUtil = spriteMedia + 100;
        spriteItem = spriteUtil + 50;
        spriteLogo = spriteItem + 1000;
        spriteProjectile = spriteLogo + 10;
        spriteTexture = spriteProjectile + 50;
        spriteTextureWorld = spriteTexture + 10;
        graphics = getGraphics();
        setTargetFps(50);
        surface = new Screen(gameWidth, gameHeight + 12, 4000, this);
        surface.mc = this;
        surface.setBounds(0, 0, gameWidth, gameHeight + 12);
        Menu.drawBackgroundArrow = false;
        Menu.baseSpriteStart = spriteUtil;
        panelMagic = new Menu(surface, 5);
        int x = surface.width2 - 199;
        byte y = 36;
        controlListMagic = panelMagic.addTextListInteractive(x, y + 24, 196, 90, 1, 500, true);
        panelMusic = new Menu(surface, 5);
        controlListMusic = panelMusic.addTextListInteractive(x, y + 24, 196, 166 - 24, 1, 2000, true);
        panelQuestList = new Menu(surface, 5);
        controlListQuest = panelQuestList.addTextListInteractive(x, y + 24, 196, 251, 1, 500, true);
        //
        createLoginPanels();
        createMessageTabPanel();
        createAppearancePanel();
        //
        loadMedia();
        if(errorLoadingData) return;
        loadEntities();
        if(errorLoadingData) return;
        scene = new Scene(surface, 15000, 15000, 4096);
        scene.setBounds(gameWidth / 2, gameHeight / 2, gameWidth / 2, gameHeight / 2, gameWidth, const_9);
        scene.clipFar3d = 2400;
        scene.clipFar2d = 2400;
        scene.fogZDensity = 1;
        scene.fogZDistance = 2300;
        scene.setLight(-50, -10, -50);
        world = new Terrain(scene, surface);
        loadTextures();
        if(errorLoadingData) return;
        loadModels();
        if(errorLoadingData) return;
        loadSounds();
        if(!errorLoadingData) {
            showLoadingProgress(100, "Starting game...");
            resetLoginScreenVariables();
            renderLoginScreenViewports();
        }
        //System.out.println("[Classic Client] Loading process finished!\n");
        //System.out.println("Console:");
        //System.out.println("____________________________________________________________");
    }

    private void drawUiTabMagic(boolean nomenus) {
        int uiX = surface.width2 - 199;
        int uiY = 36;
        surface.drawSprite(uiX - 49, 3, spriteMedia + 4);
        int uiWidth = 196;
        int uiHeight = 182;
        int l;
        int k = l = Surface.rgb2long(160, 160, 160);
        if (tabMagicPrayer == 0) {
            k = Surface.rgb2long(220, 220, 220);
        } else {
            l = Surface.rgb2long(220, 220, 220);
        }
        surface.drawBoxAlpha(uiX, uiY, uiWidth / 2, 24, k, 128);
        surface.drawBoxAlpha(uiX + uiWidth / 2, uiY, uiWidth / 2, 24, l, 128);
        surface.drawBoxAlpha(uiX, uiY + 24, uiWidth, 90, Surface.rgb2long(220, 220, 220), 128);
        surface.drawBoxAlpha(uiX, uiY + 24 + 90, uiWidth, uiHeight - 90 - 24, Surface.rgb2long(160, 160, 160), 128);
        surface.drawLineHoriz(uiX, uiY + 24, uiWidth, 0);
        surface.drawLineVert(uiX + uiWidth / 2, uiY, 24, 0);
        surface.drawLineHoriz(uiX, uiY + 113, uiWidth, 0);
        surface.drawStringCenter("Magic", uiX + uiWidth / 4, uiY + 16, 4, 0);
        surface.drawStringCenter("Prayers", uiX + uiWidth / 4 + uiWidth / 2, uiY + 16, 4, 0);
        if (tabMagicPrayer == 0) {
            panelMagic.clearList(controlListMagic);
            int i1 = 0;
            for (int i = 0; i < EntityManager.getSpells().length; i++) {
                String s = "@yel@";
                for (Map.Entry<?, ?> e : EntityManager.getSpell(i).getRunesRequired()) {
                    if (hasInventoryItems((Integer) e.getKey(), (Integer) e.getValue())) {
                        continue;
                    }
                    s = "@whi@";
                    break;
                }
                int spellLevel = playerStatCurrent[6];
                if (EntityManager.getSpell(i).getReqLevel() > spellLevel) {
                    s = "@bla@";
                }
                panelMagic.addListEntry(controlListMagic, i1++, s + "Level " + EntityManager.getSpell(i).getReqLevel() + ": " + EntityManager.getSpell(i).getName());
            }
            panelMagic.drawPanel();
            int i3 = panelMagic.getListEntryIndex(controlListMagic);
            if (i3 != -1) {
                surface.drawString("Level " + EntityManager.getSpell(i3).getReqLevel() + ": " + EntityManager.getSpell(i3).getName(), uiX + 2, uiY + 124, 1, 0xffff00);
                surface.drawString(EntityManager.getSpell(i3).getDescription(), uiX + 2, uiY + 136, 0, 0xffffff);
                int i4 = 0;
                for (Map.Entry<Integer, Integer> e : EntityManager.getSpell(i3).getRunesRequired()) {
                    int runeID = e.getKey();
                    surface.drawSprite(uiX + 2 + i4 * 44, uiY + 150, spriteItem + EntityManager.getItem(runeID).getSprite());
                    int runeInvCount = getInventoryCount(runeID);
                    int runeCount = e.getValue();
                    String s2 = "@red@";
                    if (hasInventoryItems(runeID, runeCount)) {
                        s2 = "@gre@";
                    }
                    surface.drawString(s2 + runeInvCount + "/" + runeCount, uiX + 2 + i4 * 44, uiY + 150, 1, 0xffffff);
                    i4++;
                }
            } else {
                surface.drawString("Point at a spell for a description", uiX + 2, uiY + 124, 1, 0);
            }
        }
        if (tabMagicPrayer == 1) {
            panelMagic.clearList(controlListMagic);
            int j1 = 0;
            for (int j2 = 0; j2 < EntityManager.getPrayers().length; j2++) {
                String s1 = "@whi@";
                if (EntityManager.getPrayer(j2).getReqLevel() > playerStatBase[5]) {
                    s1 = "@bla@";
                }
                if (prayerOn[j2]) {
                    s1 = "@gre@";
                }
                panelMagic.addListEntry(controlListMagic, j1++, s1 + "Level " + EntityManager.getPrayer(j2).getReqLevel() + ": " + EntityManager.getPrayer(j2).getName());
            }

            panelMagic.drawPanel();
            int j3 = panelMagic.getListEntryIndex(controlListMagic);
            if (j3 != -1) {
                surface.drawStringCenter("Level " + EntityManager.getPrayer(j3).getReqLevel() + ": " + EntityManager.getPrayer(j3).getName(), uiX + uiWidth / 2, uiY + 130, 1, 0xffff00);
                surface.drawStringCenter(EntityManager.getPrayer(j3).getDescription(), uiX + uiWidth / 2, uiY + 145, 0, 0xffffff);
                surface.drawStringCenter("Drain rate: " + EntityManager.getPrayer(j3).getDrainRate(), uiX + uiWidth / 2, uiY + 160, 1, 0);
            } else {
                surface.drawString("Point at a prayer for a description", uiX + 2, uiY + 124, 1, 0);
            }
        }
        if (!nomenus) {
            return;
        }
        int mouseX = super.mouseX - (surface.width2 - 199);
        int mouseY = super.mouseY - 36;
        if (mouseX >= 0 && mouseY >= 0 && mouseX < 196 && mouseY < 182) {
            panelMagic.handleMouse(mouseX + (surface.width2 - 199), mouseY + 36, super.lastMouseButtonDown, super.mouseButtonDown);
            if (mouseY <= 24 && mouseButtonClick == 1) {
                if (mouseX < 98 && tabMagicPrayer == 1) {
                    tabMagicPrayer = 0;
                    panelMagic.resetListProps(controlListMagic);
                } else if (mouseX > 98 && tabMagicPrayer == 0) {
                    tabMagicPrayer = 1;
                    panelMagic.resetListProps(controlListMagic);
                }
            }
            if (mouseButtonClick == 1 && tabMagicPrayer == 0) {
                int k1 = panelMagic.getListEntryIndex(controlListMagic);
                if (k1 != -1) {
                    int k2 = playerStatCurrent[6];
                    if (EntityManager.getSpell(k1).getReqLevel() > k2) {
                        showMessage("Your magic ability is not high enough for this spell", 3);
                    } else {
                        int k3 = 0;
                        for (Map.Entry<Integer, Integer> e : EntityManager.getSpell(k1).getRunesRequired()) {
                            if (!hasInventoryItems(e.getKey(), e.getValue())) {
                                showMessage("You don't have all the reagents you need for this spell", 3);
                                k3 = -1;
                                break;
                            }
                            k3++;
                        }
                        if (k3 == EntityManager.getSpell(k1).getRuneCount()) {
                            selectedSpell = k1;
                            selectedItemInventoryIndex = -1;
                        }
                    }
                }
            }
            if (mouseButtonClick == 1 && tabMagicPrayer == 1) {
                int l1 = panelMagic.getListEntryIndex(controlListMagic);
                if (l1 != -1) {
                    int l2 = playerStatBase[5];
                    if (EntityManager.getPrayer(l1).getReqLevel() > l2) {
                        showMessage("Your prayer ability is not high enough for this prayer", 3);
                    } else if (playerStatCurrent[5] == 0) {
                        showMessage("You have run out of prayer points. Return to a church to recharge", 3);
                    } else if (prayerOn[l1]) {
                        ActionManager.get(PrayerHandler.class).handlePrayer(l1, false);
                        prayerOn[l1] = false;
                        playSoundEffect(SoundEffect.PRAYER_OFF);
                    } else {
                        ActionManager.get(PrayerHandler.class).handlePrayer(l1, true);
                        prayerOn[l1] = true;
                        playSoundEffect(SoundEffect.PRAYER_ON);
                    }
                }
            }
            mouseButtonClick = 0;
        }
    }

    private void drawDialogShop() {
        if (mouseButtonClick != 0) {
            mouseButtonClick = 0;
            int mouseX = super.mouseX - 52;
            int mouseY = super.mouseY - 44;
            if (mouseX >= 0 && mouseY >= 12 && mouseX < 408 && mouseY < 246) {
                int itemIndex = 0;
                for (int row = 0; row < 5; row++) {
                    for (int col = 0; col < 8; col++) {
                        int slotX = 7 + col * 49;
                        int slotY = 28 + row * 34;
                        if (mouseX > slotX && mouseX < slotX + 49 && mouseY > slotY && mouseY < slotY + 34 && shopItem[itemIndex] != -1) {
                            shopSelectedItemIndex = itemIndex;
                            shopSelectedItemType = shopItem[itemIndex];
                        }
                        itemIndex++;
                    }

                }

                if (shopSelectedItemIndex >= 0) {
                    int itemType = shopItem[shopSelectedItemIndex];
                    if (itemType != -1) {
                        if (shopItemCount[shopSelectedItemIndex] > 0 && mouseX > 298 && mouseY >= 204 && mouseX < 408 && mouseY <= 215) {
                            ActionManager.get(ShopHandler.class).handleBuyItem(shopItem[shopSelectedItemIndex]);
                        }
                        if (getInventoryCount(itemType) > 0 && mouseX > 2 && mouseY >= 229 && mouseX < 112 && mouseY <= 240) {
                            ActionManager.get(ShopHandler.class).handleSellItem(shopItem[shopSelectedItemIndex]);
                        }
                    }
                }
            } else {
                ActionManager.get(ShopHandler.class).handleShopClose();
                return;
            }
        }
        byte dialogX = 52;
        byte dialogY = 44;
        surface.drawBox(dialogX, dialogY, 408, 12, 192);
        surface.drawBoxAlpha(dialogX, dialogY + 12, 408, 17, 0x989898, 160);
        surface.drawBoxAlpha(dialogX, dialogY + 29, 8, 170, 0x989898, 160);
        surface.drawBoxAlpha(dialogX + 399, dialogY + 29, 9, 170, 0x989898, 160);
        surface.drawBoxAlpha(dialogX, dialogY + 199, 408, 47, 0x989898, 160);
        surface.drawString(shopName/*"Buying and selling items"*/, dialogX + 1, dialogY + 10, 1, 0xffffff);
        int colour = 0xffffff;
        if (super.mouseX > dialogX + 320 && super.mouseY >= dialogY && super.mouseX < dialogX + 408 && super.mouseY < dialogY + 12) {
            colour = 0xff0000;
        }
        surface.drawstringRight("Close window", dialogX + 406, dialogY + 10, 1, colour);
        surface.drawString("Shops stock in green", dialogX + 2, dialogY + 24, 1, 65280);
        surface.drawString("Number you own in blue", dialogX + 135, dialogY + 24, 1, 65535);
        surface.drawString("Your money: " + getInventoryCount(10) + "gp", dialogX + 280, dialogY + 24, 1, 0xffff00);
        int itemIndex = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                int slotX = dialogX + 7 + col * 49;
                int slotY = dialogY + 28 + row * 34;
                if (shopSelectedItemIndex == itemIndex) {
                    surface.drawBoxAlpha(slotX, slotY, 49, 34, 0xff0000, 160);
                } else {
                    surface.drawBoxAlpha(slotX, slotY, 49, 34, 0xd0d0d0, 160);
                }
                surface.drawBoxEdge(slotX, slotY, 50, 35, 0);
                if (shopItem[itemIndex] != -1) {
                    surface.spriteClipping(slotX, slotY, 48, 32, spriteItem + EntityManager.getItem(shopItem[itemIndex]).getSprite(), EntityManager.getItem(shopItem[itemIndex]).getMask(), 0, 0, false);
                    surface.drawString(String.valueOf(shopItemCount[itemIndex]), slotX + 1, slotY + 10, 1, 65280);
                    surface.drawstringRight(String.valueOf(getInventoryCount(shopItem[itemIndex])), slotX + 47, slotY + 10, 1, 65535);
                }
                itemIndex++;
            }

        }

        surface.drawLineHoriz(dialogX + 5, dialogY + 222, 398, 0);
        if (shopSelectedItemIndex == -1) {
            surface.drawStringCenter("Select an object to buy or sell", dialogX + 204, dialogY + 214, 3, 0xffff00);
            return;
        }
        int selectedItemType = shopItem[shopSelectedItemIndex];
        if (selectedItemType != -1) {
            if (shopItemCount[shopSelectedItemIndex] > 0) {
                int itemPrice = shopItemBuyPrice[shopSelectedItemIndex];
                surface.drawString("Buy a new " + EntityManager.getItem(selectedItemType).getName() + " for " + itemPrice + "gp", dialogX + 2, dialogY + 214, 1, 0xffff00);
                colour = 0xffffff;
                if (super.mouseX > dialogX + 298 && super.mouseY >= dialogY + 204 && super.mouseX < dialogX + 408 && super.mouseY <= dialogY + 215) {
                    colour = 0xff0000;
                }
                surface.drawstringRight("Click here to buy", dialogX + 405, dialogY + 214, 3, colour);
            } else {
                surface.drawStringCenter("This item is not currently available to buy", dialogX + 204, dialogY + 214, 3, 0xffff00);
            }
            if (getInventoryCount(selectedItemType) > 0) {
                int itemPrice = shopItemSellPrice[shopSelectedItemIndex];
                if (itemPrice == 0) {
                    itemPrice = EntityManager.getItem(selectedItemType).getPrice() / 2; // TODO: needs to be fixed
                }
                surface.drawstringRight("Sell your " + EntityManager.getItem(selectedItemType).getName() + " for " + itemPrice + "gp", dialogX + 405, dialogY + 239, 1, 0xffff00);
                colour = 0xffffff;
                if (super.mouseX > dialogX + 2 && super.mouseY >= dialogY + 229 && super.mouseX < dialogX + 112 && super.mouseY <= dialogY + 240) {
                    colour = 0xff0000;
                }
                surface.drawString("Click here to sell", dialogX + 2, dialogY + 239, 3, colour);
                return;
            }
            surface.drawStringCenter("You do not have any of this item to sell", dialogX + 204, dialogY + 239, 3, 0xffff00);
        }
    }

    private boolean hasInventoryItems(int id, int mincount) {
        if (id == 31 && (isItemEquipped(197) || isItemEquipped(615) || isItemEquipped(682))) {
            return true;
        }
        if (id == 32 && (isItemEquipped(102) || isItemEquipped(616) || isItemEquipped(683))) {
            return true;
        }
        if (id == 33 && (isItemEquipped(101) || isItemEquipped(617) || isItemEquipped(684))) {
            return true;
        }
        if (id == 34 && (isItemEquipped(103) || isItemEquipped(618) || isItemEquipped(685))) {
            return true;
        }
        return getInventoryCount(id) >= mincount;
    }

    public void cantLogout() {
        logoutTimeout = 0;
        showMessage("@cya@Sorry, you can't logout at the moment", 3);
    }

    public int captchaPixels[][] = new int[0][0];
    public int captchaWidth = 0;
    public int captchaHeight = 0;
    
    private long modelSubRoutineTimeout = 0L;
    
    @SuppressWarnings("InfiniteRecursion")
    private void addModelSubRoutine(int i) {
        int x = localPlayer.currentX / 128;
        int y = localPlayer.currentY / 128;
        try {
            if(modelSubRoutineTimeout == 0L && lastHeightOffset == 0 && (world.walkableValue[x][y] & 128) == 0) {
                scene.addModel(world.roofModels[lastHeightOffset][i]);
                if (lastHeightOffset == 0) {
                    scene.addModel(world.wallModels[1][i]);
                    scene.addModel(world.roofModels[1][i]);
                    scene.addModel(world.wallModels[2][i]);
                    scene.addModel(world.roofModels[2][i]);
                }
                fogOfWar = false;
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            System.err.println("Retrying subroutine: x=" + x + " y=" + y + " cause: " + e.getMessage());
            modelSubRoutineTimeout = 800L;
            addModelSubRoutine(i);
        }
    }

    private void drawGame() {
        if (deathScreenTimeout != 0) {
            surface.fade2black();
            surface.drawStringCenter("Oh dear! You are dead...", gameWidth / 2, gameHeight / 2, 7, 0xff0000);
            drawChatMessageTabs();
            surface.draw(graphics, 0, 0);
            return;
        }
        if (showAppearanceChange) {
            drawAppearancePanelCharacterSprites();
            return;
        }
        if (isSleeping) {
            surface.fade2black();
            if (Math.random() < 0.14999999999999999D) {
                surface.drawStringCenter("ZZZ", (int) (Math.random() * 80D), (int) (Math.random() * (double) gameHeight), 5, (int) (Math.random() * 16777215D));
            }
            if (Math.random() < 0.14999999999999999D) {
                surface.drawStringCenter("ZZZ", gameWidth - (int) (Math.random() * 80D), (int) (Math.random() * (double) gameHeight), 5, (int) (Math.random() * 16777215D));
            }
            //surface.drawBox(gameWidth / 2 - 100, 160, 200, 40, Color.BLACK);
            surface.drawStringCenter("You are sleeping", gameWidth / 2, 50, 7, 0xffff00);
            surface.drawStringCenter("Fatigue: " + fatigueSleeping + "%", gameWidth / 2, 90, 7, 0xffff00);
            surface.drawStringCenter("When you want to wake up just use your", gameWidth / 2, 140, 5, 0xffffff);
            surface.drawStringCenter("keyboard to type the word in the box below", gameWidth / 2, 160, 5, 0xffffff);
            surface.drawStringCenter(super.inputTextCurrent + "*", gameWidth / 2, 180, 5, 65535);
            if (sleepingStatusText == null) {
                surface.drawPixels(captchaPixels, gameWidth / 2 - 127, 230, captchaWidth, captchaHeight);
            } else {
                surface.drawStringCenter(sleepingStatusText, gameWidth / 2, 260, 5, 0xff0000);
            }
            surface.drawBoxEdge(gameWidth / 2 - 128, 229, 257, 42, 0xffffff);
            drawChatMessageTabs();
            surface.drawStringCenter("If you can't read the word", gameWidth / 2, 290, 1, 0xffffff);
            surface.drawStringCenter("@yel@click here@whi@ to get a different one", gameWidth / 2, 305, 1, 0xffffff);
            surface.draw(graphics, 0, 0);
            return;
        }
        if (!world.playerIsAlive) {
            return;
        }
        for (int i = 0; i < 64; i++) {
            scene.removeModel(world.roofModels[lastHeightOffset][i]);
            if (lastHeightOffset == 0) {
                scene.removeModel(world.wallModels[1][i]);
                scene.removeModel(world.roofModels[1][i]);
                scene.removeModel(world.wallModels[2][i]);
                scene.removeModel(world.roofModels[2][i]);
            }
            fogOfWar = true;
            addModelSubRoutine(i);
        }

        if (objectAnimationNumberFireLightningSpell != lastObjectAnimationNumberFireLightningSpell) {
            lastObjectAnimationNumberFireLightningSpell = objectAnimationNumberFireLightningSpell;
            for (int j = 0; j < objectCount; j++) {
                if (objectID[j] == 97) {
                    updateObjectAnimation(j, "firea" + (objectAnimationNumberFireLightningSpell + 1));
                }
                if (objectID[j] == 274) {
                    updateObjectAnimation(j, "fireplacea" + (objectAnimationNumberFireLightningSpell + 1));
                }
                if (objectID[j] == 1031) {
                    updateObjectAnimation(j, "lightning" + (objectAnimationNumberFireLightningSpell + 1));
                }
                if (objectID[j] == 1036) {
                    updateObjectAnimation(j, "firespell" + (objectAnimationNumberFireLightningSpell + 1));
                }
                if (objectID[j] == 1147) {
                    updateObjectAnimation(j, "spellcharge" + (objectAnimationNumberFireLightningSpell + 1));
                }
            }

        }
        if (objectAnimationNumberTorch != lastObjectAnimationNumberTorch) {
            lastObjectAnimationNumberTorch = objectAnimationNumberTorch;
            for (int k = 0; k < objectCount; k++) {
                if (objectID[k] == 51) {
                    updateObjectAnimation(k, "torcha" + (objectAnimationNumberTorch + 1));
                }
                if (objectID[k] == 143) {
                    updateObjectAnimation(k, "skulltorcha" + (objectAnimationNumberTorch + 1));
                }
            }

        }
        if (objectAnimationNumberClaw != lastOjectAnimationNumberClaw) {
            lastOjectAnimationNumberClaw = objectAnimationNumberClaw;
            for (int l = 0; l < objectCount; l++) {
                if (objectID[l] == 1142) {
                    updateObjectAnimation(l, "clawspell" + (objectAnimationNumberClaw + 1));
                }
            }
        }
        scene.reduceSprites(spriteCount);
        spriteCount = 0;
        for (int i = 0; i < playerCount; i++) {
            Mob character = players[i];
            if (character.colourBottom != 255) {
                int x = character.currentX;
                int y = character.currentY;
                int elev = -world.getAveragedElevation(x, y);
                int id = scene.addSprite(5000 + i, x, elev, y, 145, 220, i + 10000);
                spriteCount++;
                if (character == localPlayer) {
                    scene.setLocalPlayer(id);
                }
                if (character.animationCurrent == 8) {
                    scene.setSpriteTranslateX(id, -30);
                }
                if (character.animationCurrent == 9) {
                    scene.setSpriteTranslateX(id, 30);
                }
            }
        }

        for (int i = 0; i < playerCount; i++) {
            Mob player = players[i];
            if (player.projectileRange > 0) {
                Mob character = null;
                if (player.attackingNpcServerIndex != -1) {
                    character = npcsServer[player.attackingNpcServerIndex];
                } else if (player.attackingPlayerServerIndex != -1) {
                    character = playerServer[player.attackingPlayerServerIndex];
                }
                if (character != null) {
                    int sx = player.currentX;
                    int sy = player.currentY;
                    int selev = -world.getAveragedElevation(sx, sy) - 110;
                    int dx = character.currentX;
                    int dy = character.currentY;
                    int delev = -world.getAveragedElevation(dx, dy) - EntityManager.getNPC(character.npcId).getCameraHeight() / 2;
                    int rx = (sx * player.projectileRange + dx * (projectileMaxRange - player.projectileRange)) / projectileMaxRange;
                    int rz = (selev * player.projectileRange + delev * (projectileMaxRange - player.projectileRange)) / projectileMaxRange;
                    int ry = (sy * player.projectileRange + dy * (projectileMaxRange - player.projectileRange)) / projectileMaxRange;
                    scene.addSprite(spriteProjectile + player.incomingProjectileSprite, rx, rz, ry, 32, 32, 0);
                    spriteCount++;
                }
            }
        }

        for (int i = 0; i < npcCount; i++) {
            Mob character_3 = npcs[i];
            int i3 = character_3.currentX;
            int j4 = character_3.currentY;
            int i7 = -world.getAveragedElevation(i3, j4);
            int i9 = scene.addSprite(20000 + i, i3, i7, j4, EntityManager.getNPC(character_3.npcId).getCameraWidth(), EntityManager.getNPC(character_3.npcId).getCameraHeight(), i + 30000);
            spriteCount++;
            if (character_3.animationCurrent == 8) {
                scene.setSpriteTranslateX(i9, -30);
            }
            if (character_3.animationCurrent == 9) {
                scene.setSpriteTranslateX(i9, 30);
            }
        }

        for (int i = 0; i < groundItemCount; i++) {
            int x = groundItemX[i] * magicLoc + 64;
            int y = groundItemY[i] * magicLoc + 64;
            scene.addSprite(40000 + groundItemID[i], x, -world.getAveragedElevation(x, y) - groundItemZ[i], y, 96, 64, i + 20000);
            spriteCount++;
        }

        for (int i = 0; i < teleportBubbleCount; i++) {
            int l4 = teleportBubbleX[i] * magicLoc + 64;
            int j7 = teleportBubbleY[i] * magicLoc + 64;
            int j9 = teleportBubbleType[i];
            if (j9 == 0) {
                scene.addSprite(50000 + i, l4, -world.getAveragedElevation(l4, j7), j7, 128, 256, i + 50000);
                spriteCount++;
            }
            if (j9 == 1) {
                scene.addSprite(50000 + i, l4, -world.getAveragedElevation(l4, j7), j7, 128, 64, i + 50000);
                spriteCount++;
            }
        }

        surface.interlace = false;
        surface.blackScreen();
        surface.interlace = super.interlace;
        if (lastHeightOffset == 3) {
            int i5 = 40 + (int) (Math.random() * 3D);
            int k7 = 40 + (int) (Math.random() * 7D);
            scene.setLight(i5, k7, -50, -10, -50);
        }
        itemsAboveHeadCount = 0;
        receivedMessagesCount = 0;
        healthBarCount = 0;
        if (cameraAutoAngleDebug) {
            if (optionCameraModeAuto && !fogOfWar) {
                int j5 = cameraAngle;
                autorotateCamera();
                if (cameraAngle != j5) {
                    cameraAutoRotatePlayerX = localPlayer.currentX;
                    cameraAutoRotatePlayerY = localPlayer.currentY;
                }
            }
            scene.clipFar3d = 3000;
            scene.clipFar2d = 3000;
            scene.fogZDensity = 1;
            scene.fogZDistance = 2800;
            cameraRotation = cameraAngle * 32;
            int x = cameraAutoRotatePlayerX + cameraRotationX;
            int y = cameraAutoRotatePlayerY + cameraRotationY;
            scene.setCamera(x, -world.getAveragedElevation(x, y), y, cameraPitch, cameraRotation * 4, 0, 2000);
        } else {
            if (optionCameraModeAuto && !fogOfWar) {
                autorotateCamera();
            }
            if (!super.interlace) {
                scene.clipFar3d = 2400;
                scene.clipFar2d = 2400;
                scene.fogZDensity = 1;
                scene.fogZDistance = 2300;
            } else {
                scene.clipFar3d = 2200;
                scene.clipFar2d = 2200;
                scene.fogZDensity = 1;
                scene.fogZDistance = 2100;
            }
            int x = cameraAutoRotatePlayerX + cameraRotationX;
            int y = cameraAutoRotatePlayerY + cameraRotationY;
            scene.setCamera(x, -world.getAveragedElevation(x, y), y, cameraPitch, cameraRotation * 4, 0, cameraZoom * 2);
        }
        scene.render();
        drawAboveHeadStuff();
        if (mouseClickXStep > 0) {
            surface.drawSprite(mouseClickXX - 8, mouseClickXY - 8, spriteMedia + 14 + (24 - mouseClickXStep) / 6);
        }
        if (mouseClickXStep < 0) {
            surface.drawSprite(mouseClickXX - 8, mouseClickXY - 8, spriteMedia + 18 + (24 + mouseClickXStep) / 6);
        }
        if (!loadingArea) {
            int j6 = 2203 - (localRegionY + planeHeight + regionY);
            if (localRegionX + planeWidth + regionX >= 2640) {
                j6 = -50;
            }
            if (j6 > 0) {
                int wildlvl = 1 + j6 / 6;
                surface.drawSprite(gameWidth - 65, gameHeight - 56 - 20, spriteMedia + 13);
                surface.drawStringCenter("Wilderness", gameWidth - 47 - 7, gameHeight - 20 - 20, 1, 0xffff00);
                surface.drawStringCenter("Level: " + wildlvl, gameWidth - 47 - 7, gameHeight - 7 - 20, 1, 0xffff00);
                if (showUiWildWarn == 0) {
                    showUiWildWarn = 2;
                }
            }
            if (showUiWildWarn == 0 && j6 > -10 && j6 <= 0) {
                showUiWildWarn = 1;
            }
        }
        if (messageTabSelected == 0) {
            for (int k6 = 0; k6 < 5; k6++) {
                if (messageHistoryTimeout[k6] > 0) {
                    String s = messageHistory[k6];
                    int OFFSET = 20;
                    surface.drawString(s, 7, gameHeight-OFFSET - 18 - k6 * 12, 1, 0xffff00); /* this is where game messages are shown */
                }
            }
        }
        panelMessageTabs.hide(controlTextListChat);
        panelMessageTabs.hide(controlTextListQuest);
        panelMessageTabs.hide(controlTextListPrivate);
        if (messageTabSelected == 1) {
            panelMessageTabs.show(controlTextListChat);
        } else if (messageTabSelected == 2) {
            panelMessageTabs.show(controlTextListQuest);
        } else if (messageTabSelected == 3) {
            panelMessageTabs.show(controlTextListPrivate);
        }
        Menu.textListEntryHeightMod = 2;
        panelMessageTabs.drawPanel();
        Menu.textListEntryHeightMod = 0;
        surface.drawSpriteAlpha(surface.width2 - 3 - 197, 3, spriteMedia, 128);
        drawUi();
        surface.loggedIn = false;
        drawChatMessageTabs();
        surface.draw(graphics, 0, 0);
    }
    
    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    private void loadSounds() {
        showLoadingProgress(90, "Loading sounds");
        SoundEffect.init();
    }

    private boolean isItemEquipped(int i) {
        for (int j = 0; j < inventoryItemsCount; j++) {
            if (inventoryItemId[j] == i && inventoryEquipped[j] == 1) {
                return true;
            }
        }
        return false;
    }

    private void loadEntities() {
        byte entityBuff[] = null;
        byte indexDat[] = null;
        entityBuff = readDataFile("entity.jag", "people and monsters", 30);
        if (entityBuff == null) {
            errorLoadingData = true;
            return;
        }
        indexDat = Util.unpackData("index.dat", 0, entityBuff);
        byte entityBuffMem[] = null;
        byte indexDatMem[] = null;
        if (Constants.MEMBER_WORLD) {
            entityBuffMem = readDataFile("entity.mem", "member graphics", 45);
            if (entityBuffMem == null) {
                errorLoadingData = true;
                return;
            }
            indexDatMem = Util.unpackData("index.dat", 0, entityBuffMem);
        }
        int frameCount = 0;
        anInt659 = 0;
        animIndex = anInt659;
        label0:
        for (int j = 0; j < EntityManager.getAnimations().length; j++) {
            String s = EntityManager.getAnimation(j).getName();
            if (s == null) {
                errorLoadingData = true;
                return;
            }
            for (int k = 0; k < j; k++) {
                if (!EntityManager.getAnimation(k).getName().equalsIgnoreCase(s)) {
                    continue;
                }
                EntityManager.getAnimation(j).number = EntityManager.getAnimation(k).getNumber();
                continue label0;
            }

            byte abyte7[] = Util.unpackData(s + ".dat", 0, entityBuff);
            byte abyte4[] = indexDat;
            if (abyte7 == null && Constants.MEMBER_WORLD) {
                abyte7 = Util.unpackData(s + ".dat", 0, entityBuffMem);
                abyte4 = indexDatMem;
            }
            if (abyte7 != null) {
                surface.parseSprite(animIndex, abyte7, abyte4, 15);
                frameCount += 15;
                if (EntityManager.getAnimation(j).hasA()) {
                    byte aDat[] = Util.unpackData(s + "a.dat", 0, entityBuff);
                    byte aIndexDat[] = indexDat;
                    if (aDat == null && Constants.MEMBER_WORLD) {
                        aDat = Util.unpackData(s + "a.dat", 0, entityBuffMem);
                        aIndexDat = indexDatMem;
                    }
                    surface.parseSprite(animIndex + 15, aDat, aIndexDat, 3);
                    frameCount += 3;
                }
                if (EntityManager.getAnimation(j).hasF()) {
                    byte fDat[] = Util.unpackData(s + "f.dat", 0, entityBuff);
                    byte fDatIndex[] = indexDat;
                    if (fDat == null && Constants.MEMBER_WORLD) {
                        fDat = Util.unpackData(s + "f.dat", 0, entityBuffMem);
                        fDatIndex = indexDatMem;
                    }
                    surface.parseSprite(animIndex + 18, fDat, fDatIndex, 9);
                    frameCount += 9;
                }
                if (EntityManager.getAnimation(j).getGenderModel() != 0) {
                    for (int l = animIndex; l < animIndex + 27; l++) {
                        surface.loadSprite(l);
                    }
                }
            }
            EntityManager.getAnimation(j).number = animIndex;
            animIndex += 27;
        }

        System.out.println("[Classic Client] Loaded " + frameCount + " frames of animation");
    }

    private void handleAppearancePanelControls() {
        panelAppearance.handleMouse(super.mouseX, super.mouseY, super.lastMouseButtonDown, super.mouseButtonDown);
        if (panelAppearance.isClicked(controlButtonAppearanceHead1)) {
            do {
                appearanceHeadType = ((appearanceHeadType - 1) + EntityManager.getAnimations().length) % EntityManager.getAnimations().length;
            } while ((EntityManager.getAnimation(appearanceHeadType).getGenderModel() & 3) != 1 || (EntityManager.getAnimation(appearanceHeadType).getGenderModel() & 4 * appearanceHeadGender) == 0);
        }
        if (panelAppearance.isClicked(controlButtonAppearanceHead2)) {
            do {
                appearanceHeadType = (appearanceHeadType + 1) % EntityManager.getAnimations().length;
            } while ((EntityManager.getAnimation(appearanceHeadType).getGenderModel() & 3) != 1 || (EntityManager.getAnimation(appearanceHeadType).getGenderModel() & 4 * appearanceHeadGender) == 0);
        }
        if (panelAppearance.isClicked(controlButtonAppearanceHair1)) {
            appearanceHairColour = ((appearanceHairColour - 1) + characterHairColours.length) % characterHairColours.length;
        }
        if (panelAppearance.isClicked(controlButtonAppearanceHair2)) {
            appearanceHairColour = (appearanceHairColour + 1) % characterHairColours.length;
        }
        if (panelAppearance.isClicked(controlButtonAppearanceGender1) || panelAppearance.isClicked(controlButtonAppearanceGender2)) {
            for (appearanceHeadGender = 3 - appearanceHeadGender; (EntityManager.getAnimation(appearanceHeadType).getGenderModel() & 3) != 1 || (EntityManager.getAnimation(appearanceHeadType).getGenderModel() & 4 * appearanceHeadGender) == 0; appearanceHeadType = (appearanceHeadType + 1) % EntityManager.getAnimations().length);
            for (; (EntityManager.getAnimation(appearanceBodyGender).getGenderModel() & 3) != 2 || (EntityManager.getAnimation(appearanceBodyGender).getGenderModel() & 4 * appearanceHeadGender) == 0; appearanceBodyGender = (appearanceBodyGender + 1) % EntityManager.getAnimations().length);
        }
        if (panelAppearance.isClicked(controlButtonAppearanceTop1)) {
            appearanceTopColour = ((appearanceTopColour - 1) + characterTopBottomColours.length) % characterTopBottomColours.length;
        }
        if (panelAppearance.isClicked(controlButtonAppearanceTop2)) {
            appearanceTopColour = (appearanceTopColour + 1) % characterTopBottomColours.length;
        }
        if (panelAppearance.isClicked(controlButtonAppearanceSkin1)) {
            appearanceSkinColour = ((appearanceSkinColour - 1) + characterSkinColours.length) % characterSkinColours.length;
        }
        if (panelAppearance.isClicked(controlButtonAppearanceSkin2)) {
            appearanceSkinColour = (appearanceSkinColour + 1) % characterSkinColours.length;
        }
        if (panelAppearance.isClicked(controlButtonAppearanceBottom1)) {
            appearanceBottomColour = ((appearanceBottomColour - 1) + characterTopBottomColours.length) % characterTopBottomColours.length;
        }
        if (panelAppearance.isClicked(controlButtonAppearanceBottom2)) {
            appearanceBottomColour = (appearanceBottomColour + 1) % characterTopBottomColours.length;
        }
        if (panelAppearance.isClicked(controlButtonAppearanceAccept)) {
            ActionManager.get(AppearanceHandler.class).handleAppearanceChange(appearanceHeadGender, appearanceHeadType, appearanceBodyGender, appearance2Colour, appearanceHairColour, appearanceTopColour, appearanceBottomColour, appearanceSkinColour);
            surface.blackScreen();
            showAppearanceChange = false;
        }
    }
    
    private boolean errorLoadingMemory = false;

    @Override
    protected void draw() {
        /*if (errorLoadingData) {
            Graphics g = getGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, gameWidth, gameHeight);
            g.setFont(new Font("Helvetica", 1, 16));
            g.setColor(Color.RED);
            int i = 35;
            g.drawString("ERROR: Cannot load data files", 30, i);
            i += 50;
            g.setColor(Color.WHITE);
            g.drawString("Please contact the developer, if you did not tamper with the data files", 30, i);
            setTargetFps(1);
            return;
        }
        if (errorLoadingMemory) {
            Graphics g = getGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, gameWidth, gameHeight);
            g.setFont(new Font("Helvetica", 1, 16));
            g.setColor(Color.RED);
            int i = 35;
            g.drawString("ERROR: Out of memory", 30, i);
            i += 50;
            g.setColor(Color.WHITE);
            g.drawString("Please contact the developer", 30, i);
            setTargetFps(1);
            return;
        }*/
        try {
            if (loggedIn == 0) {
                surface.loggedIn = false;
                drawLoginScreens();
                if(musicPlayer != null && musicPlayer.isRunning()) {
                    musicPlayer.stop();
                }
            } else if (loggedIn == 1) {
                surface.loggedIn = true;
                drawGame();
                if(Constants.APPLICATION_RESIZABLE) {
                    synchronized(dimension) {
                        if (!resized) {
                            resizeGame(dimension.width, dimension.height);
                            resized = true;
                        }
                    }
                }
                if(modelSubRoutineTimeout > 0) {
                    modelSubRoutineTimeout--;
                }
            }
        } catch (OutOfMemoryError e) {
            disposeAndCollect();
            e.printStackTrace();
            errorLoadingMemory = true;
        }
    }

    @Override
    protected void onClosing() {
        if(player != null) {
            player.save();
        }
    }

    private boolean walkToGroundItem(int i, int j, int k, int l, boolean walkToAction) {
        if (walkTo(i, j, k, l, k, l, false, walkToAction)) {
            return true;
        } else {
            return walkToActionSource(i, j, k, l, k, l, true, walkToAction);
        }
    }

    private void loadModels() {
        showLoadingProgress(70, "Loading 3d models");
        String[] modelNames = {
            "torcha2", "torcha3", "torcha4", "skulltorcha2", "skulltorcha3", "skulltorcha4", "firea2",
            "firea3", "fireplacea2", "fireplacea3", "firespell2", "firespell3", "lightning2", "lightning3",
            "clawspell2", "clawspell3", "clawspell4", "clawspell5", "spellcharge2", "spellcharge3"
        };
        boolean useCache = true;
        if (useCache) {
            for (String name : modelNames) {
                EntityManager.storeModel(name);
            }
            byte[] data = readDataFile("models.jag", "3d models", 60);
            if (data == null) {
                errorLoadingData = true;
                return;
            }
            for (int j = 0; j < EntityManager.getModelCount(); j++) {
                int k = Util.getDataFileOffset(EntityManager.getModelName(j) + ".ob3", data);
                if (k != 0) {
                    gameModels[j] = new Model(data, k, true);
                } else {
                    gameModels[j] = new Model(1, 1);
                }
                if (EntityManager.getModelName(j).equals("giantcrystal")) {
                    gameModels[j].transparent = true;
                }
            }
        }
    }

    private void drawDialogServerMessage() {
        int lines = serverMessage.length() - serverMessage.replace(" %", "").length(); // ' %' indicates new line
        int width = 400;
        int height = 100;
        if(serverMessageBoxBig) {
            height = 300;
        }
        int widthOffset = (gameWidth - width) / 2;
        surface.drawBox((gameWidth / 2) - (width / 2), (gameHeight / 2) - (height / 2), width, height, 0);
        surface.drawBoxEdge((gameWidth / 2) - width / 2, (gameHeight / 2) - height / 2, width, height, 0xffffff);
        surface.drawStringCentered(serverMessage, (gameWidth / 2), ((gameHeight / 2) - height / 2) + 20, 1, 0xffffff, width - 40);
        int i = ((gameHeight / 2) - 10) + height / 2;
        int color = 0xffffff;
        if (super.mouseY > i - 12 && super.mouseY <= i && super.mouseX > widthOffset + 122 && super.mouseX < widthOffset + 276) {
            color = 0xff0000;
        }
        surface.drawStringCenter("Click here to close window", (gameWidth / 2), i, 1, color);
        if (mouseButtonClick == 1) {
            if (color == 0xff0000) {
                showDialogServerMessage = false;
                serverMessageBoxBig = false;
            }
        }
        mouseButtonClick = 0;
    }

    public void showMessage(String message) {
        int tab = 3;
        if(message.endsWith("@que@")) {
            tab = 5;
        }
        showMessage(message, tab);
    }

    public void showMessage(String message, int type) {
        if (type == 2 || type == 4 || type == 6) {
            for (; message.length() > 5 && message.charAt(0) == '@' && message.charAt(4) == '@'; message = message.substring(5));
        }
        if (type == 2) {
            message = "@yel@" + message;
        }
        if (type == 3 || type == 4) {
            message = "@whi@" + message;
        }
        if (type == 6) {
            message = "@cya@" + message;
        }
        if (messageTabSelected != 0) {
            if (type == 4 || type == 3) {
                messageTabFlashAll = 200;
            }
            if (type == 2 && messageTabSelected != 1) {
                messageTabFlashHistory = 200;
            }
            if (type == 5 && messageTabSelected != 2) {
                messageTabFlashQuest = 200;
            }
            if (type == 6 && messageTabSelected != 3) {
                messageTabFlashPrivate = 200;
            }
            if (type == 3 && messageTabSelected != 0) {
                messageTabSelected = 0;
            }
            if (type == 6 && messageTabSelected != 3 && messageTabSelected != 0) {
                messageTabSelected = 0;
            }
        }
        for (int k = 4; k > 0; k--) {
            messageHistory[k] = messageHistory[k - 1];
            messageHistoryTimeout[k] = messageHistoryTimeout[k - 1];
        }

        messageHistory[0] = message;
        messageHistoryTimeout[0] = 300;
        if (type == 2) {
            if (panelMessageTabs.controlScrollAmount[controlTextListChat] == panelMessageTabs.controlListEntryCount[controlTextListChat] - 4) {
                panelMessageTabs.removeListEntry(controlTextListChat, message, true);
            } else {
                panelMessageTabs.removeListEntry(controlTextListChat, message, false);
            }
        }
        if (type == 5) {
            if (panelMessageTabs.controlScrollAmount[controlTextListQuest] == panelMessageTabs.controlListEntryCount[controlTextListQuest] - 4) {
                panelMessageTabs.removeListEntry(controlTextListQuest, message, true);
            } else {
                panelMessageTabs.removeListEntry(controlTextListQuest, message, false);
            }
        }
        if (type == 6) {
            if (panelMessageTabs.controlScrollAmount[controlTextListPrivate] == panelMessageTabs.controlListEntryCount[controlTextListPrivate] - 4) {
                panelMessageTabs.removeListEntry(controlTextListPrivate, message, true);
                return;
            }
            panelMessageTabs.removeListEntry(controlTextListPrivate, message, false);
        }
    }

    private boolean walkToObject(int x, int y, int id, int index) {
        int w;
        int h;
        if (id == 0 || id == 4) {
            w = EntityManager.getGameObjectDef(index).getWidth();
            h = EntityManager.getGameObjectDef(index).getHeight();
        } else {
            h = EntityManager.getGameObjectDef(index).getWidth();
            w = EntityManager.getGameObjectDef(index).getHeight();
        }
        if (EntityManager.getGameObjectDef(index).getType() == 2 || EntityManager.getGameObjectDef(index).getType() == 3) {
            if (id == 0) {
                x--;
                w++;
            }
            if (id == 2) {
                h++;
            }
            if (id == 4) {
                w++;
            }
            if (id == 6) {
                y--;
                h++;
            }
            return walkToActionSource(localRegionX, localRegionY, x, y, (x + w) - 1, (y + h) - 1, false, true);
        } else {
            return walkToActionSource(localRegionX, localRegionY, x, y, (x + w) - 1, (y + h) - 1, true, true);
        }
    }

    private int getInventoryCount(int id) {
        int count = 0;
        for (int k = 0; k < inventoryItemsCount; k++) {
            if (inventoryItemId[k] == id) {
                if (!EntityManager.getItem(id).isStackable()) {
                    count++;
                } else {
                    count += inventoryItemStackCount[k];
                }
            }
        }

        return count;
    }

    private void drawLoginScreens() {
        welcomScreenAlreadyShown = false;
        surface.interlace = false;
        surface.blackScreen();
        if (loginScreen == 0 || loginScreen == 2 || loginScreen == 3) {
            int i = (loginTimer * 2) % 3072;
            if (i < 1024) {
                surface.drawSprite(0, 10, spriteLogo);
                if (i > 768) {
                    surface.drawSpriteAlpha(0, 10, spriteLogo + 1, i - 768);
                }
            } else if (i < 2048) {
                surface.drawSprite(0, 10, spriteLogo + 1);
                if (i > 1792) {
                    surface.drawSpriteAlpha(0, 10, spriteMedia + 10, i - 1792);
                }
            } else {
                surface.drawSprite(0, 10, spriteMedia + 10);
                if (i > 2816) {
                    surface.drawSpriteAlpha(0, 10, spriteLogo, i - 2816);
                }
            }
        }
        if (loginScreen == 0) {
            panelLoginWelcome.drawPanel();
        }
        if (loginScreen == 1) {
            //surface.drawSprite(0, 0, spriteMedia + 22); // top blue bar
            panelRegisterUser.drawPanel();
        }
        if (loginScreen == 2) {
            String text = panelLoginExistingUser.getText(controlLoginStatus1);
            if (text != null && text.length() > 0) {
                surface.drawBoxAlpha(0, 185, gameWidth, 30, 0, 100);
            }
            panelLoginExistingUser.drawPanel();
        }
        surface.drawSprite(0, gameHeight, spriteMedia + 22); // bottom blue bar
        /* extended bottom blue bar */
        surface.drawSprite(gameWidth - 512, gameHeight, spriteMedia + 22);

        //surface.drawLineAlpha(10, 10, 0, 0, 512, 14);
        surface.draw(graphics, 0, 0);
    }

    private void drawUiTabOptions(boolean flag) {
        int uiX = surface.width2 - 199;
        int uiY = 36;
        surface.drawSprite(uiX - 49, 3, spriteMedia + 6);
        int uiWidth = 196;
        surface.drawBoxAlpha(uiX, 36, uiWidth, 96, Surface.rgb2long(181, 181, 181), 160);
        surface.drawBoxAlpha(uiX, 132, uiWidth, 40, Surface.rgb2long(201, 201, 201), 160);
        int x = uiX + 3;
        int y = uiY + 15;
        surface.drawString("Game options - click to toggle", x, y, 1, 0);
        y += 15;
        if (optionCameraModeAuto) {
            surface.drawString("Camera angle mode - @gre@Auto", x, y, 1, 0xffffff);
        } else {
            surface.drawString("Camera angle mode - @red@Manual", x, y, 1, 0xffffff);
        }
        y += 15;
        if (optionMouseButtonOne) {
            surface.drawString("Mouse buttons - @red@One", x, y, 1, 0xffffff);
        } else {
            surface.drawString("Mouse buttons - @gre@Two", x, y, 1, 0xffffff);
        }
        y += 15;
        if (optionSoundDisabled) {
            surface.drawString("Sound effects - @red@Off", x, y, 1, 0xffffff);
        } else {
            surface.drawString("Sound effects - @gre@On", x, y, 1, 0xffffff);
        }
        y += 15;
        if (optionMusicLoop) {
            surface.drawString("Music loop - @gre@On", x, y, 1, 0xffffff);
        } else {
            surface.drawString("Music loop - @red@Off", x, y, 1, 0xffffff);
        }
        y += 15;
        if (optionMusicAuto) {
            surface.drawString("Regional Music - @gre@On", x, y, 1, 0xffffff);
        } else {
            surface.drawString("Regional Music - @red@Off", x, y, 1, 0xffffff);
        }
        y += 15;
        y += 5;
        surface.drawString("Always logout when you finish", x, y, 1, 0);
        y += 15;
        int k1 = 0xffffff;
        if (super.mouseX > x && super.mouseX < x + uiWidth && super.mouseY > y - 12 && super.mouseY < y + 4) {
            k1 = 0xffff00;
        }
        surface.drawString("Click here to logout", uiX + 3, y, 1, k1);
        if (!flag) {
            return;
        }
        int mouseX = super.mouseX - (surface.width2 - 199);
        int mouseY = super.mouseY - 36;
        if (mouseX >= 0 && mouseY >= 0 && mouseX < 196 && mouseY < 265) {
            int l1 = surface.width2 - 199;
            byte byte0 = 36;
            int c1 = 196;
            int l = l1 + 3;
            int j1 = byte0 + 30;
            if (super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                optionCameraModeAuto = !optionCameraModeAuto;
                player.setGameSetting(0, optionSoundDisabled);
            }
            j1 += 15;
            if (super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                optionMouseButtonOne = !optionMouseButtonOne;
                player.setGameSetting(1, optionSoundDisabled);
            }
            j1 += 15;
            if (super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                optionSoundDisabled = !optionSoundDisabled;
                player.setGameSetting(2, optionSoundDisabled);
            }
            j1 += 15;
            if (super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                optionMusicLoop = !optionMusicLoop;
                player.setGameSetting(3, optionMusicLoop);
            }
            j1 += 15;
            if (super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                optionMusicAuto = !optionMusicAuto;
                player.setGameSetting(4, optionMusicAuto);
            }
            j1 += 35;
            if (super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                sendLogout();
            }
            mouseButtonClick = 0;
        }
    }

    private void loadTextures() {
        showLoadingProgress(50, "Loading 2d textures");
        byte[] buffTextures = readDataFile("textures.jag", "Textures", 50);
        if (buffTextures == null) {
            errorLoadingData = true;
            return;
        }
        byte buffIndex[] = Util.unpackData("index.dat", 0, buffTextures);
        scene.allocateTextures(EntityManager.getTextures().length, 7, 11);
        for (int i = 0; i < EntityManager.getTextures().length; i++) {
            String name = EntityManager.getTexture(i).getName();
            byte buff1[] = Util.unpackData(name + ".dat", 0, buffTextures);
            surface.parseSprite(spriteTexture, buff1, buffIndex, 1);
            surface.drawBox(0, 0, 128, 128, 0xff00ff);
            surface.drawSprite(0, 0, spriteTexture);
            int wh = surface.spriteWidthFull[spriteTexture];
            String nameSub = EntityManager.getTexture(i).getAnimationName();
            if (nameSub != null && nameSub.length() > 0) {
                byte buff2[] = Util.unpackData(nameSub + ".dat", 0, buffTextures);
                surface.parseSprite(spriteTexture, buff2, buffIndex, 1);
                surface.drawSprite(0, 0, spriteTexture);
            }
            surface.drawSpriteClipping(spriteTextureWorld + i, 0, 0, wh, wh);
            int area = wh * wh;
            for (int j = 0; j < area; j++) {
                if (surface.surfacePixels[spriteTextureWorld + i][j] == 65280) {
                    surface.surfacePixels[spriteTextureWorld + i][j] = 0xff00ff;
                }
            }
            surface.drawWorld(spriteTextureWorld + i);
            scene.defineTexture(i, surface.spriteColoursUsed[spriteTextureWorld + i], surface.spriteColourList[spriteTextureWorld + i], wh / 64 - 1);
        }
    }

    @Override
    protected void handleMouseDown(int i, int j, int k) {

    }

    public void drawTeleportBubble(int x, int y, int w, int h, int id, int tx, int ty) {
        int type = teleportBubbleType[id];
        int time = teleportBubbleTime[id];
        if (type == 0) {
            int j2 = 255 + time * 5 * 256;
            surface.drawCircle(x + w / 2, y + h / 2, 20 + time * 2, j2, 255 - time * 5);
        }
        if (type == 1) {
            int k2 = 0xff0000 + time * 5 * 256;
            surface.drawCircle(x + w / 2, y + h / 2, 10 + time, k2, 255 - time * 5);
        }
    }

    private void updateObjectAnimation(int i, String s) { // looks like it just updates objects like torches etc to flip between the different models and appear "animated"
        int j = objectX[i];
        int k = objectY[i];
        int l = j - localPlayer.currentX / 128;
        int i1 = k - localPlayer.currentY / 128;
        byte byte0 = 7;
        if (j >= 0 && k >= 0 && j < 96 && k < 96 && l > -byte0 && l < byte0 && i1 > -byte0 && i1 < byte0) {
            scene.removeModel(objectModel[i]);
            int j1 = EntityManager.storeModel(s);
            try {
                Model gameModel = gameModels[j1].copy();
                scene.addModel(gameModel);
                gameModel.setLight(true, 48, 48, -50, -10, -50);
                gameModel.copyPosition(objectModel[i]);
                gameModel.key = i;
                objectModel[i] = gameModel;
            } catch (Exception e) {
                //e.printStackTrace(); /* will cause NPE @ "gameModels[j1].copy()", but animation still works */
            }
        }
    }

    private void createTopMouseMenu() {
        if (selectedSpell >= 0 || selectedItemInventoryIndex >= 0) {
            menuItemText1[menuItemsCount] = "Cancel";
            menuItemText2[menuItemsCount] = "";
            menuItemID[menuItemsCount] = 4000;
            menuItemsCount++;
        }
        for (int i = 0; i < menuItemsCount; i++) {
            menuIndices[i] = i;
        }

        for (boolean flag = false; !flag;) {
            flag = true;
            for (int j = 0; j < menuItemsCount - 1; j++) {
                int l = menuIndices[j];
                int j1 = menuIndices[j + 1];
                if (menuItemID[l] > menuItemID[j1]) {
                    menuIndices[j] = j1;
                    menuIndices[j + 1] = l;
                    flag = false;
                }
            }

        }

        if (menuItemsCount > 20) {
            menuItemsCount = 20;
        }
        if (menuItemsCount > 0) {
            int k = -1;
            for (int i1 = 0; i1 < menuItemsCount; i1++) {
                if (menuItemText2[menuIndices[i1]] == null || menuItemText2[menuIndices[i1]].length() <= 0) {
                    continue;
                }
                k = i1;
                break;
            }

            String s = null;
            if ((selectedItemInventoryIndex >= 0 || selectedSpell >= 0) && menuItemsCount == 1) {
                s = "Choose a target";
            } else if ((selectedItemInventoryIndex >= 0 || selectedSpell >= 0) && menuItemsCount > 1) {
                s = "@whi@" + menuItemText1[menuIndices[0]] + " " + menuItemText2[menuIndices[0]];
            } else if (k != -1) {
                s = menuItemText2[menuIndices[k]] + ": @whi@" + menuItemText1[menuIndices[0]];
            }
            if (menuItemsCount == 2 && s != null) {
                s = s + "@whi@ / 1 more option";
            }
            if (menuItemsCount > 2 && s != null) {
                s = s + "@whi@ / " + (menuItemsCount - 1) + " more options";
            }
            if (s != null) {
                surface.drawString(s, 6, 14, 1, 0xffff00);
            }
            if (!optionMouseButtonOne && mouseButtonClick == 1 || optionMouseButtonOne && mouseButtonClick == 1 && menuItemsCount == 1) {
                menuItemClick(menuIndices[0]);
                mouseButtonClick = 0;
                return;
            }
            if (!optionMouseButtonOne && mouseButtonClick == 2 || optionMouseButtonOne && mouseButtonClick == 1) {
                menuHeight = (menuItemsCount + 1) * 15;
                menuWidth = surface.textWidth("Choose option", 1) + 5;
                for (int k1 = 0; k1 < menuItemsCount; k1++) {
                    int l1 = surface.textWidth(menuItemText1[k1] + " " + menuItemText2[k1], 1) + 5;
                    if (l1 > menuWidth) {
                        menuWidth = l1;
                    }
                }

                menuX = super.mouseX - menuWidth / 2;
                menuY = super.mouseY - 7;
                showRightClickMenu = true;
                if (menuX < 0) {
                    menuX = 0;
                }
                if (menuY < 0) {
                    menuY = 0;
                }
                if (menuX + menuWidth > gameWidth - 2) {
                    menuX = gameWidth - 2 - menuWidth;
                }
                if (menuY + menuHeight > gameHeight - 2) {
                    menuY = gameHeight - 2 - menuHeight;
                }
                mouseButtonClick = 0;
            }
        }
    }

    private void drawDialogLogout() {
        surface.drawBox(126, 137, 260, 60, 0);
        surface.drawBoxEdge(126, 137, 260, 60, 0xffffff);
        surface.drawStringCenter("Logging out...", 256, 173, 5, 0xffffff);
    }

    private void drawDialogCombatStyle() {
        byte byte0 = 7;
        byte byte1 = 15;
        int width = 175;
        if (mouseButtonClick != 0) {
            for (int i = 0; i < 5; i++) {
                if (i <= 0 || super.mouseX <= byte0 || super.mouseX >= byte0 + width || super.mouseY <= byte1 + i * 20 || super.mouseY >= byte1 + i * 20 + 20) {
                    continue;
                }
                combatStyle = i - 1;
                mouseButtonClick = 0;
                player.setCombatStyle(combatStyle);
                break;
            }

        }
        for (int j = 0; j < 5; j++) {
            if (j == combatStyle + 1) {
                surface.drawBoxAlpha(byte0, byte1 + j * 20, width, 20, Surface.rgb2long(255, 0, 0), 128);
            } else {
                surface.drawBoxAlpha(byte0, byte1 + j * 20, width, 20, Surface.rgb2long(190, 190, 190), 128);
            }
            surface.drawLineHoriz(byte0, byte1 + j * 20, width, 0);
            surface.drawLineHoriz(byte0, byte1 + j * 20 + 20, width, 0);
        }

        surface.drawStringCenter("Select combat style", byte0 + width / 2, byte1 + 16, 3, 0xffffff);
        surface.drawStringCenter("Controlled (+1 of each)", byte0 + width / 2, byte1 + 36, 3, 0);
        surface.drawStringCenter("Aggressive (+3 strength)", byte0 + width / 2, byte1 + 56, 3, 0);
        surface.drawStringCenter("Accurate   (+3 attack)", byte0 + width / 2, byte1 + 76, 3, 0);
        surface.drawStringCenter("Defensive  (+3 defense)", byte0 + width / 2, byte1 + 96, 3, 0);
    }

    private void menuItemClick(int i) {
        int mx = menuItemX[i];
        int my = menuItemY[i];
        int midx = menuSourceType[i];
        int msrcidx = menuSourceIndex[i];
        int mtargetindex = menuTargetIndex[i];
        int mitemid = menuItemID[i];
        // CAST ON GROUND ITEM
        if (mitemid == 200) {
            walkToGroundItem(localRegionX, localRegionY, mx, my, true);
            ActionManager.get(CastHandler.class).handleCastOnGroundItem(mx + regionX, my + regionY, midx, msrcidx);
            selectedSpell = -1;
        }
        // INVENTORY USE WITH GROUND ITEM
        if (mitemid == 210) {
            boolean canReach = walkToGroundItem(localRegionX, localRegionY, mx, my, true);
            if(canReach) {
                ActionManager.get(InventoryUseOnGroundItemHandler.class).handleInventoryUseOnGroundItem(mx + regionX, my + regionY, msrcidx, midx);
            } else {
                player.message("You cannot reach this item");
            }
            selectedItemInventoryIndex = -1;
        }
        // PICKUP GROUND ITEM
        if (mitemid == 220) {
            boolean canReach = walkToGroundItem(localRegionX, localRegionY, mx, my, true);
            if(canReach) {
                ActionManager.get(PickupHandler.class).handlePickup(mx + regionX, my + regionY, midx);
            } else {
                player.message("You cannot reach this item");
            }
        }
        // EXAMINE GROUND ITEM
        if (mitemid == 3200) {
            showMessage(EntityManager.getItem(midx).getDescription(), 3);
        }
        // CAST ON WALL OBJECT
        if (mitemid == 300) {
            walkToWallObject(mx, my, midx);
            /*super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_CAST_WALLOBJECT));
            super.clientStream.putShort(mx + regionX);
            super.clientStream.putShort(my + regionY);
            super.clientStream.putByte(midx);
            super.clientStream.putShort(msrcidx);
            super.clientStream.sendPacket();*/
            selectedSpell = -1;
        }
        // INVENTORY USE WITH WALL OBJECT
        if (mitemid == 310) {
            boolean canReach = walkToWallObject(mx, my, midx);
            if(canReach) {
                ActionManager.get(InventoryUseOnDoorHandler.class).handleInventoryUseOnDoor(msrcidx, mx + regionX, my + regionY);
            } else {
                player.message("You cannot reach this door");
            }
            selectedItemInventoryIndex = -1;
        }
        // WALL OBJECT COMMAND 1
        if (mitemid == 320) {
            boolean canReach = walkToWallObject(mx, my, midx);
            if(canReach) {
                ActionManager.get(DoorActionHandler.class).handleDoor(mx + regionX, my + regionY, true);
            } else {
                player.message("You cannot reach this door");
            }
        }
        // WALL OBJECT COMMAND 2
        if (mitemid == 2300) { // second door command
            walkToWallObject(mx, my, midx);
            ActionManager.get(DoorActionHandler.class).handleDoor(mx + regionX, my + regionY, false);
        }
        // EXAMINE WALL OBJECT
        if (mitemid == 3300) {
            showMessage(EntityManager.getDoor(midx).getDescription(), 3);
        }
        // CAST ON OBJECT
        if (mitemid == 400) {
            walkToObject(mx, my, midx, msrcidx);
            /*super.clientStream.newPacket(Opcode.getClient(Version.CLIENT, Command.Client.CL_CAST_OBJECT));
            super.clientStream.putShort(mx + regionX);
            super.clientStream.putShort(my + regionY);
            super.clientStream.putShort(mtargetindex);
            super.clientStream.sendPacket();*/
            selectedSpell = -1;
        }
        // INVENTORY USE WITH OBJECT
        if (mitemid == 410) {
            boolean canReach = walkToObject(mx, my, midx, msrcidx);
            if(canReach) {
                ActionManager.get(InventoryUseOnObjectHandler.class).handleInventoryUseOnObject(mtargetindex, mx + regionX, my + regionY);
            } else {
                player.message("You cannot reach this object");
            }
            selectedItemInventoryIndex = -1;
        }
        // OBJECT COMMAND 1
        if (mitemid == 420) {
            boolean canReach = walkToObject(mx, my, midx, msrcidx);
            if(canReach) {
                ActionManager.get(ObjectActionHandler.class).handleObjectAction(mx + regionX, my + regionY, true);
            } else {
                player.message("You cannot reach this object");
            }
        }
        // OBJECT COMMAND 2
        if (mitemid == 2400) {
            boolean canReach = walkToObject(mx, my, midx, msrcidx);
            if(canReach) {
                ActionManager.get(ObjectActionHandler.class).handleObjectAction(mx + regionX, my + regionY, false);
            } else {
                player.message("You cannot reach this object");
            }
        }
        // EXAMINE OBJECT
        if (mitemid == 3400) {
            showMessage(EntityManager.getGameObjectDef(midx).getDescription(), 3);
        }
        // CAST ON INVENTORY ITEM
        if (mitemid == 600) {
            ActionManager.get(CastHandler.class).handleCastOnInventoryItem(midx, msrcidx);
            selectedSpell = -1;
        }
        // INVENTORY USE ON INVENTORY ITEM
        if (mitemid == 610) {
            ActionManager.get(InventoryUseOnItemHandler.class).handleInventoryUseOnItem(midx, msrcidx);
            selectedItemInventoryIndex = -1;
        }
        // UNEQUIP INVENTORY ITEM
        if (mitemid == 620) {
            ActionManager.get(WieldHandler.class).handleUnwield(midx);
        }
        // EQUIP INVENTORY ITEM
        if (mitemid == 630) {
            ActionManager.get(WieldHandler.class).handleWield(midx);
        }
        // INVENTORY ITEM ACTION
        if (mitemid == 640) {
            ActionManager.get(InventoryActionHandler.class).handleInventoryAction(midx);
        }
        // SELECT INVENTORY ITEM
        if (mitemid == 650) {
            selectedItemInventoryIndex = midx;
            showUiTab = 0;
            selectedItemName = EntityManager.getItem(inventoryItemId[selectedItemInventoryIndex]).getName();
        }
        // DROP INVENTORY ITEM
        if (mitemid == 660) {
            ActionManager.get(DropHandler.class).handleDrop(midx);
            selectedItemInventoryIndex = -1;
            showUiTab = 0;
            showMessage("Dropping " + EntityManager.getItem(inventoryItemId[midx]).getName(), 4);
        }
        // EXAMINE INVENTORY ITEM
        if (mitemid == 3600) {
            showMessage(EntityManager.getItem(midx).getDescription(), 3);
        }
        // CAST ON NPC
        if (mitemid == 700) {
            int l1 = (mx - 64) / magicLoc;
            int l3 = (my - 64) / magicLoc;
            walkToActionSource(localRegionX, localRegionY, l1, l3, true);
            ActionManager.get(CastHandler.class).handleCastOnNpc(midx, msrcidx);
            selectedSpell = -1;
        }
        // INVENTORY USE ON NPC
        if (mitemid == 710) {
            int i2 = (mx - 64) / magicLoc;
            int i4 = (my - 64) / magicLoc;
            boolean canReach = walkToActionSource(localRegionX, localRegionY, i2, i4, true);
            if(canReach) {
                ActionManager.get(InventoryUseOnNPCHandler.class).handleInventoryUseOnNPC(midx, msrcidx);
            } else {
                player.message("You cannot reach this npc");
            }
            selectedItemInventoryIndex = -1;
        }
        // TALK TO NPC
        if (mitemid == 720) {
            int j2 = (mx - 64) / magicLoc;
            int j4 = (my - 64) / magicLoc;
            boolean canReach = walkToActionSource(localRegionX, localRegionY, j2, j4, true);
            if(canReach) {
                ActionManager.get(NPCHandler.class).handleTalk(midx);
            } else {
                player.message("You cannot reach this npc");
            }
        }
        // NPC COMMAND
        if (mitemid == 725) {
            int k2 = (mx - 64) / magicLoc;
            int k4 = (my - 64) / magicLoc;
            boolean canReach = walkToActionSource(localRegionX, localRegionY, k2, k4, true);
            if(canReach) {
                ActionManager.get(NPCHandler.class).handleCommand(midx);
            } else {
                player.message("You cannot reach this npc");
            }
        }
        // ATTACK NPC
        if (mitemid == 715 || mitemid == 2715) {
            int l2 = (mx - 64) / magicLoc;
            int l4 = (my - 64) / magicLoc;
            boolean canReach = walkToActionSource(localRegionX, localRegionY, l2, l4, true);
            if(canReach) {
                ActionManager.get(NPCHandler.class).handleAttack(midx);
            } else {
                player.message("You cannot reach this npc");
            }
        }
        // EXAMINE NPC
        if (mitemid == 3700) {
            showMessage(EntityManager.getNPC(midx).getDescription(), 3);
        }
        // CAST ON GROUND
        if (mitemid == 900) {
            walkToActionSource(localRegionX, localRegionY, mx, my, true);
            ActionManager.get(CastHandler.class).handleCastOnLand(midx);
            selectedSpell = -1;
        }
        // WALK TO TILE
        if (mitemid == 920) {
            walkToActionSource(localRegionX, localRegionY, mx, my, false);
            if (mouseClickXStep == -24) {
                mouseClickXStep = 24;
            }
        }
        // CAST ON SELF
        if (mitemid == 1000) {
            ActionManager.get(CastHandler.class).handleCastOnSelf(midx);
            selectedSpell = -1;
        }
        // RESET SELECTED
        if (mitemid == 4000) {
            selectedItemInventoryIndex = -1;
            selectedSpell = -1;
        }
    }

    public void showLoginScreenStatus(String s1, String s2) {
        if (loginScreen == 1) {
            panelRegisterUser.updateText(controlRegisterStatus, s1 + " " + s2);
        }
        if (loginScreen == 2) {
            if (s2 != null && s2.length() > 0) {
                panelLoginExistingUser.updateText(controlLoginStatus1, s1);
                panelLoginExistingUser.updateText(controlLoginStatus2, s2);
            } else {
                panelLoginExistingUser.updateText(controlLoginStatus2, s1);
            }
        }
        drawLoginScreens();
        resetTimings();
    }

    private boolean isValidCameraAngle(int i) {
        int j = localPlayer.currentX / 128;
        int k = localPlayer.currentY / 128;
        for (int l = 2; l >= 1; l--) {
            if (i == 1 && ((world.walkableValue[j][k - l] & 128) == 128 || (world.walkableValue[j - l][k] & 128) == 128 || (world.walkableValue[j - l][k - l] & 128) == 128))// 0x80
            {
                return false;
            }
            if (i == 3 && ((world.walkableValue[j][k + l] & 128) == 128 || (world.walkableValue[j - l][k] & 128) == 128 || (world.walkableValue[j - l][k + l] & 128) == 128))// 0x80
            {
                return false;
            }
            if (i == 5 && ((world.walkableValue[j][k + l] & 128) == 128 || (world.walkableValue[j + l][k] & 128) == 128 || (world.walkableValue[j + l][k + l] & 128) == 128))// 0x80
            {
                return false;
            }
            if (i == 7 && ((world.walkableValue[j][k - l] & 128) == 128 || (world.walkableValue[j + l][k] & 128) == 128 || (world.walkableValue[j + l][k - l] & 128) == 128))// 0x80
            {
                return false;
            }
            if (i == 0 && (world.walkableValue[j][k - l] & 128) == 128)// 0x80
            {
                return false;
            }
            if (i == 2 && (world.walkableValue[j - l][k] & 128) == 128)// 0x80
            {
                return false;
            }
            if (i == 4 && (world.walkableValue[j][k + l] & 128) == 128)// 0x80
            {
                return false;
            }
            if (i == 6 && (world.walkableValue[j + l][k] & 128) == 128)// 0x80
            {
                return false;
            }
        }
        return true;
    }

    private void resetLoginScreenVariables() {
        loggedIn = 0;
        loginScreen = 0;
        loginUser = "";
        playerCount = 0;
        npcCount = 0;
        newQuestNames.clear();
        questStage.clear();
        playerQuestPoints = 0;
    }

    private void drawUiTabPlayerInfo(boolean nomenus) {
        int uiX = surface.width2 - 199;
        int uiY = 36;
        surface.drawSprite(uiX - 49, 3, spriteMedia + 3);
        int uiWidth = 196;
        int uiHeight = 275;
        int l;
        int k = l = Surface.rgb2long(160, 160, 160);
        if (uiTabPlayerInfoSubTab == 0) {
            k = Surface.rgb2long(220, 220, 220);
        } else {
            l = Surface.rgb2long(220, 220, 220);
        }
        surface.drawBoxAlpha(uiX, uiY, uiWidth / 2, 24, k, 128);
        surface.drawBoxAlpha(uiX + uiWidth / 2, uiY, uiWidth / 2, 24, l, 128);
        surface.drawBoxAlpha(uiX, uiY + 24, uiWidth, uiHeight - 24, Surface.rgb2long(220, 220, 220), 128);
        surface.drawLineHoriz(uiX, uiY + 24, uiWidth, 0);
        surface.drawLineVert(uiX + uiWidth / 2, uiY, 24, 0);
        surface.drawStringCenter("Stats", uiX + uiWidth / 4, uiY + 16, 4, 0);
        surface.drawStringCenter("Quests", uiX + uiWidth / 4 + uiWidth / 2, uiY + 16, 4, 0);
        if (uiTabPlayerInfoSubTab == 0) {
            int i1 = 72;
            int k1 = -1;
            surface.drawString("Skills", uiX + 5, i1, 3, 0xffff00);
            i1 += 13;
            for (int l1 = 0; l1 < 9; l1++) {
                int i2 = 0xffffff;
                if (super.mouseX > uiX + 3 && super.mouseY >= i1 - 11 && super.mouseY < i1 + 2 && super.mouseX < uiX + 90) {
                    i2 = 0xff0000;
                    k1 = l1;
                    if (this.mouseButtonClick == 1 && this.uiTabPlayerInfoSubTab == 0) {
                        //setSkillGuideChosen(this.skillNameLong[k1]);
                        //skillGuideInterface.setVisible(true);
                        //this.showUiTab = 0;
                    }
                }
                surface.drawString(skillNameShort[l1] + ":@yel@" + playerStatCurrent[l1] + "/" + playerStatBase[l1], uiX + 5, i1, 1, i2);
                i2 = 0xffffff;
                if (super.mouseX >= uiX + 90 && super.mouseY >= i1 - 13 - 11 && super.mouseY < (i1 - 13) + 2 && super.mouseX < uiX + 196) {
                    i2 = 0xff0000;
                    k1 = l1 + 9;
                    if (this.mouseButtonClick == 1 && this.uiTabPlayerInfoSubTab == 0) {
                        //setSkillGuideChosen(this.skillNameLong[k1]);
                        //skillGuideInterface.setVisible(true);
                        //this.showUiTab = 0;
                    }
                }
                surface.drawString(skillNameShort[l1 + 9] + ":@yel@" + playerStatCurrent[l1 + 9] + "/" + playerStatBase[l1 + 9], (uiX + uiWidth / 2) - 5, i1 - 13, 1, i2);
                i1 += 13;
            }

            surface.drawString("Quest Points:@yel@" + playerQuestPoints, (uiX + uiWidth / 2) - 5, i1 - 13, 1, 0xffffff);
            i1 += 12;
            surface.drawString("Fatigue: @yel@" + statFatigue + "%", uiX + 5, i1 - 13, 1, 0xffffff);
            i1 += 8;
            surface.drawString("Equipment Status", uiX + 5, i1, 3, 0xffff00);
            i1 += 12;
            for (int j2 = 0; j2 < 3; j2++) {
                surface.drawString(equipmentStatNames[j2] + ":@yel@" + playerStatEquipment[j2], uiX + 5, i1, 1, 0xffffff);
                if (j2 < 2) {
                    surface.drawString(equipmentStatNames[j2 + 3] + ":@yel@" + playerStatEquipment[j2 + 3], uiX + uiWidth / 2 + 25, i1, 1, 0xffffff);
                }
                i1 += 13;
            }

            i1 += 6;
            surface.drawLineHoriz(uiX, i1 - 15, uiWidth, 0);
            if (k1 != -1) {
                surface.drawString(skillNameLong[k1] + " skill", uiX + 5, i1, 1, 0xffff00);
                i1 += 12;
                int k2 = experienceArray[0];
                for (int i3 = 0; i3 < 98; i3++) {
                    if (playerExperience[k1] >= experienceArray[i3]) {
                        k2 = experienceArray[i3 + 1];
                    }
                }

                surface.drawString("Total xp: " + playerExperience[k1], uiX + 5, i1, 1, 0xffffff);
                i1 += 12;
                surface.drawString("Next level at: " + k2, uiX + 5, i1, 1, 0xffffff);
            } else {
                surface.drawString("Overall levels", uiX + 5, i1, 1, 0xffff00);
                i1 += 12;
                int l2 = 0;
                for (int j3 = 0; j3 < playerStatCount; j3++) {
                    l2 += playerStatBase[j3];
                }

                surface.drawString("Skill total: " + l2, uiX + 5, i1, 1, 0xffffff);
                i1 += 12;
                surface.drawString("Combat level: " + localPlayer.level, uiX + 5, i1, 1, 0xffffff);
                i1 += 12;
            }
        }
        if (uiTabPlayerInfoSubTab == 1) {
            panelQuestList.clearList(controlListQuest);
            panelQuestList.addListEntry(controlListQuest, 0, "@whi@Quest-list (green=completed)");
            int index = 0;
            for (int questIdx : newQuestNames.keySet()) {
                panelQuestList.addListEntry(controlListQuest, index++, (questStage.get(questIdx) == 0 ? "@red@" : questStage.get(questIdx) > 0 ? "@yel@" : "@gre@") + newQuestNames.get(questIdx));
            }
            panelQuestList.drawPanel();
        }
        if (!nomenus) {
            return;
        }
        int mouseX = super.mouseX - (surface.width2 - 199);
        int mouseY = super.mouseY - 36;
        if (mouseX >= 0 && mouseY >= 0 && mouseX < uiWidth && mouseY < uiHeight) {
            if (uiTabPlayerInfoSubTab == 1) {
                panelQuestList.handleMouse(mouseX + (surface.width2 - 199), mouseY + 36, super.lastMouseButtonDown, super.mouseButtonDown);
            }
            if (mouseY <= 24 && mouseButtonClick == 1) {
                if (mouseX < 98) {
                    uiTabPlayerInfoSubTab = 0;
                    return;
                }
                if (mouseX > 98) {
                    uiTabPlayerInfoSubTab = 1;
                }
            }
        }
    }

    private void createRightClickMenu() {
        int i = 2203 - (localRegionY + planeHeight + regionY);
        if (localRegionX + planeWidth + regionX >= 2640) {
            i = -50;
        }
        int j = -1;
        for (int k = 0; k < objectCount; k++) {
            objectAlreadyInMenu[k] = false;
        }

        for (int l = 0; l < wallObjectCount; l++) {
            wallObjectAlreadyInMenu[l] = false;
        }

        int i1 = scene.getMousePickedCount();
        Model objs[] = scene.getMousePickedModels();
        int plyrs[] = scene.getMousePickedFaces();
        for (int menuidx = 0; menuidx < i1; menuidx++) {
            if (menuItemsCount > 200) {
                break;
            }
            int pid = plyrs[menuidx];
            Model gameModel = objs[menuidx];
            if (gameModel.faceTag[pid] <= 65535 || gameModel.faceTag[pid] >= 200000 && gameModel.faceTag[pid] <= 300000)// 0x30d40    0x493e0
            {
                if (gameModel == scene.view) {
                    int idx = gameModel.faceTag[pid] % 10000;
                    int type = gameModel.faceTag[pid] / 10000;
                    switch (type) {
                        case 1:
                            String s = "";
                            int k3 = 0;
                            if (localPlayer.level > 0 && players[idx].level > 0) {
                                k3 = localPlayer.level - players[idx].level;
                            }
                            if (k3 < 0) {
                                s = "@or1@";
                            }
                            if (k3 < -3) {
                                s = "@or2@";
                            }
                            if (k3 < -6) {
                                s = "@or3@";
                            }
                            if (k3 < -9) {
                                s = "@red@";
                            }
                            if (k3 > 0) {
                                s = "@gr1@";
                            }
                            if (k3 > 3) {
                                s = "@gr2@";
                            }
                            if (k3 > 6) {
                                s = "@gr3@";
                            }
                            if (k3 > 9) {
                                s = "@gre@";
                            }
                            s = " " + s + "(level-" + players[idx].level + ")";
                            if (selectedSpell >= 0) {
                                if (EntityManager.getSpell(selectedSpell).getType() == 1 || EntityManager.getSpell(selectedSpell).getType() == 2) {
                                    menuItemText1[menuItemsCount] = "Cast " + EntityManager.getSpell(selectedSpell).getName() + " on";
                                    menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
                                    menuItemID[menuItemsCount] = 800;
                                    menuItemX[menuItemsCount] = players[idx].currentX;
                                    menuItemY[menuItemsCount] = players[idx].currentY;
                                    menuSourceType[menuItemsCount] = players[idx].serverIndex;
                                    menuSourceIndex[menuItemsCount] = selectedSpell;
                                    menuItemsCount++;
                                }
                            } else if (selectedItemInventoryIndex >= 0) {
                                menuItemText1[menuItemsCount] = "Use " + selectedItemName + " with";
                                menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
                                menuItemID[menuItemsCount] = 810;
                                menuItemX[menuItemsCount] = players[idx].currentX;
                                menuItemY[menuItemsCount] = players[idx].currentY;
                                menuSourceType[menuItemsCount] = players[idx].serverIndex;
                                menuSourceIndex[menuItemsCount] = selectedItemInventoryIndex;
                                menuItemsCount++;
                            } else {
                                if (i > 0 && (players[idx].currentY - 64) / magicLoc + planeHeight + regionY < 2203) {
                                    menuItemText1[menuItemsCount] = "Attack";
                                    menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
                                    if (k3 >= 0 && k3 < 5) {
                                        menuItemID[menuItemsCount] = 805;
                                    } else {
                                        menuItemID[menuItemsCount] = 2805;
                                    }
                                    menuItemX[menuItemsCount] = players[idx].currentX;
                                    menuItemY[menuItemsCount] = players[idx].currentY;
                                    menuSourceType[menuItemsCount] = players[idx].serverIndex;
                                    menuItemsCount++;
                                } else if (Constants.MEMBER_WORLD) {
                                    menuItemText1[menuItemsCount] = "Duel with";
                                    menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
                                    menuItemX[menuItemsCount] = players[idx].currentX;
                                    menuItemY[menuItemsCount] = players[idx].currentY;
                                    menuItemID[menuItemsCount] = 2806;
                                    menuSourceType[menuItemsCount] = players[idx].serverIndex;
                                    menuItemsCount++;
                                }
                                menuItemText1[menuItemsCount] = "Trade with";
                                menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
                                menuItemID[menuItemsCount] = 2810;
                                menuSourceType[menuItemsCount] = players[idx].serverIndex;
                                menuItemsCount++;
                                menuItemText1[menuItemsCount] = "Follow";
                                menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
                                menuItemID[menuItemsCount] = 2820;
                                menuSourceType[menuItemsCount] = players[idx].serverIndex;
                                menuItemsCount++;
                            }
                            break;
                        case 2:
                            if (selectedSpell >= 0) {
                                if (EntityManager.getSpell(selectedSpell).getType() == 3) {
                                    menuItemText1[menuItemsCount] = "Cast " + EntityManager.getSpell(selectedSpell).getName() + " on";
                                    menuItemText2[menuItemsCount] = "@lre@" + EntityManager.getItem(groundItemID[idx]).getName();
                                    menuItemID[menuItemsCount] = 200;
                                    menuItemX[menuItemsCount] = groundItemX[idx];
                                    menuItemY[menuItemsCount] = groundItemY[idx];
                                    menuSourceType[menuItemsCount] = groundItemID[idx];
                                    menuSourceIndex[menuItemsCount] = selectedSpell;
                                    menuItemsCount++;
                                }
                            } else if (selectedItemInventoryIndex >= 0) {
                                menuItemText1[menuItemsCount] = "Use " + selectedItemName + " with";
                                menuItemText2[menuItemsCount] = "@lre@" + EntityManager.getItem(groundItemID[idx]).getName();
                                menuItemID[menuItemsCount] = 210;
                                menuItemX[menuItemsCount] = groundItemX[idx];
                                menuItemY[menuItemsCount] = groundItemY[idx];
                                menuSourceType[menuItemsCount] = groundItemID[idx];
                                menuSourceIndex[menuItemsCount] = selectedItemInventoryIndex;
                                menuItemsCount++;
                            } else {
                                menuItemText1[menuItemsCount] = "Take";
                                menuItemText2[menuItemsCount] = "@lre@" + EntityManager.getItem(groundItemID[idx]).getName();
                                menuItemID[menuItemsCount] = 220;
                                menuItemX[menuItemsCount] = groundItemX[idx];
                                menuItemY[menuItemsCount] = groundItemY[idx];
                                menuSourceType[menuItemsCount] = groundItemID[idx];
                                menuItemsCount++;
                                menuItemText1[menuItemsCount] = "Examine";
                                menuItemText2[menuItemsCount] = "@lre@" + EntityManager.getItem(groundItemID[idx]).getName();
                                menuItemID[menuItemsCount] = 3200;
                                menuSourceType[menuItemsCount] = groundItemID[idx];
                                menuItemsCount++;
                            }
                            break;
                        case 3:
                            String s1 = "";
                            int leveldiff = -1;
                            int id = npcs[idx].npcId;
                            boolean isPlayerAI = EntityManager.getNPC(id).isPlayerAI();
                            boolean inWilderness = i > 0 && (npcs[idx].currentY - 64) / magicLoc + planeHeight + regionY < 2203;
                            if (EntityManager.getNPC(id).isAttackable() || isPlayerAI) {
                                int attack = EntityManager.getNPC(id).getAttack();
                                int defense = EntityManager.getNPC(id).getDefense();
                                int strength = EntityManager.getNPC(id).getStrength();
                                int hitpoints = EntityManager.getNPC(id).getHitpoints();
                                int npclevel = (attack + defense + strength + hitpoints) / 4;
                                int playerlevel = (playerStatBase[0] + playerStatBase[1] + playerStatBase[2] + playerStatBase[3] + 27) / 4;
                                leveldiff = playerlevel - npclevel;
                                s1 = "@yel@";
                                if (leveldiff < 0) {
                                    s1 = "@or1@";
                                }
                                if (leveldiff < -3) {
                                    s1 = "@or2@";
                                }
                                if (leveldiff < -6) {
                                    s1 = "@or3@";
                                }
                                if (leveldiff < -9) {
                                    s1 = "@red@";
                                }
                                if (leveldiff > 0) {
                                    s1 = "@gr1@";
                                }
                                if (leveldiff > 3) {
                                    s1 = "@gr2@";
                                }
                                if (leveldiff > 6) {
                                    s1 = "@gr3@";
                                }
                                if (leveldiff > 9) {
                                    s1 = "@gre@";
                                }
                                s1 = " " + s1 + "(level-" + npclevel + ")";
                            }
                            if (selectedSpell >= 0) {
                                if (EntityManager.getSpell(selectedSpell).getType() == 2) {
                                    menuItemText1[menuItemsCount] = "Cast " + EntityManager.getSpell(selectedSpell).getName() + " on";
                                    menuItemText2[menuItemsCount] = "@yel@" + EntityManager.getNPC(npcs[idx].npcId).getName();
                                    menuItemID[menuItemsCount] = 700;
                                    menuItemX[menuItemsCount] = npcs[idx].currentX;
                                    menuItemY[menuItemsCount] = npcs[idx].currentY;
                                    menuSourceType[menuItemsCount] = npcs[idx].serverIndex;
                                    menuSourceIndex[menuItemsCount] = selectedSpell;
                                    menuItemsCount++;
                                }
                            } else if (selectedItemInventoryIndex >= 0) {
                                menuItemText1[menuItemsCount] = "Use " + selectedItemName + " with";
                                menuItemText2[menuItemsCount] = "@yel@" + EntityManager.getNPC(npcs[idx].npcId).getName();
                                menuItemID[menuItemsCount] = 710;
                                menuItemX[menuItemsCount] = npcs[idx].currentX;
                                menuItemY[menuItemsCount] = npcs[idx].currentY;
                                menuSourceType[menuItemsCount] = npcs[idx].serverIndex;
                                menuSourceIndex[menuItemsCount] = selectedItemInventoryIndex;
                                menuItemsCount++;
                            } else {
                                if (EntityManager.getNPC(id).isAttackable() || (isPlayerAI && inWilderness)) {
                                    menuItemText1[menuItemsCount] = "Attack";
                                    menuItemText2[menuItemsCount] = (isPlayerAI ? "@whi@" : "@yel@") + EntityManager.getNPC(npcs[idx].npcId).getName() + s1;
                                    if (leveldiff >= 0) {
                                        menuItemID[menuItemsCount] = 715;
                                    } else {
                                        menuItemID[menuItemsCount] = 2715;
                                    }
                                    menuItemX[menuItemsCount] = npcs[idx].currentX;
                                    menuItemY[menuItemsCount] = npcs[idx].currentY;
                                    menuSourceType[menuItemsCount] = npcs[idx].serverIndex;
                                    menuItemsCount++;
                                }
                                menuItemText1[menuItemsCount] = isPlayerAI ? "Trade with" : "Talk-to";
                                menuItemText2[menuItemsCount] = (isPlayerAI ? "@whi@" : "@yel@") + EntityManager.getNPC(npcs[idx].npcId).getName() + (isPlayerAI ? s1 : "");
                                menuItemID[menuItemsCount] = isPlayerAI ? 3720 : 720;
                                menuItemX[menuItemsCount] = npcs[idx].currentX;
                                menuItemY[menuItemsCount] = npcs[idx].currentY;
                                menuSourceType[menuItemsCount] = npcs[idx].serverIndex;
                                menuItemsCount++;
                                if (!EntityManager.getNPC(id).getCommand().equals("")) {
                                    menuItemText1[menuItemsCount] = EntityManager.getNPC(id).getCommand();
                                    menuItemText2[menuItemsCount] = "@yel@" + EntityManager.getNPC(npcs[idx].npcId).getName();
                                    menuItemID[menuItemsCount] = 725;
                                    menuItemX[menuItemsCount] = npcs[idx].currentX;
                                    menuItemY[menuItemsCount] = npcs[idx].currentY;
                                    menuSourceType[menuItemsCount] = npcs[idx].serverIndex;
                                    menuItemsCount++;
                                }
                                menuItemText1[menuItemsCount] = isPlayerAI ? "Follow" : "Examine";
                                menuItemText2[menuItemsCount] = (isPlayerAI ? "@whi@" : "@yel@") + EntityManager.getNPC(npcs[idx].npcId).getName() + (isPlayerAI ? s1 : "");
                                menuItemID[menuItemsCount] = isPlayerAI ? 3800 : 3700;
                                menuSourceType[menuItemsCount] = npcs[idx].npcId;
                                menuItemsCount++;
                            }
                            break;
                        default:
                            break;
                    }
                } else if (gameModel.key >= 10000) {
                    int idx = gameModel.key - 10000;
                    int id = wallObjectId[idx];
                    if (!wallObjectAlreadyInMenu[idx]) {
                        if (selectedSpell >= 0) {
                            if (EntityManager.getSpell(selectedSpell).getType() == 4) {
                                menuItemText1[menuItemsCount] = "Cast " + EntityManager.getSpell(selectedSpell).getName() + " on";
                                menuItemText2[menuItemsCount] = "@cya@" + EntityManager.getDoor(id).getName();
                                menuItemID[menuItemsCount] = 300;
                                menuItemX[menuItemsCount] = wallObjectX[idx];
                                menuItemY[menuItemsCount] = wallObjectY[idx];
                                menuSourceType[menuItemsCount] = wallObjectDirection[idx];
                                menuSourceIndex[menuItemsCount] = selectedSpell;
                                menuItemsCount++;
                            }
                        } else if (selectedItemInventoryIndex >= 0) {
                            menuItemText1[menuItemsCount] = "Use " + selectedItemName + " with";
                            menuItemText2[menuItemsCount] = "@cya@" + EntityManager.getDoor(id).getName();
                            menuItemID[menuItemsCount] = 310;
                            menuItemX[menuItemsCount] = wallObjectX[idx];
                            menuItemY[menuItemsCount] = wallObjectY[idx];
                            menuSourceType[menuItemsCount] = wallObjectDirection[idx];
                            menuSourceIndex[menuItemsCount] = selectedItemInventoryIndex;
                            menuItemsCount++;
                        } else {
                            if (!EntityManager.getDoor(id).getCommandFirst().equalsIgnoreCase("WalkTo")) {
                                menuItemText1[menuItemsCount] = EntityManager.getDoor(id).getCommandFirst();
                                menuItemText2[menuItemsCount] = "@cya@" + EntityManager.getDoor(id).getName();
                                menuItemID[menuItemsCount] = 320;
                                menuItemX[menuItemsCount] = wallObjectX[idx];
                                menuItemY[menuItemsCount] = wallObjectY[idx];
                                menuSourceType[menuItemsCount] = wallObjectDirection[idx];
                                menuItemsCount++;
                            }
                            if (!EntityManager.getDoor(id).getCommandSecond().equalsIgnoreCase("Examine")) {
                                menuItemText1[menuItemsCount] = EntityManager.getDoor(id).getCommandSecond();
                                menuItemText2[menuItemsCount] = "@cya@" + EntityManager.getDoor(id).getName();
                                menuItemID[menuItemsCount] = 2300;
                                menuItemX[menuItemsCount] = wallObjectX[idx];
                                menuItemY[menuItemsCount] = wallObjectY[idx];
                                menuSourceType[menuItemsCount] = wallObjectDirection[idx];
                                menuItemsCount++;
                            }
                            menuItemText1[menuItemsCount] = "Examine";
                            menuItemText2[menuItemsCount] = "@cya@" + EntityManager.getDoor(id).getName();
                            menuItemID[menuItemsCount] = 3300;
                            menuSourceType[menuItemsCount] = id;
                            menuItemsCount++;
                        }
                        wallObjectAlreadyInMenu[idx] = true;
                    }
                } else if (gameModel.key >= 0) {
                    int idx = gameModel.key;
                    int id = objectID[idx];
                    if (!objectAlreadyInMenu[idx]) {
                        if (selectedSpell >= 0) {
                            if (EntityManager.getSpell(selectedSpell).getType() == 5) {
                                menuItemText1[menuItemsCount] = "Cast " + EntityManager.getSpell(selectedSpell).getName() + " on";
                                menuItemText2[menuItemsCount] = "@cya@" + EntityManager.getGameObjectDef(id).getName();
                                menuItemID[menuItemsCount] = 400;
                                menuItemX[menuItemsCount] = objectX[idx];
                                menuItemY[menuItemsCount] = objectY[idx];
                                menuSourceType[menuItemsCount] = objectDirection[idx];
                                menuSourceIndex[menuItemsCount] = objectID[idx];
                                menuTargetIndex[menuItemsCount] = selectedSpell;
                                menuItemsCount++;
                            }
                        } else if (selectedItemInventoryIndex >= 0) {
                            menuItemText1[menuItemsCount] = "Use " + selectedItemName + " with";
                            menuItemText2[menuItemsCount] = "@cya@" + EntityManager.getGameObjectDef(id).getName();
                            menuItemID[menuItemsCount] = 410;
                            menuItemX[menuItemsCount] = objectX[idx];
                            menuItemY[menuItemsCount] = objectY[idx];
                            menuSourceType[menuItemsCount] = objectDirection[idx];
                            menuSourceIndex[menuItemsCount] = objectID[idx];
                            menuTargetIndex[menuItemsCount] = selectedItemInventoryIndex;
                            menuItemsCount++;
                        } else {
                            if (!EntityManager.getGameObjectDef(id).getFirstCommand().equalsIgnoreCase("WalkTo")) {
                                menuItemText1[menuItemsCount] = EntityManager.getGameObjectDef(id).getFirstCommand();
                                menuItemText2[menuItemsCount] = "@cya@" + EntityManager.getGameObjectDef(id).getName();
                                menuItemID[menuItemsCount] = 420;
                                menuItemX[menuItemsCount] = objectX[idx];
                                menuItemY[menuItemsCount] = objectY[idx];
                                menuSourceType[menuItemsCount] = objectDirection[idx];
                                menuSourceIndex[menuItemsCount] = objectID[idx];
                                menuItemsCount++;
                            }
                            if (!EntityManager.getGameObjectDef(id).getSecondCommand().equalsIgnoreCase("Examine")) {
                                menuItemText1[menuItemsCount] = EntityManager.getGameObjectDef(id).getSecondCommand();
                                menuItemText2[menuItemsCount] = "@cya@" + EntityManager.getGameObjectDef(id).getName();
                                menuItemID[menuItemsCount] = 2400;
                                menuItemX[menuItemsCount] = objectX[idx];
                                menuItemY[menuItemsCount] = objectY[idx];
                                menuSourceType[menuItemsCount] = objectDirection[idx];
                                menuSourceIndex[menuItemsCount] = objectID[idx];
                                menuItemsCount++;
                            }
                            menuItemText1[menuItemsCount] = "Examine";
                            menuItemText2[menuItemsCount] = "@cya@" + EntityManager.getGameObjectDef(id).getName();
                            menuItemID[menuItemsCount] = 3400;
                            menuSourceType[menuItemsCount] = id;
                            menuItemsCount++;
                        }
                        objectAlreadyInMenu[idx] = true;
                    }
                } else {
                    if (pid >= 0) {
                        pid = gameModel.faceTag[pid] - 200000;
                    }
                    if (pid >= 0) {
                        j = pid;
                    }
                }
            }
        }

        if (selectedSpell >= 0 && EntityManager.getSpell(selectedSpell).getType() <= 1) {
            menuItemText1[menuItemsCount] = "Cast " + EntityManager.getSpell(selectedSpell).getName() + " on self";
            menuItemText2[menuItemsCount] = "";
            menuItemID[menuItemsCount] = 1000;
            menuSourceType[menuItemsCount] = selectedSpell;
            menuItemsCount++;
        }
        if (j != -1) {
            if (selectedSpell >= 0) {
                if (EntityManager.getSpell(selectedSpell).getType() == 6) {
                    menuItemText1[menuItemsCount] = "Cast " + EntityManager.getSpell(selectedSpell).getName() + " on ground";
                    menuItemText2[menuItemsCount] = "";
                    menuItemID[menuItemsCount] = 900;
                    menuItemX[menuItemsCount] = world.selectedX[j];
                    menuItemY[menuItemsCount] = world.selectedY[j];
                    menuSourceType[menuItemsCount] = selectedSpell;
                    menuItemsCount++;
                }
            } else if (selectedItemInventoryIndex < 0) {
                menuItemText1[menuItemsCount] = "Walk here";
                menuItemText2[menuItemsCount] = "";
                menuItemID[menuItemsCount] = 920;
                menuItemX[menuItemsCount] = world.selectedX[j];
                menuItemY[menuItemsCount] = world.selectedY[j];
                menuItemsCount++;
            }
        }
    }

    @Override
    protected void handleInputs() {
        try {
            loginTimer++;
            if (loggedIn == 0) {
                handleLoginScreenInput();
            }
            if (loggedIn == 1) {
                handleGameInput();
            }
            super.lastMouseButtonDown = 0;
            cameraRotationTime++;
            if (cameraRotationTime > 500) {
                cameraRotationTime = 0;
                int i = (int) (Math.random() * 4D);
                if ((i & 1) == 1) {
                    cameraRotationX += cameraRotationXIncrement;
                }
                if ((i & 2) == 2) {
                    cameraRotationY += cameraRotationYIncrement;
                }
            }
            if (cameraRotationX < -50) {
                cameraRotationXIncrement = 2;
            }
            if (cameraRotationX > 50) {
                cameraRotationXIncrement = -2;
            }
            if (cameraRotationY < -50) {
                cameraRotationYIncrement = 2;
            }
            if (cameraRotationY > 50) {
                cameraRotationYIncrement = -2;
            }
            if (messageTabFlashAll > 0) {
                messageTabFlashAll--;
            }
            if (messageTabFlashHistory > 0) {
                messageTabFlashHistory--;
            }
            if (messageTabFlashQuest > 0) {
                messageTabFlashQuest--;
            }
            if (messageTabFlashPrivate > 0) {
                messageTabFlashPrivate--;
            }
        } catch (OutOfMemoryError e) {
            disposeAndCollect();
            errorLoadingMemory = true;
        }
    }

    private void handleLoginScreenInput() {
        switch(loginScreen) {
            case 0:
                panelLoginWelcome.handleMouse(super.mouseX, super.mouseY, super.lastMouseButtonDown, super.mouseButtonDown);
                if (panelLoginWelcome.isClicked(controlWelcomeNewUser)) {
                    loginScreen = 1;
                    panelRegisterUser.updateText(controlRegisterStatus, "@dre@Read this before creating an account");
                    panelRegisterUser.updateText(controlRegisterUser, "");
                    panelLoginExistingUser.setFocus(controlRegisterUser);
                }   if (panelLoginWelcome.isClicked(controlWelcomeExistingUser)) {
                    loginScreen = 2;
                    panelLoginExistingUser.updateText(controlLoginStatus1, "");
                    panelLoginExistingUser.updateText(controlLoginStatus2, "Please enter your username");
                    panelLoginExistingUser.updateText(controlLoginUser, "");
                    panelLoginExistingUser.setFocus(controlLoginUser);
                }
                break;
            case 1:
                panelRegisterUser.handleMouse(super.mouseX, super.mouseY, super.lastMouseButtonDown, super.mouseButtonDown);
                if (panelRegisterUser.isClicked(controlRegisterCancel)) {
                    loginScreen = 0;
                }
                if (panelRegisterUser.isClicked(controlRegisterSubmit)) {
                    String user = panelRegisterUser.getText(controlRegisterUser);
                    user = user.toLowerCase().trim();
                    if (user.length() == 0) {
                        panelRegisterUser.updateText(controlRegisterStatus, "@red@Please pick a username to continue");
                        return;
                    }
                    if (user.length() < 3) {
                        panelRegisterUser.updateText(controlRegisterStatus, "@red@Username must be at least 3 characters long");
                        return;
                    }
                    if (ActionManager.get(RegisterHandler.class).handleRegister(user)) {
                        panelRegisterUser.updateText(controlRegisterStatus, "@gre@Account \"" + Util.title(user) + "\" successfully created!");
                    } else {
                        panelRegisterUser.updateText(controlRegisterStatus, "@or2@The username \"" + Util.title(user) + "\" is already in use");
                    }
                }
                break;
            case 2:
                panelLoginExistingUser.handleMouse(super.mouseX, super.mouseY, super.lastMouseButtonDown, super.mouseButtonDown);
                if (panelLoginExistingUser.isClicked(controlLoginCancel)) {
                    loginScreen = 0;
                }
                if (panelLoginExistingUser.isClicked(controlLoginUser) || panelLoginExistingUser.isClicked(controlLoginOk)) {
                    loginUser = panelLoginExistingUser.getText(controlLoginUser);
                    login(loginUser);
                }
                break;
            default:
                break;
        }
    }

    private void login(String u) {
        username = u;
        u = Util.formatAuthString(u, 20);
        if (u.trim().length() == 0) {
            showLoginScreenStatus("You did not enter your username", "Please try again");
            return;
        }
        showLoginScreenStatus("Please wait...", "Reading character data");
        int loginResponse = ActionManager.get(LoginHandler.class).handleLogin(username);
        switch (loginResponse) {
            case 0:
            case 50:
                resetGame();
                application.setResizable(true);
                // removed temp: application.setSize(tempDimension == null ? player.preferredDimension : tempDimension);
                application.setLocationRelativeTo(null);
                break;
            case 1:
                showLoginScreenStatus("Account does not exist", "To create an account, select 'new user'");
                break;
            default:
                showLoginScreenStatus("Unable to log in", "Contact the developer with the stacktrace");
        }
    }

    public Model createWallModel(int x, int y, int direction, int id, int count) {
        int x1 = x;
        int y1 = y;
        int x2 = x;
        int y2 = y;
        int j2 = EntityManager.getDoor(id).getTextureFront();
        int k2 = EntityManager.getDoor(id).getTextureBack();
        int l2 = EntityManager.getDoor(id).getHeight();
        Model gameModel = new Model(4, 1);
        // wall = --
        if (direction == 0) {
            x2 = x + 1;
        }
        // wall = |
        if (direction == 1) {
            y2 = y + 1;
        }
        // wall = /
        if (direction == 2) {
            x1 = x + 1;
            y2 = y + 1;
        }
        // wall = \
        if (direction == 3) {
            x2 = x + 1;
            y2 = y + 1;
        }
        x1 *= magicLoc;
        y1 *= magicLoc;
        x2 *= magicLoc;
        y2 *= magicLoc;
        int i3 = gameModel.vertexAt(x1, -world.getAveragedElevation(x1, y1), y1);
        int j3 = gameModel.vertexAt(x1, -world.getAveragedElevation(x1, y1) - l2, y1);
        int k3 = gameModel.vertexAt(x2, -world.getAveragedElevation(x2, y2) - l2, y2);
        int l3 = gameModel.vertexAt(x2, -world.getAveragedElevation(x2, y2), y2);
        int ai[] = {
            i3, j3, k3, l3
        };
        gameModel.createFace(4, ai, j2, k2);
        gameModel.setLight(false, 60, 24, -50, -10, -50);
        if (x >= 0 && y >= 0 && x < 96 && y < 96) {
            scene.addModel(gameModel);
        }
        gameModel.key = count + 10000;
        return gameModel;
    }

}
