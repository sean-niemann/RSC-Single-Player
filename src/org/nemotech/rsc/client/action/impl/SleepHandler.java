package org.nemotech.rsc.client.action.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.nemotech.rsc.client.action.ActionHandler;
import org.nemotech.rsc.client.mudclient;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.World;

public class SleepHandler implements ActionHandler {
    
    private mudclient mc = mudclient.getInstance();
    
    private BufferedImage[] imageFiles;
    
    private String[] wordNames;
    
    private int numImages;
    
    public void init() {
        try {
            File[] captchaImages = new File(Constants.CACHE_DIRECTORY + "captcha").listFiles();
            numImages = captchaImages.length;
            imageFiles = new BufferedImage[numImages];
            wordNames = new String[numImages];
            int i = 0;
            for(File file : captchaImages) {
                if(!file.getName().endsWith(".png")) {
                    continue;
                }
                imageFiles[i] = ImageIO.read(file);
                String fileName = file.getName();
                wordNames[i] = fileName.substring(0, fileName.lastIndexOf("."));
                i++;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public BufferedImage generateCaptcha(Player p) {
        int random = Util.random(numImages);
        p.setSleepword(wordNames[random]);
        return imageFiles[random];
    }
    
    public boolean handleGuess(String word) {
        Player player = World.getWorld().getPlayer();
        if(word.equalsIgnoreCase(player.getSleepword())) {
            player.getSender().sendWakeUp(true, false);
            return true;
        }
        //System.out.println("Entered wrong sleep word: " + word);
        return false;
    }
    
    public void handleSleep(BufferedImage image) {
        if (!mc.isSleeping) {
            mc.fatigueSleeping = mc.statFatigue;
        }
        mc.isSleeping = true;
        mc.inputTextCurrent = "";
        mc.inputTextFinal = "";
        try {
            mc.captchaWidth = image.getWidth();
            mc.captchaHeight = image.getHeight();
            mc.captchaPixels = new int[mc.captchaWidth][mc.captchaHeight];
            for(int x = 0; x < mc.captchaWidth; x++) {
                for(int y = 0; y < mc.captchaHeight; y++) {
                    mc.captchaPixels[x][y] = image.getRGB(x, y);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        mc.sleepingStatusText = null;
    }
    
}