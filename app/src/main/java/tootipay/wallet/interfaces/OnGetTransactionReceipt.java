package tootipay.wallet.interfaces;

import tootipay.wallet.di.ResponseHelper.GetTransactionReceiptResponse;

public interface OnGetTransactionReceipt extends OnMessageInterface {
    void onGetTransactionReceipt(GetTransactionReceiptResponse receiptResponse);
}
