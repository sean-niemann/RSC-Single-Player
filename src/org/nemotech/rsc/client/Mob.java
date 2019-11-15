package org.nemotech.rsc.client;

public class Mob {
    
    public Mob() {
        waypointsX = new int[10];
        waypointsY = new int[10];
        equippedItem = new int[12];
        level = -1;
    }
    
    public String name;
    public int serverIndex;
    public int serverId;
    public int currentX;
    public int currentY;
    public int npcId;
    public int stepCount;
    public int animationCurrent;
    public int animationNext;
    public int movingStep;
    public int waypointCurrent;
    public int waypointsX[];
    public int waypointsY[];
    public int equippedItem[];
    public String message;
    public int messageTimeout;
    public int bubbleItem;
    public int bubbleTimeout;
    public int damageTaken;
    public int healthCurrent;
    public int healthMax;
    public int combatTimer;
    public int level;
    public int colourHair;
    public int colourTop;
    public int colourBottom;
    public int colourSkin;
    public int incomingProjectileSprite;
    public int attackingPlayerServerIndex;
    public int attackingNpcServerIndex;
    public int projectileRange;
    public int skullVisible;
    
}