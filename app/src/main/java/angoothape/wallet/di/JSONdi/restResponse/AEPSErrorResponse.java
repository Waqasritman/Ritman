package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AEPSErrorResponse {
    @SerializedName("Status")
    @Expose
    public boolean status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("Data")
    @Expose
    public String data;
}
