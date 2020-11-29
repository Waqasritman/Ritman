package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.CalTransferResponse;

public interface OnGetTransferRates extends OnMessageInterface {
    void onGetTransferRates(CalTransferResponse response);
}
