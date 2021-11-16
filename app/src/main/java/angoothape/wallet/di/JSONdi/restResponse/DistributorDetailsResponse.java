package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class DistributorDetailsResponse extends ApiResponse<DistributorDetailsResponse> {
    @SerializedName("Distributor_Available_Balance_INR")
    public String balance;

    @SerializedName("Distributor_Sub_Agents")
    List<DistributorAgents> distributorAgents;


    public String getBalance() {
        return balance;
    }

    public List<DistributorAgents> getDistributorAgents() {
        return distributorAgents;
    }
}
