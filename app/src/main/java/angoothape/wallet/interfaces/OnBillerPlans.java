package angoothape.wallet.interfaces;

import angoothape.wallet.di.JSONdi.restResponse.PrepaidPlanResponse;

public interface OnBillerPlans extends OnMessageInterface {
    //void onBillerPlans(List<WRBillerPlanResponse> plansList);

    void onBillerPlanSelect(PrepaidPlanResponse.Plan plan);
}
