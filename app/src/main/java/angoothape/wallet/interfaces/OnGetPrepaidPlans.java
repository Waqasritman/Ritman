package angoothape.wallet.interfaces;

import java.util.List;

import angoothape.wallet.di.XMLdi.ResponseHelper.GetPrepaidPlansResponse;

public interface OnGetPrepaidPlans extends OnMessageInterface {
    void onGetPrepaidPlans(List<GetPrepaidPlansResponse> plansList);

    void onSelectPlan(GetPrepaidPlansResponse plan);
}
