package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

public class FundingBankingDetailsResponse {
    @SerializedName("Bank_Name")
    public String bankName;
    @SerializedName("Account_No")
    public String accountNo;
    @SerializedName("IFSC_Code")
    public String ifscCode;
    @SerializedName("Sr_No")
    public String srNO;
}
