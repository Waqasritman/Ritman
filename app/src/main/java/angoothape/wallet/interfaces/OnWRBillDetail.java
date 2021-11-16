package angoothape.wallet.interfaces;

import java.util.List;

import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerFieldsResponseN;

public interface OnWRBillDetail extends OnMessageInterface {
    void onBillDetails(List<GetWRBillerFieldsResponseN> response);
}
