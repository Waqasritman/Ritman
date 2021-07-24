package ritman.wallet.interfaces;

import java.util.List;

import ritman.wallet.di.JSONdi.restResponse.GetWRCountryListResponseC;
import ritman.wallet.di.XMLdi.ResponseHelper.WRCountryListResponse;

public interface OnWRCountryList extends OnMessageInterface {
    void onWRCountryList(List<GetWRCountryListResponseC> list);
    void onWRSelectCountry(GetWRCountryListResponseC country);
}
