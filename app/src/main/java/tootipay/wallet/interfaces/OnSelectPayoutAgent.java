package tootipay.wallet.interfaces;

import tootipay.wallet.di.ResponseHelper.GetBankNetworkListResponse;
import tootipay.wallet.di.ResponseHelper.GetCashNetworkListResponse;

public interface OnSelectPayoutAgent {
    void onSelectPayoutAgent(GetCashNetworkListResponse response);
}
