package Admin;

import Data.Camper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class AdminPanel implements Initializable {
    @FXML
    private TableView<Camper> camperTable;
    @FXML
    private TableColumn<Camper, TextField> id, name, coinCount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellValueFactories();

    }

    private void setCellValueFactories(){
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        coinCount.setCellValueFactory(new PropertyValueFactory<>("coinCount"));
    }
}
