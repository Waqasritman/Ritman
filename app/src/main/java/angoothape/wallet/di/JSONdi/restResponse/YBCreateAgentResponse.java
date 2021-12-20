package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class YBCreateAgentResponse extends ApiResponse<YBCreateAgentResponse> {

//    @SerializedName("otpToken")
//    public String otpToken;
//
//    @SerializedName("requestId")
//    public String requestId;
//
//    @SerializedName("status")
//    public String status;
    @SerializedName("Agent_MobileNo")
    public String Agent_MobileNo;
    @SerializedName("AgentCreate_Response")
    public AgentCreateResponse AgentCreate_Response;

}
