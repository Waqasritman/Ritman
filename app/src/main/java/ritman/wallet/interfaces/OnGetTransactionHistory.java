package ritman.wallet.interfaces;

import ritman.wallet.di.XMLdi.ResponseHelper.TransactionHistoryResponse;

import java.util.List;

public interface OnGetTransactionHistory extends OnMessageInterface {
    void onGetHistoryList(List<TransactionHistoryResponse> historyList);
}
