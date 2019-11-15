package org.nemotech.rsc.model.player;


import java.util.*;

public class Bank {
    
    public static final int MAX_SIZE = 192;
    
    private ArrayList<InvItem> list = new ArrayList<>();
    
    public Bank() { }
    
    public ArrayList<InvItem> getItems() {
        return list;
    }
    
    public int add(InvItem item) {
        if(item.getAmount() <= 0) {
            return -1;
        }
        for(int index = 0;index < list.size();index++) {
            if(item.equals(list.get(index))) {
                list.get(index).setAmount(list.get(index).getAmount() + item.getAmount());
                return index;
            }
        }
        list.add(item);
        return list.size() - 2;
    }
    
    public int remove(int id, int amount) {
        Iterator<InvItem> iterator = list.iterator();
        for(int index = 0;iterator.hasNext();index++) {
            InvItem i = iterator.next();
            if(id == i.getID()) {
                if(amount < i.getAmount()) {
                    i.setAmount(i.getAmount() - amount);
                }
                else {
                    iterator.remove();
                }
                return index;
            }
        }
        return -1;
    }
    
    public int remove(InvItem item) {
        return remove(item.getID(), item.getAmount());
    }
    
    public void remove(int index) {
        InvItem item = get(index);
        if(item == null) {
            return;
        }
        remove(item.getID(), item.getAmount());
    }
    
    public ListIterator<InvItem> iterator() {
        return list.listIterator();
    }
    
    public int getFirstIndexById(int id) {
        for(int index = 0;index < list.size();index++) {
            if(list.get(index).getID() == id) {
                return index;
            }
        }
        return -1;
    }
    
    public int countId(int id) {
        for(InvItem i : list) {
            if(i.getID() == id) {
                return i.getAmount();
            }
        }
        return 0;
    }
    
    public boolean full() {
        return list.size() >= MAX_SIZE;
    }
    
    public boolean contains(InvItem i) {
        return list.contains(i);
    }
    
    public boolean contains(int id) {
        return contains(new InvItem(id));
    }
    
    public InvItem get(InvItem item) {
            for(InvItem i : list) {
                if(item.equals(i)) {
                    return i;
                }
            }
            return null;
    }
    
    public InvItem get(int index) {
        if(index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }

    public int size() {
        return list.size();
    }
    
    public int getRequiredSlots(List<InvItem> items) {
        int requiredSlots = 0;
        for(InvItem item : items) {
            if(list.contains(item)) {
                continue;
            }
            requiredSlots++;
        }
        return requiredSlots;
    }
    
    public int getRequiredSlots(InvItem item) {
        return (list.contains(item) ? 0 : 1);
    }
    
    public boolean canHold(InvItem item) {
        return (MAX_SIZE - list.size()) >= getRequiredSlots(item);
    }
    
    public boolean canHold(ArrayList<InvItem> items) {
        return (MAX_SIZE - list.size()) >= getRequiredSlots(items);
    }
    
    public void swap(int item1, int slot1, int item2, int slot2) {
        if((slot1 > -1) && (slot2 > -1) && (item1 > -1) && (item2 > -1) && (slot1 != slot2) && (item1 != item2)) {
            InvItem bankItem1 = new InvItem(item2, countId(item2));
            InvItem bankItem2 = new InvItem(item1, countId(item1));
            list.set(slot1, bankItem1);
            list.set(slot2, bankItem2);
        }
    }

}