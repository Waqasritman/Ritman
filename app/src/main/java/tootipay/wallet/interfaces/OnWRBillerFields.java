package tootipay.wallet.interfaces;

import java.util.List;

import tootipay.wallet.di.ResponseHelper.WRBillerFieldsResponse;

public interface OnWRBillerFields extends OnMessageInterface {
    void onWRBillerField(List<WRBillerFieldsResponse> response);
}
