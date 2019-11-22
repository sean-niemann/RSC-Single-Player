package org.nemotech.rsc;

import org.nemotech.rsc.client.mudclient;
import org.nemotech.rsc.client.action.ActionManager;
import org.nemotech.rsc.client.update.UpdateManager;
import org.nemotech.rsc.core.EngineThread;
import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.plugins.PluginManager;

/**
 * RSC Single Player
 * 
 * 
 * 
 * 
 * Pass me a bottle, Mr. Jones
 * 
 * @author Sean Niemann    (Zoso_)
 * @author Jamie Furnaghan (Reines)
 * @author n0m
 */
public final class Main {
    
    public static void main(String[] args) {
        System.out.println("Welcome to RSC Single Player v" + Constants.VERSION + "\n");
        System.out.println("To report any bugs, exploits, missing or incorrect content, etc. you can contact");
        System.out.println("the developer at sean@nemotech.org [Sean Niemann / Zoso]\n");
        // initialize gson
        EntityManager.init();
        // start client
        mudclient.INSTANCE = new mudclient();
        mudclient.INSTANCE.start();
        // initialize core components
        try {
            PluginManager.getInstance().init();
        } catch(ReflectiveOperationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        // start core services
        new EngineThread().start();
        // load action handlers
        new ActionManager().init();
        // load updaters
        new UpdateManager().init();
        // load captcha into memory (disabled due to popular demand)
        //ActionManager.get(SleepHandler.class).init();
    }
    
}
