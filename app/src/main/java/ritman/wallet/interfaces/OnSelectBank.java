package ritman.wallet.interfaces;

import ritman.wallet.di.JSONdi.restResponse.BankListResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.GetBankNetworkListResponse;

public interface OnSelectBank {
    void onSelectBank(BankListResponse bankDetails);
    void onSelectBankName(String bankName);
    void onSelectBranch(BankListResponse branchName);
}
