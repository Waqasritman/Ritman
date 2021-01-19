package tootipay.wallet.interfaces;

import tootipay.wallet.di.ResponseHelper.GetBankNetworkListResponse;

public interface OnSelectBank {
    void onSelectBank(GetBankNetworkListResponse bankDetails);
    void onSelectBankName(String bankName);
    void onSelectBranch(GetBankNetworkListResponse branchName);
}
