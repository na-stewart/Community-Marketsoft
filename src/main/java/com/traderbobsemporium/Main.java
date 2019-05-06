package main.java.com.traderbobsemporium;
import javafx.application.Application;
import javafx.stage.Stage;
import main.java.com.traderbobsemporium.datasource.DatabaseConnector;
import main.java.com.traderbobsemporium.dao.CamperDAO;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseConnector.configSetup();
    }
}