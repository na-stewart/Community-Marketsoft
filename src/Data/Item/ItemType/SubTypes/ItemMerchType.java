package Data.Item.ItemType.SubTypes;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public enum ItemMerchType {
    HATS, SOCKS, SHIRTS, SWEATERS, MISC;

    public static int merchTypeToInt(ItemMerchType itemMerchType){
        switch (itemMerchType) {
            case HATS:
                return 0;
            case SOCKS:
                return 1;
            case SHIRTS:
                return 2;
            case SWEATERS:
                return 3;
            case MISC:
                return 4;
        }
        return 0;
    }

    public static ItemMerchType intToMerchType(int itemConsumableTypeInt){
        switch (itemConsumableTypeInt){
            case 0:
                return HATS;
            case 1:
                return SOCKS;
            case 2:
                return SHIRTS;
            case 3:
                return SWEATERS;
            case 4:
                return MISC;
        }
        return null;
    }

}
