package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

public class SeatLayout {
    @SerializedName("operatorId")
    public int operatorId;
    @SerializedName("SourceStationID")
    public String sourceStationID;
    @SerializedName("DestinationStationID")
    public String destinationStationID;
    @SerializedName("Route")
    public String route;
    @SerializedName("JourneyDate")
    public String journeyDate;
    @SerializedName("ServiceID")
    public String serviceID;
    @SerializedName("SeatLayoutID")
    public int seatLayoutID;
    @SerializedName("upperTotalRows")
    public int upperTotalRows;
    @SerializedName("upperTotalColumns")
    public String upperTotalColumns;
    @SerializedName("lowerTotalRows")
    public String lowerTotalRows;
    @SerializedName("lowerTotalColumns")
    public String lowerTotalColumns;
    @SerializedName("lowerDividerRow")
    public String lowerDividerRow;
    @SerializedName("upperDividerRow")
    public int upperDividerRow;
    @SerializedName("maxNumberOfSeats")
    public int maxNumberOfSeats;
    @SerializedName("tentativeSeats")
    public String tentativeSeats;
    @SerializedName("TotalSeatList")
    public TotalSeatList totalSeatList;
    @SerializedName("seatLayoutUniqueId")
    public String seatLayoutUniqueId;
    @SerializedName("isAcSeat")
    public boolean isAcSeat;
    @SerializedName("fareDetails")
    public String fareDetails;
    @SerializedName("minimumAge")
    public String minimumAge;
    @SerializedName("minEligibleAge")
    public int minEligibleAge;
    @SerializedName("maxChildAge")
    public String maxChildAge;
    @SerializedName("isFareCallRequired")
    public String isFareCallRequired;
    @SerializedName("additionalInfoValue")
    public String additionalInfoValue;
}
