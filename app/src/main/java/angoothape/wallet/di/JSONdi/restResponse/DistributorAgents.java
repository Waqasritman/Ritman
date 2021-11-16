package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

public class DistributorAgents {
    @SerializedName("Sub_Agent_ID")
    public String subAgentID;
    @SerializedName("Sub_Agent_Name")
    public String agentName;
    @SerializedName("Sub_Agent_Available_Balance_INR")
    public String balance;
}
