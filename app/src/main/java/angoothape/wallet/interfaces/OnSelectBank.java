package angoothape.wallet.interfaces;

import angoothape.wallet.di.JSONdi.restResponse.BankListResponse;
import angoothape.wallet.di.JSONdi.restResponse.BanksList;

public interface OnSelectBank {
    void onSelectBank(BankListResponse bankDetails);
    void onSelectBank(BanksList bankDetails);
    void onSelectBankName(String bankName);
    void onSelectBranch(BankListResponse branchName);
}
