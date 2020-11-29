package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.GetBeneficiaryListResponse;

public interface OnSelectBeneficiary {
    void onSelectBeneficiary(GetBeneficiaryListResponse response);
}
