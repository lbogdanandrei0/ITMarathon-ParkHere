package application;

import dataModel.CityParkingLots;
import dataModel.ParkingLot;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;

public class Controller {

    private boolean markersInitialized = false;

    @FXML
    VBox rootNode;

    @FXML
    WebView map;

    @FXML
    Button buttonFunction;

    WebEngine webEngine;

    public void initialize()
    {
        final URL urlGoogleMaps = getClass().getResource("googleMapsView.html");
        webEngine = map.getEngine();
        webEngine.load(urlGoogleMaps.toExternalForm());
        map.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!markersInitialized)
                    initMarkers();
                String parkingLotName = (String) webEngine.executeScript("getSelectedMarker();");
                if(parkingLotName!= null && !parkingLotName.equals("undefined"))
                {
                    ParkingLot temp = CityParkingLots.getInstance().getParkingLot(parkingLotName);
                    showDialog(temp);
                }
            }
        });
    }

    private void initMarkers()
    {
        markersInitialized = true;
        Iterator<ParkingLot> iterator = CityParkingLots.getInstance().getParkingLots().values().iterator();
        while(iterator.hasNext())
        {
            ParkingLot temp = iterator.next();
            addParkingLotMarker(temp.getLatitude(), temp.getLongitude(), temp.getName());
        }
    }

    private void addParkingLotMarker(float latitude, float longitude, String name)
    {
        webEngine.executeScript("addMarker("+latitude+", "+ longitude + " ,\"" + name + "\" );");
    }

    private void showDialog(ParkingLot parkingLot)
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(rootNode.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ReserveDialog.fxml"));
        try
        {
            dialog.getDialogPane().setContent(loader.load());
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        DialogController controller = loader.getController();
        controller.initReserveDialog(parkingLot.getParkingLots());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent())
            if(result.get()==ButtonType.OK) {
                controller.updateParkingLot();
            }
    }
}
