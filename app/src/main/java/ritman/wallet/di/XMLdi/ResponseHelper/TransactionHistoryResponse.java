package ritman.wallet.di.XMLdi.ResponseHelper;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ritman.wallet.di.generic_response.ApiResponse;

public class TransactionHistoryResponse extends ApiResponse<List<TransactionHistoryResponse>> {
    @SerializedName("TRANSACTIONDATE")
    public String transactionDate;
    @SerializedName("PURPOSEOFTRANSFER")
    public String purposeOfTransfer;
    @SerializedName("PAYMENTTYPE")
    public String paymentType;
    @SerializedName("BANKNAME")
    public String bankName;
    @SerializedName("TRANSACTIONNUMBER")
    public String transactionNumber;
    @SerializedName("SENDERNAME")
    public String senderName;
    @SerializedName("STATUSID")
    public int statusID;
    @SerializedName("PAYMENTTYPEID")
    public int paymentTypeID;
    @SerializedName("RECEIVERNAME")
    public String receiverName;
    @SerializedName("SENDINGAMOUNT")
    public String sendingAmount;
    @SerializedName("RECEIVINGAMOUNT")
    public String receiverAmount;
    @SerializedName("STATUS")
    public String status;
    @SerializedName("CCYSHORTNAME")
    public String currency;
}
