package ritman.wallet.interfaces;

import java.util.List;

import ritman.wallet.di.JSONdi.restResponse.GetWRBillerFieldsResponseN;
import ritman.wallet.di.XMLdi.ResponseHelper.WRBillDetailsResponse;

public interface OnWRBillDetail extends OnMessageInterface {
    void onBillDetails(List<GetWRBillerFieldsResponseN> response);
}
