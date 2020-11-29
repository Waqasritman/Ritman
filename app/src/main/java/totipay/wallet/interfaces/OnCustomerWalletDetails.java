package totipay.wallet.interfaces;

import java.util.List;

import totipay.wallet.di.ResponseHelper.GetCustomerWalletDetailsResponse;

public interface OnCustomerWalletDetails extends OnMessageInterface{
    void onCustomerWalletDetails(List<GetCustomerWalletDetailsResponse> walletList);
    void onSelectWallet(GetCustomerWalletDetailsResponse wallet);
}
