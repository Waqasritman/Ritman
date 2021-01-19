package tootipay.wallet.interfaces;

import tootipay.wallet.di.ResponseHelper.GetCountryListResponse;

public interface OnSelectCountry {
    void onSelectCountry(GetCountryListResponse country);
}
