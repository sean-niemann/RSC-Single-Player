package org.nemotech.rsc.client.action;

import java.util.HashMap;
import java.util.Map;

import org.nemotech.rsc.util.Util;

public class ActionManager {
    
    private static Map<Class<?>, ActionHandler> handlers = new HashMap<>();
    
    private final String PACKAGE_NAME = ActionManager.class.getPackage().getName() + ".impl";
    
    public void init() {
        int count = 0;
        try {
            for(Class<?> class_ : Util.loadClasses(PACKAGE_NAME)) {
                if(!class_.getName().contains("$")) {
                    add((ActionHandler) class_.getDeclaredConstructor().newInstance());
                    count++;
                }
            }
        } catch(ReflectiveOperationException e) {
            e.printStackTrace();
        }
        System.out.println("[Action Manager] Loaded " + count + " action handlers");
    }
    
    public void add(ActionHandler handler) {
        handlers.put(handler.getClass(), handler);
    }
    
    public static <A> A get(Class<A> class_) {
        return (A) handlers.get(class_);
    }

}