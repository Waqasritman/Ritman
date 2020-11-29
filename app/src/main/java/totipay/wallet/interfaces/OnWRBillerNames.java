package totipay.wallet.interfaces;

import java.util.List;

import totipay.wallet.di.ResponseHelper.WRBillerNamesResponse;

public interface OnWRBillerNames extends OnMessageInterface {
    void onBillerNamesResponse(List<WRBillerNamesResponse> responses);

    void onSelectBillerName(WRBillerNamesResponse billerName);
}
