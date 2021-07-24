package ritman.wallet.interfaces;

import java.util.List;

import ritman.wallet.di.JSONdi.restResponse.GetWRBillerCategoryResponseC;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerNamesResponseC;
import ritman.wallet.di.JSONdi.restResponse.PlanCategoriesResponse;

public interface PlanName extends OnMessageInterface{
    void onPlanName(List<PlanCategoriesResponse> responseList);
    void onSelectPlanName(PlanCategoriesResponse PlanName);
}
