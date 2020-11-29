package totipay.wallet.interfaces;

import java.util.List;

import totipay.wallet.di.ResponseHelper.WalletTransferHistoryResponse;

public interface OnWalletHistoryResponse extends OnMessageInterface {
    void onHistory(List<WalletTransferHistoryResponse> list);
}
