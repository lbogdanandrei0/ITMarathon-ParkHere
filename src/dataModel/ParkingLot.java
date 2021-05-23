package dataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class ParkingLot implements Serializable {

    private ObservableList<ParkingSlot> ParkingLots;
    private final DateTimeFormatter dateFormatter;
    private String parkingLotName;
    private float latitude;
    private float longitude;



    public ParkingLot()
    {
        ParkingLots = FXCollections.observableArrayList();
        dateFormatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return parkingLotName;
    }

    public void setName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }

    public void initParkingLot(String fileName)
    {
        ParkingLots = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);

        try {
            BufferedReader reader = Files.newBufferedReader(path);
            String input;
            input = reader.readLine();
            if(input != null)
            {
                String[] coords = input.split(" & ");
                setName(coords[0]);
                setLatitude(Float.parseFloat(coords[1]));
                setLongitude(Float.parseFloat(coords[2]));
            }
            while((input = reader.readLine()) != null)
            {
                String[] slotDetails = input.split(" & ");
                String parkId = slotDetails[0];
                boolean isReserved = Boolean.parseBoolean(slotDetails[1]);
                LocalDate deadline = LocalDate.parse(slotDetails[2]);

                ParkingSlot temp = new ParkingSlot(parkId, isReserved, deadline);
                ParkingLots.add(temp);
            }
            reader.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void saveParkingLot(String fileName)
    {
        Path path = Paths.get(fileName);
        try
        {
            BufferedWriter writer = Files.newBufferedWriter(path);
            writer.write(String.format("%s & %s & %s", getName(), getLatitude(), getLongitude()));
            writer.newLine();
            Iterator<ParkingSlot> iterator = ParkingLots.iterator();
            while(iterator.hasNext())
            {
                ParkingSlot temp = iterator.next();
                writer.write(String.format("%s & %s & %s", temp.getParkId(), temp.isReserved(), temp.getDeadline().toString()));
                writer.newLine();
            }
            writer.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addParkingSlot(ParkingSlot toAdd)
    {
        ParkingLots.add(toAdd);
    }

    public int getNrOfSlots()
    {
        return this.ParkingLots.size();
    }

    public ObservableList<ParkingSlot> getParkingLots() {
        return ParkingLots;
    }
}
