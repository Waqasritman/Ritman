package tootipay.wallet.interfaces;

import tootipay.wallet.di.ResponseHelper.WalletTransferHistoryResponse;

public interface OnSelectWalletTransaction {
    void onSelectWalletTransaction(WalletTransferHistoryResponse response);
    void onSelectReceipt(WalletTransferHistoryResponse response);
}
