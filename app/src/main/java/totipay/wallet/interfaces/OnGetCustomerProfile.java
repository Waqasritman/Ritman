package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.CustomerProfile;

public interface OnGetCustomerProfile extends OnMessageInterface {
    void onGetCustomerProfile(CustomerProfile customerProfile);
}
