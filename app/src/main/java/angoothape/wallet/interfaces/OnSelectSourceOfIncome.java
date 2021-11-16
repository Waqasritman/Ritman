package angoothape.wallet.interfaces;

import angoothape.wallet.di.XMLdi.ResponseHelper.GetSourceOfIncomeListResponse;

public interface OnSelectSourceOfIncome {
    void onSelectSourceOfIncome(GetSourceOfIncomeListResponse response);
}
