package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.GetCashNetworkListResponse;

import java.util.List;

public interface OnGetCashNetworkList extends OnMessageInterface {
    void onGetNetworkList(List<GetCashNetworkListResponse> networkLists);
}
