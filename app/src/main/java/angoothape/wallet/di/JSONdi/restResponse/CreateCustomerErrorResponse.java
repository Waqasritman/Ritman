package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

public class CreateCustomerErrorResponse {
    @SerializedName("otpToken")
    public String otpToken = "";
    @SerializedName("requestId")
    public String requestId;
    @SerializedName("status")
    public String status;
    @SerializedName("responseCode")
    public String responseCode;
    @SerializedName("responseMessage")
    public String responseMessage;
}
