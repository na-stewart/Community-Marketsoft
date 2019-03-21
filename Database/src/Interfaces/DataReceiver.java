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
public interface DataReceiver {
    void retrieveDatabaseData(DataViewer dataViewer) throws SQLException;
}
