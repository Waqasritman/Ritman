package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BiometricKYCErrorResponse {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("responseCode")
    @Expose
    public String responseCode;
    @SerializedName("responseMessage")
    @Expose
    public String responseMessage;
}
