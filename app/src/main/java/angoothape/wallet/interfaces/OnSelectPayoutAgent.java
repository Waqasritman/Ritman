package angoothape.wallet.interfaces;

import angoothape.wallet.di.XMLdi.ResponseHelper.GetCashNetworkListResponse;

public interface OnSelectPayoutAgent {
    void onSelectPayoutAgent(GetCashNetworkListResponse response);
}
