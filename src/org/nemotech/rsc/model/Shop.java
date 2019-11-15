package org.nemotech.rsc.model;

import org.nemotech.rsc.model.player.InvItem;
import java.util.ArrayList;
import java.util.Iterator;

import org.nemotech.rsc.client.action.ActionManager;
import org.nemotech.rsc.client.action.impl.ShopHandler;
import org.nemotech.rsc.client.mudclient;

public final class Shop {
    
    private final String name;
    
    private final boolean general;

    private final int type, respawnRate, buyModifier, sellModifier;

    private final InvItem[] items;

    private final ArrayList<InvItem> shopItems = new ArrayList<>();

    public Shop(String name, boolean general, int type, int respawnRate, int buyModifier, int sellModifier, InvItem... items) {
        this.name = name;
        this.general = general;
        this.type = type;
        this.respawnRate = respawnRate;
        this.buyModifier = buyModifier;
        this.sellModifier = sellModifier;
        this.items = items;

        for (InvItem item : items) {
            shopItems.add(new InvItem(item.getID(), item.getAmount()));
        }
    }
    
    public Shop(Shop copy) {
        this.name = copy.name;
        this.general = copy.general;
        this.type = copy.type;
        this.respawnRate = copy.respawnRate;
        this.buyModifier = copy.buyModifier;
        this.sellModifier = copy.sellModifier;
        this.items = copy.items;
        
        for (InvItem item : items) {
            shopItems.add(new InvItem(item.getID(), item.getAmount()));
        }
    }

    public void restock() {
        synchronized (shopItems) {
            boolean updatePlayers = false;

            for (int i = 0; i < shopItems.size(); i++) {
                InvItem shopItem = shopItems.get(i);
                int amount = shopItem.getAmount();
                int delimitor = i - (items.length - 1); //check if the item if custom, or original shop item

                if (delimitor <= 0) { //its an original item
                    if (amount < items[i].getAmount()) { //add item
                        shopItem.setAmount(++amount);
                        updatePlayers = true;
                    } else if (amount > items[i].getAmount()) {
                        shopItem.setAmount(--amount);
                        updatePlayers = true;
                    }
                } else { //its custom
                    shopItem.setAmount(--amount);

                    if (amount <= 0) {
                        shopItems.remove(i);
                    }
                    updatePlayers = true;
                }
            }
            if (updatePlayers && mudclient.getInstance().showDialogShop) {
                ActionManager.get(ShopHandler.class).handleShopOpen(this);
            }
        }
    }

    public void addShopItem(InvItem item) {
        boolean has = false;
        synchronized (shopItems) {
            for (InvItem i : shopItems) {
                if (i.getID() == item.getID()) {
                    i.setAmount(i.getAmount() + item.getAmount());
                    has = true;
                    break;
                }
            }

            if (!has) {
                shopItems.add(item);
            }
            ActionManager.get(ShopHandler.class).handleShopOpen(this); // updates
        }
    }

    public void removeShopItem(InvItem item) {
        synchronized (shopItems) {
            Iterator<InvItem> shopItem = shopItems.iterator();
            while(shopItem.hasNext()) {
                InvItem i = shopItem.next();
                if (i.getID() == item.getID()) {
                    if (i.getAmount() - item.getAmount() <= 0) {
                        boolean original = false;
                        synchronized (items) {
                            for (InvItem i2 : items) {
                                if (i.getID() == i2.getID()) {
                                    original = true;
                                    break;
                                }
                            }
                        }
                        if (!original) {
                            shopItem.remove();
                        } else {
                            i.setAmount(0);
                        }
                    } else {
                        i.setAmount(i.getAmount() - 1);
                    }
                }
            }
            ActionManager.get(ShopHandler.class).handleShopOpen(this); // updates the screen
        }
    }

    public boolean canHoldItem(InvItem item) {
        return (40 - shopItems.size()) >= (shopItems.contains(item) ? 0 : 1);
    }

    public int getItemBuyPrice(int id) {
        synchronized (shopItems) {
            for (int i = 0; i < shopItems.size(); i++) {
                if (shopItems.get(i).getID() == id) {
                    return getItemPrice(i, true);
                }
            }
        }
        return 0;
    }

    public int getItemSellPrice(int id) {
        synchronized (shopItems) {
            for (int i = 0; i < shopItems.size(); i++) {
                if (shopItems.get(i).getID() == id) {
                    return getItemPrice(i, false);
                }
            }
        }
        return 0;
    }

    public int getGenericPrice(InvItem item, boolean buy) {
        int amount = item.getAmount();
        int basePrice = item.getDef().getPrice();

        if (basePrice == 0) {
            return 0;
        }

        int equilibrium = 0;

        if (shouldStock(item.getID())) {
            for (InvItem item1 : items) {
                if (item1.getID() == item.getID()) {
                    equilibrium = item1.getAmount();
                }
            }
        } else {
            equilibrium = -2;
        }

        double percent = (equilibrium > 100 ? 0.01 : 0.05);

        int minItemValue = basePrice / 4;
        int maxItemValue = basePrice * 2;

        int price;

        if (buy) {
            price = basePrice;
            if (amount < equilibrium) {
                price += Math.round(price * (percent * (equilibrium - amount)));
            }
        } else {
            price = basePrice / 2;
            if (amount > equilibrium) {
                price -= Math.round(price * (percent * (amount - equilibrium)));
            }
        }

        if (price > maxItemValue) {
            price = maxItemValue; 
        } else if (price < minItemValue) {
            price = minItemValue; 
        }

        if (price == 0) {
            return buy ? 1 : 0;
        }
        return price;
    }

    public int getItemPrice(int index, boolean buy) {
        if (index >= shopItems.size() || index < 0) {
            return 0;
        }
        InvItem item = shopItems.get(index);

        int amount = item.getAmount();
        int basePrice = item.getDef().getPrice();

        if (basePrice == 0) {
            return 0;
        }

        int equilibrium = 0;

        if (shouldStock(item.getID())) {
            for (InvItem item1 : items) {
                if (item1.getID() == item.getID()) {
                    equilibrium = item1.getAmount();
                }
            }
        } else {
            equilibrium = -2;
        }

        double percent = (equilibrium > 100 ? 0.01 : 0.05);

        int minItemValue = basePrice / 4;
        int maxItemValue = basePrice * 2;

        int price;

        if (buy) {
            price = basePrice;
            if (amount < equilibrium) {
                price += Math.round(price * (percent * (equilibrium - amount)));
            } 
        } else {
            price = basePrice / 2;
            if (amount > equilibrium) {
                price -= Math.round(price * (percent * (amount - equilibrium)));
            } 
        }

        if (price > maxItemValue) {
            price = maxItemValue; 
        } else if (price < minItemValue) {
            price = minItemValue; 
        }

        if (price == 0) {
            return buy ? 1 : 0;
        }
        return price;
    }

    public boolean shouldStock(int id) {
        if (general) {
            return true;
        }
        if (type == 1) {
            return true;
        }
        if (type == 2) {
            return false;
        }
        for (InvItem item : items) {
            if (item.getID() == id) {
                return true;
            }
        }
        return false;
    }

    public InvItem getShopItem(int index) {
        return shopItems.get(index);
    }

    public int getItemCount(int id) {
        int count = 0;

        synchronized (shopItems) {
            for (InvItem item : shopItems) {
                if (item.getID() == id) {
                    count += item.getAmount();
                }
            }
        }

        return count;
    }
    
    public String getName() {
        return name;
    }
    
    public int getType() {
        return type;
    }

    public int getShopSize() {
        return shopItems.size();
    }

    public int getRespawnRate() {
        return respawnRate;
    }

    public int getBuyModifier() {
        return buyModifier;
    }

    public int getSellModifier() {
        return sellModifier;
    }
    
    public boolean isGeneral() {
        return general;
    }

}