package tootipay.wallet.interfaces;

import tootipay.wallet.di.ResponseHelper.GetSendRecCurrencyResponse;

import java.util.List;

public interface OnSelectCurrency extends OnMessageInterface{
    void onCurrencyResponse(List<GetSendRecCurrencyResponse> response);
    void onCurrencySelect(GetSendRecCurrencyResponse response);
}
