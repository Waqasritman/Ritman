package tootipay.wallet.interfaces;

import tootipay.wallet.di.ResponseHelper.GetSourceOfIncomeListResponse;

public interface OnSelectSourceOfIncome {
    void onSelectSourceOfIncome(GetSourceOfIncomeListResponse response);
}
