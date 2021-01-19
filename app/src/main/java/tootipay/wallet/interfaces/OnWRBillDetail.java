package tootipay.wallet.interfaces;

import tootipay.wallet.di.ResponseHelper.WRBillDetailsResponse;

public interface OnWRBillDetail extends OnMessageInterface {
    void onBillDetails(WRBillDetailsResponse response);
}
