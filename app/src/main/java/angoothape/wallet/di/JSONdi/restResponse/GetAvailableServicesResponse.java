package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class GetAvailableServicesResponse extends ApiResponse<GetAvailableServicesResponse> {

    @SerializedName("status")
    public String status;

    @SerializedName("services")
    public List<Services> servicesList = new ArrayList<>();

}
