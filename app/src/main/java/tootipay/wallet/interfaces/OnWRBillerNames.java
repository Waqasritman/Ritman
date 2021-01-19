package tootipay.wallet.interfaces;

import java.util.List;

import tootipay.wallet.di.ResponseHelper.WRBillerNamesResponse;

public interface OnWRBillerNames extends OnMessageInterface {
    void onBillerNamesResponse(List<WRBillerNamesResponse> responses);

    void onSelectBillerName(WRBillerNamesResponse billerName);
}
