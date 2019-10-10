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
import javafx.stage.Stage;
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
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
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
    private ApiConsumer<PurchasesActivity> apiConsumer = new ApiConsumer<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellFactories();
        setChoiceBox();
        populateAll();
        itemTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> populateItemTilePane());
        itemTilePane.prefWidthProperty().bind(itemScrollPane.widthProperty());
        filterTextField.textProperty().addListener(customerFilter);
    }

    private void populateAll() {
        customerListView.setItems(getUnfilteredCustomerData());
        populateItemTilePane();
    }

    private void populateItemTilePane(){
        itemTilePane.getChildren().clear();
        try {
            List<Item> itemList = itemTypeChoiceBox.getValue().getName().equals("All") ? itemDTO.getAll() :
                    itemDTO.getAllByCategory(itemTypeChoiceBox.getValue().getName());
             for (Item item : itemList) {
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
                setCustomerInfoDisplay(customer);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setCustomerInfoDisplay(Customer customer) throws IOException, ApiException {
        customerPurchasesTableView.getItems().clear();
        remainingBalanceText.setText(customer.getBalanceString());
        dailyLimitText.setText("Daily limit: " + String.valueOf(customer.getDailyLimit()));
        customerPurchasesTableView.getItems().setAll(apiConsumer.getAll("retailer/customerpurchases?customerName="
                + customer.getName() + "&date=" + Util.date(false), PurchasesActivity.class));
    }

    @FXML
    private void checkout() {
        Util.displayAlert("Are you sure you want to checkout?", getCheckoutText(), Alert.AlertType.CONFIRMATION);
        try {
            apiConsumer.post(checkoutNamePairs(), "retailer/checkout");
            clear();
        }catch (Exception e){
            e.printStackTrace();
            Util.displayAlert("Checkout failed! Customer either out of balance or item is out of quantity!", Alert.AlertType.ERROR);
        }
    }

    private List<BasicNameValuePair> checkoutNamePairs(){
        Gson gson = new Gson();
        Customer selectedCustomer = customerListView.getSelectionModel().getSelectedItem();
        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("customer", gson.toJson(selectedCustomer, Customer.class)));
        basicNameValuePairs.add(new BasicNameValuePair("itemList", gson.toJson(itemsSelectedListView.getItems())));
        return basicNameValuePairs;
    }

    private String getCheckoutText(){
        BigDecimal total = new BigDecimal(0);
        StringBuilder stringBuilder = new StringBuilder();
        for (Item items : itemsSelectedListView.getItems()) {
            stringBuilder.append(items.getName()).append(" : ").append(items.getPriceString()).append("\n");
            total = total.add(items.getPrice());
        }
        BigDecimal remainingBalance = customerListView.getSelectionModel().getSelectedItem().getBalance().subtract(total);
        stringBuilder.append("Your total is ").append(total).append(" and customer remaining balance would be $").append(remainingBalance);
        return String.valueOf(stringBuilder);
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
        Stage stage = (Stage) itemTilePane.getScene().getWindow();
        stage.close();
        new GUI("main/resources/view/LoginGUI.fxml").display();
    }


    @FXML
    private void setFullScreen(){
        Stage stage = (Stage) itemTilePane.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }

    private void setChoiceBox() {
        ItemCategory itemCategory = new ItemCategory("All");
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
        filteredData.setPredicate(camper -> {
            String lowerCaseFilter = newValue.toLowerCase();
            return !newValue.isEmpty() &&
                    camper.getName().toLowerCase().startsWith(lowerCaseFilter) ||
                    String.valueOf(camper.getId()).startsWith(lowerCaseFilter);
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
        camperColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));

    }
}
