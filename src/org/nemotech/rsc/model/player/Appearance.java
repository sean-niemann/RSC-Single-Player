package org.nemotech.rsc.model.player;

import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.util.Formulae;

public class Appearance {

    private byte hairColour;
    private byte topColour;
    private byte trouserColour;
    private byte skinColour;
    
    private int head;
    private int body;
    
    public Appearance(int hairColour, int topColour, int trouserColour, int skinColour, int head, int body) {
        this.hairColour = (byte)hairColour;
        this.topColour = (byte)topColour;
        this.trouserColour = (byte)trouserColour;
        this.skinColour = (byte)skinColour;
        this.head = head;
        this.body = body;
    }
    
    public int getSprite(int pos) {
        switch(pos) {
            case 0:
                return head;
            case 1:
                return body;
            case 2:
                return 3;
            default:
                return 0;
        }
    }
    
    public int[] getSprites() {
        return new int[]{head, body, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }
    
    public byte getHairColor() {
        return hairColour;
    }
    
    public byte getShirtColor() {
        return topColour;
    }
    
    public byte getPantsColor() {
        return trouserColour;
    }
    
    public byte getSkinColor() {
        return skinColour;
    }
    
    public int getHeadType() {
        return head;
    }
    
    public int getBodyType() {
        return body;
    }
    
    public boolean isValid() {
        if(!Util.inArray(Formulae.HEAD_SPRITES, head) || !Util.inArray(Formulae.BODY_SPRITES, body)) {
            return false;
        }
        if(hairColour < 0 || topColour < 0 || trouserColour < 0 || skinColour < 0) {
            return false;
        }
        if(hairColour > 9 || topColour > 14 || trouserColour > 14 || skinColour > 4) {
            return false;
        }
        return true;
    }

}
