package ritman.wallet.interfaces;

public interface OnWalletBalanceReceived extends OnMessageInterface{
    void onWalletBalanceReceived(String walletBalance);
    void onLockWalletOption(boolean isLocked);
}
