package angoothape.wallet.interfaces;

public interface OnOTPVerified extends OnMessageInterface{
    void onOTPVerified(boolean isVerified);
}
