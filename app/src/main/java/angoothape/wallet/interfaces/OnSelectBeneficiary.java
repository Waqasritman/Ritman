package angoothape.wallet.interfaces;

import angoothape.wallet.di.JSONdi.restResponse.aepssattlement.AEPSBeneficiary;
import angoothape.wallet.di.XMLdi.ResponseHelper.GetBeneficiaryListResponse;

public interface OnSelectBeneficiary {
    void onSelectBeneficiary(GetBeneficiaryListResponse response);

    void onSelectAEPSBeneficiary(AEPSBeneficiary response);

    // 1 for Active, 0 for Inactive
    void onChangeTheStatusOfBeneficiary(GetBeneficiaryListResponse response, int pushToActive, int position);
}
