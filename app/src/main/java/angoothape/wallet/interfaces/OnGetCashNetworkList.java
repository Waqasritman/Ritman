package angoothape.wallet.interfaces;

import angoothape.wallet.di.XMLdi.ResponseHelper.GetCashNetworkListResponse;

import java.util.List;

public interface OnGetCashNetworkList extends OnMessageInterface {
    void onGetNetworkList(List<GetCashNetworkListResponse> networkLists);
}
