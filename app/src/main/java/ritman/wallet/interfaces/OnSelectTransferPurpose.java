package ritman.wallet.interfaces;

import ritman.wallet.di.XMLdi.ResponseHelper.PurposeOfTransferListResponse;

public interface OnSelectTransferPurpose {
    void onSelectTransferPurpose(PurposeOfTransferListResponse response);
}
