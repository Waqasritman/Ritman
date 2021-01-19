package tootipay.wallet.interfaces;

import tootipay.wallet.di.ResponseHelper.CalTransferResponse;

public interface OnGetTransferRates extends OnMessageInterface {
    void onGetTransferRates(CalTransferResponse response);
}
