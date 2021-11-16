package angoothape.wallet.interfaces;

import angoothape.wallet.di.JSONdi.restRequest.TransactionHistoryRequest;

public interface OnApplyFilterInterface {
    void onApply(TransactionHistoryRequest request);
}
