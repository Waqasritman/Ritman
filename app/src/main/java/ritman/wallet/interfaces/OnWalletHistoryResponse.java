package ritman.wallet.interfaces;

import java.util.List;

import ritman.wallet.di.XMLdi.ResponseHelper.WalletTransferHistoryResponse;

public interface OnWalletHistoryResponse extends OnMessageInterface {
    void onHistory(List<WalletTransferHistoryResponse> list);
}
