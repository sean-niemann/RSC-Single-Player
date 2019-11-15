package org.nemotech.rsc.external.definition;

import org.nemotech.rsc.external.EntityDef;

public class ItemDef extends EntityDef {
    
    public int price;
    
    public String command;
    
    public int mask;
    
    public boolean members;
    
    public int sprite;
    
    public boolean stackable;
    
    public boolean trade;
    
    public int wieldable;

    public boolean isTradable() {
        return trade;
    }

    public int getPrice() {
        return price;
    }

    public String getCommand() {
        return command;
    }

    public String getWearCommand() {
        return wieldable == 16 ? "Wield" : "Wear";
    }

    public int getMask() {
        return mask;
    }

    public int getSprite() {
        return sprite;
    }

    public boolean isMembers() {
        return members;
    }

    public boolean isStackable() {
        return stackable;
    }

    public boolean isWieldable() {
        return wieldable > 0;
    }
    
}