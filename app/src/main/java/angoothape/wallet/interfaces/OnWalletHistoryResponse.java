package angoothape.wallet.interfaces;

import java.util.List;

import angoothape.wallet.di.XMLdi.ResponseHelper.WalletTransferHistoryResponse;

public interface OnWalletHistoryResponse extends OnMessageInterface {
    void onHistory(List<WalletTransferHistoryResponse> list);
}
