package angoothape.wallet.interfaces;

import angoothape.wallet.di.XMLdi.ResponseHelper.GetCountryListResponse;

public interface OnSelectCountry {
    void onSelectCountry(GetCountryListResponse country);
}
