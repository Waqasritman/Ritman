package tootipay.wallet.interfaces;

import tootipay.wallet.di.ResponseHelper.GetBeneficiaryListResponse;

public interface OnSelectBeneficiary {
    void onSelectBeneficiary(GetBeneficiaryListResponse response);
}
