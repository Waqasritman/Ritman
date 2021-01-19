package tootipay.wallet.interfaces;

import tootipay.wallet.di.ResponseHelper.TransactionHistoryResponse;

import java.util.List;

public interface OnGetTransactionHistory extends OnMessageInterface {
    void onGetHistoryList(List<TransactionHistoryResponse> historyList);
}
