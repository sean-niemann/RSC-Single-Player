package org.nemotech.rsc.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.nemotech.rsc.model.Entity;

public class EntityList<T extends Entity> implements Iterable<T> {
    
    public static final int DEFAULT_CAPACITY = 2000;
    
    protected Object[] entities;
    protected Set<Integer> indicies = new HashSet<>();
    protected int curIndex = 0;
    protected int capacity;

    public EntityList(int capacity) {
        entities = new Object[capacity];
        this.capacity = capacity;
    }

    public EntityList() {
        this(DEFAULT_CAPACITY);
    }

    public void remove(T entity) {
        entities[entity.getIndex()] = null;
        indicies.remove(entity.getIndex());
    }

    public T remove(int index) {
        Object temp = entities[index];
        entities[index] = null;
        indicies.remove(index);
        return (T) temp;
    }

    public T get(int index) {
        return (T) entities[index];
    }
    
    public void add(T entity) {
        if(entities[curIndex] != null) {
            increaseIndex();
            add(entity);
        }
        else {
            entities[curIndex] = entity;
            entity.setIndex(curIndex);
            indicies.add(curIndex);
            increaseIndex();
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new EntityListIterator<>(entities, indicies, this);
    }

    public void increaseIndex() {
        curIndex++;
        if (curIndex >= capacity) {
            curIndex = 0;
        }
    }

    public boolean contains(T entity) {
        return indexOf(entity) > -1;
    }

    public int indexOf(T entity) {
        for (int index : indicies) {
            if (entities[index].equals(entity)) {
                return index;
            }
        }
        return -1;
    }

    public int count() {
        return indicies.size();
    }
    
    public int size() {
        return indicies.size();
    }
    
    private class EntityListIterator<E extends Entity> implements Iterator<E> {
        
        private Integer[] indicies;
        private Object[] entities;
        private EntityList<E> entityList;
        private int curIndex = 0;

        public EntityListIterator(Object[] entities, Set<Integer> indicies, EntityList<E> entityList) {
            this.entities = entities;
            this.indicies = indicies.toArray(new Integer[0]);
            this.entityList = entityList;
        }

        @Override
        public boolean hasNext() {
            return indicies.length != curIndex;
        }

        @Override
        public E next() {
            Object temp = entities[indicies[curIndex]];
            curIndex++;
            return (E) temp;
        }

        @Override
        public void remove() {
            if (curIndex >= 1) {
                entityList.remove(indicies[curIndex - 1]);
            }
        }
        
    }
    
}