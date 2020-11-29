package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.WalletTransferHistoryResponse;

public interface OnSelectWalletTransaction {
    void onSelectWalletTransaction(WalletTransferHistoryResponse response);
    void onSelectReceipt(WalletTransferHistoryResponse response);
}
