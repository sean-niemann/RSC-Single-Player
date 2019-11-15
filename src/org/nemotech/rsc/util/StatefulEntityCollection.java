package org.nemotech.rsc.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.nemotech.rsc.model.Entity;

/**
 * This class is a collection which is backed by 3 separate Sets.
 *
 * These sets control the state of this collection.
 *
 * To update this collections current state,
 * you need to explicity call the update method.
 *
 * The purpose of this collection is to separate new values added to
 * this collection until the update method has been called. Removal
 * of entities will NOT take effect until the update method is called.
 * This is so we can see what is being removed (and in cases this is
 * required by the server) to handle them specially.
 * 
 */
public class StatefulEntityCollection<T extends Entity> {

    private Set<T> newEntities = new HashSet<>();
    private Set<T> knownEntities = new HashSet<>();
    private Set<T> entitiesToRemove = new HashSet<>();

    // We need to keep these in the order they logged in, currently it doesn't seem to?

    public void add(T entity) {
        newEntities.add(entity);
    }

    public void add(Collection<T> entities) {
        newEntities.addAll(entities);
    }

    public boolean contains(T entity) {
        return newEntities.contains(entity) || knownEntities.contains(entity);
    }
    
    public void remove(T entity) {
        entitiesToRemove.add(entity);
    }

    public boolean isRemoving(T entity) {
        return entitiesToRemove.contains(entity);
    }
    
    public void clear() {
        knownEntities.clear();
        newEntities.clear();
        entitiesToRemove.clear();
    }
    
    public void update() {
        knownEntities.removeAll(entitiesToRemove);
        knownEntities.addAll(newEntities);
        newEntities.clear();
        entitiesToRemove.clear();
    }
    
    public boolean changed() {
        return !entitiesToRemove.isEmpty() || !newEntities.isEmpty();
    }
    
    public Collection<T> getRemovingEntities() {
        return entitiesToRemove;
    }

    public Collection<T> getNewEntities() {
        return newEntities;
    }

    public Collection<T> getKnownEntities() {
        return knownEntities;
    }
    
    public Collection<T> getAllEntities() {
        Set<T> temp = new HashSet<>();
        temp.addAll(newEntities);
        temp.addAll(knownEntities);
        return temp;
    }
                                         
}