package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class BanksList extends ApiResponse<List<BanksList>> {
    @SerializedName("ID_")
    public String id;
    @SerializedName("Bank_Name_")
    public String bank_name;
    @SerializedName("IFSC_CODE_")
    public String ifscCode;
}
