package tootipay.wallet.interfaces;

import java.util.List;

import tootipay.wallet.di.ResponseHelper.YLocationResponse;

public interface OnGetYLocation extends OnMessageInterface {
    void onGetYLocations(List<YLocationResponse> yLocations);

    void onSelectYLocation(YLocationResponse location);
}
