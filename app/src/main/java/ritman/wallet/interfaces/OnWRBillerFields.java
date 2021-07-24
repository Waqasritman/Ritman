package ritman.wallet.interfaces;

import java.util.List;

import ritman.wallet.di.JSONdi.restResponse.GetCasheAccomodationListResponse;
import ritman.wallet.di.JSONdi.restResponse.GetWRBillerFieldsResponseN;
import ritman.wallet.di.XMLdi.ResponseHelper.WRBillerFieldsResponse;

public interface OnWRBillerFields extends OnMessageInterface {
    void onWRBillerField(List<GetWRBillerFieldsResponseN> response);

}
