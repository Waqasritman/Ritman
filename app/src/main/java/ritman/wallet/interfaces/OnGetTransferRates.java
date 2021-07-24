package ritman.wallet.interfaces;

import ritman.wallet.di.XMLdi.ResponseHelper.CalTransferResponse;

public interface OnGetTransferRates extends OnMessageInterface {
    void onGetTransferRates(CalTransferResponse response);
}
