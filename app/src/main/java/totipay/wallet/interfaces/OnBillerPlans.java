package totipay.wallet.interfaces;

import java.util.List;

import totipay.wallet.di.ResponseHelper.WRBillerPlanResponse;

public interface OnBillerPlans extends OnMessageInterface {
    void onBillerPlans(List<WRBillerPlanResponse> plansList);

    void onBillerPlanSelect(WRBillerPlanResponse billerPlan);
}
