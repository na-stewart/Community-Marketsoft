package Interfaces;

import Data.DataViewer;
import javafx.scene.Node;

import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public interface MultiReceive {
    // TODO: 12/27/2018 Remove dataviewer object, seems useless at this point. Instead pass object and table.  
    void retrieveDatabaseData(DataViewer dataViewer) throws SQLException;
}
