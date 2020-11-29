package totipay.wallet.interfaces;

public interface OnWalletBalanceReceived extends OnMessageInterface{
    void onWalletBalanceReceived(String walletBalance);
}
