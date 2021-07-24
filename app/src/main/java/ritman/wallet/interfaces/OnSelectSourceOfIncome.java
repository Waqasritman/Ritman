package ritman.wallet.interfaces;

import ritman.wallet.di.XMLdi.ResponseHelper.GetSourceOfIncomeListResponse;

public interface OnSelectSourceOfIncome {
    void onSelectSourceOfIncome(GetSourceOfIncomeListResponse response);
}
