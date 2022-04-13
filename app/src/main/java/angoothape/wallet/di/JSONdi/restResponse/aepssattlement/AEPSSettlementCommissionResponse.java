package angoothape.wallet.di.JSONdi.restResponse.aepssattlement;

import com.google.gson.annotations.SerializedName;

public class AEPSSettlementCommissionResponse {
    @SerializedName("Transfer_Amount_")
    public String transferAmount;
    @SerializedName("Payment_Id_")
    public String paymentID;
    @SerializedName("Commission_")
    public String commission;
    @SerializedName("TotalPayable_")
    public String totalPayable;
}
