package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class YBCreateCustomerResponse extends ApiResponse<YBCreateCustomerResponse> {
    @SerializedName("Customer_MobileNo")
    public String Agent_MobileNo;
    @SerializedName("CustomerKYC_Response")
    public AgentCreateResponse AgentCreate_Response;

    public static class CustomerKYCResponse {
        @SerializedName("otpToken")
        public String otpToken;
        @SerializedName("requestId")
        public String requestID;

    }
}
