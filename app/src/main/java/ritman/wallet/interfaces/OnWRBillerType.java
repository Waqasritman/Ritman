package ritman.wallet.interfaces;

import java.util.List;

import ritman.wallet.di.JSONdi.restResponse.GetWRBillerTypeResponse;
import ritman.wallet.di.XMLdi.ResponseHelper.WRBillerTypeResponse;

public interface OnWRBillerType extends OnMessageInterface{
    void onBillerTypeList(List<GetWRBillerTypeResponse> billerTypeList);
    void onBillerTypeSelect(GetWRBillerTypeResponse billerType);
}
