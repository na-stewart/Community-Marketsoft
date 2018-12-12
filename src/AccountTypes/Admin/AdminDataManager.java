package AccountTypes.Admin;

import Data.Customers.Employee;
import Data.Customers.EmployeeType;
import Data.ObservableDataSupplier;
import Manager.DbManagers.DatabaseManager;
import Manager.Interfaces.MultiQuery;
import Manager.DbTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class AdminDataManager implements MultiQuery {

    private DatabaseManager databaseManager = new DatabaseManager();
    private ObservableDataSupplier observableDataSupply = new ObservableDataSupplier();

    @Override
    public void updateDatabase(String query) throws SQLException {
        databaseManager.update(query);
    }

    @Override
    public void retrieveDatabaseData(DbTable tables, Object dataViewer) throws SQLException {
        ResultSet resultSet = databaseManager.receiver("SELECT * FROM " + tables.name().toLowerCase());
        TableView tableView = (TableView) dataViewer;
        ObservableList observableList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            switch (tables.name().toLowerCase()) {
                case "employee":
                    observableDataSupply.populateEmployeeList(resultSet, observableList);
                    break;
                case "item":
                     observableDataSupply.populateItemList(resultSet, observableList);
                    break;
                case "camper":
                    observableDataSupply.populateCamperList(resultSet, observableList);
                    break;
            }
        }
        tableView.setItems(observableList);
        resultSet.close();
    }
}
