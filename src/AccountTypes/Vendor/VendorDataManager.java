package AccountTypes.Vendor;

import AccountTypes.Vendor.Nodes.ItemVBox;
import Data.Users.Camper;
import Data.DataBaseManager;
import Data.DataObjectBuilder;
import Data.DataViewer;
import Data.Item.Item;
import Interfaces.MultiReceive;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
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
import java.util.ArrayList;
import java.util.List;

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
        TableView<Camper> tableView = (TableView) dataViewer.getNode();
        unfilteredCamperData.clear();
        while (resultSet.next())
            unfilteredCamperData.add((Camper) objBuilder.getData(dataViewer.getQuery()));
        tableView.setItems(unfilteredCamperData);
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
        int total = 0;
        StringBuilder stringBuilder = new StringBuilder("Are you sure you want to checkout?\n");
        stringBuilder.append("=========================================");
        for (Item items : selectedItems) {
            stringBuilder.append(items.getName() + " : " + items.getPrice() + "\n");
            total += items.getPrice();
        }
        int remainingBalance = selectedCamper.getBalance() - total;
        stringBuilder.append("Your total is " + total + " and camper remaining balance would be " + remainingBalance);
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
        List<String> itemsNotCheckedOut = new ArrayList<>();
        boolean checkoutWarning = false;
        for (Item items : selectedItems) {
            if (items.getPrice() <= selectedCamper.getBalance() && items.getQuantity() > 0) {
                items.setQuantity(items.getQuantity() - 1);
                selectedCamper.setBalanceWithString(selectedCamper.getBalance() - items.getPrice());
            } else{
                itemsNotCheckedOut.add(items.getName());
                checkoutWarning = true;
            }
        }
        if (checkoutWarning)
            displayCheckoutFailureDialog(itemsNotCheckedOut);
        selectedItems.clear();
    }

    private void displayCheckoutFailureDialog(List<String> itemsNotCheckedOut){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Checkout Failure");
        alert.setHeaderText("Checkout Failure!");
        alert.setContentText("The following items did not checkout due to the campers insufficient balance " +
                "or the item is out of stock! \n" + itemsNotCheckedOut);
        alert.showAndWait();
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
