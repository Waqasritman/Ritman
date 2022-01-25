package angoothape.wallet.di.JSONdi.restRequest;

public class BusBlockTicketRequest {

    public Credentials Credentials = new Credentials();
    public String operator_id;
    public String journey_date;
    public String service_id;
    public String layoutId;
    public String source_ID;
    public String destinationID;
    public String boardingPointID;
    public String droppingPointID;
    public String address;
    public String contactNumber;
    public String emailid;
    public String namelist;
    public String genderlist;
    public String agelist;
    public String seatNumbersList;
    public String seatFareList;
    public String seatTypeIds;
    public String seatTypesList;
    public String isAcSeat;
    public String seatLayoutUniqueId;
    public String serviceTaxList;
    public String singleLady;
    public boolean issingleLadyspecify;
    public String concessionId;
    public boolean isconcessionIdspecify;
    public String additionalInfoValue;

    @Override
    public String toString() {
        return "BusBlockTicketRequest{" +
                "Credentials=" + Credentials +
                ", operator_id='" + operator_id + '\'' +
                ", journey_date='" + journey_date + '\'' +
                ", service_id='" + service_id + '\'' +
                ", layoutId='" + layoutId + '\'' +
                ", source_ID='" + source_ID + '\'' +
                ", destinationID='" + destinationID + '\'' +
                ", boardingPointID='" + boardingPointID + '\'' +
                ", droppingPointID='" + droppingPointID + '\'' +
                ", address='" + address + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", emailid='" + emailid + '\'' +
                ", namelist='" + namelist + '\'' +
                ", genderlist='" + genderlist + '\'' +
                ", agelist='" + agelist + '\'' +
                ", seatNumbersList='" + seatNumbersList + '\'' +
                ", seatFareList='" + seatFareList + '\'' +
                ", seatTypeIds='" + seatTypeIds + '\'' +
                ", seatTypesList='" + seatTypesList + '\'' +
                ", isAcSeat='" + isAcSeat + '\'' +
                ", seatLayoutUniqueId='" + seatLayoutUniqueId + '\'' +
                ", serviceTaxList='" + serviceTaxList + '\'' +
                ", singleLady='" + singleLady + '\'' +
                ", issingleLadyspecify=" + issingleLadyspecify +
                ", concessionId='" + concessionId + '\'' +
                ", isconcessionIdspecify=" + isconcessionIdspecify +
                ", additionalInfoValue='" + additionalInfoValue + '\'' +
                '}';
    }
}
