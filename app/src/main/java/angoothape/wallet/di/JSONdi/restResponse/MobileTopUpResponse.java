package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class MobileTopUpResponse extends ApiResponse<List<MobileTopUpResponse>> {

    @SerializedName("ID")
    public Integer ID;
    @SerializedName("Name")
    public String Name;

}
