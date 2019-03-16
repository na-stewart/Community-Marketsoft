package App.Vendor.Nodes;

import Data.Item.Item;
import javafx.scene.layout.VBox;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class ItemVBox extends VBox {
    private Item item;

    public ItemVBox(Item item) {
        super();
        this.item = item;
    }

    public Item getItem() {
        return item;
    }


}
