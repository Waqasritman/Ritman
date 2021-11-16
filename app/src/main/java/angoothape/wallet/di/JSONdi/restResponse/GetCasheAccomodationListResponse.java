package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class GetCasheAccomodationListResponse extends ApiResponse<List<GetCasheAccomodationListResponse>> {

    @SerializedName("ID")
    public Integer ID;
    @SerializedName("Value")
    String Value;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
