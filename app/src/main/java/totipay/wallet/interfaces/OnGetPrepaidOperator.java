package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.PrepaidOperatorResponse;

public interface OnGetPrepaidOperator extends OnMessageInterface {
    void onGetPrepaidOperator(PrepaidOperatorResponse operator);
}
