package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class GetBusStationsResponse extends ApiResponse<List<GetBusStationsResponse>> {
    @SerializedName("ID_")
    public long id;
    @SerializedName("Name_")
    public String name;
}
