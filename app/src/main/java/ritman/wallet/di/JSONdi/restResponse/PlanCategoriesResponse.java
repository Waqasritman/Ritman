package ritman.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ritman.wallet.di.generic_response.ApiResponse;

public class PlanCategoriesResponse extends ApiResponse<List<PlanCategoriesResponse>> {
    @SerializedName("PlanCategory")
    public String PlanCategory;

    public String getPlanCategory() {
        return PlanCategory;
    }

    public void setPlanCategory(String planCategory) {
        PlanCategory = planCategory;
    }
}
