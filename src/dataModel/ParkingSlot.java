package dataModel;

import java.time.LocalDate;

public class ParkingSlot {

    private String parkId;
    private boolean isReserved;
    private LocalDate deadline;

    public ParkingSlot(String parkId, boolean isReserved)
    {
        this.parkId = parkId;
        this.isReserved = isReserved;
    }

    public ParkingSlot(String parkId, boolean isReserved, LocalDate deadline) {
        this(parkId, isReserved);
        this.deadline = deadline;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    @Override
    public String toString() {
        return parkId;
    }
}
