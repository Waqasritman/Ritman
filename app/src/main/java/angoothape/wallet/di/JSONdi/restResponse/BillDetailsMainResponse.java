package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import angoothape.wallet.di.generic_response.ApiResponse;

public class BillDetailsMainResponse extends ApiResponse<BillDetailResponse> {
    @SerializedName("Customer_ID_")
    public String customerID;
    @SerializedName("Bill_Details_Resp_")
    public BillDetailResponse billDetailResponse;
}
