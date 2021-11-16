package angoothape.wallet.interfaces;

import angoothape.wallet.di.XMLdi.ResponseHelper.GetTransactionReceiptResponse;

public interface OnGetTransactionReceipt extends OnMessageInterface {
    void onGetTransactionReceipt(GetTransactionReceiptResponse receiptResponse);
}
