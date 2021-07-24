package ritman.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import ritman.wallet.di.generic_response.ApiResponse;

public class VerifyOtpResponse  {

    @SerializedName("ResponseCode")
    public Integer responseCode;
    @SerializedName("Description")
    public String description;
    @SerializedName("data")
    public Object data;
}
