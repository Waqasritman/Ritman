package ritman.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import ritman.wallet.di.generic_response.ApiResponse;

public class VerifyBeneficiary extends ApiResponse<VerifyBeneficiary> {
    @SerializedName("TransactionNumber")
    public String transactionNumber;
    @SerializedName("CustomerNo")
    public String customerNo;
    @SerializedName("BeneficiaryNo")
    public String beneficiaryNo;
}
