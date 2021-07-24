package ritman.wallet.interfaces;

import ritman.wallet.di.XMLdi.ResponseHelper.WalletTransferHistoryResponse;

public interface OnSelectWalletTransaction {
    void onSelectWalletTransaction(WalletTransferHistoryResponse response);
    void onSelectReceipt(WalletTransferHistoryResponse response);
}
