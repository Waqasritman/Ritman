package ritman.wallet.interfaces;

import ritman.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;

public interface OnSelectBeneficiary {
    void onSelectBeneficiary(GetBeneficiaryListResponse response);
}
