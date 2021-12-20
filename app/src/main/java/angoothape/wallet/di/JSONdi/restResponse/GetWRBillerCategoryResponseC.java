package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class GetWRBillerCategoryResponseC extends ApiResponse<List<GetWRBillerCategoryResponseC>> {

    @SerializedName("ID")
    public Integer ID;
    @SerializedName("Name")
    public String Name;

    @SerializedName("IconName")
    public String IconName;

    @SerializedName("IconURL")
    public String IconURL;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIconName() {
        return IconName;
    }

    public void setIconName(String iconName) {
        IconName = iconName;
    }

    public String getIconURL() {
        return IconURL;
    }

    public void setIconURL(String iconURL) {
        IconURL = iconURL;
    }
}
