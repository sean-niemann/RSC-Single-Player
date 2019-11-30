package org.nemotech.rsc.plugins;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import org.nemotech.rsc.Constants;

import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.model.World;

public final class PluginManager {

    public static PluginManager pluginHandler = null;

    private Object defaultHandler = null;

    public static PluginManager getInstance() {
        if (pluginHandler == null) {
            pluginHandler = new PluginManager();
        }
        return pluginHandler;
    }
    
    private final String PLUGIN_PREFIX = "org.nemotech.rsc.plugins.";
    private final String[] PLUGIN_PACKAGES = {
        /* miscellaneous scripts */
        "default_", "commands", "default_", "items", "misc", "skills", "skills.agility",
        
        /* mini games */
        "minigames.barcrawl", "minigames.blurberrysbar", "minigames.fishingtrawler", "minigames.gnomerestaurant",
        
        /* all npcs (that are not handled in quest scripts */
        "npcs", "npcs.alkharid", "npcs.ardougne.east", "npcs.ardougne.west", "npcs.barbarian", "npcs.brimhaven", "npcs.catherby", "npcs.draynor",
        "npcs.dwarvenmine", "npcs.edgeville", "npcs.entrana", "npcs.falador", "npcs.grandtree", "npcs.gutanoth", "npcs.hemenster",
        "npcs.karamja", "npcs.khazard", "npcs.lostcity", "npcs.lumbridge", "npcs.portsarim", "npcs.rimmington", "npcs.seers",
        "npcs.shilo", "npcs.taverly", "npcs.tutorial", "npcs.varrock", "npcs.wilderness.banditcamp", "npcs.wilderness.mage_arena",
        "npcs.yanille",
        
        /* free quests */
        "quests.free",
        /* members quests (single-script) */
        "quests.members",
        /* digsite quest */
        "quests.members.digsite",
        /* grand tree quest */
        "quests.members.grandtree",
        /* shilo village quest */
        "quests.members.shilovillage",
        /* tourist trap quest */
        "quests.members.touristtrap",
        /* watchtower quest */
        "quests.members.watchtower",
        /* underground pass (contains sub packages) */
        "quests.members.undergroundpass.mechanism", "quests.members.undergroundpass.npcs", "quests.members.undergroundpass.obstacles",
        /* legend's quest (contains sub packages) */
        "quests.members.legendsquest.mechanism", "quests.members.legendsquest.npcs", "quests.members.legendsquest.npcs.shop", "quests.members.legendsquest.obstacles"
    };

    private Map<String, Set<Object>> actionPlugins = new HashMap<>();
    private Map<String, Set<Object>> executivePlugins = new HashMap<>();
    private ExecutorService executor = Executors.newCachedThreadPool();
    private List<Class<?>> knownInterfaces = new ArrayList<>();
    private Map<String, Class<?>> queue = new ConcurrentHashMap<>();

    public boolean blockDefaultAction(final String interfce, final Object[] data) {
        return blockDefaultAction(interfce, data, true);
    }

    /**
     * 
     * @param interfce
     * @param data
     * @param callAction
     * @return
     */

    public boolean blockDefaultAction(final String interfce, final Object[] data, final boolean callAction) {
        boolean shouldBlock = false, flagStop = false;
        queue.clear();
        if (executivePlugins.containsKey(interfce + "ExecutiveListener")) {
            for (final Object c : executivePlugins.get(interfce + "ExecutiveListener")) {
                try {
                    final Class<?>[] dataClasses = new Class<?>[data.length];
                    int i = 0;
                    for (final Object o : data) {
                        dataClasses[i++] = o.getClass();
                    }
                    final Method m = c.getClass().getMethod("block" + interfce, dataClasses);
                    shouldBlock = (Boolean) m.invoke(c, data);
                    if (shouldBlock) {
                        queue.put(interfce, c.getClass());
                        flagStop = true;
                    } else if(queue.size() > 1) {

                    } else if (queue.isEmpty()) {
                        queue.put(interfce, defaultHandler.getClass());
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (callAction) {
            handleAction(interfce, data);
        }
        return flagStop; // not sure why it matters if its false or true
    }


    public Map<String, Set<Object>> getActionPlugins() {
        return actionPlugins;
    }

    public Map<String, Set<Object>> getExecutivePlugins() {
        return executivePlugins;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public List<Class<?>> getKnownInterfaces() {
        return knownInterfaces;
    }

    public void handleAction(final String interfce, final Object[] data) {
        if (actionPlugins.containsKey(interfce + "Listener")) {
            for (final Object c : actionPlugins.get(interfce + "Listener")) {
                try {
                    final Class<?>[] dataClasses = new Class<?>[data.length];
                    int i = 0;
                    for (final Object o : data) {
                        dataClasses[i++] = o.getClass();
                    }

                    final Method m = c.getClass().getMethod("on" + interfce, dataClasses);
                    boolean flag = false;

                    if (queue.containsKey(interfce)) {
                        for (final Class<?> clz : queue.values()) {
                            if (clz.getName().equalsIgnoreCase(c.getClass().getName())) {
                                flag = true;
                                if(Constants.DEBUG_PLUGINS) {
                                    System.out.println("Executing with : " + clz.getName());
                                }
                                break;
                            }
                        }
                    } else {
                        flag = true;
                    }

                    if (flag) {
                        final FutureTask<Integer> task = new FutureTask<>(() -> {
                            try {
                                m.invoke(c, data);
                            } catch (Exception cme) {
                                cme.printStackTrace();
                            }
                            return 1;
                        });
                        getExecutor().execute(task);
                    }
                } catch (final Exception e) {
                    System.err.println("Exception at plugin handling: ");
                    e.printStackTrace();
                }
            }
        }
    }

    public void init() throws ReflectiveOperationException {
        Map<String, Object> loadedPlugins = new HashMap<>();
        List<Class<?>> loadedClassFiles = new ArrayList<>();
        
        for(String packageName : PLUGIN_PACKAGES) {
            List<Class<?>> temp = Util.loadClasses(PLUGIN_PREFIX + packageName);
            loadedClassFiles.addAll(temp);
        }

        for (final Class<?> interfce : Util.loadInterfaces("org.nemotech.rsc.plugins.listeners.action")) {
            final String interfceName = interfce.getName().substring(interfce.getName().lastIndexOf(".") + 1);
            knownInterfaces.add(interfce);
            for (final Class<?> plugin : loadedClassFiles) {
                if (!interfce.isAssignableFrom(plugin)) {
                    continue;
                }
                Object instance = plugin.getConstructor().newInstance();
                if(instance instanceof DefaultHandler && defaultHandler == null) {
                    defaultHandler = instance;
                    continue;
                }
                /*if (instance instanceof ShopInterface) {
                    final ShopInterface it = (ShopInterface) instance;

                    for (final Shop s : it.getShops()) {
                        World.getWorld().getShops().add(s);
                        Server.getServer().getEventHandler().add(new ShopRestockEvent(s));
                    }
                }*/
                if (loadedPlugins.containsKey(instance.getClass().getName())) {
                    instance = loadedPlugins.get(instance.getClass().getName());
                } else {
                    loadedPlugins.put(instance.getClass().getName(), instance);
                    if (instance instanceof QuestInterface) {
                        final QuestInterface q = (QuestInterface) instance;
                        try {
                            World.getWorld().registerQuest(q);
                        } catch (final Exception e) {
                            System.err.println("Error registering quest " + q.getQuestName());
                            e.printStackTrace();
                            continue;
                        }
                    }
                }
                if (actionPlugins.containsKey(interfceName)) {
                    final Set<Object> data = actionPlugins.get(interfceName);
                    data.add(instance);
                    actionPlugins.put(interfceName, data);
                } else {
                    final Set<Object> data = new HashSet<>();
                    data.add(instance);
                    actionPlugins.put(interfceName, data);
                }
            }
        }
        for (final Class<?> interfce : Util.loadInterfaces("org.nemotech.rsc.plugins.listeners.executive")) {
            final String interfceName = interfce.getName().substring(interfce.getName().lastIndexOf(".") + 1);
            knownInterfaces.add(interfce);
            for (final Class<?> plugin : loadedClassFiles) {
                if (!interfce.isAssignableFrom(plugin)) {
                    continue;
                }
                Object instance = plugin.newInstance();
                if (loadedPlugins.containsKey(instance.getClass().getName())) {
                    instance = loadedPlugins.get(instance.getClass().getName());
                } else {
                    loadedPlugins.put(instance.getClass().getName(), instance);

                    if (Arrays.asList(instance.getClass().getInterfaces()).contains(QuestInterface.class)) {
                        final QuestInterface q = (QuestInterface) instance;
                        try {
                            World.getWorld().registerQuest((QuestInterface) instance);
                        } catch (final Exception e) {
                            System.err.println("Error registering quest " + q.getQuestName());
                            e.printStackTrace();
                            continue;
                        }
                    }
                }

                if (executivePlugins.containsKey(interfceName)) {
                    final Set<Object> data = executivePlugins.get(interfceName);
                    data.add(instance);
                    executivePlugins.put(interfceName, data);
                } else {
                    final Set<Object> data = new HashSet<>();
                    data.add(instance);
                    executivePlugins.put(interfceName, data);
                }
            }
        }
        System.out.println("[Plugin Manager] Loaded " + World.getWorld().getQuests().size() + " quests");
        System.out.println("[Plugin Manager] Loaded " + loadedPlugins.size() + " plugins");
    }
    
}