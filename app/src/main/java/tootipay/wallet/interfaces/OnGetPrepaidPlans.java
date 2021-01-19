package tootipay.wallet.interfaces;

import java.util.List;

import tootipay.wallet.di.ResponseHelper.GetPrepaidPlansResponse;

public interface OnGetPrepaidPlans extends OnMessageInterface {
    void onGetPrepaidPlans(List<GetPrepaidPlansResponse> plansList);

    void onSelectPlan(GetPrepaidPlansResponse plan);
}
