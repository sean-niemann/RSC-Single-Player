package org.nemotech.rsc.external.definition;

import org.nemotech.rsc.external.EntityDef;

public class DoorDef extends EntityDef {

    public String command1, command2;
    
    public int type, visibility;
    
    public int height, textureFront, textureBack;

    public String getCommandFirst() {
        return command1.toLowerCase();
    }

    public String getCommandSecond() {
        return command2.toLowerCase();
    }

    public int getType() {
        return type;
    }
    
    public int getVisibility() {
        return visibility;
    }

    public int getHeight() {
        return height;
    }

    public int getTextureFront() {
        return textureFront;
    }

    public int getTextureBack() {
        return textureBack;
    }
    
}