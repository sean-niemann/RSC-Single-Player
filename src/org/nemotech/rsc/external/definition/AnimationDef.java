package org.nemotech.rsc.external.definition;

import org.nemotech.rsc.external.EntityDef;

public class AnimationDef extends EntityDef {
    
    public int charColour;
    
    public int genderModel;
    
    public boolean hasA;
    
    public boolean hasF;
    
    public int number;

    public int getCharColour() {
        return charColour;
    }

    public int getGenderModel() {
        return genderModel;
    }

    public boolean hasA() {
        return hasA;
    }

    public boolean hasF() {
        return hasF;
    }

    public int getNumber() {
        return number;
    }

}