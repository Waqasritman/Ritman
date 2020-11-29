package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.GetBankNetworkListResponse;

import java.util.List;

public interface OnGetBankNetworkListTaskInterface {
    void onSuccess(List<GetBankNetworkListResponse> response);
    void onMessageResponse(String message);
}
