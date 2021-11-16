package angoothape.wallet.interfaces;

import angoothape.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;

public interface OnSelectBeneficiary {
    void onSelectBeneficiary(GetBeneficiaryListResponse response);
    // 1 for Active, 0 for Inactive
    void onChangeTheStatusOfBeneficiary(GetBeneficiaryListResponse response , int pushToActive , int position);
}
