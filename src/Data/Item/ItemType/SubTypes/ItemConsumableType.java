package Data.Item.ItemType.SubTypes;

public enum ItemConsumableType{
    CANDY, DRINK, CHIPS, ICECREAM;

    public static int consumableTypeToInt(ItemConsumableType itemConsumableType){
        switch (itemConsumableType) {
            case CANDY:
                return 0;
            case DRINK:
                return 1;
            case CHIPS:
                return 2;
            case ICECREAM:
                return 3;
        }
        return 0;
    }

    public static ItemConsumableType intToConsumableType(int itemConsumableTypeInt){
        switch (itemConsumableTypeInt){
            case 0:
                return CANDY;
            case 1:
                return DRINK;
            case 2:
                return CHIPS;
            case 3:
                return ICECREAM;
        }
        return null;
    }

}
