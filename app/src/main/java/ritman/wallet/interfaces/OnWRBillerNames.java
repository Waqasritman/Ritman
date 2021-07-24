package ritman.wallet.interfaces;

import java.util.List;

import ritman.wallet.di.JSONdi.restResponse.GetWRBillerNamesResponseC;
import ritman.wallet.di.XMLdi.ResponseHelper.WRBillerNamesResponse;

public interface OnWRBillerNames extends OnMessageInterface {
    void onBillerNamesList(List<GetWRBillerNamesResponseC> responses);

    void onSelectBillerName(GetWRBillerNamesResponseC billerName);
}
