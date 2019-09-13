package main.java.com.marketsoftcommunity.consumers;

import com.google.gson.Gson;
import main.java.com.marketsoftcommunity.model.Item;
import main.java.com.marketsoftcommunity.model.ItemCategory;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class ItemDTO extends GeneralDTO<Item>{
    private ApiConsumer<ItemCategory> consumer = new ApiConsumer<>();

    public ItemDTO() {
        super("item", Item.class);
    }

    public List<ItemCategory> getAllCategories() throws IOException{
        return consumer.getAll("item/categories", ItemCategory.class);
    }

    public void addCategory(ItemCategory itemCategory) throws Exception {
        consumer.post(Collections.singletonList(new BasicNameValuePair("category", new Gson().toJson(itemCategory))), "item/itemcategory");
    }

    public void deleteCategory(int id) throws Exception {
        consumer.delete("/item/itemcategory?id=" +  id);
    }
}
