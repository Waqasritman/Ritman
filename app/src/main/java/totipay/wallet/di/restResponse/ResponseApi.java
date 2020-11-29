package totipay.wallet.di.restResponse;

import com.google.gson.annotations.SerializedName;

public class ResponseApi {

    @SerializedName("ResponseCode")
    public String status;

    @SerializedName("Description")
    public String description;

}
