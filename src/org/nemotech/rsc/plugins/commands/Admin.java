package org.nemotech.rsc.plugins.commands;

import java.util.Map;
import java.util.HashMap;

import org.nemotech.rsc.model.Mob;
import org.nemotech.rsc.model.Point;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.client.action.ActionManager;
import org.nemotech.rsc.event.SingleEvent;
import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.plugins.listeners.action.CommandListener;
import org.nemotech.rsc.model.player.states.CombatState;
import org.nemotech.rsc.util.Formulae;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.client.action.impl.WieldHandler;
import org.nemotech.rsc.external.definition.DoorDef;
import org.nemotech.rsc.external.definition.GameObjectDef;
import org.nemotech.rsc.external.definition.ItemDef;
import org.nemotech.rsc.external.definition.NPCDef;
import org.nemotech.rsc.model.player.Cache;

public class Admin extends Plugin implements CommandListener {

    @Override
    public void onCommand(String command, String[] args, Player player) {
        // Must be an administrator to use the following commands
        if(!player.isAdmin()) {
            return;
        }
        
        if(command.equals("stress")) {
            String failMessage = "Syntax: ::stress <item|npc|object> <radius>";
            if(args.length != 2) {
                player.message(failMessage);
                return;
            }
            int num;
            try {
                num = Integer.parseInt(args[1]) - 1;
            } catch(NumberFormatException e) {
                player.message(failMessage);
                return;
            }
            int x = player.getX();
            int y = player.getY();
            switch(args[0]) {
                case "item":
                    for(int i = 0; i < num; i++) {
                        for(int j = 0; j < num; j++) {
                            int id = (int) (Math.random() * EntityManager.getItems().length);
                            spawnItem(id, x + i, y + j, 1, player);
                            spawnItem(id, x + i, y - j, 1, player);
                            spawnItem(id, x - i, y + j, 1, player);
                            spawnItem(id, x - i, y - j, 1, player);
                        }
                    }
                    break;
                case "npc":
                    for(int i = 0; i < num; i++) {
                        for(int j = 0; j < num; j++) {
                            int id = (int) (Math.random() * EntityManager.getNPCs().length);
                            spawnNpcNoAggro(id, x + i, y + j);
                            spawnNpcNoAggro(id, x + i, y - j);
                            spawnNpcNoAggro(id, x - i, y + j);
                            spawnNpcNoAggro(id, x - i, y - j);
                        }
                    }
                    break;
                case "object":
                    for(int i = 0; i < num; i++) {
                        for(int j = 0; j < num; j++) {
                            int id = (int) (Math.random() * EntityManager.getGameObjectDefs().length);
                            spawnObject(new GameObject(new Point(x + i, y + j), id, 0, 0));
                            spawnObject(new GameObject(new Point(x + i, y - j), id, 0, 0));
                            spawnObject(new GameObject(new Point(x - i, y + j), id, 0, 0));
                            spawnObject(new GameObject(new Point(x - i, y - j), id, 0, 0));
                        }
                    }
                    break;
                default:
                    player.message(failMessage);
                    break;
            }
            return;
        }
        
        if(command.equals("clearcache")) {
            if(args.length != 1) {
                player.message("Syntax: ::clearcache <all|key>");
                return;
            }
            if(args[0].equals("all")) {
                // TODO - BROKEN (ALL COMMAND)
                for(Map.Entry<String, Object> entry : player.getCache().getCacheMap().entrySet()) {
                    player.getCache().remove(entry.getKey());
                }
                player.message("All player cache successfully removed");
            } else {
                if(player.getCache().hasKey(args[0])) {
                    player.getCache().remove(args[0]);
                    player.message("Removed cache key: " + args[0]);
                } else {
                    player.message("Key not found in cache map: " + args[0]);
                }
            }
            return;
        }
        
        if(command.equals("queststage")) {
            if(args.length != 1) {
                player.message("Syntax: ::queststage <quest_id>");
                return;
            }
            int id = Integer.parseInt(args[0]);
            player.message("Quest stage for [Q" + id + "] is " + player.getQuestStage(id));
            return;
        }
        
        if(command.equals("dumpcache")) {
            Cache cache = player.getCache();
            for(Map.Entry<String, Object> entry : cache.getCacheMap().entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            player.message("cache map printed to console");
            return;
        }
        
        if(command.equals("find")) {
            if(args.length != 2) {
                player.message("Syntax: ::find <entity> <string>");
                return;
            }
            String phrase = args[1].replaceAll("_", " ").toLowerCase();
            switch(args[0]) {
                case "item":
                    ItemDef[] items = EntityManager.getItems();
                    for(int i = 0; i < items.length; i++) {
                        if(items[i].getName().toLowerCase().contains(phrase)) {
                            player.message("@whi@[" + i + "] : " + items[i].getName() + "@que@");
                        }
                    }
                    break;
                case "npc":
                    NPCDef[] npcs = EntityManager.getNPCs();
                    for(int i = 0; i < npcs.length; i++) {
                        if(npcs[i].getName().toLowerCase().contains(phrase)) {
                            player.message("@whi@[" + i + "] : " + npcs[i].getName() + "@que@");
                        }
                    }
                    break;
                case "object":
                    GameObjectDef[] objects = EntityManager.getGameObjectDefs();
                    for(int i = 0; i < objects.length; i++) {
                        if(objects[i].getName().toLowerCase().contains(phrase)) {
                            player.message("@whi@[" + i + "] : " + objects[i].getName() + "@que@");
                        }
                    }
                    break;
                case "door":
                case "wall":
                    DoorDef[] doors = EntityManager.getDoorDefs();
                    for(int i = 0; i < doors.length; i++) {
                        if(doors[i].getName().toLowerCase().contains(phrase)) {
                            player.message("@whi@[" + i + "] : " + doors[i].getName() + "@que@");
                        }
                    }
                    break;
                default:
                    player.message("Invalid entity");
                    break;
            }
            return;
        }
        
        if(command.equals("test")) {
            player.setLastDamage(50);
            player.setCurStat(HITS, player.getCurStat(HITS) - 50);
            player.getSender().sendStat(HITS);
            player.informOfModifiedHits(player);
            if(player.getCurStat(HITS) <= 0) {
                player.killedBy(null);
                return;
            }
            return;
        }
        
        if(command.equals("quest")) {
            if(args.length != 2) {
                player.getSender().sendMessage("Syntax: ::quest <id> <stage>");
                return;
            }
            int id = Integer.parseInt(args[0]);
            int stage = Integer.parseInt(args[1]);
            if(world.getQuest(id) != null) {
                player.updateQuestStage(id, stage);
                player.getSender().sendMessage("Quest ID " + id + " has been set to stage " + stage);
            } else {
                player.getSender().sendMessage("Invalid quest ID");
            }
        }
        
        if(command.equals("island")) {
            player.teleport(791, 15, true);
            player.getSender().sendMessage("You teleport to the staff area");
            return;
        }
        
        if(command.equals("pos") || command.equals("coords")) {
            player.getSender().sendMessage("@whi@" + player.getX() + " " + player.getY() + "@que@");
            return;
        }
        
        if(command.equals("runes")) {
            player.getInventory().add(new InvItem(31, 5000));
            player.getInventory().add(new InvItem(32, 5000));
            player.getInventory().add(new InvItem(33, 5000));
            player.getInventory().add(new InvItem(34, 5000));
            player.getInventory().add(new InvItem(35, 5000));
            player.getInventory().add(new InvItem(36, 5000));
            player.getInventory().add(new InvItem(38, 1000));
            player.getInventory().add(new InvItem(40, 1000));
            player.getInventory().add(new InvItem(41, 1000));
            player.getInventory().add(new InvItem(42, 1000));
            player.getInventory().add(new InvItem(46, 1000));
            player.getInventory().add(new InvItem(619, 1000));
            player.getInventory().add(new InvItem(825, 500));
            player.getSender().sendInventory();
            return;
        }
        
        if(command.equals("sector")) {
            int x = player.getX();
            int y = player.getY();
            int sectorH = 0;
            int sectorX = 0;
            int sectorY = 0;
            if (x != -1 && y != -1) {
                if (y >= 0 && y <= 1007)
                    sectorH = 0;
                else if (y >= 1007 && y <= 1007 + 943) {
                    sectorH = 1;
                    y -= 943;
                } else if (y >= 1008 + 943 && y <= 1007 + (943 * 2)) {
                    sectorH = 2;
                    y -= 943 * 2;
                } else {
                    sectorH = 3;
                    y -= 943 * 3;
                }
                sectorX = (x / 48) + 48;
                sectorY = (y / 48) + 37;
            }
            player.getSender().sendMessage("@que@" + "h" + sectorH + "x" + sectorX + "y" + sectorY);
        }
        
        if(command.equals("stuff")) {
            int count = 0;
            for(;;) {
                InvItem item = new InvItem(Util.random(1200));
                if(item.getDef().isWieldable()) {
                    player.getInventory().add(item);
                    count++;
                }
                if(count >= 5) break;
            }
            player.getSender().sendInventory();
            player.getSender().sendMessage("You have received 5 random wearable items");
        }

        if(command.equals("set")) {
            if(args.length != 2) {
                player.getSender().sendMessage("Syntax: ::set <statname> <level>");
                return;
            }

            int stat = Formulae.getStatIndex(args[0]);
            int lvl = Integer.parseInt(args[1]);

            if (lvl < 1 || lvl > 99) {
                player.getSender().sendMessage("Invalid " + Formulae.STAT_NAMES[stat] + " level");
                return;
            }

            player.setCurStat(stat, lvl);
            player.setMaxStat(stat, lvl);
            player.setExp(stat, Formulae.lvlToXp(lvl));

            int comb = Formulae.getCombatlevel(player.getMaxStats());
            if (comb != player.getCombatLevel()) {
                player.setCombatLevel(comb);
            }
            player.getSender().sendStat(stat);
            player.getSender().sendMessage("Your " + Formulae.STAT_NAMES[stat] + " level is now " + args[1]);
            return;
        }

        if(command.equals("addbank")) {
            if (args.length < 1 || args.length > 2) {
                player.getSender().sendMessage("Syntax: ::addbank <id> [<amount>]");
                return;
            }
            int id = Integer.parseInt(args[0]);
            if (EntityManager.getItem(id) != null) {
                int amount = 1;
                if (args.length == 2) {
                    amount = Integer.parseInt(args[1]);
                }
                player.getBank().add(new InvItem(id, amount));
                player.getSender().sendMessage("You add " + amount + " " + EntityManager.getItem(id).getName() + " to your bank account");
                player.getSender().updateBankItem(player.getBank().getFirstIndexById(id), id, player.getBank().countId(id));
            } else {
                player.getSender().sendMessage("Invalid item!");
            }
            return;
        }
        
        if(command.equals("removebank")) {
            if (args.length < 1 || args.length > 2) {
                player.getSender().sendMessage("Syntax: ::removebank <id> [<amount>]");
                return;
            }
            int id = Integer.parseInt(args[0]);
            if (EntityManager.getItem(id) != null) {
                int amount = 1;
                if (args.length == 2) {
                    amount = Integer.parseInt(args[1]);
                }
                if(amount <= player.getBank().countId(id)) {
                    player.getBank().remove(new InvItem(id, amount));
                    player.getSender().sendMessage("You remove " + amount + " " + EntityManager.getItem(id).getName() + " from your bank account");
                    player.getSender().updateBankItem(player.getBank().getFirstIndexById(id), id, player.getBank().countId(id));
                } else {
                    player.getSender().sendMessage("You do not have that many of that item!");
                }
            } else {
                player.getSender().sendMessage("Invalid item!");
            }
            return;
        }

        if(command.equals("bank")) {
            player.getSender().showBank();
            player.getSender().sendMessage("You access your bank account");
            return;
        }

        if(command.equals("appearance")) {
            player.setChangingAppearance(true);
            player.getSender().sendAppearanceScreen();
            return;
        }

        if(command.equals("empty")) {
            for (InvItem i : player.getInventory().getItems()) {
                if (i.isWielded()) {
                    ActionManager.get(WieldHandler.class).handleUnwield(i, false);
                }
            }
            player.getInventory().getItems().clear();
            player.getSender().sendInventory();
            player.getSender().sendMessage("You have cleared your inventory");
            return;
        }

        if(command.equals("town")) {
            if (args.length != 1) {
                player.getSender().sendMessage("Syntax: ::town <location>");
                return;
            }
            Point town = TOWNS.get(args[0]);
            if(town != null) {
                player.teleport(town.getX(), town.getY(), true);
            } else {
                player.getSender().sendMessage("Invalid town!");
            }
            return;
        }
        
        if(command.equals("teleport")) {
            if(args.length != 2) {
                player.getSender().sendMessage("Syntax: ::teleport <x> <y>");
                return;
            }
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            if(World.getWorld().withinWorld(x, y)) {
                player.teleport(x, y, false);
            } else {
                player.getSender().sendMessage("Invalid coordinates!");
            }
            return;
        }

        if(command.equals("item")) {
            if (args.length < 1 || args.length > 2) {
                player.getSender().sendMessage("Syntax: ::item <id> [<amount>]");
                return;
            }
            int id = Integer.parseInt(args[0]);
            if (EntityManager.getItem(id) != null) {
                int amount = 1;
                if (args.length == 2) {
                    amount = Integer.parseInt(args[1]);
                }
                if (!EntityManager.getItem(id).isStackable() && amount > player.getInventory().getFreeSpaces()) {
                    player.getSender().sendMessage("You cannot hold that many items!");
                    return;
                }
                if (EntityManager.getItem(id).isStackable())
                    player.getInventory().add(new InvItem(id, amount));
                else {
                    for (int i = 0; i < amount; i++) {
                        player.getInventory().add(new InvItem(id, 1));
                    }
                }
                player.getSender().sendInventory();
                player.getSender().sendMessage("You spawn " + amount + " " + EntityManager.getItem(id).getName());
            } else {
                player.getSender().sendMessage("Invalid item!");
            }
            return;
        }
        
        if(command.equals("npc")) {
            if (args.length != 1) {
                player.getSender().sendMessage("Syntax: ::npc <id>");
                return;
            }
            int id = Integer.parseInt(args[0]);
            int x = player.getX();
            int y = player.getY();
            if (EntityManager.getNPC(id) != null) {
                final NPC n = new NPC(id, x, y, x - 2, x + 2, y - 2, y + 2);
                n.setRespawn(false);
                World.getWorld().registerNpc(n);
                World.getWorld().getDelayedEventHandler().add(new SingleEvent(null, 60000) {
                    @Override
                    public void action() {
                        Mob opponent = n.getOpponent();
                        if (opponent != null) {
                            opponent.resetCombat(CombatState.ERROR);
                        }
                        n.resetCombat(CombatState.ERROR);
                        world.unregisterNpc(n);
                        n.remove();
                    }
                });
            } else {
                player.getSender().sendMessage("Invalid id");
            }
            return;
        }
        
        if (command.equals("object")) {
            if (args.length < 1 || args.length > 2) {
                player.getSender().sendMessage("Syntax: ::object <id> [dir]");
                return;
            }
            int id = Integer.parseInt(args[0]);
            if (id < 0) {
                GameObject object = World.getWorld().getTile(player.getLocation()).getGameObject();
                if (object != null) {
                    World.getWorld().unregisterGameObject(object);
                }
            } else if (EntityManager.getGameObjectDef(id) != null) {
                int dir = args.length == 2 ? Integer.parseInt(args[1]) : 0;
                GameObject obj = new GameObject(player.getLocation(), id, dir, 0);
                World.getWorld().registerGameObject(obj);
            } else {
                player.getSender().sendMessage("Invalid id");
            }
            return;
        }
        
        if (command.equals("door")) {
            if (args.length < 1 || args.length > 2) {
                player.getSender().sendMessage("Syntax: ::door <id> [dir]");
                return;
            }
            int id = Integer.parseInt(args[0]);
            if (id < 0) {
                GameObject object = World.getWorld().getTile(player.getLocation()).getGameObject();
                if (object != null) {
                    World.getWorld().unregisterGameObject(object);
                }
            } else if (EntityManager.getDoor(id) != null) {
                int dir = args.length == 2 ? Integer.parseInt(args[1]) : 0;
                World.getWorld().registerGameObject(new GameObject(player.getLocation(), id, dir, 1));
            } else {
                player.getSender().sendMessage("Invalid id");
            }
            return;
        }
    }

    private final Map<String, Point> TOWNS = new HashMap<String, Point>() {{
        put("varrock",   new Point(122, 509));
        put("falador",   new Point(304, 542));
        put("draynor",   new Point(214, 632));
        put("portsarim", new Point(269, 643));
        put("karamja",   new Point(370, 685));
        put("alkharid",  new Point(89,  693));
        put("lumbridge", new Point(120, 648));
        put("edgeville", new Point(217, 449));
        put("taverly",   new Point(373, 498));
        put("seers",     new Point(501, 450));
        put("barbarian", new Point(233, 513));
        put("rimmington",new Point(325, 663));
        put("catherby",  new Point(440, 501));
        put("ardougne",  new Point(549, 589));
        put("yanille",   new Point(583, 747));
        put("lostcity",  new Point(127, 3518));
        put("gnome",     new Point(703, 527));
        put("tutorial",  new Point(219, 742));
    }};

}
