package tootipay.wallet.interfaces;

import java.util.List;

import tootipay.wallet.di.ResponseHelper.WRCountryListResponse;

public interface OnWRCountryList extends OnMessageInterface {
    void onWRCountryList(List<WRCountryListResponse> list);
    void onWRSelectCountry(WRCountryListResponse country);
}
