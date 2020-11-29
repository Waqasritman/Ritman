package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.GetSourceOfIncomeListResponse;

public interface OnSelectSourceOfIncome {
    void onSelectSourceOfIncome(GetSourceOfIncomeListResponse response);
}
