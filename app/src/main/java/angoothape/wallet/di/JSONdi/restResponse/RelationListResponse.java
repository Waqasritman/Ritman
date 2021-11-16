package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class RelationListResponse extends ApiResponse<List<RelationListResponse>> {
    @SerializedName("RelationID")
    public int id;
    @SerializedName("RelationName")
    public String relationName;

}
