package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class OsVersionResponse extends ApiResponse<OsVersionResponse> {
    @SerializedName("Andriod_Version")
    public String Andriod_Version;

    @SerializedName("Force_Update")
    public boolean Force_Update;
}
