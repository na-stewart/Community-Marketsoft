package AccountTypes.Vendor.VendorGUI;

import AccountTypes.Vendor.VendorDataManager;
import Data.Customers.Camper;
import Data.DataViewer;
import Data.Item.Item;
import Data.Item.ItemType;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    private ListView<Camper> camperListView;
    private ObservableList<Camper> unfilteredCamperData = FXCollections.observableArrayList();
    @FXML
    private TextField filterField;
    @FXML
    private Text camperNameText;
    private VendorDataManager vendorDataManager = new VendorDataManager();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemTilePane.prefWidthProperty().bind(itemScrollPane.widthProperty());
        itemTypes.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> tryToPopulateTilePane());
        bindBackgroundImage();
        setCellFactories();
        setChoiceBox();
        refreshData();
    }


    private void bindBackgroundImage(){
        backgroundImage.fitWidthProperty().bind(anchorPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(anchorPane.heightProperty());
    }

    private void refreshData(){
        tryToPopulateCamper();
        tryToPopulateTilePane();

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
            filterField.textProperty().removeListener(camperListViewFilter);
            vendorDataManager.retrieveDatabaseData(new DataViewer(camperListView, "SELECT * FROM camper"));
            filterField.textProperty().addListener(camperListViewFilter);
        } catch (SQLException e) {
            new ExceptionDialog(e).showAndWait();
        }
    }

    private ChangeListener<String> camperListViewFilter = (observable, oldValue, newValue) -> {
        FilteredList<Camper> filteredData = new FilteredList<>(vendorDataManager.getUnfilteredCamperData(), c -> true);
        filteredData.setPredicate(camper -> {
            String lowerCaseFilter = newValue.toLowerCase();
            return !newValue.isEmpty() &&
                    camper.getName().toLowerCase().startsWith(lowerCaseFilter) ||
                    String.valueOf(camper.getId()).startsWith(lowerCaseFilter);

        });
        camperListView.setItems(filteredData);
    };


    @FXML
   private void buttonListener(ActionEvent actionEvent) {
        String buttonText = ((Button) actionEvent.getSource()).getText();
        if (buttonText.equals("Delete"))
            vendorDataManager.getSelectedItems().remove(selectedItems.getSelectionModel().getSelectedIndex());
        else {
            displayCheckoutDialog();
            vendorDataManager.getSelectedItems().clear();
        }
        selectedItems.setItems(vendorDataManager.getSelectedItems());
    }

    private void displayCheckoutDialog(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Checkout");
        alert.setHeaderText("Checkout?");
        alert.setContentText(vendorDataManager.getCheckoutText());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            vendorDataManager.tryToCheckOut();
            refreshData();
        }
    }

    @FXML
    private void tilePaneClickListener(){
        selectedItems.setItems(vendorDataManager.getSelectedItems());
    }

    @FXML
    private void filterFieldKeyListener(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.DOWN) {
            camperListView.requestFocus();
            camperListView.getSelectionModel().select(0);
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
        Camper camper = camperListView.getSelectionModel().getSelectedItem();
        vendorDataManager.setSelectedCamper(camper);
        camperNameText.setText(camper.getName());
    }


    private void setCellFactories(){
        camperListView.setCellFactory(camperViewCallback());
        selectedItems.setCellFactory(selectedItemsTableViewCallback());
    }

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

    private Callback camperViewCallback() {
        return new Callback<ListView<Camper>, ListCell<Camper>>() {
            @Override
            public ListCell<Camper> call(ListView<Camper> p) {
                ListCell<Camper> cell = new ListCell<Camper>() {
                    @Override
                    protected void updateItem(Camper c, boolean bln) {
                        super.updateItem(c, bln);
                        if (c != null)
                            setText(c.getId() + " : " + c.getName() + " : " + c.getBalance());
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



