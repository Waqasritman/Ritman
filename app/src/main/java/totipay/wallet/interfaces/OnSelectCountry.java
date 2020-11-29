package totipay.wallet.interfaces;

import totipay.wallet.di.ResponseHelper.GetCountryListResponse;

public interface OnSelectCountry {
    void onSelectCountry(GetCountryListResponse country);
}
