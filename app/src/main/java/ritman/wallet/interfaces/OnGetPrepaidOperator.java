package ritman.wallet.interfaces;

import ritman.wallet.di.XMLdi.ResponseHelper.PrepaidOperatorResponse;

public interface OnGetPrepaidOperator extends OnMessageInterface {
    void onGetPrepaidOperator(PrepaidOperatorResponse operator);

    void onErrorWithCode(String code, String error);
}
