package ritman.wallet.interfaces;

import java.util.List;

import ritman.wallet.di.JSONdi.restResponse.PlanCategoriesResponse;
import ritman.wallet.di.JSONdi.restResponse.RechargePlansResponse;

public interface RechargePlanName extends OnMessageInterface{
    void onRechargePlanName(List<RechargePlansResponse> responseList);
    void onSelectRechargePlanName(RechargePlansResponse PlanName);
}
