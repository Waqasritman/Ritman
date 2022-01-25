package angoothape.wallet.di.JSONdi.models;

public class BoardingInfo {
    public int id;
    public String seatNo;
    public String rowNo;
    public String colNo;
    public String seatType; //(SS - singleseat  , LB - lowerseat , UB - upperberth)
    public String availbility;
    public String gender;
    public String fare;
    public String seatTypeId;
    public String serviceTypeId;
    public String serviceTax;
    public String childFare;

    public boolean isSelected = false;

    public String passengerName = "";
    public String passengerAge = "";


    public boolean getMale() {
        return gender.equalsIgnoreCase("m");
    }

    public boolean isAvailable() {
        return availbility.equalsIgnoreCase("y");
    }
}
