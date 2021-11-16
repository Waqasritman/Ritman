package angoothape.wallet.interfaces;

import angoothape.wallet.di.XMLdi.ResponseHelper.PrepaidOperatorResponse;

public interface OnGetPrepaidOperator extends OnMessageInterface {
    void onGetPrepaidOperator(PrepaidOperatorResponse operator);

    void onErrorWithCode(String code, String error);
}
