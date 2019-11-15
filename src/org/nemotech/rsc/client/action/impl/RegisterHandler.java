package org.nemotech.rsc.client.action.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.nemotech.rsc.Constants;
import org.nemotech.rsc.model.player.Cache;
import org.nemotech.rsc.client.action.ActionHandler;
import org.nemotech.rsc.model.player.SaveFile;

public class RegisterHandler implements ActionHandler {
    
    public boolean handleRegister(String username) {
        username = username.replace("_", " ");
        File dataFile = new File(Constants.CACHE_DIRECTORY + "players" + File.separator + username + "_data.dat");
        File cacheFile = new File(Constants.CACHE_DIRECTORY + "players" + File.separator + username + "_cache.dat");
        if(!dataFile.exists() && !cacheFile.exists()) {
            try {
                // player data file
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile));
                SaveFile playerData = new SaveFile(true);
                oos.writeObject(playerData);
                oos.close();
                
                // player cache file
                oos = new ObjectOutputStream(new FileOutputStream(cacheFile));
                Cache cache = new Cache();
                oos.writeObject(cache);
                oos.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
    
}