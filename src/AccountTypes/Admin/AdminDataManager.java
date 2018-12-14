package AccountTypes.Admin;

import Data.Customers.EmployeeType;
import Data.ID;
import Data.Item.ItemType;
import Data.ObservableDataSupplier;
import Manager.DataViewer;
import Manager.DatabaseManager;
import Interfaces.MultiQuery;
import Tables.TableType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;


import javax.xml.crypto.Data;
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
    public void retrieveDatabaseData(DataViewer dataViewer) throws SQLException {
        String table = dataViewer.getTable().name().toLowerCase();
        ResultSet resultSet = databaseManager.receiver("SELECT * FROM " + table);
        TableView tableView = (TableView) dataViewer.getDataViewer();
        ObservableList observableList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            switch (table) {
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

    public String addToCamperTableQuery(String[] fields) throws SQLException {
      return "INSERT INTO camper VALUES('" + new ID().getId() + "','" +
                fields[0] + "','" +
                fields[1] + "')";
    }

    public String addToItemTableQuery(String[] fields) {
        return "INSERT INTO item VALUES('" + new ID().getId() + "','" +
                fields[0] + "','" +
                fields[1] + "','" +
                fields[2] + "','" +
                fields[3] + "')";
    }

    public String addToEmployeeTableQuery(String[] fields) {
       return "INSERT INTO employee VALUES('" + new ID().getId() + "','" +
                fields[0] + "','" +
                fields[1] + "','" +
                fields[2] + "')";
    }

    public String editEmployeeTableQuery(int id, String[] fields) {
        return  "UPDATE employee SET " +
                "username = '" + fields[0] + "'," +
                "password = '" + fields[1] + "'," +
                "accounttype = '" + fields[2] + "' " +
                "WHERE id = " + id + ";";
    }



    public String  editCamperTableQuery(int id, String[] fields) {
        return  "UPDATE camper SET " +
                "name = '" + fields[0] + "'," +
                "balance = '" + fields[1] + "' " +
                "WHERE id = " + id + ";";

    }

    public String editItemTableQuery(int id, String[] fields) {
        return  "UPDATE item SET " +
                "name = '" + fields[0] + "'," +
                "price = '" + fields[1] + "'," +
                "imageurl = '" + fields[2] + "'," +
                "itemtype = '" + fields[3] + "' " +
                "WHERE id = " + id + ";";
    }

}
