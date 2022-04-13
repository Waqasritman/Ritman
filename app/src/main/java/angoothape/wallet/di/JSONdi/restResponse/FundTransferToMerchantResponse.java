package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

public class FundTransferToMerchantResponse {

    @SerializedName("Distributor_Available_Balance_INR")
    public String availableBalance;
    @SerializedName("Fund_Transfer_Status")
    public String status;
}
