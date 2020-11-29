package totipay.wallet.interfaces;

import java.util.List;

import totipay.wallet.di.ResponseHelper.GetPrepaidPlansResponse;

public interface OnGetPrepaidPlans extends OnMessageInterface {
    void onGetPrepaidPlans(List<GetPrepaidPlansResponse> plansList);

    void onSelectPlan(GetPrepaidPlansResponse plan);
}
