package tootipay.wallet.interfaces;

import java.util.List;

import tootipay.wallet.di.ResponseHelper.WalletTransferHistoryResponse;

public interface OnWalletHistoryResponse extends OnMessageInterface {
    void onHistory(List<WalletTransferHistoryResponse> list);
}
