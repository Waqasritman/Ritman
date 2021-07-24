package ritman.wallet.interfaces;

import ritman.wallet.di.XMLdi.ResponseHelper.CustomerProfile;

public interface OnGetCustomerProfile extends OnMessageInterface {
    void onGetCustomerProfile(CustomerProfile customerProfile);
}
