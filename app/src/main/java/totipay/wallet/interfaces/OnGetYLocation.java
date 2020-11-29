package totipay.wallet.interfaces;

import java.util.List;

import totipay.wallet.di.ResponseHelper.YLocationResponse;

public interface OnGetYLocation extends OnMessageInterface {
    void onGetYLocations(List<YLocationResponse> yLocations);

    void onSelectYLocation(YLocationResponse location);
}
