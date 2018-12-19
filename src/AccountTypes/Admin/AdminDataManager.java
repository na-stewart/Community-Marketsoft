package AccountTypes.Admin;

import Data.Customers.Camper;
import Data.Customers.Employee;
import Data.Customers.EmployeeType;
import Data.DataObjectSupplier;
import Data.ID;
import Data.Item.Item;
import Data.Item.ItemType;
import Manager.DataViewer;
import Manager.DatabaseManager;
import Interfaces.MultiQuery;
import Util.LoggedInAccountUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;


import javax.security.auth.login.AccountException;
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

    @Override
    public void updateDatabase(String query) throws SQLException {
        databaseManager.update(query);
    }

    @Override
    public void retrieveDatabaseData(DataViewer dataViewer) throws SQLException {
        ResultSet resultSet = databaseManager.receiver("SELECT * FROM " + dataViewer.getTableType().name().toLowerCase());
        TableView tableView = (TableView) dataViewer.getViewer();
        ObservableList observableList = FXCollections.observableArrayList();
        while(resultSet.next())
            observableList.add(new DataObjectSupplier(resultSet).getData(dataViewer.getTableType()));
        tableView.setItems(observableList);
        resultSet.close();
    }

    public String addToCamperTableQuery(String[] fields) {
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

    public String addToEmployeeTableQuery(String[] fields) throws SQLException {
        accountPermission(fields[2]);
       return "INSERT INTO employee VALUES('" + new ID().getId() + "','" +
                fields[0] + "','" +
                fields[1] + "','" +
                fields[2] + "')";
    }

    public String editEmployeeTableQuery(int id, String[] fields) throws SQLException {
        accountPermission(fields[2]);
        return  "UPDATE employee SET " +
                "username = '" + fields[0] + "'," +
                "password = '" + fields[1] + "'," +
                "accounttype = '" + fields[2] + "' " +
                "WHERE id = " + id + ";";
    }

    private void accountPermission(String field) throws SQLException {
        if (LoggedInAccountUtil.thisAccountType == EmployeeType.EMPLOYEE) {
            if (field.equals("0") || field.equals("2") || field.equals("3"))
                throw new SQLException("Permissions not granted to execute query");
        }
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
