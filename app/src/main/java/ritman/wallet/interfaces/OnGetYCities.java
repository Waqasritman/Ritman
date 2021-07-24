package ritman.wallet.interfaces;

import java.util.List;

import ritman.wallet.di.XMLdi.ResponseHelper.YCityResponse;

public interface OnGetYCities extends OnMessageInterface {
    void onGetCities(List<YCityResponse> citiesList);

    void onSelectYCity(YCityResponse city);
}
