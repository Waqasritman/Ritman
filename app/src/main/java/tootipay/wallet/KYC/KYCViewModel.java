package tootipay.wallet.KYC;

import androidx.lifecycle.ViewModel;

import tootipay.wallet.di.RequestHelper.EditCustomerProfileRequest;

public class KYCViewModel extends ViewModel {
    public byte[] frontPicture;
    public byte[] backPicture;
    public byte[] customerPicture;
    public EditCustomerProfileRequest editCustomerProfileRequest = new EditCustomerProfileRequest();

}
