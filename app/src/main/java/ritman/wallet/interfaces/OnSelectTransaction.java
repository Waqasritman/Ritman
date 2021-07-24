package ritman.wallet.interfaces;

import ritman.wallet.di.XMLdi.ResponseHelper.GetTransactionReceiptResponse;

public interface OnSelectTransaction {
    void onSelectTransaction(GetTransactionReceiptResponse receiptResponse);
    void onSelectTransactionReceipt(String txtNumber);
    void onRepeatTransaction(GetTransactionReceiptResponse receiptResponse);
}
