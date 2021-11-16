package angoothape.wallet.di.JSONdi.restResponse.ledger;

import com.google.gson.annotations.SerializedName;

public class StatementOfAccount {
    @SerializedName("Date")
    public String date;
    @SerializedName("Ref_No")
    public String ref_No;
    @SerializedName("Description")
    public String description;
    @SerializedName("Debit_INR")
    public String debit_INR;
    @SerializedName("Credit_INR")
    public String credit_INR;
}
