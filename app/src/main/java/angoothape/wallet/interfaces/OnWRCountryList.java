package angoothape.wallet.interfaces;

import java.util.List;

import angoothape.wallet.di.JSONdi.restResponse.GetWRCountryListResponseC;

public interface OnWRCountryList extends OnMessageInterface {
    void onWRCountryList(List<GetWRCountryListResponseC> list);
    void onWRSelectCountry(GetWRCountryListResponseC country);
}
