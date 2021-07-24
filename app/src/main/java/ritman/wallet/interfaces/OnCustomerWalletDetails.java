package ritman.wallet.interfaces;

import java.util.List;

import ritman.wallet.di.XMLdi.ResponseHelper.GetCustomerWalletDetailsResponse;

public interface OnCustomerWalletDetails extends OnMessageInterface{
    void onCustomerWalletDetails(List<GetCustomerWalletDetailsResponse> walletList);
    void onSelectWallet(GetCustomerWalletDetailsResponse wallet);
}
