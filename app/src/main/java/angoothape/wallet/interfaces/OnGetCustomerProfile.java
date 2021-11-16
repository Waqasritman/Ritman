package angoothape.wallet.interfaces;

import angoothape.wallet.di.XMLdi.ResponseHelper.CustomerProfile;

public interface OnGetCustomerProfile extends OnMessageInterface {
    void onGetCustomerProfile(CustomerProfile customerProfile);
}
