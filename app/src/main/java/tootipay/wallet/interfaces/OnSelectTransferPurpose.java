package tootipay.wallet.interfaces;

import tootipay.wallet.di.ResponseHelper.PurposeOfTransferListResponse;

public interface OnSelectTransferPurpose {
    void onSelectTransferPurpose(PurposeOfTransferListResponse response);
}
