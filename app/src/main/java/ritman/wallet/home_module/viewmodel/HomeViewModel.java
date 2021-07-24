package ritman.wallet.home_module.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.List;

import ritman.wallet.di.XMLdi.ResponseHelper.CustomerProfile;
import ritman.wallet.di.XMLdi.ResponseHelper.GetCustomerWalletDetailsResponse;

public class HomeViewModel extends ViewModel {
    public CustomerProfile customerProfile = new CustomerProfile();
    public List<GetCustomerWalletDetailsResponse> walletDetailsResponses;

}
