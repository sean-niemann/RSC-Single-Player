package org.nemotech.rsc.model;

import org.nemotech.rsc.model.player.Player;

public abstract class MenuHandler {
    /**
     * The Player this handler is responsible for
     */
    protected Player owner;
    /**
     * Array of possible options that can be chosen
     */
    protected String[] options;
    
    /**
     * Creates a new MenuHandler with the given options
     */
    public MenuHandler(String[] options) {
        this.options = options;
    }
    
    /**
     * Set the Player this MenuHandler is responsible for
     */
    public final void setOwner(Player owner) {
        this.owner = owner;
    }
    
    /**
     * Gets the appropriate option string
     */
    public final String getOption(int index) {
        if(index < 0 || index >= options.length) {
            return null;
        }
        return options[index];
    }
    
    public final String[] getOptions() {
        return options;
    }
    
    /**
     * Abstract method for handling the reply
     */
    public abstract void handleReply(int option, String reply);
}