package Admin.AdminGUI;

import Admin.AdminPanel;
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
import java.sql.SQLException;
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
    private AdminPanel adminPanel = new AdminPanel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellValueFactories();
        try {
            adminPanel.retrieveDatabaseData("SELECT * FROM camper", camperTableView);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void setCellValueFactories() {
        camperID.setCellValueFactory(new PropertyValueFactory<>("id"));
        camperName.setCellValueFactory(new PropertyValueFactory<>("name"));
        camperBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
    }
}
