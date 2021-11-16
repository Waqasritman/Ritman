package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

public class CustomerServiceResponse {
    @SerializedName("Sr_No")
    public int srNo;
    @SerializedName("Sales_Officer")
    public String salesOfficer;
    @SerializedName("Contact_No")
    public String contactNo;
    @SerializedName("Email_ID")
    public String emailId;
}
