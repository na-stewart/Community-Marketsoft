package Data.Item;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public enum ItemType {
    CONSUMABLE, MERCH, TOILETRIES;

    public static ItemType intToItemType(int itemTypeInt){
        switch (itemTypeInt){
            case 0:
                return CONSUMABLE;
            case 1:
                return MERCH;
            case 2:
                return TOILETRIES;
        }
        return null;
    }

    public static int itemTypeToInt(ItemType itemType){
        switch (itemType) {
            case CONSUMABLE:
                return 0;
            case MERCH:
                return 1;
            case TOILETRIES:
                return 2;
        }
        return 0;
    }
}

