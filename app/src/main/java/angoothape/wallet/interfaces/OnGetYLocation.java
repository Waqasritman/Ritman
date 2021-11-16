package angoothape.wallet.interfaces;

import java.util.List;

import angoothape.wallet.di.XMLdi.ResponseHelper.YLocationResponse;

public interface OnGetYLocation extends OnMessageInterface {
    void onGetYLocations(List<YLocationResponse> yLocations);

    void onSelectYLocation(YLocationResponse location);
}
