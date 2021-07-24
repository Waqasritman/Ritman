package ritman.wallet.interfaces;

import ritman.wallet.di.XMLdi.ResponseHelper.GetCountryListResponse;

public interface OnSelectCountry {
    void onSelectCountry(GetCountryListResponse country);
}
