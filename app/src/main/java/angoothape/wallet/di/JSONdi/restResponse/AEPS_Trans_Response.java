package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.JSONdi.restRequest.DecryptedResponse;
import angoothape.wallet.di.generic_response.ApiResponse;

public class AEPS_Trans_Response extends ApiResponse<AEPS_Trans_Response> {
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

    @SerializedName("Mini_Statement_Json_Str_")
    public List<MiniStatement> miniStatementList;

    public static class MiniStatement {
        @SerializedName("Date")
        public String date;

        @SerializedName("Mode_Of_TXN")
        public String modeOfTxn;

        @SerializedName("DC")
        public String dc;

        @SerializedName("Amount")
        public String amount;
    }


}
