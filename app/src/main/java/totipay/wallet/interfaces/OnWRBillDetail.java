package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.WRBillDetailsResponse;

public interface OnWRBillDetail extends OnMessageInterface {
    void onBillDetails(WRBillDetailsResponse response);
}
