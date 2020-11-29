package totipay.wallet.interfaces;

import java.util.List;

import totipay.wallet.di.ResponseHelper.WRBillerFieldsResponse;

public interface OnWRBillerFields extends OnMessageInterface {
    void onWRBillerField(List<WRBillerFieldsResponse> response);
}
