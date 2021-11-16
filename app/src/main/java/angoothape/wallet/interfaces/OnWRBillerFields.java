package angoothape.wallet.interfaces;

import java.util.List;

import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerFieldsResponseN;

public interface OnWRBillerFields extends OnMessageInterface {
    void onWRBillerField(List<GetWRBillerFieldsResponseN> response);

}
