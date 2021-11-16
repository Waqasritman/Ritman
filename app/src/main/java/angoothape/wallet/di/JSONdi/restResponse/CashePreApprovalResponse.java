package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class CashePreApprovalResponse extends ApiResponse<CashePreApprovalResponse> {

    @SerializedName("payLoad")
    public Object payLoad;

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("statusCode")
    public Integer statusCode;

}


