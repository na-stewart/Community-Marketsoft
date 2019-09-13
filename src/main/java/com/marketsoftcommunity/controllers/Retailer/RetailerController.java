package main.java.com.marketsoftcommunity.controllers.Retailer;

import com.amazonaws.services.mediastoredata.model.ItemType;
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
import main.java.com.marketsoftcommunity.consumers.GeneralDTO;
import main.java.com.marketsoftcommunity.consumers.ItemDTO;
import main.java.com.marketsoftcommunity.gui.GUI;
import main.java.com.marketsoftcommunity.gui.GUIManager;
import main.java.com.marketsoftcommunity.gui.InitGUI;
import main.java.com.marketsoftcommunity.model.Customer;
import main.java.com.marketsoftcommunity.model.Item;
import main.java.com.marketsoftcommunity.model.ItemCategory;
import main.java.com.marketsoftcommunity.model.exceptions.InsufficientBalanceException;
import main.java.com.marketsoftcommunity.model.exceptions.QuantityException;
import main.java.com.marketsoftcommunity.model.logging.PurchasesActivity;
import main.java.com.marketsoftcommunity.util.AuthUtil;
import main.java.com.marketsoftcommunity.util.DatabaseUtil;
import main.java.com.marketsoftcommunity.util.LoggingUtil;
import main.java.com.marketsoftcommunity.util.Util;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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
        } catch (IOException e) {
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
        try {
            subtractBalance();
            subtractQuantities();
            logPurchases();
        }  catch (InsufficientBalanceException e){
            Util.displayAlert("Checkout failed! Customer has insufficient balance to complete transaction", Alert.AlertType.ERROR);
        } catch (QuantityException e){
            Util.displayAlert("Checkout failed! Item is out of stock!", Alert.AlertType.ERROR);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        clear();
    }

    private void subtractBalance() throws InsufficientBalanceException, QuantityException, IOException {
        Customer syncedCustomer = customerDAO.get(customerListView.getSelectionModel().getSelectedItem().getId());
        BigDecimal sum = BigDecimal.ZERO;
        for (Item item : itemsSelectedListView.getItems()) {
            if (item.getQuantity() <= 0)
                throw new QuantityException();
            sum = sum.add(item.getPrice());
        }
        if (sum.compareTo(syncedCustomer.getBalance()) <= 0)
            syncedCustomer.setBalance(syncedCustomer.getBalance().subtract(sum));
        else
            throw new InsufficientBalanceException();
        customerDAO.update(syncedCustomer);
    }


    private void subtractQuantities() throws IOException {
        Map<Item, Long> counts = itemsSelectedListView.getItems().stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        for (Item item : counts.keySet()) {
            item.setQuantity((int) (item.getQuantity() - counts.get(item)));
            itemDTO.update(item);
        }
    }

    private void logPurchases() throws Exception {
        Customer customer = customerListView.getSelectionModel().getSelectedItem();
        for (Item item : itemsSelectedListView.getItems())
            purchasesActivity.add(new PurchasesActivity(item, customer));
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
    }

    @FXML
    private void setFullScreen(){
        GUI thisGUI = GUIManager.getInstance().getByName("RetGUI");
        thisGUI.getStage().setFullScreen(!thisGUI.getStage().isFullScreen());
    }

    private void setChoiceBox() {
        try {
            for (ItemCategory itemType : itemDTO.getAllCategories())
                itemTypeChoiceBox.getItems().add(itemType);
        } catch (IOException e) {
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
        catch (IOException e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
        return null;
    }

    @Override
    public void exit() {
        DatabaseUtil.DATA_SOURCE.close();
    }

    private void setCellFactories() {
        itemsSelectedListView.setCellFactory(selectedItemsListViewCallback());
        customerListView.setCellFactory(customersListViewCallback());
        camperColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));

    }
}
