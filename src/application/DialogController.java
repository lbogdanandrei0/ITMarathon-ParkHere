package application;

import dataModel.ParkingSlot;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.Optional;

public class DialogController {

    @FXML
    DatePicker deadlineDate;

    @FXML
    ListView<ParkingSlot> parkingSlotsList;

    @FXML
    Label parkingSlotIdLabel;

    @FXML
    Label passwordLabel;

    @FXML
    PasswordField passwordField;

    public void updateParkingLot() {
        ParkingSlot selected = parkingSlotsList.getSelectionModel().getSelectedItem();
        if (selected != null){
            if (!selected.isReserved()) {
                if (deadlineDate.getValue() != null) {
                        if(passwordField.getText().trim().isEmpty())
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Introduceti o parola pentru a putea rezerva locul");
                            alert.setTitle("Introduceti parola");
                            alert.setContentText("Retineti parola introdusa pentru a putea anula rezervarea");
                            Optional<ButtonType> confirmation = alert.showAndWait();
                        }else {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText("");
                            alert.setTitle("Confirmati rezervarea");
                            alert.setContentText("Rezervati locul " + selected.getParkId() + " pana la data de " + deadlineDate.getValue());
                            Optional<ButtonType> confirmation = alert.showAndWait();
                            if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
                                selected.setReservedPassword(passwordField.getText().trim());
                                selected.setReserved(true);
                                selected.setDeadline(deadlineDate.getValue());
                            }
                        }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Data pana la care se rezerva locul nu poate sa lipseasca");
                    alert.setTitle("Data incorecta");
                    alert.setContentText("Rezervarea a esuat, va rugam sa reincercati mai tarziu");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                }
            } else {

                if(selected.tryToUnreserve(passwordField.getText().trim()))
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Rezervarea pentru locul " + selected.getParkId() + " a fost anulata");
                    alert.setTitle("Rezervare anulata");
                    alert.setContentText("Va mai asteptam");
                    selected.setReservedPassword(null);
                    selected.setReserved(false);
                    selected.setDeadline(LocalDate.now().minusDays(2));
                    alert.showAndWait();
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Locul " + selected.getParkId() + " este deja rezervat");
                    alert.setTitle("Loc indisponibil");
                    alert.setContentText("Va rugam sa selectati un loc disponibil");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                }
            }
    }
    }

    public void initReserveDialog(ObservableList<ParkingSlot> lots) {

        deadlineDate.getEditor().setDisable(true);
        deadlineDate.getEditor().setOpacity(0);
        deadlineDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(newValue.isBefore(LocalDate.now())) {
                    deadlineDate.setValue(LocalDate.now());
                }
            }
        });

        parkingSlotsList.setCellFactory(new Callback<ListView<ParkingSlot>, ListCell<ParkingSlot>>() {
            @Override
            public ListCell<ParkingSlot> call(ListView<ParkingSlot> param) {
                ListCell<ParkingSlot> cell = new ListCell<ParkingSlot>() {
                    @Override
                    protected void updateItem(ParkingSlot item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null) {
                            passwordField.setVisible(true);
                            passwordField.setEditable(true);

                            passwordLabel.setVisible(true);
                            setText(item.getParkId());
                            if(item.getDeadline().isBefore(LocalDate.now()))
                                item.setReserved(false);
                            if (item.isReserved()) {
                                setDisabled(true);
                                setTextFill(Color.RED);
                            } else {
                                setTextFill(Color.GREEN);
                            }
                        }
                    }
                };
                return cell;
            }
        });
        parkingSlotsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ParkingSlot>() {
            @Override
            public void changed(ObservableValue<? extends ParkingSlot> observable, ParkingSlot oldValue, ParkingSlot newValue) {
                if(!newValue.isReserved()) {
                    deadlineDate.getEditor().setDisable(false);
                    deadlineDate.getEditor().setOpacity(1);
                    parkingSlotIdLabel.setText(newValue.getParkId());
                }else{
                    deadlineDate.getEditor().setDisable(true);
                    deadlineDate.getEditor().setOpacity(0);
                    parkingSlotIdLabel.setText("Locul " + newValue.getParkId() + " va fi disponibil dupa data de " + newValue.getDeadline());
                }
            }
        });
        parkingSlotsList.setItems(lots);
    }
}


