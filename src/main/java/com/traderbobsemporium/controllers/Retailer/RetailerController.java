package main.java.com.traderbobsemporium.controllers.Retailer;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.util.Callback;
import main.java.com.traderbobsemporium.dao.CamperDAO;
import main.java.com.traderbobsemporium.dao.ItemDAO;
import main.java.com.traderbobsemporium.dao.Loggers.PurchasesActivityLogger;
import main.java.com.traderbobsemporium.gui.GUI;
import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.model.Camper;
import main.java.com.traderbobsemporium.model.Item;
import main.java.com.traderbobsemporium.model.ItemType;
import main.java.com.traderbobsemporium.model.Logging.PurchasesActivity;
import main.java.com.traderbobsemporium.util.AuthUtil;
import main.java.com.traderbobsemporium.util.Util;
import org.controlsfx.dialog.ExceptionDialog;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class RetailerController implements Initializable {
    private ItemDAO itemDAO = new ItemDAO();
    private CamperDAO camperDAO = new CamperDAO();


    @FXML
    private TextField filterTextField;
    @FXML
    private TableView<Camper> camperTableView;
    @FXML
    private TableColumn<Camper, String> camperIdColumn, camperNameColumn, camperBalanceColumn;
    @FXML
    private ListView<Item> itemsSelectedListView;
    @FXML
    private ScrollPane itemScrollPane;
    @FXML
    private TilePane itemTilePane;
    @FXML
    private ChoiceBox<ItemType> itemTypeChoiceBox;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellFactory();
        setChoiceBox();
        tryToPopulateTilePane();
        itemTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> tryToPopulateTilePane());
        itemTilePane.prefWidthProperty().bind(itemScrollPane.widthProperty());
        camperTableView.setItems(getUnfilteredCamperData());
        filterTextField.textProperty().addListener(camperTableViewFilter);

    }


    private void tryToPopulateTilePane() {
        try {
            itemTilePane.getChildren().clear();
            for (Item item : itemDAO.getAll()) {
                if (item.getItemType() == itemTypeChoiceBox.getValue()) {
                    ItemVBox itemVBox = new ItemVBox(item);
                    itemTilePane.getChildren().add(itemVBox);
                    itemVBox.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                        if (camperTableView.getSelectionModel().getSelectedItem() != null)
                            itemsSelectedListView.getItems().add(itemVBox.getItem());
                    });
                }
            }
        } catch (SQLException e) {
            new ExceptionDialog(e).showAndWait();
        }
    }

    private Callback<ListView<Item>, ListCell<Item>> selectedItemsTableViewCallback(){
        return new Callback<ListView<Item>, ListCell<Item>>() {
            @Override
            public ListCell<Item> call(ListView<Item> p) {
                return new ListCell<Item>() {
                    @Override
                    protected void updateItem(Item i, boolean bln) {
                        super.updateItem(i, bln);
                        if (i != null)
                            setText(i.getName());
                        else
                            setText("");
                    }
                };
            }
        };
    }

    @FXML
    private void checkout(){
        Camper selectedCamper = camperTableView.getSelectionModel().getSelectedItem();
        List<Item> selectedItems = itemsSelectedListView.getItems();
        List<String> itemsNotCheckedOut = new ArrayList<>();
        boolean checkoutWarning = false;
        Util.displayAlert(getCheckoutText(), Alert.AlertType.CONFIRMATION);
        for (Item item : selectedItems) {
            if (item.getPrice().doubleValue() <= selectedCamper.getBalance().doubleValue() && item.getQuantity() > 0) {
                item.setQuantity(item.getQuantity() - 1);
                selectedCamper.setBalance(selectedCamper.getBalance().subtract(item.getPrice()));
                itemDAO.update(item);
            } else{
                itemsNotCheckedOut.add(item.getName());
                checkoutWarning = true;
            }
        }
        if (checkoutWarning)
            Util.displayAlert("The following items did not checkout due to the campers insufficient balance " +
                            "or the item is out of stock!\n" + itemsNotCheckedOut, Alert.AlertType.WARNING);
        camperDAO.update(selectedCamper);
        camperTableView.setItems(getUnfilteredCamperData());
        logPurchasedItems(selectedCamper, selectedItems);
        tryToPopulateTilePane();
        selectedItems.clear();
    }

    @FXML
    private void delete(){
        itemsSelectedListView.getItems().remove(itemsSelectedListView.getSelectionModel().getSelectedIndex());
    }

    private void logPurchasedItems(Camper camper, List<Item> selectedItems){
        for (Item item : selectedItems){
            new PurchasesActivityLogger().add(new PurchasesActivity(item, camper));
        }
    }

    private String getCheckoutText(){
        BigDecimal total = new BigDecimal(0);
        StringBuilder stringBuilder = new StringBuilder("Are you sure you want to checkout?\n");
        stringBuilder.append("=========================================");
        for (Item items : itemsSelectedListView.getItems()) {
            stringBuilder.append(items.getName()).append(" : ").append(items.getPrice()).append("\n");
            total = total.add(items.getPrice());
        }
        BigDecimal remainingBalance = camperTableView.getSelectionModel().getSelectedItem().getBalance().subtract(total);
        stringBuilder.append("Your total is ").append(total)
                .append(" and camper remaining balance would be $").append(remainingBalance);
        return String.valueOf(stringBuilder);
    }

    @FXML
    private void logout(){
        AuthUtil.LOGOUT();
    }

    @FXML
    private void setFullScreen(){
        GUI thisGUI = GUIManager.getInstance().getByName("RetGUI");
        thisGUI.getStage().setFullScreen(!thisGUI.getStage().isFullScreen());
    }

    private void setChoiceBox() {
        for (ItemType itemType : ItemType.values())
            itemTypeChoiceBox.getItems().add(itemType);
        itemTypeChoiceBox.setValue(ItemType.CHIPS);

    }

    private void setCellFactory(){
        itemsSelectedListView.setCellFactory(selectedItemsTableViewCallback());
        camperIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        camperNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        camperBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("balanceString"));
    }

    private ChangeListener<String> camperTableViewFilter = (observable, oldValue, newValue) -> {
        FilteredList<Camper> filteredData = new FilteredList<>(Objects.requireNonNull(getUnfilteredCamperData()), c -> true);
        filteredData.setPredicate(camper -> {
            String lowerCaseFilter = newValue.toLowerCase();
            return !newValue.isEmpty() &&
                    camper.getName().toLowerCase().startsWith(lowerCaseFilter) ||
                    String.valueOf(camper.getId()).startsWith(lowerCaseFilter);
        });
        camperTableView.setItems(filteredData);
    };

    private ObservableList<Camper> getUnfilteredCamperData() {
        try {
            return FXCollections.observableArrayList(camperDAO.getAll());
        } catch (SQLException e){
            new ExceptionDialog(e).showAndWait();
        }
        return null;
    }
}
