package App.Admin;

import Data.Users.Camper;
import Data.Users.Employee;
import Data.Users.EmployeeType;
import Data.DataObjectBuilder;
import Data.DataViewer;
import Data.Item.Item;
import Data.Item.ItemType;
import Data.DatabaseQueryReceiver;
import Interfaces.MultiQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import org.controlsfx.control.table.TableFilter;
import org.jasypt.util.password.StrongPasswordEncryptor;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class AdminDataManager implements MultiQuery {

    private DatabaseQueryReceiver databaseQueryReceiver = new DatabaseQueryReceiver();

    @Override
    public void retrieveDatabaseData(DataViewer dataViewer) throws SQLException {
        ResultSet resultSet = databaseQueryReceiver.receiver(dataViewer.getQuery());
        TableView tableView = (TableView) dataViewer.getNode();
        TableFilter.Builder tableFilter = TableFilter.forTableView(tableView);
        ObservableList observableList = FXCollections.observableArrayList();
        while(resultSet.next())
            observableList.add(new DataObjectBuilder(resultSet).getData(dataViewer.getQuery()));
        tableView.setItems(observableList);
        tableFilter.apply();
        resultSet.close();
    }

    public void tryToDeleteRow(String tableName, int id) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE id = '" + id + "'";
        databaseQueryReceiver.update(query);
    }

    public void addToCamperTableQuery(Camper camper) throws SQLException {
      String query =  "INSERT INTO camper VALUES('" + camper.getId() + "','" + camper.getName() + "','" +
                camper.getBalance()+ "')";
      databaseQueryReceiver.update(query);
    }

    public void addToItemTableQuery(Item item) throws SQLException {
      String query = "INSERT INTO item VALUES('" + item.getId() + "','" + item.getName() + "','" + item.getPrice() +
                "','" + item.getQuantity() + "','"+ item.getImageURL() + "','" + ItemType.itemTypeToInt(item.getItemType()) + "')";
      databaseQueryReceiver.update(query);

    }

    public void addToEmployeeTableQuery(Employee employee) throws SQLException {
        employee.requestPermissionToModifyEmployees();
        String query = "INSERT INTO employee VALUES('" + employee.getId() + "','" + employee.getUsername() + "','" +
                new StrongPasswordEncryptor().encryptPassword(employee.getPassword()) + "','" +
                EmployeeType.employeeTypeToInt(employee.getEmployeeType()) + "')";
        databaseQueryReceiver.update(query);
    }


}
