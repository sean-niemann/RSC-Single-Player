package org.nemotech.rsc.external.definition;

import org.nemotech.rsc.external.EntityDef;

public class GameObjectDef extends EntityDef {

    public String command1, command2, modelName;
    
    public int width, height, itemHeight, type;
    
    public int modelID;

    public String getFirstCommand() {
        return command1.toLowerCase();
    }

    public String getSecondCommand() {
        return command2.toLowerCase();
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }

    public String getModelName() {
        return modelName;
    }
    
    public int getItemHeight() {
        return itemHeight;
    }

    public int getType() {
        return type;
    }
    
}