package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class RitmanPaySendResponse extends ApiResponse<RitmanPaySendResponse> {
    @SerializedName("TransactionNumber")
    public String TransactionNumber;
    @SerializedName("CustomerNo")
    public String CustomerNo;
    @SerializedName("BeneficiaryNo")
    public String BeneficiaryNo;

    public String getTransactionNumber() {
        return TransactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        TransactionNumber = transactionNumber;
    }

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String customerNo) {
        CustomerNo = customerNo;
    }

    public String getBeneficiaryNo() {
        return BeneficiaryNo;
    }

    public void setBeneficiaryNo(String beneficiaryNo) {
        BeneficiaryNo = beneficiaryNo;
    }
}
