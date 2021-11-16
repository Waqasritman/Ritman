package angoothape.wallet.di.JSONdi.restResponse.ledger;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class LedgerRoot extends ApiResponse<LedgerRoot> {
    @SerializedName("Opening_Bal_data_")
    public List<OpeningBalData> opening_Bal_data_;
    @SerializedName("Statement_Of_Account_")
    public List<StatementOfAccount> statement_Of_Account_;
    @SerializedName("Credit_data_")
    public List<CreditData> credit_data_;
    @SerializedName("Debit_data_")
    public List<DebitData> debit_data_;
    @SerializedName("Closing_Bal_data_")
    public List<ClosingBalData> closing_Bal_data_;
}
