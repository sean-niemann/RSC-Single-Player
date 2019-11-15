package org.nemotech.rsc.model.player;

import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.external.definition.ItemDef;
import org.nemotech.rsc.external.definition.extra.ItemSmeltingDef;
import org.nemotech.rsc.external.definition.extra.ItemUnIdentHerbDef;
import org.nemotech.rsc.external.definition.extra.ItemWieldableDef;
import org.nemotech.rsc.model.Entity;

public class InvItem extends Entity implements Comparable<InvItem> {
    
    public boolean isEdible() {
        return EntityManager.getItemEdibleHeals(id) > 0;
    }
    
    public int eatingHeals() {
        if (!isEdible()) {
            return 0;
        }
        return EntityManager.getItemEdibleHeals(id);
    }

    @Override
    public int compareTo(InvItem item) {
        if (item.getDef().isStackable()) {
            return -1;
        }
        if (getDef().isStackable()) {
            return 1;
        }
        return item.getDef().getPrice() - getDef().getPrice();
    }
    
    public ItemSmeltingDef getSmeltingDef() {
        return EntityManager.getItemSmeltingDef(id); 
    }
    
    public ItemWieldableDef getWieldDef() {
        return EntityManager.getItemWieldableDef(id);
    }
    
    public ItemUnIdentHerbDef getUnIdentHerbDef() {
        return EntityManager.getItemUnIdentHerbDef(id);
    }

    private int amount;
    private boolean wielded = false;

    public InvItem(int id) {
        setID(id);
        setAmount(1);
    }

    public InvItem(int id, int amount) {
        setID(id);
        setAmount(amount);
    }

    public ItemDef getDef() {
        return EntityManager.getItem(id);
    }

    public boolean isWielded() {
        return wielded;
    }

    public void setWield(boolean wielded) {
        this.wielded = wielded;
    }

    public void setAmount(int amount) {
        if(amount < 0) {
            amount = 0;
        }
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
    
    public boolean wieldingAffectsItem(InvItem i) {
        if (!i.getDef().isWieldable() || !getDef().isWieldable()) {
            return false;
        }
        for (int affected : EntityManager.getItemWieldableDef(getID()).getAffectedTypes()) {
            if (EntityManager.getItemWieldableDef(i.getID()).getType() == affected) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof InvItem) {
            InvItem item = (InvItem)o;
            return item.getID() == getID();
        }
        return false;
    }
    
    public ItemWieldableDef getWieldableDef() {
        return EntityManager.getItemWieldableDef(id);
    }

}