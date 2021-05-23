package dataModel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public final class CityParkingLots {
    private static CityParkingLots instance = new CityParkingLots();

    private Map<String, ParkingLot> parkingLots = new HashMap<String, ParkingLot>();

    public static CityParkingLots getInstance()
    {
        return instance;
    }

    public void loadCityParkingLots(String fileName)
    {
        try
        {
            BufferedReader reader = Files.newBufferedReader(Paths.get(fileName));
            String input;
            while((input = reader.readLine())!=null)
            {
                ParkingLot temp = new ParkingLot();
                temp.initParkingLot(input);
                this.parkingLots.put(temp.getName(), temp);
            }
            reader.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void saveCityParkingLots(String fileName)
    {
        try
        {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
            for(String  temp : parkingLots.keySet())
            {
                ParkingLot toSave = parkingLots.get(temp);
                toSave.saveParkingLot(toSave.getName().toLowerCase() + ".txt");
                writer.write(temp.toLowerCase() + ".txt");
                writer.newLine();
            }
            writer.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addParkingLot(ParkingLot toAdd)
    {
        this.parkingLots.put(toAdd.getName(), toAdd);
    }

    public int getNrOfParkingLots()
    {
        return parkingLots.size();
    }
    public Map<String, ParkingLot> getParkingLots()
    {
        return parkingLots;
    }

    public ParkingLot getParkingLot(String name)
    {
        return parkingLots.get(name);
    }
}
