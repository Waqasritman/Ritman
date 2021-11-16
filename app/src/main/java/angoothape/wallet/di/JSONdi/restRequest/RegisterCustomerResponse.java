package angoothape.wallet.di.JSONdi.restRequest;

import com.google.gson.annotations.SerializedName;

public class RegisterCustomerResponse  {
    @SerializedName("CustomerNumber")
    public String customerNo;
    @SerializedName("ResponseCode")
    public Integer responseCode;
    @SerializedName("Description")
    public String description;
}
