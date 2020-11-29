package totipay.wallet.interfaces;

import java.util.List;

import totipay.wallet.di.ResponseHelper.YCityResponse;

public interface OnGetYCities extends OnMessageInterface {
    void onGetCities(List<YCityResponse> citiesList);

    void onSelectYCity(YCityResponse city);
}
