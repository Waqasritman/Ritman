package tootipay.wallet.interfaces;

import tootipay.wallet.di.ResponseHelper.CustomerProfile;

public interface OnGetCustomerProfile extends OnMessageInterface {
    void onGetCustomerProfile(CustomerProfile customerProfile);
}
