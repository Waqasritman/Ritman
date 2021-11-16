package angoothape.wallet.interfaces;

import angoothape.wallet.di.XMLdi.ResponseHelper.PurposeOfTransferListResponse;

public interface OnSelectTransferPurpose {
    void onSelectTransferPurpose(PurposeOfTransferListResponse response);
}
