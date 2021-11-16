package angoothape.wallet.interfaces;

import angoothape.wallet.di.XMLdi.ResponseHelper.GetBankNetworkListResponse;

import java.util.List;

public interface OnGetBankNetworkListTaskInterface {
    void onSuccess(List<GetBankNetworkListResponse> response);
    void onMessageResponse(String message);
}
