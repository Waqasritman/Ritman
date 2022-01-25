package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class BusBlockTicketResponse extends ApiResponse<BusBlockTicketResponse> {
    @SerializedName("status")
    public String status;
    @SerializedName("ReferenceNo")
    public String referenceNo;
}
