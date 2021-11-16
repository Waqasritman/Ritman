package angoothape.wallet.KYC;

import androidx.lifecycle.ViewModel;

import angoothape.wallet.di.XMLdi.RequestHelper.EditCustomerProfileRequest;

public class KYCViewModel extends ViewModel {
    public byte[] frontPicture;
    public byte[] backPicture;
    public byte[] customerPicture;
    public EditCustomerProfileRequest editCustomerProfileRequest = new EditCustomerProfileRequest();

}
