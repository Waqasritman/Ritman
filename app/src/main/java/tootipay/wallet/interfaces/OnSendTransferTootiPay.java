package tootipay.wallet.interfaces;

public interface OnSendTransferTootiPay extends OnMessageInterface {
    void onMoneyTransferSuccessfully(String transactionNumber);
}
