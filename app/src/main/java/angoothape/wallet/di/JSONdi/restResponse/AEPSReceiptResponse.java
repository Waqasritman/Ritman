package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.JSONdi.restRequest.DecryptedResponse;

public class AEPSReceiptResponse {
    @SerializedName("BC_Name")
    public String bC_Name;
    @SerializedName("Agent_ID")
    public String agent_ID;
    @SerializedName("Txn_Date")
    public String Txn_Date;
    @SerializedName("Txn_Time")
    public String Txn_Time;
    @SerializedName("Aadhar_Number")
    public String Aadhar_Number;
    @SerializedName("Txn_Amount")
    public String Txn_Amount;
    @SerializedName("Decrypted_Response_")
    public List<DecryptedResponse> decrypted_Response;
}

