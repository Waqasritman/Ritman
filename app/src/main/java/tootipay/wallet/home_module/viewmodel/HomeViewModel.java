package tootipay.wallet.home_module.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.List;

import tootipay.wallet.di.ResponseHelper.CustomerProfile;
import tootipay.wallet.di.ResponseHelper.GetCustomerWalletDetailsResponse;

public class HomeViewModel extends ViewModel {
    public CustomerProfile customerProfile = new CustomerProfile();
    public List<GetCustomerWalletDetailsResponse> walletDetailsResponses;

}
