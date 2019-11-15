package org.nemotech.rsc.external.definition.extra;

public class ItemHerbDef {
    
    /**
     * The exp given completing this potion
     */
    public double exp;
    /**
     * The ID of the potion created
     */
    public int potionId;
    /**
     * The level required to complete this potion
     */
    public int requiredLvl;
    /**
     * The ID of the second ingredient
     */
    public int secondID;
    /**
     * The ID of the unfinished potion required
     */
    public int unfinishedID;

    public double getExp() {
        return exp;
    }

    public int getPotionID() {
        return potionId;
    }

    public int getReqLevel() {
        return requiredLvl;
    }

    public int getSecondID() {
        return secondID;
    }

    public int getUnfinishedID() {
        return unfinishedID;
    }
    
}