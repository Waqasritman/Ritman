package angoothape.wallet.interfaces;

import angoothape.wallet.di.XMLdi.ResponseHelper.GetTransactionReceiptResponse;

public interface OnSelectTransaction {
    void onSelectTransaction(GetTransactionReceiptResponse receiptResponse);
    void onSelectTransactionReceipt(String txtNumber);
    void onSelectTransactionAEPSReceipt(String txtNumber);
    void onRepeatTransaction(GetTransactionReceiptResponse receiptResponse);
}
