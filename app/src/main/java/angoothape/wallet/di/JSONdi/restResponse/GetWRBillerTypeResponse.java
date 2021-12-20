package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class GetWRBillerTypeResponse extends ApiResponse<List<GetWRBillerTypeResponse>>{

    @SerializedName("ID")
    private Integer iD;
    @SerializedName("Name")
    private String name;

    @SerializedName("URL")
    private String uRL;


    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getURL() {
        return uRL;
    }

    public void setURL(String uRL) {
        this.uRL = uRL;
    }


}
