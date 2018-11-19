package Admin.AdminGUI;

import Data.Camper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class GUIController implements Initializable {
    @FXML
    private TabPane tabPane;
    @FXML
    private TableView<Camper> camperTableView;
    @FXML
    private TableColumn<Camper, String> camperID, camperName, camperBalance;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //setCellValueFactories();


    }

    private void setCellValueFactories() {
        camperID.setCellValueFactory(new PropertyValueFactory<>("id"));
        camperName.setCellValueFactory(new PropertyValueFactory<>("name"));
        camperBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));


    }



    private  void something() {
        ObservableList<Camper> words = FXCollections.observableArrayList();
        camperTableView.setItems(words);

    }
}
