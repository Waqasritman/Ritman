package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillDetailsErrorResponse {
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("error_type")
    @Expose
    public String errorType;
    @SerializedName("error_code")
    @Expose
    public String errorCode;

}
