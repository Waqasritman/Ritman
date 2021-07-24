package ritman.wallet.interfaces;

import ritman.wallet.di.XMLdi.ResponseHelper.GetCashNetworkListResponse;

import java.util.List;

public interface OnGetCashNetworkList extends OnMessageInterface {
    void onGetNetworkList(List<GetCashNetworkListResponse> networkLists);
}
