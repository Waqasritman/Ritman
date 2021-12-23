package angoothape.wallet.interfaces;

import angoothape.wallet.di.JSONdi.restResponse.GetBusStationsResponse;

public interface OnSelectBusStation {
    void onSelectBusStation(GetBusStationsResponse response);
}
