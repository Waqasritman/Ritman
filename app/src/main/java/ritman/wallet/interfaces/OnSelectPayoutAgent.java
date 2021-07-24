package ritman.wallet.interfaces;

import ritman.wallet.di.XMLdi.ResponseHelper.GetCashNetworkListResponse;

public interface OnSelectPayoutAgent {
    void onSelectPayoutAgent(GetCashNetworkListResponse response);
}
