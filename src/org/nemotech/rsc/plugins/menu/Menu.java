package org.nemotech.rsc.plugins.menu;

import java.util.ArrayList;
import java.util.List;

import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.plugins.Plugin;

/**
 * This system is for adding a new menu item on NPC under certain circumstances.
 * If this system is used, the whole starting menu needs to be done using this.
 * 
 * @author n0m
 */
public class Menu {

    private List<Option> options = new ArrayList<>();

    /**
     * Adds a single option to the menu. Usage: Menu defaultMenu = new Menu();
     * defaultMenu.addOption(new Option("Hello, this is a menu item") { public
     * void action() {
     * 
     * } }
     * 
     * @param option
     * @return
     */
    public Menu addOption(Option option) {
        options.add(option);
        return this;
    }

    /**
     * Adds multiple options at once. defaultMenu.addOptions(new
     * Option("Hello, this is a menu item") { public void action() {
     * 
     * } }, new Option("Hello, this is a menu item") { public void action() {
     * 
     * } });
     * 
     * @param opts
     * @return
     */
    public Menu addOptions(Option... opts) {
        for (Option i : opts) {
            options.add(i);
        }
        return this;
    }

    /**
     * Builds and displays the menu to the player.
     * 
     * @param player
     */
    public void showMenu(final Player player, final NPC npc) {
        String[] option = new String[options.size()];
        int i = 0;
        for (Option opt : options) {
            option[i] = opt.getOption();
            i++;
        }
        player.setMenu(this);
        player.getSender().sendMenu(option);
        //long start = System.currentTimeMillis();
        while (true) {
            if (/*System.currentTimeMillis() - start > 19500 || */player.getMenu() == null) {
                break;
            }
            if (player.getInteractingNpc() != null) {
                player.getInteractingNpc().setBusyTimer(1500);
            }
            Plugin.sleep(100);
        }
    }

    
    public int size() {
        return options.size();
    }
    
    public void handleReply(Player player, int i) {
        Option option = options.get(i);
        if (option != null) {
            Plugin.playerTalk(player, player.getInteractingNpc(), option.getOption());
            option.action();
        }
        player.resetMenuHandler();
    }
}
