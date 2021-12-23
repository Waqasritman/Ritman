package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TotalSeatList {
    @SerializedName("lowerdeck_seat_nos")
    public List<String> lowerDiskSeats;
    @SerializedName("upperdeck_seat_nos")
    public List<String> upperDiskSeats;
}
