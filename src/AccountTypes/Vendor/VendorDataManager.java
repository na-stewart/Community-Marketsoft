package AccountTypes.Vendor;

import Interfaces.MultiQuery;
import Manager.DataViewer;
import Manager.DatabaseManager;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class VendorDataManager implements MultiQuery {

    @Override
    public void updateDatabase(String query) throws SQLException {
        //Update's camper information to subtract balance
    }

    @Override
    public void retrieveDatabaseData(DataViewer dataViewer) throws SQLException {
        GridPane gridPane = (GridPane) dataViewer.getViewer();
        //Retreive items and camper data.
        //Retreive gridpane and update gridpane with panes that include database data.

    }
}
