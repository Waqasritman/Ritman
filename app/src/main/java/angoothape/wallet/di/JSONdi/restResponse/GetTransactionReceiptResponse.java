package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class GetTransactionReceiptResponse extends ApiResponse<GetTransactionReceiptResponse> {

    @SerializedName("TransactionNumber")
    public String transactionNumber;
    @SerializedName("TransactionDateTime")
    public String transactionDateTime;
    @SerializedName("SendingAmount")
    public Float sendingAmount;
    @SerializedName("PayInCurrency")
    public String payInCurrency;
    @SerializedName("ReceivingAmount")
    public Float receivingAmount;

    @SerializedName("PayOutCurrency")
    public String payOutCurrency;


    @SerializedName("CommissionCharge")
    public Float commissionCharge;
    @SerializedName("OtherCharges")
    public Float otherCharges;
    @SerializedName("VATCharges")
    public Float vatCharges;
    @SerializedName("VATPercentage")
    public Float vatPercentage;
    @SerializedName("TotalPayable")
    public  Float totalPayable;
    @SerializedName("ExchangeRate")
    public Float exchangeRate;
    @SerializedName("PurposeOfTransfer")
    public String purposeOfTransfer;
    @SerializedName("RemitterNo")
    public  String remitterNo;
    @SerializedName("RemitterName")
    public String remitterName;
    @SerializedName("RemitterContactNo")
    public String remitterContactNo;
    @SerializedName("RemitterAddress")
    public String remitterAddress;
    @SerializedName("Remitter_DOB")
    public String remitter_DOB;
    @SerializedName("ID_Type")
    public String id_Type;
    @SerializedName("ClientTxnNo")
    public  String clientTxnNo;
    @SerializedName("SenderIDNumber")
    public String senderIDNumber;
    @SerializedName("SenderIDExpiry")
    public String senderIDExpiry;
    @SerializedName("RelationWithBeneficiary")
    public String relationWithBeneficiary;
    @SerializedName("BenMessageToAgent")
    public String benMessageToAgent;
    @SerializedName("RemMessageToBen")
    public String remMessageToBen;
    @SerializedName("BeneficiaryNo")
    public  String beneficiaryNo;
    @SerializedName("BeneficiaryName")
    public  String beneficiaryName;
    @SerializedName("BeneficiaryAddress")
    public  String beneficiaryAddress;
    @SerializedName("BeneficiaryContactNo")
    public  String beneficiaryContactNo;
    @SerializedName("BankName")
    public String bankName;
    @SerializedName("BankAccountNo")
    public  String bankAccountNo;
    @SerializedName("BankBranch")
    public String bankBranch;
    @SerializedName("BankAddress")
    public String bankAddress;
    @SerializedName("BankCode")
    public  String bankCode;
    @SerializedName("PayOutPhone")
    public String payOutPhone;
    @SerializedName("CardNo")
    public String cardNo;
    @SerializedName("CustomerEmail")
    public String customerEmail;
    @SerializedName("MembershipType")
    public String membershipType;
    @SerializedName("PayInCountry")
    public  String payInCountry;
    @SerializedName("PayOutCountry")
    public  String payOutCountry;
    @SerializedName("earnPoint")
    public String earnPoint;
    @SerializedName("AvailPoints")
    public  String availPoints;
    @SerializedName("PaymentMode")
    public  String paymentMode;
    @SerializedName("PayoutAgent_Name")
    public String payoutAgent_Name;
    @SerializedName("EnteredBy")
    public String enteredBy;

}
