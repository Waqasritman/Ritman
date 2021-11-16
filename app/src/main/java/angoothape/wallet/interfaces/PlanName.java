package angoothape.wallet.interfaces;

import java.util.List;

import angoothape.wallet.di.JSONdi.restResponse.PlanCategoriesResponse;

public interface PlanName extends OnMessageInterface{
    void onPlanName(List<PlanCategoriesResponse> responseList);
    void onSelectPlanName(PlanCategoriesResponse PlanName);
}
