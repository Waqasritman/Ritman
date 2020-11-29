package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.TransactionHistoryResponse;

import java.util.List;

public interface OnGetTransactionHistory extends OnMessageInterface {
    void onGetHistoryList(List<TransactionHistoryResponse> historyList);
}
