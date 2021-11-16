package angoothape.wallet.interfaces;

import angoothape.wallet.di.XMLdi.ResponseHelper.CalTransferResponse;

public interface OnGetTransferRates extends OnMessageInterface {
    void onGetTransferRates(CalTransferResponse response);
}
