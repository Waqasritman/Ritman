package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.GetTransactionReceiptResponse;

public interface OnGetTransactionReceipt extends OnMessageInterface {
    void onGetTransactionReceipt(GetTransactionReceiptResponse receiptResponse);
}
