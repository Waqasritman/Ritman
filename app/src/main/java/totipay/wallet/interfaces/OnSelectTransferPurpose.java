package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.PurposeOfTransferListResponse;

public interface OnSelectTransferPurpose {
    void onSelectTransferPurpose(PurposeOfTransferListResponse response);
}
