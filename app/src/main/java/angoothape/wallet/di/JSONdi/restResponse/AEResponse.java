package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class AEResponse extends ApiResponse<AEResponse> {
    @SerializedName("body")
    public String body;
}
