package Data.Item;

import Data.Item.ItemType.ItemTypes;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class Item {
    private String name;
    private int price;
    private ItemTypes itemType;
    private Enum itemSubType;

    public Item(String name, int price, ItemTypes itemType, Enum itemSubType) {
        this.name = name;
        this.price = price;
        this.itemType = itemType;
        this.itemSubType = itemSubType;

    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public ItemTypes getItemType() {
        return itemType;
    }

    public Enum getItemSubType() {
        return itemSubType;
    }
}
