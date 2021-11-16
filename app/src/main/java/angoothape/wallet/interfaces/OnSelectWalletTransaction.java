package angoothape.wallet.interfaces;

import angoothape.wallet.di.XMLdi.ResponseHelper.WalletTransferHistoryResponse;

public interface OnSelectWalletTransaction {
    void onSelectWalletTransaction(WalletTransferHistoryResponse response);
    void onSelectReceipt(WalletTransferHistoryResponse response);
}
