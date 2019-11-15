package org.nemotech.rsc.external.definition;

import org.nemotech.rsc.external.EntityDef;

/**
 * The definition wrapper for npcs
 */
public class NPCDef extends EntityDef {
    /**
     * Whether the npc is aggressive
     */
    public int aggressive;
    /**
     * The attack lvl
     */
    public int attack;
    /**
     * Whether the npc is attackable
     */
    public int attackable;
    /**
     * Colour of our legs
     */
    public int bottomColour;
    /**
     * Something to do with the camera
     */
    public int camera1, camera2;
    /**
     * The primary command
     */
    public String command;
    /**
     * The def lvl
     */
    public int defense;
    /**
     * Colour of our hair
     */
    public int hairColour;
    /**
     * The hit points
     */
    public int hits;
    /**
     * How long the npc takes to respawn
     */
    public int respawnTime;
    /**
     * Skin colour
     */
    public int skinColour;
    /**
     * Sprites used to make up this npc
     */
    public int sprites1, sprites2, sprites3, sprites4, sprites5, sprites6, sprites7, sprites8, sprites9, sprites10, sprites11, sprites12;
    /**
     * The strength lvl
     */
    public int strength;
    /**
     * Colour of our top
     */
    public int topColour;
    /**
     * Something to do with models
     */
    public int walkModel, combatModel, combatSprite;

    public int getAttack() {
        return attack;
    }

    public int getPantsColor() {
        return bottomColour;
    }

    public int getCameraWidth() {
        return camera1;
    }

    public int getCameraHeight() {
        return camera2;
    }

    public int getCombatModel() {
        return combatModel;
    }

    public int getCombatSprite() {
        return combatSprite;
    }

    public String getCommand() {
        return command;
    }

    public int getDefense() {
        return defense;
    }

    public int getHairColor() {
        return hairColour;
    }

    public int getHitpoints() {
        return hits;
    }

    public int getSkinColor() {
        return skinColour;
    }

    public int getSprite(int index) {
        int[] sprites = { sprites1, sprites2, sprites3, sprites4, sprites5, sprites6, sprites7, sprites8, sprites9, sprites10, sprites11, sprites12 };
        return sprites[index];
    }

    public int[] getStats() {
        return new int[]{attack, defense, strength};
    }

    public int getStrength() {
        return strength;
    }

    public int getShirtColour() {
        return topColour;
    }

    public int getWalkModel() {
        return walkModel;
    }

    public boolean isAggressive() {
        return attackable != 0 && aggressive != 0;
    }

    public boolean isAttackable() {
        return attackable != 0;
    }

    public int getRespawnTime() {
        return respawnTime;
    }
    
    public boolean isPlayerAI() {
        return description.equals("A computer controlled player");
    }
    
}