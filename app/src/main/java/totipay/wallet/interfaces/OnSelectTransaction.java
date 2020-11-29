package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.GetTransactionReceiptResponse;

public interface OnSelectTransaction {
    void onSelectTransaction(GetTransactionReceiptResponse receiptResponse);
    void onSelectTransactionReceipt(String txtNumber);
    void onRepeatTransaction(GetTransactionReceiptResponse receiptResponse);
}
