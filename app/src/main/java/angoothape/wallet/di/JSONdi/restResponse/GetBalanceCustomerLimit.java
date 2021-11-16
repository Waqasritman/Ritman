package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class GetBalanceCustomerLimit extends ApiResponse<GetBalanceCustomerLimit> {
    @SerializedName("Customer_Monthly_Available_Balance_INR_")
    public String dailyAvailLimit_;
    @SerializedName("Is_DMT_Live")
    public boolean isDMTLive;
}
