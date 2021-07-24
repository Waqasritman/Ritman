package ritman.wallet.interfaces;

import java.util.List;

import ritman.wallet.di.JSONdi.restResponse.GetCasheAccomodationListResponse;

public interface CasheAccomodationListInterface extends OnMessageInterface {
    void onAccomodation(List<GetCasheAccomodationListResponse> responseAccomodationList);
    void onSelectAccomodation(GetCasheAccomodationListResponse AccomodationSelect);
}
