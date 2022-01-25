package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class BusBookingPreCancelResponse extends ApiResponse<BusBookingPreCancelResponse> {
    @SerializedName("status")
    public String status;

    @SerializedName("can_ret_amt")
    public String canRetariedAmount;

    @SerializedName("Collect_amt")
    public String collectAmount;

    @SerializedName("canncellation_charges")
    public String cancellationCharges;

    @SerializedName("return_amount")
    public String returnAmount;
}
