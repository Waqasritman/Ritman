package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class BusTicketConfirmResponse extends ApiResponse<BusTicketConfirmResponse> {
    @SerializedName("status")
    public String status;
    @SerializedName("TicketNo")
    public String ticketNo;
}
