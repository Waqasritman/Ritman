package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

public class CustomerTransactionHistory {
    @SerializedName("TxnNo")
    public String txnNo;//
    @SerializedName("TxnDate")
    public String txnDate;//
    @SerializedName("BeneName")
    public String beneName;//
    @SerializedName("AccountNumber")
    public String AccountNumber;//
    @SerializedName("TxnMode")
    public String TxnMode;
    @SerializedName("TxnAmount")
    public String TxnAmount;


}
