package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

public class VerifyOtpResponse  {

    @SerializedName("ResponseCode")
    public Integer responseCode;
    @SerializedName("Description")
    public String description;
    @SerializedName("data")
    public Object data;
}
