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
import main.java.com.traderbobsemporium.dao.CamperDAO;
import main.java.com.traderbobsemporium.dao.ItemDAO;
import main.java.com.traderbobsemporium.dao.Loggers.PurchasesActivityLogger;
import main.java.com.traderbobsemporium.gui.GUI;
import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.gui.InitGUI;
import main.java.com.traderbobsemporium.model.Camper;
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
    private CamperDAO camperDAO = new CamperDAO();
    private PurchasesActivityLogger purchasesActivity = new PurchasesActivityLogger();
    @FXML
    private TabPane filterTabPane;
    @FXML
    private ListView<Camper> camperListView;
    @FXML
    private ListView<String> camperPurchasesListView;
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
        filterTextField.textProperty().addListener(camperTableViewFilter);
        camperPurchasesListView.setSelectionModel(null);
    }

    private void setCellFactories() {
        itemsSelectedListView.setCellFactory(selectedItemsListViewCallback());
        camperListView.setCellFactory(campersListViewCallback());
    }

    private void populateAll() {
        camperListView.setItems(getUnfilteredCamperData());
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
            if (camperListView.getSelectionModel().getSelectedItem() != null)
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

    private Callback<ListView<Camper>, ListCell<Camper>> campersListViewCallback() {
        return new Callback<ListView<Camper>, ListCell<Camper>>() {
            @Override
            public ListCell<Camper> call(ListView<Camper> p) {
                return new ListCell<Camper>() {
                    @Override
                    protected void updateItem(Camper i, boolean bln) {
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
            camperListView.getSelectionModel().selectFirst();
            filterTabPane.getSelectionModel().select(1);

        }
        if (keyEvent.getCode() == KeyCode.DOWN) {
            camperListView.requestFocus();
            camperListView.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void displayCamperPurchases() {
        Camper camper = camperListView.getSelectionModel().getSelectedItem();
        if (!camperPurchasesListView.getItems().isEmpty())
            camperPurchasesListView.getItems().clear();
        if (camper == null) {
            remainingBalanceText.setText("");
            return;
        }
        remainingBalanceText.setText("Remaining balance: " + camper.getBalanceString());
        retreiveSpecificPurchasesActivity(camper);

    }

    private void retreiveSpecificPurchasesActivity(Camper camper){
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT itemName FROM purchasesactivity WHERE " +
                     "date = ? AND camperName = ?")) {
            statement.setString(1, Util.date(false));
            statement.setString(2, camper.getName());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    camperPurchasesListView.getItems().add(resultSet.getString("itemName"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
    }

    @FXML
    private void checkout() {
        Camper selectedCamper = camperDAO.get(camperListView.getSelectionModel().getSelectedItem().getId());
        Util.displayAlert(getCheckoutText(selectedCamper), Alert.AlertType.CONFIRMATION);
        List<String> nameOfItemsNotCheckedOut = new ArrayList<>();
        attemptToCheckoutSelectedItems(nameOfItemsNotCheckedOut, selectedCamper);
        clear();
        displayItemsNotCheckedOut(nameOfItemsNotCheckedOut);
        filterTextField.requestFocus();


    }

    private void clear(){
        filterTextField.clear();
        itemsSelectedListView.getItems().clear();
        camperListView.setItems(getUnfilteredCamperData());
    }

    private void displayItemsNotCheckedOut(List<String> nameOfItemsNotCheckedOut){
        if (!nameOfItemsNotCheckedOut.isEmpty()) {
            String dialog = "The following items did not checkout due to the campers insufficient balance " +
                    "or the item is out of stock!\n" + nameOfItemsNotCheckedOut;
            System.out.println(dialog);
            Util.displayAlert(dialog, Alert.AlertType.WARNING);
        }
    }

    private void attemptToCheckoutSelectedItems(List<String> itemsNotCheckout, Camper selectedCamper) {
        try {
            for (Item item : itemsSelectedListView.getItems()) {
                Item syncedItem = itemDAO.get(item.getId());
                if (syncedItem.getPrice().compareTo(selectedCamper.getBalance()) <= 0 && syncedItem.getQuantity() > 0)
                    calculateCheckoutValues(syncedItem, selectedCamper);
                else
                    itemsNotCheckout.add(item.getName());
            }
            camperDAO.update(selectedCamper);
        } catch (SQLException e){
            LoggingUtil.logExceptionToFile(e);
            e.printStackTrace();
        }
    }

    private void calculateCheckoutValues(Item item, Camper camper) throws SQLException {
        item.setQuantity(item.getQuantity() - 1);
        camper.setBalance(camper.getBalance().subtract(item.getPrice()));
        purchasesActivity.add(new PurchasesActivity(item, camper));
        itemDAO.update(item);
    }

    @FXML
    private void delete(){
        itemsSelectedListView.getItems().remove(itemsSelectedListView.getSelectionModel().getSelectedIndex());
    }

    private String getCheckoutText(Camper selectedCamper){
        String camperName = selectedCamper.getName();
        BigDecimal total = new BigDecimal(0);
        StringBuilder stringBuilder = new StringBuilder("Are you sure you want to checkout " + camperName + "?\n");
        stringBuilder.append("=========================================");
        for (Item items : itemsSelectedListView.getItems()) {
            stringBuilder.append(items.getName()).append(" : ").append(items.getPrice()).append("\n");
            total = total.add(items.getPrice());
        }
        BigDecimal remainingBalance = selectedCamper.getBalance().subtract(total);
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
        itemTypeChoiceBox.setValue(ItemType.CANDY);

    }

    private ChangeListener<String> camperTableViewFilter = (observable, oldValue, newValue) -> {
        FilteredList<Camper> filteredData = new FilteredList<>(Objects.requireNonNull(getUnfilteredCamperData()), c -> true);
        filteredData.setPredicate(camper -> {
            String lowerCaseFilter = newValue.toLowerCase();
            return !newValue.isEmpty() &&
                    camper.getName().toLowerCase().startsWith(lowerCaseFilter) ||
                    String.valueOf(camper.getId()).startsWith(lowerCaseFilter);
        });
        camperListView.setItems(filteredData);
    };

    private ObservableList<Camper> getUnfilteredCamperData() {
        try {
            return FXCollections.observableArrayList(camperDAO.getAll());
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
