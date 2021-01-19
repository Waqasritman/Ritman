package tootipay.wallet.interfaces;

public interface OnOTPVerified extends OnMessageInterface{
    void onOTPVerified(boolean isVerified);
}
