package angoothape.wallet.interfaces;

import java.util.List;

import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerNamesResponseC;

public interface OnWRBillerNames extends OnMessageInterface {
    void onBillerNamesList(List<GetWRBillerNamesResponseC> responses);

    void onSelectBillerName(GetWRBillerNamesResponseC billerName);
}
