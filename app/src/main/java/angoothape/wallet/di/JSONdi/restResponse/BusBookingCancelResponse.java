package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class BusBookingCancelResponse extends ApiResponse<BusBookingCancelResponse> {
    @SerializedName("return_amount")
    public String returnAmount;

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;
}
