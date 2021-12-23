package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class BusSeatingLayoutResponse extends ApiResponse<BusSeatingLayoutResponse> {
    @SerializedName("status")
    public String status;
    @SerializedName("seatlayout")
    public SeatLayout seatLayout;
}
