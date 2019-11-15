package org.nemotech.rsc.client.update;

import java.util.HashMap;
import java.util.Map;

import org.nemotech.rsc.util.Util;

public class UpdateManager {
    
    private static Map<Class<?>, Updater> updaters = new HashMap<>();
    
    private final String PACKAGE_NAME = UpdateManager.class.getPackage().getName() + ".impl";
    
    public void init() {
        int count = 0;
        try {
            for(Class<?> class_ : Util.loadClasses(PACKAGE_NAME)) {
                if(!class_.getName().contains("$")) {
                    add((Updater) class_.newInstance());
                    count++;
                }
            }
        } catch(ReflectiveOperationException e) {
            e.printStackTrace();
        }
        System.out.println("\t[Update Manager] Loaded " + count + " entity updaters");
    }
    
    private void add(Updater updater) {
        updaters.put(updater.getClass(), updater);
    }
    
    public static <U> U get(Class<U> class_) {
        return (U) updaters.get(class_);
    }
    
}