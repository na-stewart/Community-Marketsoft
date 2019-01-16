package Data.Item;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public enum ItemType {
    CHIPS, DRINK, CANDY, ICECREAM, MERCH, TOILETRIES;

    public static ItemType intToItemType(int itemTypeInt){
        switch (itemTypeInt){
            case 0:
                return CHIPS;
            case 1:
                return DRINK;
            case 2:
               return CANDY;
            case 3:
                return ICECREAM;
            case 4:
                return MERCH;
            case 5:
                return TOILETRIES;
        }
        return null;
    }

    public static int itemTypeToInt(ItemType itemType){
        switch (itemType) {
            case CHIPS:
                return 0;
            case DRINK:
                return 1;
            case CANDY:
                return 2;
            case ICECREAM:
                return 3;
            case MERCH:
                return 4;
            case TOILETRIES:
                return 5;
        }
        return 0;
    }
}

