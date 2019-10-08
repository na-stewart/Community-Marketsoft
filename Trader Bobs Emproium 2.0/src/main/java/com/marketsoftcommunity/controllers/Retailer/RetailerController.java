package main.java.com.marketsoftcommunity.controllers.Retailer;

import com.google.gson.Gson;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import main.java.com.marketsoftcommunity.consumers.ApiConsumer;
import main.java.com.marketsoftcommunity.consumers.GeneralDTO;
import main.java.com.marketsoftcommunity.consumers.ItemDTO;
import main.java.com.marketsoftcommunity.gui.GUI;
import main.java.com.marketsoftcommunity.gui.InitGUI;
import main.java.com.marketsoftcommunity.model.Customer;
import main.java.com.marketsoftcommunity.model.Item;
import main.java.com.marketsoftcommunity.model.ItemCategory;

import main.java.com.marketsoftcommunity.model.exceptions.ApiException;
import main.java.com.marketsoftcommunity.model.logging.PurchasesActivity;
import main.java.com.marketsoftcommunity.util.AuthUtil;
import main.java.com.marketsoftcommunity.util.Util;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class RetailerController implements InitGUI {
    private ItemDTO itemDTO= new ItemDTO();
    private GeneralDTO<Customer> customerDAO = new GeneralDTO<>("customer", Customer.class);
    private GeneralDTO<PurchasesActivity> purchasesActivity = new GeneralDTO<>("purchasesactivity", PurchasesActivity.class);
    @FXML
    private TabPane filterTabPane;
    @FXML
    private ListView<Customer> customerListView;
    @FXML
    private TableView<PurchasesActivity> customerPurchasesTableView;
    @FXML
    private TableColumn<PurchasesActivity, String> camperColumn, itemColumn, itemTypeColumn;
    @FXML
    private Text remainingBalanceText, dailyLimitText;
    @FXML
    private TextField filterTextField;
    @FXML
    private ListView<Item> itemsSelectedListView;
    @FXML
    private ScrollPane itemScrollPane;
    @FXML
    private TilePane itemTilePane;
    @FXML
    private ChoiceBox<ItemCategory> itemTypeChoiceBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellFactories();
        setChoiceBox();
        populateAll();
        itemTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> populateItemTilePane());
        itemTilePane.prefWidthProperty().bind(itemScrollPane.widthProperty());
        filterTextField.textProperty().addListener(customerFilter);
        customerPurchasesTableView.setSelectionModel(null);
    }

    private void populateAll() {
        customerListView.setItems(getUnfilteredCustomerData());
        populateItemTilePane();
    }

    private void populateItemTilePane(){
        itemTilePane.getChildren().clear();
        try {
            for (Item item : itemDTO.getAll("WHERE itemType = ?", itemTypeChoiceBox.getValue().getName())) {
                ItemVBox itemVbox = new ItemVBox(item);
                itemTilePane.getChildren().add(itemVbox);
                addListener(itemVbox);
            }
        } catch (IOException | ApiException e) {
            e.printStackTrace();
        }
        setVBoxImage();
    }



    private void setVBoxImage() {
        new Thread(() -> {
            for (int i = 0; i < itemTilePane.getChildren().size(); i++) {
                ItemVBox itemVBox = (ItemVBox) itemTilePane.getChildren().get(i);
                itemVBox.setImage();
            }
        }).start();
    }

    private void addListener(ItemVBox itemVBox) {
        itemVBox.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (customerListView.getSelectionModel().getSelectedItem() != null)
                itemsSelectedListView.getItems().add(itemVBox.getItem());
        });
    }


    private Callback<ListView<Item>, ListCell<Item>> selectedItemsListViewCallback() {
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

    private Callback<ListView<Customer>, ListCell<Customer>> customersListViewCallback() {
        return new Callback<ListView<Customer>, ListCell<Customer>>() {
            @Override
            public ListCell<Customer> call(ListView<Customer> p) {
                return new ListCell<Customer>() {
                    @Override
                    protected void updateItem(Customer i, boolean bln) {
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
    private void filerKeyEvents(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            customerListView.getSelectionModel().selectFirst();
            filterTabPane.getSelectionModel().select(1);

        }
        if (keyEvent.getCode() == KeyCode.DOWN) {
            customerListView.requestFocus();
            customerListView.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void displaySelectedItemOnFilterListView(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.ENTER)
            filterTabPane.getSelectionModel().select(1);
    }

    @FXML
    private void displayCustomerPurchases() {
        try {
            Customer customer = customerDAO.get(customerListView.getSelectionModel().getSelectedItem().getId());
            if (customer == null) {
                remainingBalanceText.setText("");
                dailyLimitText.setText("");
            } else {
                if (!customerPurchasesTableView.getItems().isEmpty())
                    customerPurchasesTableView.getItems().clear();
                remainingBalanceText.setText(customer.getBalanceString());
                dailyLimitText.setText("Daily limit: " + String.valueOf(customer.getDailyLimit()));
                customerPurchasesTableView.getItems().setAll(purchasesActivity.getAll("WHERE date = ? AND customerName = ?",
                        new String[]{Util.date(false), customer.getName()}));
            }
        } catch (Exception ignored) { }
    }

    @FXML
    private void checkout() {
        Util.displayAlert("Are you sure you want to checkout?", Alert.AlertType.CONFIRMATION);
        Gson gson = new Gson();
        Customer selectedCustomer = customerListView.getSelectionModel().getSelectedItem();
        List<Item> itemList = FXCollections.observableArrayList(itemsSelectedListView.getItems());
        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("customer", gson.toJson(selectedCustomer, Customer.class)));
        basicNameValuePairs.add(new BasicNameValuePair("itemList", gson.toJson(itemList)));
        try {
            new ApiConsumer<>().post(basicNameValuePairs, "retailer/checkout");
            clear();
        }catch (Exception e){
            e.printStackTrace();
            Util.displayAlert("Checkout failed! Customer either out of balance or item is out of quantity!", Alert.AlertType.ERROR);
        }
    }


    private void clear(){
        filterTextField.clear();
        itemsSelectedListView.getItems().clear();
        filterTabPane.getSelectionModel().select(0);
    }


    @FXML
    private void delete(){
        itemsSelectedListView.getItems().remove(itemsSelectedListView.getSelectionModel().getSelectedIndex());
    }


    @FXML
    private void logout(){
        AuthUtil.LOGOUT();
//TODO
    }


    @FXML
    private void setFullScreen(){
       //TODO
    }

    private void setChoiceBox() {
        ItemCategory itemCategory = new ItemCategory("Misc");
        itemTypeChoiceBox.getItems().add(itemCategory);
        itemTypeChoiceBox.setValue(itemCategory);
        try {
            for (ItemCategory itemType : itemDTO.getAllCategories())
                itemTypeChoiceBox.getItems().add(itemType);
        } catch (IOException | ApiException e) {
            e.printStackTrace();
        }
    }

    private ChangeListener<String> customerFilter = (observable, oldValue, newValue) -> {
        FilteredList<Customer> filteredData = new FilteredList<>(Objects.requireNonNull(getUnfilteredCustomerData()), c -> true);
        filteredData.setPredicate(customer -> {
            String lowerCaseFilter = newValue.toLowerCase();
            return !newValue.isEmpty() &&
                    customer.getName().toLowerCase().startsWith(lowerCaseFilter);
        });
        customerListView.setItems(filteredData);
    };

    private ObservableList<Customer> getUnfilteredCustomerData() {
        try {
            return FXCollections.observableArrayList(customerDAO.getAll());
        }
        catch (IOException | ApiException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public void exit() {
        AuthUtil.LOGOUT();
    }

    private void setCellFactories() {
        itemsSelectedListView.setCellFactory(selectedItemsListViewCallback());
        customerListView.setCellFactory(customersListViewCallback());
        camperColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));

    }
}
