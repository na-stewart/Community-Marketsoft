package AccountTypes.Vendor;

import AccountTypes.Vendor.Nodes.ItemVBox;
import Data.Customers.Camper;
import Data.DataBaseManager;
import Data.DataObjectBuilder;
import Data.DataViewer;
import Data.Item.Item;
import Interfaces.MultiReceive;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import org.controlsfx.dialog.ExceptionDialog;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class VendorDataManager implements MultiReceive {
    private DataBaseManager databaseManager = new DataBaseManager();
    private ObservableList<Item> selectedItems = FXCollections.observableArrayList();
    private ObservableList<Camper> unfilteredCamperData = FXCollections.observableArrayList();
    private Camper selectedCamper;
    private int total;

    @Override
    public void retrieveDatabaseData(DataViewer dataViewer) throws SQLException {
        ResultSet resultSet = databaseManager.receiver(dataViewer.getQuery());
        DataObjectBuilder objBuilder = new DataObjectBuilder(resultSet);
        if (dataViewer.getQuery().contains("item"))
            populateItems(dataViewer, resultSet, objBuilder);
         else {
            populateCampers(dataViewer, resultSet, objBuilder);
        }
    }

    private void populateItems(DataViewer dataViewer, ResultSet resultSet, DataObjectBuilder objBuilder) throws SQLException {
        TilePane tilePane = (TilePane) dataViewer.getNode();
        tilePane.getChildren().clear();
        while (resultSet.next())
            tilePane.getChildren().add(vBoxDbBuilder((Item) objBuilder.getData(dataViewer.getQuery())));
    }

    private void populateCampers(DataViewer dataViewer, ResultSet resultSet, DataObjectBuilder objBuilder) throws SQLException {
        ListView listView = (ListView) dataViewer.getNode();
        listView.getItems().clear();
        while (resultSet.next())
            listView.getItems().add(objBuilder.getData(dataViewer.getQuery()));
        unfilteredCamperData.setAll(listView.getItems());

    }

    private ItemVBox vBoxDbBuilder(Item item) {
        ItemVBox vBox = new ItemVBox(item);
        addChildren(vBox, item);
        vBox.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (selectedCamper != null)
                selectedItems.add(vBox.getItem());
        });
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setStyle("-fx-background-color: white");
        vBox.setSpacing(8);
        return vBox;
    }

    private void addChildren(ItemVBox vBox, Item item){
        vBox.getChildren().add(vBoxTextBuilder("Id: " + String.valueOf(item.getId())));
        vBox.getChildren().add(vBoxTextBuilder("Price: " + String.valueOf(item.getPrice())));
        vBox.getChildren().add(vBoxTextBuilder("Quantity: " + String.valueOf(item.getQuantity())));
        vBox.getChildren().add(vBoxTextBuilder(item.getName()));
        vBox.getChildren().add(imageView(item.getImageURL()));
    }


    private Text vBoxTextBuilder(String data){
        Text text = new Text(data);
        text.setStyle("-fx-font: 25 arial;");
        return text;
    }

    private ImageView imageView(String url) {
        ImageView imageView = new ImageView();
        new Thread(() -> imageView.setImage(SwingFXUtils.toFXImage(getImage(url), null))).start();
        imageView.setFitWidth(200);
        imageView.setFitHeight(220);
        return imageView;
    }

    private BufferedImage getImage(String url) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new URL(url));
        } catch (Exception ex){
            try {
                String path = "/AccountTypes/Vendor/VendorGUI/Resources/MTC Tree Poster.jpg";
                image = ImageIO.read(getClass().getResourceAsStream(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    public String getCheckoutText(){
        StringBuilder stringBuilder = new StringBuilder("Are you sure you want to checkout?\n");
        stringBuilder.append("=========================================");
        for (Item items : selectedItems) {
            stringBuilder.append(items.getName() + " : " + items.getPrice() + "\n");
            total += items.getPrice();
        }
        stringBuilder.append("Your total is " + total + " and camper balance is " + selectedCamper.getBalance());
        return String.valueOf(stringBuilder);
    }

    public void tryToCheckOut(){
        try {
            checkout();
        } catch (Exception e) {
            new ExceptionDialog(e).showAndWait();
        }
    }

    private void checkout() throws Exception {
        if (total <= selectedCamper.getBalance()) {
            for (Item items : selectedItems) {
                if (items.getQuantity() > 0)
                    items.setQuantity(items.getQuantity() - 1);
                else
                    throw new Exception("One or more of selected items are or will be out of stock! please try again!");
            }
            selectedCamper.setBalance(selectedCamper.getBalance() - total);
            total = 0;
        }
    }

    public ObservableList<Item> getSelectedItems() {
        return selectedItems;
    }

    public ObservableList<Camper> getUnfilteredCamperData() {
        return unfilteredCamperData;
    }

    public void setSelectedCamper(Camper selectedCamper) {
        this.selectedCamper = selectedCamper;
    }
}
