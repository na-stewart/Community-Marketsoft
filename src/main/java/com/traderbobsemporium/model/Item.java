package main.java.com.traderbobsemporium.model;

import java.net.URL;

public class Item extends Profile {
    private URL imageURL;
    private int price;

    public Item(long id, String name, URL imageURL, int price) {
        super(id, name);
        this.imageURL = imageURL;
        this.price = price;
    }

    public URL getImageURL() {
        return imageURL;
    }

    public void setImageURL(URL imageURL) {
        this.imageURL = imageURL;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
