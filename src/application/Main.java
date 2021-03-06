package application;

import dataModel.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        primaryStage.setTitle("ParkHere");
        primaryStage.setScene(new Scene(root, 1200, 1000));
        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        CityParkingLots.getInstance().loadCityParkingLots("iasi.txt");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        CityParkingLots.getInstance().saveCityParkingLots("iasi.txt");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
