package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class RefundOTPResponse extends ApiResponse<RefundOTPResponse> {

    @SerializedName("responseCode")
    public String response;

    @SerializedName("responseMsg")
    public String responseMSG;

    @SerializedName("txnId")
    public String txtID;

}
