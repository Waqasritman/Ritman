package ritman.wallet.interfaces;

import java.util.List;

import ritman.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.WRBillerPlanResponse;

public interface OnBillerPlans extends OnMessageInterface {
    //void onBillerPlans(List<WRBillerPlanResponse> plansList);

    void onBillerPlanSelect(PrepaidPlanResponse.Plan plan);
}
