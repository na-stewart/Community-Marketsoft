package App.Vendor.VendorGUI;

import App.Vendor.VendorDataManager;
import Data.Users.Camper;
import Data.DataViewer;
import Data.Item.Item;
import Data.Item.ItemType;
import GUILoader.GUI;
import javafx.beans.value.ChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.controlsfx.dialog.ExceptionDialog;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class GUIController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private ScrollPane itemScrollPane;
    @FXML
    private TilePane itemTilePane;
    @FXML
    private ChoiceBox<ItemType> itemTypes;
    @FXML
    private ListView<Item> selectedItems;
    @FXML
    private TableView<Camper> camperTableView;
    @FXML
    private TableColumn<Camper, String> id, name, balance;
    @FXML
    private TextField filterField;
    @FXML
    private Text camperNameText;
    private VendorDataManager vendorDataManager = new VendorDataManager();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemTypes.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> tryToPopulateTilePane());
        itemTilePane.prefWidthProperty().bind(itemScrollPane.widthProperty());
        filterField.textProperty().addListener(camperListViewFilter);
        bindBackgroundImage();
        setCellFactories();
        setChoiceBox();
        refreshData();
    }

    private void refreshData(){
        tryToPopulateTilePane();
        tryToPopulateCamper();
    }

    private void tryToPopulateTilePane() {
        try {
            String query = "SELECT * FROM item" + " WHERE itemtype=" + ItemType.itemTypeToInt(itemTypes.getValue());
            vendorDataManager.retrieveDatabaseData(new DataViewer(itemTilePane, query));
        } catch (SQLException e) {
            new ExceptionDialog(e).showAndWait();
        }
    }

    private void tryToPopulateCamper(){
        try {
            vendorDataManager.retrieveDatabaseData(new DataViewer(camperTableView, "SELECT * FROM camper"));
        } catch (SQLException e) {
            new ExceptionDialog(e).showAndWait();
        }
    }

    @FXML
   private void buttonListener(ActionEvent actionEvent) {
        String buttonText = ((Button) actionEvent.getSource()).getText();
        switch (buttonText){
            case "Delete":
                selectedItems.getItems().remove(selectedItems.getSelectionModel().getSelectedIndex());
                break;
            case "Check Out":
                displayCheckoutDialog();
                break;
            case "Logout":
                new GUI("App/Login/LoginGUI/LoginGUI.fxml");
                break;
        }
    }

    private void displayCheckoutDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Checkout");
        alert.setHeaderText("Checkout?");
        alert.setContentText(vendorDataManager.getCheckoutText());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            checkout();
    }

    private void checkout(){
        vendorDataManager.tryToCheckOut();
        vendorDataManager.setSelectedCamper(null);
        camperNameText.setText("Selected Camper");
        filterField.requestFocus();
        refreshData();
    }

    @FXML
    private void tilePaneClickListener(){
        selectedItems.setItems(vendorDataManager.getSelectedItems());
    }

    @FXML
    private void filterFieldKeyListener(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.DOWN) {
            camperTableView.requestFocus();
            camperTableView.getSelectionModel().select(0);
        }
    }

    @FXML
    private void camperTableViewClickListener(MouseEvent mouseEvent){
        if (mouseEvent.getClickCount() == 2) {
            setSelectedCamper();
            selectedItems.getItems().clear();
        }
    }

    @FXML
    private void camperTableViewKeyListener(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.ENTER) {
            setSelectedCamper();
            selectedItems.getItems().clear();
        }
    }

    private void setSelectedCamper(){
        Camper camper = camperTableView.getSelectionModel().getSelectedItem();
        vendorDataManager.setSelectedCamper(camper);
        camperNameText.setText(camper.getName());
    }


    private void setCellFactories(){
        setCamperTableViewCells();
        selectedItems.setCellFactory(selectedItemsTableViewCallback());
    }

    private void setCamperTableViewCells() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
    }

    private void bindBackgroundImage(){
        backgroundImage.fitWidthProperty().bind(anchorPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(anchorPane.heightProperty());
    }

    private ChangeListener<String> camperListViewFilter = (observable, oldValue, newValue) -> {
        FilteredList<Camper> filteredData = new FilteredList<>(vendorDataManager.getUnfilteredCamperData(), c -> true);
        filteredData.setPredicate(camper -> {
            String lowerCaseFilter = newValue.toLowerCase();
            return !newValue.isEmpty() &&
                    camper.getName().toLowerCase().startsWith(lowerCaseFilter) ||
                    String.valueOf(camper.getId()).startsWith(lowerCaseFilter);
        });
        camperTableView.setItems(filteredData);
    };

    private Callback selectedItemsTableViewCallback(){
        return new Callback<ListView<Item>, ListCell<Item>>() {
            @Override
            public ListCell<Item> call(ListView<Item> p) {
                ListCell<Item> cell = new ListCell<Item>() {
                    @Override
                    protected void updateItem(Item i, boolean bln) {
                        super.updateItem(i, bln);
                        if (i != null)
                            setText(i.getName());
                        else
                            setText("");
                    }
                };
                return cell;
            }
        };
    }


    private void setChoiceBox() {
        for (ItemType itemType : ItemType.values())
            itemTypes.getItems().add(itemType);
        itemTypes.setValue(ItemType.CHIPS);

    }




}



