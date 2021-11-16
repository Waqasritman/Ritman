package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

public class BeneficiaryOTPResponse {
    @SerializedName("responseCode")
    public String responseCode;
    @SerializedName("responseMsg")
    public String message;
    @SerializedName("txnId")
    public String txnID;
}
