package angoothape.wallet.interfaces;

import java.util.List;

import angoothape.wallet.di.JSONdi.restResponse.RechargePlansResponse;

public interface RechargePlanName extends OnMessageInterface{
    void onRechargePlanName(List<RechargePlansResponse> responseList);
    void onSelectRechargePlanName(RechargePlansResponse PlanName);
}
