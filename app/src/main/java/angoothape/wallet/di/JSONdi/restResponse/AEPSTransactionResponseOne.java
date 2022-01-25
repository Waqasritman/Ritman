package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.JSONdi.restRequest.DecryptedResponse;
import angoothape.wallet.di.generic_response.ApiResponse;

public class AEPSTransactionResponseOne extends ApiResponse<AEPS_Trans_Response> {
    @SerializedName("BC_Name")
    public String bC_Name;
    @SerializedName("Agent_ID")
    public String agent_ID;
    @SerializedName("Decrypted_Response_")
    public DecryptedResponse decrypted_Response;

    @SerializedName("Txn_Date")
    public String txtDate;

    @SerializedName("Txn_Time")
    public String txnTime;

    //next both wil be same
    @SerializedName("Mini_Statement_Json_Str_")
    public MiniStatementError miniStatementError;

    public static class MiniStatementError {
        @SerializedName("Mini_Statement")
        public String error;
    }
}
