package main.java.com.traderbobsemporium.controllers.Retailer;

import com.sun.javafx.image.BytePixelSetter;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import main.java.com.traderbobsemporium.dao.CustomerDAO;
import main.java.com.traderbobsemporium.dao.ItemDAO;
import main.java.com.traderbobsemporium.dao.Loggers.PurchasesActivityLogger;
import main.java.com.traderbobsemporium.gui.GUI;
import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.gui.InitGUI;
import main.java.com.traderbobsemporium.model.Customer;
import main.java.com.traderbobsemporium.model.Item;
import main.java.com.traderbobsemporium.model.ItemType;
import main.java.com.traderbobsemporium.model.Logging.PurchasesActivity;
import main.java.com.traderbobsemporium.util.AuthUtil;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.LoggingUtil;
import main.java.com.traderbobsemporium.util.Util;
import org.controlsfx.dialog.ExceptionDialog;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
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
public class RetailerController implements InitGUI {
    private ItemDAO itemDAO = new ItemDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private PurchasesActivityLogger purchasesActivity = new PurchasesActivityLogger();
    @FXML
    private TabPane filterTabPane;
    @FXML
    private ListView<Customer> customerListView;
    @FXML
    private ListView<String> customerPurchasesListView;
    @FXML
    private Text remainingBalanceText;
    @FXML
    private TextField filterTextField;
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
        setCellFactories();
        setChoiceBox();
        populateAll();
        purchasesActivity.start();
        itemTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> populateItemTilePane());
        itemTilePane.prefWidthProperty().bind(itemScrollPane.widthProperty());
        filterTextField.textProperty().addListener(customerTableViewFilter);
        customerPurchasesListView.setSelectionModel(null);
    }

    private void setCellFactories() {
        itemsSelectedListView.setCellFactory(selectedItemsListViewCallback());
        customerListView.setCellFactory(customersListViewCallback());
    }

    private void populateAll() {
        customerListView.setItems(getUnfilteredCustomerData());
        populateItemTilePane();
    }

    private void populateItemTilePane() {
        itemTilePane.getChildren().clear();
        try {
            for (Item item : itemDAO.getAll()) {
                ItemVBox itemVbox = new ItemVBox(item);
                itemTilePane.getChildren().add(itemVbox);
                addListener(itemVbox);
            }
            setVBoxImage();
        } catch (SQLException e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
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
    private void displayCustomerPurchases() {
        Customer customer = customerListView.getSelectionModel().getSelectedItem();
        if (!customerPurchasesListView.getItems().isEmpty())
            customerPurchasesListView.getItems().clear();
        if (customer == null) {
            remainingBalanceText.setText("");
            return;
        }
        remainingBalanceText.setText("Remaining balance: " + customer.getBalanceString());
        retreiveSpecificPurchasesActivity(customer);

    }

    private void retreiveSpecificPurchasesActivity(Customer customer){
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT itemName FROM purchasesactivity WHERE " +
                     "date = ? AND customerName = ?")) {
            statement.setString(1, Util.date(false));
            statement.setString(2, customer.getName());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    customerPurchasesListView.getItems().add(resultSet.getString("itemName"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
    }

    @FXML
    private void checkout() {
        Customer selectedCustomer = customerDAO.get(customerListView.getSelectionModel().getSelectedItem().getId());
        Util.displayAlert(getCheckoutText(selectedCustomer), Alert.AlertType.CONFIRMATION);
        List<String> nameOfItemsNotCheckedOut = new ArrayList<>();
        attemptToCheckoutSelectedItems(nameOfItemsNotCheckedOut, selectedCustomer);
        clear();
        displayItemsNotCheckedOut(nameOfItemsNotCheckedOut);
        filterTextField.requestFocus();


    }

    private void clear(){
        filterTextField.clear();
        itemsSelectedListView.getItems().clear();
        customerListView.setItems(getUnfilteredCustomerData());
    }

    private void displayItemsNotCheckedOut(List<String> nameOfItemsNotCheckedOut){
        if (!nameOfItemsNotCheckedOut.isEmpty()) {
            String dialog = "The following items did not checkout due to the customers insufficient balance " +
                    "or the item is out of stock!\n" + nameOfItemsNotCheckedOut;
            System.out.println(dialog);
            Util.displayAlert(dialog, Alert.AlertType.WARNING);
        }
    }

    private void attemptToCheckoutSelectedItems(List<String> itemsNotCheckout, Customer selectedCustomer) {
        try {
            for (Item item : itemsSelectedListView.getItems()) {
                Item syncedItem = itemDAO.get(item.getId());
                if (syncedItem.getPrice().compareTo(selectedCustomer.getBalance()) <= 0 && syncedItem.getQuantity() > 0)
                    calculateCheckoutValues(syncedItem, selectedCustomer);
                else
                    itemsNotCheckout.add(item.getName());
            }
            customerDAO.update(selectedCustomer);
        } catch (SQLException e){
            LoggingUtil.logExceptionToFile(e);
            e.printStackTrace();
        }
    }

    private void calculateCheckoutValues(Item item, Customer customer) throws SQLException {
        item.setQuantity(item.getQuantity() - 1);
        customer.setBalance(customer.getBalance().subtract(item.getPrice()));
        purchasesActivity.add(new PurchasesActivity(item, customer));
        itemDAO.update(item);
    }

    @FXML
    private void delete(){
        itemsSelectedListView.getItems().remove(itemsSelectedListView.getSelectionModel().getSelectedIndex());
    }

    private String getCheckoutText(Customer selectedCustomer){
        String customerName = selectedCustomer.getName();
        BigDecimal total = new BigDecimal(0);
        StringBuilder stringBuilder = new StringBuilder("Are you sure you want to checkout " + customerName + "?\n");
        stringBuilder.append("=========================================");
        for (Item items : itemsSelectedListView.getItems()) {
            stringBuilder.append(items.getName()).append(" : ").append(items.getPrice()).append("\n");
            total = total.add(items.getPrice());
        }
        BigDecimal remainingBalance = selectedCustomer.getBalance().subtract(total);
        stringBuilder.append("Your total is ").append(total)
                .append(" and customer remaining balance would be $").append(remainingBalance);
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
        itemTypeChoiceBox.setValue(ItemType.CANDY);

    }

    private ChangeListener<String> customerTableViewFilter = (observable, oldValue, newValue) -> {
        FilteredList<Customer> filteredData = new FilteredList<>(Objects.requireNonNull(getUnfilteredCustomerData()), c -> true);
        filteredData.setPredicate(customer -> {
            String lowerCaseFilter = newValue.toLowerCase();
            return !newValue.isEmpty() &&
                    customer.getName().toLowerCase().startsWith(lowerCaseFilter) ||
                    String.valueOf(customer.getId()).startsWith(lowerCaseFilter);
        });
        customerListView.setItems(filteredData);
    };

    private ObservableList<Customer> getUnfilteredCustomerData() {
        try {
            return FXCollections.observableArrayList(customerDAO.getAll());
        } catch (SQLException e){
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
        return null;
    }

    @Override
    public void exit() {
        purchasesActivity.stop();
        DatabaseUtil.DATA_SOURCE.close();
    }
}
