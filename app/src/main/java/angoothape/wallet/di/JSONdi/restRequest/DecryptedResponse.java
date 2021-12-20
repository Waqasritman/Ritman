package angoothape.wallet.di.JSONdi.restRequest;

import com.google.gson.annotations.SerializedName;

public class DecryptedResponse {
    @SerializedName("STAN")
    public String sTAN;
    @SerializedName("RRN")
    public String rRN;
    @SerializedName("IIN")
    public String iIN;
    @SerializedName("ResponseCode")
    public String responseCode;
    @SerializedName("AccountType")
    public String accountType;
    @SerializedName("BalanceType")
    public String balanceType;
    @SerializedName("CurrancyCode")
    public String currancyCode;
    @SerializedName("BalanceIndicator")
    public String balanceIndicator;
    @SerializedName("BalanceAmount")
    public String balanceAmount;
    @SerializedName("AccountTypeLedger")
    public String accountTypeLedger;
    @SerializedName("BalanceTypeLedger")
    public String balanceTypeLedger;
    @SerializedName("CurrancyCodeLedger")
    public String currancyCodeLedger;
    @SerializedName("BalanceIndicatorLedger")
    public String balanceIndicatorLedger;
    @SerializedName("BalanceAmountLedger")
    public String balanceAmountLedger;
    @SerializedName("AccountTypeActual")
    public String accountTypeActual;
    @SerializedName("BalanceTypeActual")
    public String balanceTypeActual;
    @SerializedName("CurrancyCodeActual")
    public String currancyCodeActual;
    @SerializedName("BalanceIndicatorActual")
    public String balanceIndicatorActual;
    @SerializedName("BalanceAmountActual")
    public String balanceAmountActual;
    @SerializedName("Status")
    public int status;
    @SerializedName("UIDAIAuthenticationCode")
    public String uIDAIAuthenticationCode;
    @SerializedName("RetailerTxnId")
    public String retailerTxnId;
    @SerializedName("MiniStatementData")
    public String miniStatementData;
    @SerializedName("BankName")
    public String bankName;
    @SerializedName("OTPKey")
    public String oTPKey;
}
