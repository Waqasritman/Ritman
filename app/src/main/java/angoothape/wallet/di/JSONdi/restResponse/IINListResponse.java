package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;


public class IINListResponse extends ApiResponse<List<IINListResponse>> {
    @SerializedName("SNO")
    public String SNO;
    @SerializedName("Issuer_Bank_Name")
    public String Issuer_Bank_Name;
    @SerializedName("IIN")
    public String IIN;
    @SerializedName("Acquirer_id")
    public String Acquirer_id;
    @SerializedName("Bank_Code")
    public String Bank_Code;
}
