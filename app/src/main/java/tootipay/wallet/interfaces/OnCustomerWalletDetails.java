package tootipay.wallet.interfaces;

import java.util.List;

import tootipay.wallet.di.ResponseHelper.GetCustomerWalletDetailsResponse;

public interface OnCustomerWalletDetails extends OnMessageInterface{
    void onCustomerWalletDetails(List<GetCustomerWalletDetailsResponse> walletList);
    void onSelectWallet(GetCustomerWalletDetailsResponse wallet);
}
