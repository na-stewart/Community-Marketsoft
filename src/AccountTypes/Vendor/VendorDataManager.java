package AccountTypes.Vendor;

import Data.DataViewer;
import Interfaces.MultiReceive;
import Data.DataManager;
import javafx.scene.layout.GridPane;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class VendorDataManager implements MultiReceive {

    private DataManager databaseManager = new DataManager();

    @Override
    public void retrieveDatabaseData(DataViewer dataViewer) throws SQLException {


    }
}
