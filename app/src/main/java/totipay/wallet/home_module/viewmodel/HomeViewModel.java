package totipay.wallet.home_module.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.List;

import totipay.wallet.di.ResponseHelper.CustomerProfile;
import totipay.wallet.di.ResponseHelper.GetCustomerWalletDetailsResponse;

public class HomeViewModel extends ViewModel {
    public CustomerProfile customerProfile = new CustomerProfile();
    public List<GetCustomerWalletDetailsResponse> walletDetailsResponses;

}
