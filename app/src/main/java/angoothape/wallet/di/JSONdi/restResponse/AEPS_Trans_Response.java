package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.JSONdi.restRequest.DecryptedResponse;
import angoothape.wallet.di.generic_response.ApiResponse;

public class AEPS_Trans_Response extends ApiResponse<AEPS_Trans_Response> {
    @SerializedName("BC_Name")
    public String bC_Name;
    @SerializedName("Agent_ID")
    public String agent_ID;
    @SerializedName("Decrypted_Response_")
    public DecryptedResponse decrypted_Response;

}
